package com.cleancoder.interviews.rssreader;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.cleancoder.base.android.ui.DatabaseListFragment;
import com.cleancoder.interviews.rssreader.data.RssReaderContract.RssFeedEntry;
import com.cleancoder.interviews.rssreader.data.RssReaderDbHelper;

/**
 * Created by Leonid on 09.11.2014.
 */
public class RssFeedsFragment extends DatabaseListFragment {

    public static interface Callbacks {
        void onRssFeedClicked(long rssFeedId);
    }

    private static final Callbacks DUMMY_CALLBACKS = new Callbacks() {
        @Override
        public void onRssFeedClicked(long rssFeedId) {
            // do nothing
        }
    };

    private static final String[] COLUMNS_TO_READ = { RssFeedEntry._ID, RssFeedEntry.COLUMN_TITLE };
    private static final String[] COLUMNS_TO_DISPLAY = { RssFeedEntry.COLUMN_TITLE };
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
                    RssFeedEntry.TABLE_NAME,
                    COLUMNS_TO_READ,
                    null,
                    null,
                    null,
                    null,
                    RssFeedEntry.COLUMN_TIME_STAMP + " DESC"
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
        public CharSequence getEmptyText() {
            return getString(R.string.no_rss_feeds);
        }
    };

    @Override
    protected Helper getHelper() {
        return helper;
    }

    @Override
    protected void onItemClicked(Cursor cursor) {
        int indexId = cursor.getColumnIndexOrThrow(RssFeedEntry._ID);
        long id = cursor.getLong(indexId);
        callbacks.onRssFeedClicked(id);
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

}
