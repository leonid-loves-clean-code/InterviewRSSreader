package com.cleancoder.interviews.rssreader;

import android.content.Intent;
import android.os.Bundle;

import com.cleancoder.base.android.ui.ActivityHelper;
import com.cleancoder.base.android.ui.ToastUtils;


public class RssItemsActivity extends ActivityHelper implements RssItemsFragment.Callbacks {

    private static final String KEY_RSS_FEED_ID = "rss_feed_id";

    public static void putArguments(Intent intent, long rssFeedId) {
        intent.putExtra(KEY_RSS_FEED_ID, rssFeedId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rss_items);
        if (savedInstanceState == null) {
            if (!getIntent().hasExtra(KEY_RSS_FEED_ID)) {
                throw new IllegalStateException("You can't start activity not passing rss feed ID");
            }
            long rssFeedId = getIntent().getLongExtra(KEY_RSS_FEED_ID, -1);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, RssItemsFragment.newInstance(rssFeedId))
                    .commit();
        }
    }

    @Override
    public void onRssItemClicked(long rssItemId) {
        ToastUtils.SHORT.show(this, "RSS item (" + rssItemId + ") clicked");
        // TODO
    }

}
