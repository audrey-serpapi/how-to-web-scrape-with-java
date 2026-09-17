package org.example.scraping;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;

import java.util.List;

public class PlaywrightDynamicScraper {

    public static void main(String[] args) {
        // Launch a browser with Playwright
        try (Playwright playwright = Playwright.create()) {
            Browser browser = playwright.chromium().launch(
                    new BrowserType.LaunchOptions().setHeadless(true));
            Page page = browser.newPage();

            // Grab the site and wait for it to load
            page.navigate("https://news.ycombinator.com/");
            page.waitForSelector("tr.athing");

            // Select and extract the data
            List<Locator> entries = page.locator("tr.athing").all();
            int limit = Math.min(entries.size(), 10);

            // Display the results
            System.out.println("Top " + limit + " Hacker News Stories:\n");
            for (int i = 0; i < limit; i++) {
                Locator entry = entries.get(i);
                
                /* Extract title text and 'href' attribute from the anchor tag
                '.titleline > a' because we only want to access the 'a' (anchor) tag
                 within within the 'titleline' class 
                 .textContext and .getAttribute to grab the data type (title and link)
                 */
                String title = entry.locator(".titleline > a").textContent();
                String link = entry.locator(".titleline > a").getAttribute("href");

                System.out.printf("%2d. %s%n    Link: %s%n%n", (i + 1), title, link);
            }

            browser.close();
        }
    }
}