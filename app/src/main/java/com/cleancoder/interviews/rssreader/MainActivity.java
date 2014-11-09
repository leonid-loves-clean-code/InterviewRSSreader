package com.cleancoder.interviews.rssreader;

import android.os.Bundle;
import android.view.Menu;

import com.cleancoder.base.android.ui.ActivityHelper;
import com.cleancoder.base.android.ui.ToastUtils;

import java.util.List;


public class MainActivity extends ActivityHelper
                    implements RssFeedsLoaderFragment.Callbacks,
                               ExceptionDisplayFragment.Callbacks,
                               RssFeedsDisplayFragment.Callbacks {

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
        // TODO do something with <log>
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, new RssFeedsDisplayFragment())
                .commit();
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
    }

}
