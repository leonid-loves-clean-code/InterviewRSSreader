package com.cleancoder.base.android.ui;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.View;
import android.widget.AdapterView;

import com.cleancoder.base.common.util.IOUtils;

/**
 * Created by Leonid on 09.11.2014.
 */
public abstract class DatabaseListFragment extends android.support.v4.app.ListFragment {

    protected interface Helper {
        SQLiteOpenHelper createSQLiteOpenHelper(Context context);
        Cursor query(SQLiteDatabase db);
        String[] getColumnsToDisplay();
        int[] getViewIds();
        int getListItemLayoutId();
        CharSequence getEmptyText();
    }

    private Cursor cursor;
    private Helper helper;
    private SimpleCursorAdapter adapter;
    private SQLiteDatabase db;
    private SQLiteOpenHelper dbHelper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        helper = getHelper();
    }

    protected abstract Helper getHelper();

    @Override
    public void onStart() {
        super.onStart();
        init();
    }

    private void init() {
        setEmptyText(helper.getEmptyText());
        AsyncTask<Void,Void,Cursor> task = new AsyncTask<Void,Void,Cursor>() {
            @Override
            protected Cursor doInBackground(Void... params) {
                return prepareCursor();
            }

            @Override
            protected void onPostExecute(Cursor cursor) {
                super.onPostExecute(cursor);
                onCursorIsReady(cursor);
            }
        };
        task.execute();
    }

    private void onCursorIsReady(final Cursor cursor) {
        if (adapter == null) {
            adapter = createAdapter(cursor);
        } else {
            adapter.swapCursor(cursor);
        }
        setListAdapter(adapter);
        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                cursor.moveToPosition(position);
                onItemClicked(cursor);
            }
        });
    }

    protected abstract void onItemClicked(Cursor cursor);

    private SimpleCursorAdapter createAdapter(Cursor cursor) {
        return new SimpleCursorAdapter(
                getActivity(),
                helper.getListItemLayoutId(),
                cursor,
                helper.getColumnsToDisplay(),
                helper.getViewIds(),
                0);
    }

    private Cursor prepareCursor() {
        dbHelper = helper.createSQLiteOpenHelper(getActivity());
        db = dbHelper.getReadableDatabase();
        return helper.query(db);
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