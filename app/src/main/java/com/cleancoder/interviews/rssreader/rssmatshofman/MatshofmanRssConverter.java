package com.cleancoder.interviews.rssreader.rssmatshofman;

import com.cleancoder.base.common.data.TableRow;
import com.cleancoder.interviews.rssreader.data.RssReaderContract.RssFeedEntry;
import com.cleancoder.interviews.rssreader.data.RssReaderContract.RssItemEntry;

import java.util.Date;

import nl.matshofman.saxrssreader.RssFeed;
import nl.matshofman.saxrssreader.RssItem;

/**
 * Created by Leonid on 09.11.2014.
 */
class MatshofmanRssConverter {

    void convertToFeedMetadata(RssFeed feed, TableRow tableRow) {
        tableRow.set(RssFeedEntry.COLUMN_TITLE, feed.getTitle());
        tableRow.set(RssFeedEntry.COLUMN_DESCRIPTION, feed.getDescription());
        tableRow.set(RssFeedEntry.COLUMN_TIME_STAMP, System.currentTimeMillis());
    }

    void convertToItem(RssItem item, TableRow tableRow) {
        tableRow.set(RssItemEntry.COLUMN_TITLE, item.getTitle());
        tableRow.set(RssItemEntry.COLUMN_DESCRIPTION, item.getDescription());
        tableRow.set(RssItemEntry.COLUMN_LINK, item.getLink());
        Date pubDate = item.getPubDate();
        long timeStamp = (pubDate != null) ? pubDate.getTime() : 0;
        tableRow.set(RssItemEntry.COLUMN_TIME_STAMP, timeStamp);
    }

}
