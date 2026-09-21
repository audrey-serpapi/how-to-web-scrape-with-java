package org.example.scraping;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

/**
 * Simplest possible "automation": loop over Hacker News's paginated
 * listing pages with a manual delay between requests, collecting
 * titles/links until we have the top 10.
 */
public class ThreadSleepLoopExample {

    public static void main(String[] args) throws InterruptedException {
        String[] pageUrls = {
                "https://news.ycombinator.com/news",
                "https://news.ycombinator.com/news?p=2"
        };

        List<String> titles = new ArrayList<>();
        List<String> links = new ArrayList<>();

        for (String url : pageUrls) {
            if (titles.size() >= 10) break;

            try {
                Document doc = Jsoup.connect(url).userAgent("Mozilla/5.0").get();
                Elements titleLinks = doc.select(".titleline > a");

                for (Element link : titleLinks) {
                    if (titles.size() >= 10) break;
                    titles.add(link.text());
                    links.add(link.attr("href"));
                }
            } catch (Exception e) {
                System.err.println("Failed to fetch " + url + ": " + e.getMessage());
            }

            Thread.sleep(1000); // basic politeness delay between requests
        }

        System.out.println("Top " + titles.size() + " Hacker News posts:\n");
        for (int i = 0; i < titles.size(); i++) {
            System.out.printf("%2d. %s%n    %s%n%n", i + 1, titles.get(i), links.get(i));
        }
    }
}