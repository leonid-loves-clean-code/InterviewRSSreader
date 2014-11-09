package com.cleancoder.interviews.rssreader;

import android.test.AndroidTestCase;

import com.cleancoder.base.android.util.TaggedLogger;

import org.xml.sax.SAXException;

import java.io.IOException;
import java.net.URL;
import java.util.Collection;

import nl.matshofman.saxrssreader.RssFeed;
import nl.matshofman.saxrssreader.RssItem;
import nl.matshofman.saxrssreader.RssReader;

/**
 * Created by Leonid on 09.11.2014.
 */
public class TestLearnParseRss extends AndroidTestCase {
    private static final boolean DO_NOT_NEED_LOG = true;

    private static final TaggedLogger logger = TaggedLogger.forClass(TestLearnParseRss.class);

    public void testSimpleExampleOfReading() {
        Collection<String> urls = new DummyDefaultRssFeedUrlsProvider().getUrls();
        for (String url : urls) {
            try {
                logRssFeed(url);
            } catch (Throwable exception) {
                logger.exception(exception.getMessage(), exception);
                throw new RuntimeException(exception);
            }
        }
    }

    private static void logRssFeed(String url) throws IOException, SAXException {
        RssFeed rssFeed = RssReader.read(new URL(url));
        Collection<RssItem> rssItems = rssFeed.getRssItems();
        if (DO_NOT_NEED_LOG) {
            return;
        }
        logger.debug("-------------------------------------------");
        logger.debug("--------------[  RSS Feed  ]---------------");
        logger.debug("-------------------------------------------");
        logger.debug("Title: " + rssFeed.getTitle());
        logger.debug("Description: " + rssFeed.getDescription());
        logger.debug("-------------------------------------------");
        for (RssItem rssItem : rssItems) {
            logger.debug(rssItem.getTitle());
        }
        logger.debug("===========================================");
        logger.debug("===========================================");
    }

}
