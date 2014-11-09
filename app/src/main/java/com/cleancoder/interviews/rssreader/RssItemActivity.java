package com.cleancoder.interviews.rssreader;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import com.cleancoder.base.android.ui.ActivityHelper;
import com.cleancoder.base.common.data.TableRow;
import com.cleancoder.base.common.util.IOUtils;
import com.cleancoder.interviews.rssreader.data.RssReaderContract.RssItemEntry;
import com.cleancoder.interviews.rssreader.data.RssReaderDbHelper;


public class RssItemActivity extends ActivityHelper {

    private static final int EXCEPTION_DISPLAY_LOADING_RSS_ITEM = 2;

    private static final String KEY_RSS_ITEM_ID = "rss_item_id";
    private static final String KEY_LOADING_FINISHED = "loading_finished";

    private boolean loadingFinished;

    public static void putArguments(Intent intent, long rssItemId) {
        intent.putExtra(KEY_RSS_ITEM_ID, rssItemId);
    }

    private final AsyncTask<Void,Void,Runnable> taskLoadRssItem = new AsyncTask<Void, Void, Runnable>() {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            findViewById(R.id.progress_bar).setVisibility(View.VISIBLE);
            loadingFinished = false;
        }

        @Override
        protected Runnable doInBackground(Void... params) {
            try {
                final TableRow rssItem = loadRssItem();
                return new Runnable() {
                    @Override
                    public void run() {
                        onRssItemLoaded(rssItem);
                    }
                };
            } catch (final Throwable exception) {
                return new Runnable() {
                    @Override
                    public void run() {
                        onExceptionWhileLoadingRssItem(exception);
                    }
                };
            }
        }

        @Override
        protected void onPostExecute(Runnable runnable) {
            super.onPostExecute(runnable);
            findViewById(R.id.progress_bar).setVisibility(View.INVISIBLE);
            runnable.run();
            loadingFinished = true;
        }
    };

    private TableRow loadRssItem() {
        if (!getIntent().hasExtra(KEY_RSS_ITEM_ID)) {
            throw new IllegalStateException("You can't start activity without passing RSS item id");
        }
        long rssItemId = getIntent().getLongExtra(KEY_RSS_ITEM_ID, -1);
        RssReaderDbHelper dbHelper = new RssReaderDbHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.query(
                    RssItemEntry.TABLE_NAME,
                    new String[]{RssItemEntry.COLUMN_TITLE, RssItemEntry.COLUMN_DESCRIPTION},
                    RssItemEntry._ID + " = ?",
                    new String[]{String.valueOf(rssItemId)},
                    null,
                    null,
                    null
            );
            if ((cursor.getCount() != 1) || !cursor.moveToFirst()) {
                throw new IllegalStateException("There is no RSS item with id: " + rssItemId);
            }
            return readRssItem(cursor);

        } finally {
            IOUtils.close(cursor);
            IOUtils.close(db);
            dbHelper.close();
        }
    }

    private TableRow readRssItem(Cursor cursor) {
        int columnTitle = cursor.getColumnIndexOrThrow(RssItemEntry.COLUMN_TITLE);
        int columnDescription = cursor.getColumnIndexOrThrow(RssItemEntry.COLUMN_DESCRIPTION);
        String title = cursor.getString(columnTitle);
        String description = cursor.getString(columnDescription);
        TableRow rssItem = new TableRow();
        rssItem.set(RssItemEntry.COLUMN_TITLE, title);
        rssItem.set(RssItemEntry.COLUMN_DESCRIPTION, description);
        return rssItem;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rss_item);
        loadingFinished = (savedInstanceState == null) ? false : savedInstanceState.getBoolean(KEY_LOADING_FINISHED, false);
        if (!loadingFinished) {
            taskLoadRssItem.execute();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(KEY_LOADING_FINISHED, loadingFinished);
    }

    private void onRssItemLoaded(TableRow rssItem) {
        getSupportFragmentManager().beginTransaction()
            .replace(R.id.container, RssItemFragment.newInstance(rssItem))
            .commit();
    }

    private void onExceptionWhileLoadingRssItem(Throwable exception) {
        Fragment fragment = ExceptionDisplayFragment.newInstance(exception, EXCEPTION_DISPLAY_LOADING_RSS_ITEM);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, fragment)
                .commit();
    }

}
