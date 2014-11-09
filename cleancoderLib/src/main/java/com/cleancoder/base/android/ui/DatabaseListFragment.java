package com.cleancoder.base.android.ui;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.View;
import android.widget.AdapterView;

import com.cleancoder.base.common.util.IOUtils;

/**
 * Created by Leonid on 09.11.2014.
 */
public abstract class DatabaseListFragment extends android.support.v4.app.ListFragment {

    public static interface Callbacks {
        void onItemClicked(Cursor cursor);
    }

    private static final Callbacks DUMMY_CALLBACKS = new Callbacks() {
        @Override
        public void onItemClicked(Cursor cursor) {
            // do nothing
        }
    };

    protected interface Helper {
        SQLiteOpenHelper createSQLiteOpenHelper(Context context);
        Cursor query(SQLiteDatabase db);
        String[] getColumnsToDisplay();
        int[] getViewIds();
        int getListItemLayoutId();
        void setEmptyText();
    }

    private Callbacks callbacks;
    private Cursor cursor;
    private Helper helper;
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
        helper.setEmptyText();
        cursor = prepareCursor();
        if (adapter == null) {
            adapter = createAdapter(cursor);
        } else {
            adapter.swapCursor(cursor);
        }
        setListAdapter(adapter);
        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                onItemClicked(position);
            }
        });
    }

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

    private void onItemClicked(int position) {
        cursor.moveToPosition(position);
        callbacks.onItemClicked(cursor);
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