package com.cleancoder.interviews.rssreader;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.os.Bundle;

import com.cleancoder.base.android.ui.TaskFragment;
import com.cleancoder.base.common.util.IOUtils;
import com.cleancoder.interviews.rssreader.data.RssReaderDbHelper;
import com.cleancoder.interviews.rssreader.rssmatshofman.MatshofmanRssFeedLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Leonid on 10.11.2014.
 */
public class RssFeedAddedByUserLoaderFragment extends TaskFragment {

    public static interface Callbacks {
        void onRssFeedAddedSuccessFully(RssFeedAddedByUserLoaderFragment fragment);
        void onExceptionWhileAddingRssFeed(RssFeedAddedByUserLoaderFragment fragment, Throwable exception);
    }

    private static final String KEY_URL = "url";

    public static RssFeedAddedByUserLoaderFragment newInstance(String url) {
        RssFeedAddedByUserLoaderFragment fragment = new RssFeedAddedByUserLoaderFragment();
        Bundle args = new Bundle();
        args.putString(KEY_URL, url);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void startTask() {
        final String url = getArguments().getString(KEY_URL);
        AsyncTask<Void,Void,Runnable> task = new AsyncTask<Void, Void, Runnable>() {
            @Override
            protected Runnable doInBackground(Void... params) {
                try {
                    loadRssFeed(url);
                    return new Runnable() {
                        @Override
                        public void run() {
                            getCallbacks().onRssFeedAddedSuccessFully(RssFeedAddedByUserLoaderFragment.this);
                        }
                    };
                } catch (final Throwable exception) {
                    return new Runnable() {
                        @Override
                        public void run() {
                            getCallbacks().onExceptionWhileAddingRssFeed(RssFeedAddedByUserLoaderFragment.this, exception);
                        }
                    };
                }
            }

            @Override
            protected void onPostExecute(Runnable runnable) {
                super.onPostExecute(runnable);
                runnable.run();
            }
        };
        task.execute();
    }

    private Callbacks getCallbacks() {
        return (Callbacks) getActivity();
    }

    private void loadRssFeed(String url) throws Exception {
        SQLiteOpenHelper dbHelper = new RssReaderDbHelper(getActivity());
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        List<String> log = new ArrayList<String>();
        MatshofmanRssFeedLoader rssFeedLoader = new MatshofmanRssFeedLoader(this, db, log);
        try {
            rssFeedLoader.loadAndSave(url);
            RssFeedsActivity.debugLog(log);
        } finally {
            IOUtils.close(db);
            dbHelper.close();
        }
    }
}
