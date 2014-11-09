package com.cleancoder.interviews.rssreader.rssmatshofman;

import com.cleancoder.base.common.data.TableRow;
import com.cleancoder.interviews.rssreader.RssFeedsLoaderFragment;
import com.cleancoder.interviews.rssreader.data.RssAdapter;
import com.cleancoder.interviews.rssreader.data.RssReaderContract.*;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import nl.matshofman.saxrssreader.RssFeed;
import nl.matshofman.saxrssreader.RssItem;
import nl.matshofman.saxrssreader.RssReader;

/**
 * Created by Leonid on 09.11.2014.
 */
public class MatshofmanRssFeedLoader implements RssFeedsLoaderFragment.RssFeedLoader {

    private static final MatshofmanRssConverter RSS_CONVERTER = new MatshofmanRssConverter();

    @Override
    public RssAdapter loadRssFeed(String url) throws Exception {
        RssFeed rssFeed = RssReader.read(new URL(url));
        TableRow feedMetadata = new TableRow();
        RSS_CONVERTER.convertToFeedMetadata(rssFeed, feedMetadata);
        feedMetadata.set(RssFeedEntry.COLUMN_URL, url);
        List<RssItem> rssItems = rssFeed.getRssItems();
        List<TableRow> rssItemsTable = new ArrayList<TableRow>(rssItems.size());
        for (RssItem rssItem : rssItems) {
            TableRow rssItemRow = new TableRow();
            RSS_CONVERTER.convertToItem(rssItem, rssItemRow);
            rssItemsTable.add(rssItemRow);
        }
        return new RssAdapter(feedMetadata, rssItemsTable);
    }
}
