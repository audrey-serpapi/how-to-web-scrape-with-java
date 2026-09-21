package org.example.scraping;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class JacksonExportExample {

    public static void main(String[] args) throws Exception {

        // Fetch and parse the page
        Document doc = Jsoup.connect("https://news.ycombinator.com/news")
                .userAgent("Mozilla/5.0")
                .get();

        // Grab the title links using a CSS selector
        Elements titleLinks = doc.select(".titleline > a");
        int limit = Math.min(10, titleLinks.size());

        // LinkedHashMap keeps the keys in insertion order, so the
        // JSON output comes out in the order we added fields.
        List<Map<String, Object>> results = new ArrayList<>();

        for (int i = 0; i < limit; i++) {
            Element link = titleLinks.get(i);
            Map<String, Object> row = new LinkedHashMap<>();
            row.put("rank", i + 1);
            row.put("title", link.text());
            row.put("link", link.attr("href"));
            results.add(row);
        }

        // Jackson's ObjectMapper converts Java objects to JSON.
        // writerWithDefaultPrettyPrinter() just adds formattting so the file is readable instead of one long line.
        ObjectMapper mapper = new ObjectMapper();
        mapper.writerWithDefaultPrettyPrinter().writeValue(new File("hackernews.json"), results);

        System.out.println("Wrote top " + results.size() + " posts to hackernews.json");
    }
}