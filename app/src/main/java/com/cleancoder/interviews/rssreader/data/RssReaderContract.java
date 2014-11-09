package com.cleancoder.interviews.rssreader.data;

import android.provider.BaseColumns;

/**
 * Created by Leonid on 09.11.2014.
 */
public class RssReaderContract {

    public static final class RssFeedUrlEntry implements BaseColumns {
        public static final String TABLE_NAME = "rss_feed_url";
        public static final String COLUMN_URL = "url";
    }

    public static final class RssFeedEntry implements BaseColumns {
        public static final String TABLE_NAME = "rss_feed";
        public static final String COLUMN_URL = "url";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_TIME_STAMP = "time_stamp";
    }

    public static final class RssItemEntry implements BaseColumns {
        public static final String TABLE_NAME = "rss_item";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_FEED_ID = "feed_id";
        public static final String COLUMN_LINK = "link";
        public static final String COLUMN_TIME_STAMP = "time_stamp";
    }

}
