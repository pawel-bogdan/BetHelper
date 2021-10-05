package io.github.pawel_bogdan.downloaders;

import io.github.pawel_bogdan.model.FootballMatch;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import static java.util.stream.Collectors.toList;

public class FortunaUtil {
    private static final String MAIN_PAGE_URL = "https://www.efortuna.pl";
    private static final Logger LOGGER = LoggerFactory.getLogger(FortunaUtil.class);

    /**
     * Downloads basic info about football matches like: date, names of teams, courses.
     * @param urlToBets For example: https://www.efortuna.pl/zaklady-bukmacherskie/pi%C5%82ka-nozna/ekstraklasa-polska
     * @return List containing football matches.
     */
    public static Optional<List<FootballMatch>> downloadLBasicInfoAboutMatches(String urlToBets){
        try {
            Document document = Jsoup.connect(urlToBets).get(); // downloading whole html doc
            var matchesURLs = downloadEventsURLs(document); // downloading all references to single bets at page
            return Optional.of(downloadMatchesBasicInfo(matchesURLs));
        } catch (IOException e) {
            LOGGER.error("Error occured while connecting to: " + urlToBets);
            return Optional.empty();
        }
    }

    /**
     * Downloads and collects URLs to single events from given HTML document.
     * @param document HTML document which contains hrefs to single events.
     * @return List containing URLs to single events.
     */
    private static List<String> downloadEventsURLs(Document document) {
        return document.getElementsByClass("event-link").stream()   // all elements which are links to
                .map(element -> element.attributes().get("href")).collect(toList()); // bet have "event-link" css class
    }

    /**
     * Downloads events info like: date and name. Creates event objects and collects them into the list.
     * @param matchesURLs - URL to single match
     * @return List containing events.
     */
    private static List<FootballMatch> downloadMatchesBasicInfo(List<String> matchesURLs) {
        var matches = new ArrayList<FootballMatch>(matchesURLs.size());
        for (String matchURL : matchesURLs) {
            try {
                Document doc = Jsoup.connect(MAIN_PAGE_URL + matchURL).get();
                var eventName = doc.getElementsByClass("event-name").first().text();
                var eventDate = doc.getElementsByClass("event-datetime").text();
                var courses = downloadBasicCourses(doc);
                matches.add(new FootballMatch(eventName, eventDate, courses, matchURL));
            } catch (IOException e) {
                LOGGER.error("Error occured while connecting to: " + MAIN_PAGE_URL + matchURL);
            }
        }
        return matches;
    }

    /**
     * Downloads and collects football match's basic courses. Basic courses means 5 main ones:
     * 1) Host team victory
     * 2) Draw
     * 3) Guest team victory
     * 4) Host team victory or draw
     * 5) Guest team victory or draw
     * @param document HTML document containing above mentioned courses.
     * @return List of courses.
     */
    private static List<Float> downloadBasicCourses(Document document) {
        return document.getElementsByClass("odds-value").stream()
                .map(course -> Float.parseFloat(course.text())).collect(toList()).subList(0, 5);
    }
}
