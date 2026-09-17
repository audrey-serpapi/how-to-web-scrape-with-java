package org.example.scraping;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class SeleniumDynamicScraper {

    public static void main(String[] args) {
        // Launch a browser 
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new");

        WebDriver driver = new ChromeDriver(options);

        // Select the content and wait for it to load
        try {
            driver.get("https://news.ycombinator.com/");

            // Wait until the story rows are fully present in the DOM
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("tr.athing")));

            // Find all story rows
            List<WebElement> entries = driver.findElements(By.cssSelector("tr.athing"));
            int limit = Math.min(entries.size(), 10);
            
            // Display the results
            System.out.println("Top " + limit + " Hacker News Stories:\n");
            for (int i = 0; i < limit; i++) {
                WebElement entry = entries.get(i);
                
                // Locate the title and URL link inside each row
                WebElement titleLink = entry.findElement(By.cssSelector(".titleline > a"));
                
                String title = titleLink.getText();
                String link = titleLink.getAttribute("href");

                System.out.printf("%2d. %s%n    Link: %s%n%n", (i + 1), title, link);
            }
        } finally {
            driver.quit();
        }
    }
}

