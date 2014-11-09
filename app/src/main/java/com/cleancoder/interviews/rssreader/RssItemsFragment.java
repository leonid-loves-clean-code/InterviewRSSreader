package com.cleancoder.interviews.rssreader;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;

import com.cleancoder.base.android.ui.DatabaseListFragment;
import com.cleancoder.interviews.rssreader.data.RssReaderContract.*;
import com.cleancoder.interviews.rssreader.data.RssReaderDbHelper;

/**
 * Created by Leonid on 10.11.2014.
 */
public class RssItemsFragment extends DatabaseListFragment {

    public static interface Callbacks {
        void onRssItemClicked(long rssItemId);
    }

    private static final Callbacks DUMMY_CALLBACKS = new Callbacks() {
        @Override
        public void onRssItemClicked(long rssItemId) {
            // do nothing
        }
    };

    private static final String KEY_RSS_FEED_ID = "rss_feed_id";

    private static final String[] COLUMNS_TO_READ = { RssItemEntry._ID, RssItemEntry.COLUMN_TITLE };
    private static final String[] COLUMNS_TO_DISPLAY = { RssItemEntry.COLUMN_TITLE };
    private static final int[] VIEW_IDS = { R.id.title };

    private Callbacks callbacks;

    private final Helper helper = new Helper() {
        @Override
        public SQLiteOpenHelper createSQLiteOpenHelper(Context context) {
            return new RssReaderDbHelper(context);
        }

        @Override
        public Cursor query(SQLiteDatabase db) {
            return db.query(
                    RssItemEntry.TABLE_NAME,
                    COLUMNS_TO_READ,
                    RssItemEntry.COLUMN_FEED_ID + " = ?",
                    new String[] { String.valueOf(getFeedId()) },
                    null,
                    null,
                    RssItemEntry.COLUMN_TIME_STAMP + " DESC"
            );
        }

        @Override
        public String[] getColumnsToDisplay() {
            return COLUMNS_TO_DISPLAY;
        }

        @Override
        public int[] getViewIds() {
            return VIEW_IDS;
        }

        @Override
        public int getListItemLayoutId() {
            return R.layout.rss_feeds_list_item;
        }

        @Override
        public String getEmptyText() {
            return getString(R.string.rss_feed_is_empty);
        }
    };

    private long getFeedId() {
        return getArguments().getLong(KEY_RSS_FEED_ID);
    }


    public static RssItemsFragment newInstance(long rssFeedId) {
        RssItemsFragment fragment = new RssItemsFragment();
        Bundle args = new Bundle();
        args.putLong(KEY_RSS_FEED_ID, rssFeedId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        callbacks = (Callbacks) activity;
    }

    @Override
    public void onDetach() {
        callbacks = DUMMY_CALLBACKS;
        super.onDetach();
    }

    @Override
    protected Helper getHelper() {
        return helper;
    }

    @Override
    protected void onItemClicked(Cursor cursor) {
        int columnId = cursor.getColumnIndexOrThrow(RssItemEntry._ID);
        long id = cursor.getLong(columnId);
        callbacks.onRssItemClicked(id);
    }

}
