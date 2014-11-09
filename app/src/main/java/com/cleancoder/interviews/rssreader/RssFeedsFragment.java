package com.cleancoder.interviews.rssreader;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.EditText;

import com.cleancoder.base.android.ui.DatabaseListFragment;
import com.cleancoder.base.android.ui.ToastUtils;
import com.cleancoder.interviews.rssreader.data.RssReaderContract.RssFeedEntry;
import com.cleancoder.interviews.rssreader.data.RssReaderDbHelper;

/**
 * Created by Leonid on 09.11.2014.
 */
public class RssFeedsFragment extends DatabaseListFragment {

    public static interface Callbacks {
        void onRssFeedClicked(long rssFeedId);
        void onNeedToAddRssFeed(String url);
    }

    private static final Callbacks DUMMY_CALLBACKS = new Callbacks() {
        @Override
        public void onRssFeedClicked(long rssFeedId) {
            // do nothing
        }

        @Override
        public void onNeedToAddRssFeed(String url) {
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_rss_feed, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add_rss_feed:
                onAddRssFeed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void onAddRssFeed() {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        final View dialogLayout = inflater.inflate(R.layout.dialog_add_feed, null);
        final EditText inputView = (EditText) dialogLayout.findViewById(R.id.item_added);
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(dialogLayout).setTitle(R.string.add_rss_feed);
        builder.setPositiveButton(R.string.add_rss_feed_positive_button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                String url =  inputView.getText().toString();
                if (!URLUtil.isValidUrl(url)) {
                    notifyUserThatUrlIsInvalid();
                } else {
                    onUserAddedRssFeed(url);
                    dialog.dismiss();
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void onUserAddedRssFeed(String url) {
        /*
        List<String> urls = Arrays.asList(url);
        getActivity().getIntent().putExtra(RssFeedsActivity.KEY_RSS_FEED_URLS, (Serializable) urls);
        ((ActivityHelper) getActivity()).refreshActivity();
        */
        callbacks.onNeedToAddRssFeed(url);
    }

    private void notifyUserThatUrlIsInvalid() {
        ToastUtils.SHORT.show(getActivity(), R.string.add_rss_feed_notification_url_is_invalid);
    }
}
