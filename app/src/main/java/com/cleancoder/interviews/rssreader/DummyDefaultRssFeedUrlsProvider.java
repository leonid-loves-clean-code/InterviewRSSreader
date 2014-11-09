package com.cleancoder.interviews.rssreader;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by Leonid on 09.11.2014.
 */
public class DummyDefaultRssFeedUrlsProvider implements RssFeedsLoaderFragment.DefaultRssFeedUrlsProvider {
    private static final List<String> URLS = Arrays.asList(
            "http://news.nationalgeographic.com/index.rss",
            "http://www.npr.org/rss/rss.php?id=1020",
            "http://reviews.cnet.com/4924-3504_7-0.xml?orderBy=-7rvDte&7rType=70-80&maxhits=10"
    );

    @Override
    public List<String> getUrls() {
        return Collections.unmodifiableList(URLS);
    }

}
