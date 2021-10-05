package io.github.pawel_bogdan.services;

import io.github.pawel_bogdan.downloaders.FortunaUtil;
import io.github.pawel_bogdan.downloaders.TransfermarktUtil;
import io.github.pawel_bogdan.model.Club;
import io.github.pawel_bogdan.model.ClubRepository;
import io.github.pawel_bogdan.model.FootballMatch;
import io.github.pawel_bogdan.model.FootballMatchDTO;
import org.apache.commons.math3.util.Precision;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


public class Service {

    private final ClubRepository clubRepository;
    private final Logger logger = LoggerFactory.getLogger(Service.class);

    public Service() {
        clubRepository= new ClubRepository();
    }

    public List<FootballMatchDTO> getMatchesAndUpdateDB(String urlToBets) throws IllegalArgumentException {
        if(!validateUrlToBets(urlToBets)) {
            throw new IllegalArgumentException("URL does not start with: \"https://www.efortuna.pl/zaklady-bukmacherskie/pilka-nozna/\"");
        }
        var matches = FortunaUtil.downloadLBasicInfoAboutMatches(urlToBets);
        if (matches.isEmpty()) {
            return List.of();
        }

        for (FootballMatch match : matches.get()) {
            var host = match.getHostClub();
            var guest = match.getGuestClub();
            if(clubRepository.contains(host.getShortName())) {
                host = clubRepository.findById(host.getShortName()).get();
            }
            if(clubRepository.contains(guest.getShortName())) {
                guest = clubRepository.findById(guest.getShortName()).get();
            }

            setClubValue(host);
            setClubValue(guest);

            clubRepository.update(host);
            clubRepository.update(guest);
            match.setHostClub(host);
            match.setGuestClub(guest);
            setSVCRatings(match);
        }
        return matches.get().stream().map(match -> new FootballMatchDTO(match)).collect(Collectors.toList());
    }

    private void setSVCRatings(FootballMatch match) {
        var host = match.getHostClub();
        var guest = match.getGuestClub();
        var hostClubValue = TransfermarktUtil.parseTransfermarktValue(host.getSquadValue());
        var guestClubValue = TransfermarktUtil.parseTransfermarktValue(guest.getSquadValue());
        match.setHostClubSVCRating(Precision.round((hostClubValue.orElse(0.0) / 100000000) * Math.sqrt(match.getHostTeamVictoryCourse()), 2));
        match.setGuestClubSVCRating(Precision.round((guestClubValue.orElse(0.0) / 100000000) * Math.sqrt(match.getGuestTeamVictoryCourse()), 2));
    }

    private void setClubValue(Club club) {
        if(club.getUpdatedOnDate() != null && club.getUpdatedOnDate().plusMinutes(5l).isAfter(LocalDateTime.now())
                && club.getSquadValue() != null) {
            logger.info((club.getFullName() != null ? club.getFullName() : club.getShortName())  + " squad value was " +
                    "updated no longer that 5 minutes ago");
            return;
        }
        String clubTeamValue;
        if(club.getFullName() != null) {
            clubTeamValue = TransfermarktUtil.downloadSquadValue(club.getFullName()).orElse(null);
        }
        else {
            clubTeamValue = TransfermarktUtil.downloadSquadValue(club.getShortName()).orElse(null);
        }
        club.setSquadValue(clubTeamValue);
    }

    private boolean validateUrlToBets(String urlToBets) {
        return urlToBets.startsWith("https://www.efortuna.pl/zaklady-bukmacherskie/pilka-nozna/");
    }


}
