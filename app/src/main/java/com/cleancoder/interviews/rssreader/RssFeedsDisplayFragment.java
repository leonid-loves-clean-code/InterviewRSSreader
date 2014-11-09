package com.cleancoder.interviews.rssreader;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.View;
import android.widget.AdapterView;

import com.cleancoder.base.common.util.IOUtils;
import com.cleancoder.interviews.rssreader.data.RssReaderContract.RssFeedEntry;
import com.cleancoder.interviews.rssreader.data.RssReaderDbHelper;

/**
 * Created by Leonid on 09.11.2014.
 */
public class RssFeedsDisplayFragment extends android.support.v4.app.ListFragment {

    public static interface Callbacks {
        void onRssFeedClicked(long rssFeedId);
    }

    private static final Callbacks DUMMY_CALLBACKS = new Callbacks() {
        @Override
        public void onRssFeedClicked(long rssFeedId) {
            // do nothing
        }
    };

    private static final String[] columnsToRead = { RssFeedEntry._ID, RssFeedEntry.COLUMN_TITLE };
    private static final String[] columnsToDisplay = { RssFeedEntry.COLUMN_TITLE };
    private static final int[] viewIds = { R.id.title };

    private Callbacks callbacks;
    private Cursor cursor;
    private SimpleCursorAdapter adapter;
    private SQLiteDatabase db;
    private SQLiteOpenHelper dbHelper;

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
    public void onStart() {
        super.onStart();
        init();
    }

    private void init() {
        setEmptyText(getString(R.string.no_rss_feeds));
        cursor = prepareCursor(columnsToRead);
        if (adapter == null) {
            adapter = createAdapter(cursor);
        } else {
            adapter.swapCursor(cursor);
        }
        setListAdapter(adapter);
        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                onRssFeedClicked(position);
            }
        });
    }

    private SimpleCursorAdapter createAdapter(Cursor cursor) {
        return new SimpleCursorAdapter(
            getActivity(),
            R.layout.rss_feeds_list_item,
            cursor,
            columnsToDisplay,
            viewIds,
            0);
    }

    private Cursor prepareCursor(String[] projection) {
        dbHelper = new RssReaderDbHelper(getActivity());
        db = dbHelper.getReadableDatabase();
        return db.query(
                RssFeedEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                RssFeedEntry.COLUMN_TIME_STAMP
        );
    }

    private void onRssFeedClicked(int position) {
        cursor.moveToPosition(position);
        int indexId = cursor.getColumnIndexOrThrow(RssFeedEntry._ID);
        long id = cursor.getLong(indexId);
        callbacks.onRssFeedClicked(id);
    }

    @Override
    public void onStop() {
        super.onStop();
        releaseResources();
    }

    private void releaseResources() {
        IOUtils.close(cursor);
        cursor = null;
        IOUtils.close(db);
        db = null;
        if (dbHelper != null) {
            dbHelper.close();
            dbHelper = null;
        }
    }

}
