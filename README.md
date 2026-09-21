# Java Web Scraping Examples

A collection of small, self-contained Java examples showing different approaches to web scraping. Explores different methods from simple static HTML parsing to full headless-browser automation, crawling, and exporting scraped data to CSV/JSON. All examples scrape the [Hacker News](https://news.ycombinator.com) front page as a common, consistent target so the techniques are easy to compare side by side.

## Prerequisites

- Java 26 (configured via Gradle toolchain)
- Gradle (wrapper included — no local install needed)
- Internet access (examples make live requests to Hacker News)

## Setup

Dependencies are managed in `app/build.gradle` and resolve automatically on first run:

```bash
./gradlew build
```

> **Note:** `edu.uci.ics:crawler4j` pulls in `com.sleepycat:je`, which requires Oracle's Maven repository (`https://download.oracle.com/maven`) in addition to Maven Central — this is already configured in `build.gradle`.

## Examples

Each file is a standalone `main()` — run any of them directly via its dedicated Gradle task:

| File | Run command | What it demonstrates |
|---|---|---|
| `JsoupStaticScraper.java` | `./gradlew runJsoup` | Basic static-HTML scraping with [Jsoup](https://jsoup.org/) — connect, select with CSS selectors, extract text/attributes. The starting point for any scraper. |
| `PlaywrightDynamicScraper.java` | `./gradlew runPlaywright` | Headless-browser scraping with [Playwright](https://playwright.dev/java/), for pages that render content via JavaScript (which Jsoup can't execute). |
| `SeleniumDynamicScraper.java` | `./gradlew runSelenium` | The same dynamic-rendering use case as Playwright, implemented with [Selenium WebDriver](https://www.selenium.dev/) instead. |
| `Crawler4jExample.java` | `./gradlew runCrawler4j` | Multi-page crawling with [crawler4j](https://github.com/yasserg/crawler4j) that defines a custom `WebCrawler` subclass with `shouldVisit`/`visit` logic, then parses each fetched page's HTML with Jsoup. |
| `ThreadSleepLoopExample.java` | `./gradlew runThreadSleepLoop` | The simplest possible multi-page scraper: a manual loop over paginated URLs with a `Thread.sleep` delay between requests, no crawling library required. |
| `OpenCsvExportExample.java` | `./gradlew runOpenCsvExport` | Scrapes results with Jsoup, then exports them to `hackernews.csv` using [OpenCSV](http://opencsv.sourceforge.net/). |
| `JacksonExportExample.java` | `./gradlew runJacksonExport` | Same scrape pipeline, exported instead to `hackernews.json` using [Jackson](https://github.com/FasterXML/jackson-databind)'s `ObjectMapper`. |
| `GsonParsingExample.java` | `./gradlew runGsonParsing` | Builds and reads back a JSON representation of scraped results using [Gson](https://github.com/google/gson)'s `JsonObject`/`JsonArray` API. |

## A note on logging

Several dependencies (crawler4j, Apache HttpClient) log at `DEBUG` by default via Logback, which can flood the console with raw HTTP wire data. Add a `logback.xml` under `app/src/main/resources` to quiet this down:

```xml
<configuration>
    <appender name="STDOUT" class="ch.qos.logback.core.ConsoleAppender">
        <encoder>
            <pattern>%d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n</pattern>
        </encoder>
    </appender>

    <root level="WARN">
        <appender-ref ref="STDOUT" />
    </root>

    <logger name="edu.uci.ics.crawler4j" level="INFO" />
    <logger name="org.apache.http" level="WARN" />
</configuration>
```

## Responsible scraping

These examples target a single, low-frequency page fetch against Hacker News for demonstration purposes. If you adapt them for your own scraping:

- Respect `robots.txt` and each site's terms of service.
- Add reasonable delays between requests (politeness delays are built into the crawler4j and thread-sleep examples).
- Avoid unnecessary load on sites that aren't equipped to handle high request volumes.