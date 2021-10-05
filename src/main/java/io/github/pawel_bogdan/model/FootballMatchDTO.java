package io.github.pawel_bogdan.model;

public class FootballMatchDTO {
    public String name;
    public String date;
    public ClubDTO hostClub;
    public ClubDTO guestClub;
    public Float hostTeamVictoryCourse;
    public Float drawCourse;
    public Float guestTeamVictoryCourse;
    public Float hostTeamVictoryOrDrawCourse;
    public Float guestTeamVictoryOrDrawCourse;
    public Double hostTeamSVCRating;
    public Double guestTeamSVCRating;

    public FootballMatchDTO(FootballMatch match) {
        this.name = match.name;
        this.date = match.date;
        this.hostClub = new ClubDTO(match.getHostClub());
        this.guestClub = new ClubDTO(match.getGuestClub());
        this.hostTeamVictoryCourse = match.getHostTeamVictoryCourse();
        this.drawCourse = match.getDrawCourse();
        this.guestTeamVictoryCourse = match.getGuestTeamVictoryCourse();
        this.hostTeamVictoryOrDrawCourse = match.getHostTeamVictoryOrDrawCourse();
        this.guestTeamVictoryOrDrawCourse = match.getGuestTeamVictoryOrDrawCourse();
        this.hostTeamSVCRating = match.getHostClubSVCRating();
        this.guestTeamSVCRating = match.getGuestClubSVCRating();
    }
}
