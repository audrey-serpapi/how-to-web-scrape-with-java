package org.example.scraping;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;
import edu.uci.ics.crawler4j.url.WebURL;

public class Crawler4jExample {

    /**
     * This is your custom crawler. Two methods are required:
     *   - shouldVisit: decides which links are allowed to be followed
     *   - visit: runs once for every page the crawler actually fetches
     *
     * crawler4j creates instances of this class internally (that's why
     * we only pass the *class* itself to controller.start() below,
     * not an object).
     */

    public static class MyCrawler extends WebCrawler {

        @Override
        public boolean shouldVisit(Page referringPage, WebURL url) {
            return url.getURL().startsWith("https://news.ycombinator.com");
        }

        @Override
        public void visit(Page page) {
            // getParseData() can return different types depending on what
            // was fetched (HTML, image, binary file, etc). We only care
            // about HTML pages, so we check the type before using it.
            if (page.getParseData() instanceof HtmlParseData) {

                // HtmlParseData gives you easy access to common page info
                // without needing any extra parsing library.
                HtmlParseData parseData = (HtmlParseData) page.getParseData();
                System.out.println("Title: " + parseData.getTitle());
                System.out.println("URL:   " + page.getWebURL().getURL());
            }
        }
    }

    public static void main(String[] args) throws Exception {
        CrawlConfig config = new CrawlConfig();

        // crawler4j needs a folder on disk to keep track of its internal
        // state (which URLs it has queued/visited). This is required even
        // for a single-page crawl like this one.
        config.setCrawlStorageFolder("/tmp/crawler4j-data");
        config.setMaxDepthOfCrawling(0); // just the seed page

        // This is the standard way for Crawler4j fetches pages and respects robot.txt rules.
        PageFetcher pageFetcher = new PageFetcher(config);
        RobotstxtServer robotstxtServer = new RobotstxtServer(new RobotstxtConfig(), pageFetcher);
        CrawlController controller = new CrawlController(config, pageFetcher, robotstxtServer);

         // The seed is the starting URL (or URLs) the crawl begins from.
        // You can call addSeed() multiple times to start from several pages.
        controller.addSeed("https://news.ycombinator.com");
        controller.start(MyCrawler.class, 1);
    }
}