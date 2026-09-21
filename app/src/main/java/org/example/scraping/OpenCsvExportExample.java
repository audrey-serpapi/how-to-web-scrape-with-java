package org.example.scraping;

import com.opencsv.CSVWriter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.FileWriter;

public class OpenCsvExportExample {

    public static void main(String[] args) throws Exception {
        Document doc = Jsoup.connect("https://news.ycombinator.com/news")
                .userAgent("Mozilla/5.0")
                .get();

        Elements titleLinks = doc.select(".titleline > a");
        int limit = Math.min(10, titleLinks.size());

        // CSVWriter handles the messy parts of CSV formatting for us
        // (quoting fields, escaping commas, line endings) so we don't
        // have to build the file as raw text ourselves.
        // The try-with-resources syntax (try (...) { }) automatically
        // closes the file when we're done, even if an error occurs.
        try (CSVWriter writer = new CSVWriter(new FileWriter("hackernews.csv"))) {
            // Every row, including the header, is written as a String array, one element per column.
            writer.writeNext(new String[]{"Rank", "Title", "Link"});

            for (int i = 0; i < limit; i++) {
                Element link = titleLinks.get(i);
                writer.writeNext(new String[]{
                        String.valueOf(i + 1), // for rank convert int to String, CSVWriter needs strings
                        link.text(),
                        link.attr("href")
                });
            }
        }

        System.out.println("Wrote top " + limit + " posts to hackernews.csv");
    }
}