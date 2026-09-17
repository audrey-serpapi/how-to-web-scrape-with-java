package org.example.scraping;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;  
import org.jsoup.nodes.Element;   
import org.jsoup.select.Elements;

public class JsoupStaticScraper {

    public static void main(String[] args) throws Exception {
        // Fetching a page
        String url = "https://news.ycombinator.com";

        Document doc = Jsoup.connect(url)
            .userAgent("Mozilla/5.0") 
            .referrer("https://google.com")
            .timeout(10_000)
            .get();

        //Select and extract the data
        Elements entries = doc.select("tr.athing"); 
        int limit = Math.min(entries.size(), 10);
        
        //Display the results
        System.out.println("Top " + limit + " Hacker News Stories: \n");
        for (int i = 0; i < limit; i++) {
            Element entry = entries.get(i);
            String title = entry.select(".titleline > a").text(); 
            String link = entry.select(".titleline > a").attr("href"); 

            System.out.printf("%2d. %s%n Link: %s%n", (i + 1), title, link);
            System.out.println();
        }
    }
}
