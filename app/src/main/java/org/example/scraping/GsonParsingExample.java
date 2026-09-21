package org.example.scraping;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class GsonParsingExample {

    public static void main(String[] args) throws Exception {
        Document doc = Jsoup.connect("https://news.ycombinator.com/news")
                .userAgent("Mozilla/5.0")
                .get();

        Elements titleLinks = doc.select(".titleline > a");
        int limit = Math.min(10, titleLinks.size());

        // Build a JSON array of the scraped results
        JsonArray postsArray = new JsonArray();
        for (int i = 0; i < limit; i++) {
            Element link = titleLinks.get(i);

            JsonObject post = new JsonObject();
            post.addProperty("rank", i + 1);
            post.addProperty("title", link.text());
            post.addProperty("link", link.attr("href"));
            postsArray.add(post);
        }

        // Read it back out and print it, same as the other examples
        System.out.println("Top " + limit + " Hacker News posts:\n");
        for (int i = 0; i < postsArray.size(); i++) {
            JsonObject post = postsArray.get(i).getAsJsonObject();
            System.out.printf("%2d. %s%n    %s%n%n",
                    post.get("rank").getAsInt(),
                    post.get("title").getAsString(),
                    post.get("link").getAsString());
        }

        // Show the raw JSON
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        System.out.println("Raw JSON:\n" + gson.toJson(postsArray));
    }
}