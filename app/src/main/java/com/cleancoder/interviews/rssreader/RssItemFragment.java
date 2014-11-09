package com.cleancoder.interviews.rssreader;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cleancoder.base.common.data.TableRow;
import com.cleancoder.interviews.rssreader.data.RssReaderContract.*;

/**
 * Created by Leonid on 10.11.2014.
 */
public class RssItemFragment extends Fragment {

    private static final String KEY_RSS_ITEM = "rss_item";

    private View contentView;

    public static RssItemFragment newInstance(TableRow rssItem) {
        RssItemFragment fragment = new RssItemFragment();
        Bundle args = new Bundle();
        args.putSerializable(KEY_RSS_ITEM, rssItem);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        contentView = inflater.inflate(R.layout.fragment_rss_item, null);
        initContentView();
        return contentView;
    }

    private void initContentView() {
        TableRow rssItem = (TableRow) getArguments().getSerializable(KEY_RSS_ITEM);
        String title = rssItem.<String>get(RssItemEntry.COLUMN_TITLE);
        String description = rssItem.<String>get(RssItemEntry.COLUMN_DESCRIPTION);
        TextView titleTextView = (TextView) contentView.findViewById(R.id.title_text_view);
        TextView descriptionTextView = (TextView) contentView.findViewById(R.id.description_text_view);
        titleTextView.setText(title);
        descriptionTextView.setText(description);
    }

}
