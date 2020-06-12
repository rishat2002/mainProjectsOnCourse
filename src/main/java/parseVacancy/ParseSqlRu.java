package parseVacancy;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class ParseSqlRu implements Parse {
    private String link;

    public ParseSqlRu(String link) {
        this.link = link;
    }

    @Override
    public List<Vacancy> list() throws ParseException {
        List<Vacancy> list = new ArrayList<>();
        Document doc = null;
        try {
            doc = Jsoup.connect(link).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Elements forVac = doc.getElementsByTag("tr");
        for (Element s1 : forVac) {
            Elements elementForTag = s1.getElementsByClass("postslisttopic");
            for (Element elem : elementForTag) {
                if ((elem.text().matches(".*Java.*")) && (!(elem.text().matches(".*JavaScript.*")
                        || elem.text().matches(".*Java Script.*")))) {
                    Elements e = elem.getElementsByTag("a");
                    Document d = null;
                    try {
                        d = Jsoup.connect(e.attr("href")).get();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                    Elements forText = d.getElementsByTag("meta");
                    for (Element n : forText) {
                        if (n.attr("name").equals("Description")) {
                            String date = null;
                            Elements elementForDate = s1.getElementsByClass("altCol");
                            for (Element s : elementForDate) {
                                for (Element e2 : s.getElementsByAttribute("style")) {
                                    date = e2.text();
                                }
                            }
                            Vacancy newVac = new Vacancy(elem.text(), e.attr("href"), n.attr("content"));
                            newVac.setDate(date);
                            list.add(newVac);
                        }
                    }
                }
            }
        }

        return list;
    }

    private static DateFormatSymbols myDateFormatSymbols = new DateFormatSymbols() {

        @Override
        public String[] getMonths() {
            return new String[]{"янв", "фев", "мар", "апр", "май", "июн",
                    "июл", "авг", "сент", "окт", "ноя", "дек"};
        }

    };

    public Date parseString(String s) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("d MMMM yy, HH:mm", myDateFormatSymbols);
        Date newDate = format.parse(s);
        return newDate;
    }

    @Override
    public Vacancy detail(String link) throws IOException {
        Document doc = Jsoup.connect(link).get();
        String vacancyText;
        String vacancyName;
        String vacancyLink;
        Elements elementForTag = doc.getElementsByClass("msgBody");
        vacancyText = elementForTag.text();
        Element elForName = doc.tagName("font");
        vacancyName = elForName.text().substring(0, elForName.text().indexOf("/"));
        vacancyLink = link;
        return new Vacancy(vacancyName, vacancyLink, vacancyText);
    }


    public static void main(String[] args) throws IOException, ParseException {

    }
}
