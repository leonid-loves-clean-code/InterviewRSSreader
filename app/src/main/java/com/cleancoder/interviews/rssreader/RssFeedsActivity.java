package com.cleancoder.interviews.rssreader;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;

import com.cleancoder.base.android.ui.ActivityHelper;
import com.cleancoder.base.android.ui.ToastUtils;
import com.cleancoder.base.android.util.TaggedLogger;

import java.util.List;


public class RssFeedsActivity extends ActivityHelper
                    implements RssFeedsLoaderFragment.Callbacks,
                               ExceptionDisplayFragment.Callbacks,
                               RssFeedsFragment.Callbacks,
                               RssFeedAddedByUserLoaderFragment.Callbacks {

    public static final String KEY_RSS_FEED_URLS = "rss_feed_urls";

    private static final TaggedLogger logger = TaggedLogger.withTag("RSS feed loading log");

    private static final int EXCEPTION_DISPLAY_RSS_FEEDS_LOADER = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new RssFeedsLoaderFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public void onRssFeedsLoaded(List<String> log) {
        // TODO do something useful with <log>
        debugLog(log);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, new RssFeedsFragment())
                .commit();
    }

    public static void debugLog(List<String> log) {
        for (String message : log) {
            logger.debug(message);
            logger.debug("     ");
            logger.debug("-----------------------------");
            logger.debug("     ");
        }
    }

    @Override
    public void onExceptionWhileLoadingRssFeeds(Throwable exception) {
        ExceptionDisplayFragment fragment =
                ExceptionDisplayFragment.newInstance(exception, EXCEPTION_DISPLAY_RSS_FEEDS_LOADER);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, fragment)
                .commit();
    }

    @Override
    public void onButtonRetryClicked(int code) {
        switch (code) {
            case EXCEPTION_DISPLAY_RSS_FEEDS_LOADER:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, new RssFeedsLoaderFragment())
                        .commit();
                break;

            default:
                break;
        }
    }

    @Override
    public void onRssFeedClicked(long rssFeedId) {
        ToastUtils.LONG.show(this, "RSS feed (" + rssFeedId + ") clicked");
        Intent intent = new Intent(this, RssItemsActivity.class);
        RssItemsActivity.putArguments(intent, rssFeedId);
        startActivity(intent);
    }

    @Override
    public void onNeedToAddRssFeed(String url) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.layout_in_center, RssFeedAddedByUserLoaderFragment.newInstance(url))
                .commit();
    }

    @Override
    public void onRssFeedAddedSuccessFully(RssFeedAddedByUserLoaderFragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .remove(fragment)
                .replace(R.id.container, new RssFeedsFragment())
                .commit();
        logger.debug("on added successfully");
    }

    @Override
    public void onExceptionWhileAddingRssFeed(RssFeedAddedByUserLoaderFragment fragment, Throwable exception) {
        getSupportFragmentManager().beginTransaction()
                .remove(fragment)
                .commit();
        logger.debug("on exception");
        String message = getString(R.string.cannot_add_rss_feed) + "\n" +
                getString(R.string.cause) + ": " + exception.getMessage();
        ToastUtils.LONG.show(this, message);
    }

}
