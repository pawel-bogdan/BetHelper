package io.github.pawel_bogdan.downloaders;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Optional;

public class TransfermarktUtil {
    private static final String MAIN_PAGE_URL = "https://www.transfermarkt.pl";
    private static final Logger LOGGER = LoggerFactory.getLogger(TransfermarktUtil.class);

    /**
     * Connects to the transfermarkt site and downloads squad value of given team.
     * @param team Name of the team.
     * @return squad value in euro
     * @throws IOException
     */
    public static Optional<String> downloadSquadValue(String team) {
        LOGGER.info("searching value for: " + team);
        var urlToConnect = MAIN_PAGE_URL + "/schnellsuche/ergebnis/schnellsuche?query=" + team;
        Document document = null;
        try {
            document = Jsoup.connect(urlToConnect).get();
        } catch (IOException e) {
            LOGGER.error("Error occured while connecting to: " + urlToConnect);
            return Optional.empty();
        }
        var selectorForClubLinks = "tr>.hauptlink>.vereinprofil_tooltip";
        var searchingResults = document.select(selectorForClubLinks);

        if (searchingResults.isEmpty()) {
            LOGGER.warn("No result found");
            return Optional.empty();
        }

        var bestResultReference = searchingResults.first().attr("href");

        Elements squadValueElement;
        try {
            squadValueElement = Jsoup.connect(MAIN_PAGE_URL + bestResultReference).get().
                    getElementsByClass("dataMarktwert").select("a");
        } catch (IOException e) {
            LOGGER.error("Error occured while connecting to: " + MAIN_PAGE_URL + bestResultReference);
            return Optional.empty();
        }

        var squadValue = squadValueElement.text();
        var indexOfEuroSign = squadValueElement.text().indexOf("€");

        if(indexOfEuroSign < 0)
            return Optional.empty();

        squadValue = squadValue.substring(0, indexOfEuroSign);
        return Optional.of(squadValue);
    }

    /**
     * Parses transfermarkt value. For strings like: 3 mln, 1.13 mld, 900.90 tys returns their number equivalents.
     * @param tmValue String representation of number.
     * @return Number representing that string mentioned above.
     */
    public static Optional<Double> parseTransfermarktValue(String tmValue) {
        if(tmValue == null)
            return Optional.empty();
        var prefix = tmValue.split(" ")[0];
        var suffix = tmValue.split(" ")[1];
        double numberPrefix;
        try {
            numberPrefix = Double.parseDouble(prefix.replace(",", "."));
        }
        catch (NumberFormatException e) {
            LOGGER.error("Can not parse into the double: " + prefix);
            numberPrefix = 0;
        }
        int factor = 1;
        if(suffix.startsWith("mln"))
            factor = 1000000;
        else if(suffix.startsWith("mld"))
            factor = 1000000000;
        else if(suffix.startsWith("tys"))
            factor = 1000;
        return Optional.of(numberPrefix * factor);
    }
}
