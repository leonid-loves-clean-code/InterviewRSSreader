package com.cleancoder.interviews.rssreader;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;

import com.cleancoder.base.android.ui.TaskFragment;
import com.cleancoder.base.common.data.TableRow;
import com.cleancoder.interviews.rssreader.data.RssAdapter;
import com.cleancoder.interviews.rssreader.data.RssReaderContract.RssFeedEntry;
import com.cleancoder.interviews.rssreader.data.RssReaderContract.RssFeedUrlEntry;
import com.cleancoder.interviews.rssreader.data.RssReaderContract.RssItemEntry;
import com.cleancoder.interviews.rssreader.data.RssReaderDbHelper;
import com.cleancoder.interviews.rssreader.rssmatshofman.MatshofmanRssFeedLoader;

import org.xml.sax.SAXException;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Leonid on 09.11.2014.
 */
public class RssFeedsLoaderFragment extends TaskFragment {

    public static interface Callbacks {
        void onRssFeedsLoaded(List<String> log);
        void onExceptionWhileLoadingRssFeeds(Throwable exception);
    }

    public static interface DefaultRssFeedUrlsProvider {
        List<String> getUrls();
    }

    public static interface RssFeedLoader {
        RssAdapter loadRssFeed(String url) throws Exception;
    }

    private static final DefaultRssFeedUrlsProvider
            DEFAULT_RSS_FEED_URLS_PROVIDER = new DummyDefaultRssFeedUrlsProvider();

    private static final RssFeedLoader RSS_FEED_LOADER = new MatshofmanRssFeedLoader();

    private Callbacks callbacks;
    private List<String> log;
    private RssReaderDbHelper dbHelper;
    private SQLiteDatabase readableDatabase;
    private SQLiteDatabase writableDatabase;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        callbacks = (Callbacks) activity;
    }

    @Override
    public void onDetach() {
        callbacks = null;
        super.onDetach();
    }

    @Override
    protected void startTask() {
        new RssFeedsLoaderTask().execute();
    }

    private class RssFeedsLoaderTask extends AsyncTask<Void,Void,Void> {
        @Override
        protected Void doInBackground(Void... params) {
            doWork();
            return null;
        }
    }

    private void doWork() {
        setStatus(R.string.loading_rss_feeds);
        log = new ArrayList<String>();
        dbHelper = new RssReaderDbHelper(getActivity());
        writableDatabase = dbHelper.getWritableDatabase();
        readableDatabase = dbHelper.getReadableDatabase();
        try {
            loadAndSaveRssFeeds();
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    callbacks.onRssFeedsLoaded(log);
                }
            });
        } catch (final Throwable exception) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    callbacks.onExceptionWhileLoadingRssFeeds(exception);
                }
            });
        } finally {
            writableDatabase.close();
            readableDatabase.close();
            dbHelper.close();
        }
    }

    private void loadAndSaveRssFeeds() throws Exception {
        int numberOfLoadedRssFeeds = 0;
        for (String url : loadRssFeedUrls()) {
            try {
                RssAdapter rssAdapter = RSS_FEED_LOADER.loadRssFeed(url);
                save(rssAdapter);
                ++numberOfLoadedRssFeeds;
            } catch (SAXException xmlException) {
                addToLog("Couldn't parse rss feed from url: " + url, xmlException);
            }
        }
        if (numberOfLoadedRssFeeds < 1) {
            addToLog("Couldn't load rss feeds", null);
        }
    }

    private List<String> loadRssFeedUrls() {
        prepareDefaultRssFeedUrls();
        String[] columns = { RssFeedUrlEntry.COLUMN_URL };
        Cursor cursor = readableDatabase.query(RssFeedUrlEntry.TABLE_NAME, columns, null, null, null, null, null);
        try {
            return readRssFeedUrls(cursor);
        } finally {
            cursor.close();
        }
    }

    private void prepareDefaultRssFeedUrls() {
        ContentValues row = new ContentValues();
        for (String url : DEFAULT_RSS_FEED_URLS_PROVIDER.getUrls()) {
            row.put(RssFeedUrlEntry.COLUMN_URL, url);
            writableDatabase.insert(RssFeedUrlEntry.TABLE_NAME, null, row);
        }
    }

    private static List<String> readRssFeedUrls(Cursor cursor) {
        int numberOfUrls = cursor.getCount();
        if (numberOfUrls < 1) {
            throw new RuntimeException("Couldn't load rss feed urls");
        }
        List<String> urls = new ArrayList<String>(numberOfUrls);
        if (cursor.moveToFirst() == false) {
            throw new RuntimeException("Couldn't read cursor with rss feed urls");
        }
        int indexUrl = cursor.getColumnIndexOrThrow(RssFeedUrlEntry.COLUMN_URL);
        while (!cursor.isAfterLast()) {
            String url = cursor.getString(indexUrl);
            urls.add(url);
            cursor.moveToNext();
        }
        return urls;
    }

    private void addToLog(String message, Throwable exception) {
        log.add("====================");
        log.add("--------------------");
        if (message != null) {
            log.add(message);
        }
        if (exception != null) {
            log.add("Exception message: " + exception.getMessage());
        }
        log.add("--------------------");
    }

    private void save(RssAdapter rssAdapter) {
        TableRow feedMetadata = rssAdapter.getFeedMetadata();
        ContentValues row = new ContentValues();
        String title = feedMetadata.<String>get(RssFeedEntry.COLUMN_TITLE);
        String description = feedMetadata.<String>get(RssFeedEntry.COLUMN_DESCRIPTION);
        String url = feedMetadata.<String>get(RssFeedEntry.COLUMN_URL);
        long timeStamp = feedMetadata.<Long>get(RssFeedEntry.COLUMN_TIME_STAMP);

        setStatus(getString(R.string.loading_rss_feed_with_title) + ": " + title);

        row.put(RssFeedEntry.COLUMN_TITLE, title);
        row.put(RssFeedEntry.COLUMN_DESCRIPTION, description);
        row.put(RssFeedEntry.COLUMN_URL, url);
        row.put(RssFeedEntry.COLUMN_TIME_STAMP, timeStamp);
        try {
            long feedId = writableDatabase.insertOrThrow(RssFeedEntry.TABLE_NAME, null, row);
            saveItems(feedId, rssAdapter.getItems());
        } catch (RuntimeException exception) {
            addToLog("Couldn't save feed: " + title, exception);
            throw exception;
        }
    }

    private void saveItems(long feedId, List<TableRow> items) {
        ContentValues row = new ContentValues();
        for (TableRow item : items) {
            try {
                saveItem(row, feedId, item);
            } catch (Throwable exception) {
                String title = item.<String>get(RssItemEntry.COLUMN_TITLE);
                addToLog("Can't save rss item: " + title, exception);
            }
        }
    }

    private void saveItem(ContentValues row, long feedId, TableRow item) {
        row.put(RssItemEntry.COLUMN_FEED_ID, feedId);
        row.put(RssItemEntry.COLUMN_TITLE, item.<String>get(RssItemEntry.COLUMN_TITLE));
        row.put(RssItemEntry.COLUMN_DESCRIPTION, item.<String>get(RssItemEntry.COLUMN_DESCRIPTION));
        row.put(RssItemEntry.COLUMN_LINK, item.<String>get(RssItemEntry.COLUMN_LINK));
        row.put(RssItemEntry.COLUMN_TIME_STAMP, item.<Long>get(RssItemEntry.COLUMN_TIME_STAMP));
        writableDatabase.insert(RssItemEntry.TABLE_NAME, null, row);
    }

}
