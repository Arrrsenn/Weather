import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {

    private final Pattern pattern = Pattern.compile("\\d{2}\\.\\d{2}");

    private static Document getPage() throws IOException {
        String url = "https://world-weather.ru/pogoda/russia/moscow/7days/";
        Document page = Jsoup.parse(new URL(url), 5000);
        return page;
    }

    public static void main(String[] args) throws IOException {
        Document page = getPage();
        Elements tableWt = page.select("div[class=weather-short]");
        Elements names = tableWt.select("table[class=weather-today short]");
        Elements params = page.select("table[class=weather-today]");
        int index = 0;

        for (Element date : tableWt) {
            String dateWeekday = date.select("div[class=dates short-d]").text();
            String dateWeekend = date.select("div[class=dates short-d red]").text();
            System.out.println(dateWeekend + dateWeekday);
            for (Element pm : params.select("td")) {
                System.out.print(pm.text() + "\t\t");
            }
            System.out.println();
            printParameters(names, index);
            System.out.println();
            index++;
        }
    }


    private static void printParameters(Elements parameters, int index) {
        Element value = parameters.get(index);
        for (Element td : value.select("td")) {
            if (td.text().equals("Утро") || td.text().equals("День") || td.text().equals("Вечер")) {
                System.out.println();
            }
            System.out.print(td.text() + "\t\t\t\t\t\t");
        }
        System.out.println();
    }

    private String getDateFromString(String stringDate) throws Exception {
        Matcher matcher = pattern.matcher(stringDate);
        if (matcher.find()) {
            return matcher.group();
        }
        throw new Exception("Can't extract date from string");
    }
}
