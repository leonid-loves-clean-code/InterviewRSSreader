package com.cleancoder.interviews.rssreader;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Leonid on 09.11.2014.
 */
public class ExceptionDisplayFragment extends Fragment {

    public static interface Callbacks {
        void onButtonRetryClicked(int code);
    }

    private static final String KEY_CODE = "code";
    private static final String KEY_EXCEPTION = "exception";

    private View contentView;

    public static ExceptionDisplayFragment newInstance(Throwable exception, int code) {
        ExceptionDisplayFragment fragment = new ExceptionDisplayFragment();
        Bundle args = new Bundle();
        args.putInt(KEY_CODE, code);
        args.putSerializable(KEY_EXCEPTION, exception);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        contentView = inflater.inflate(R.layout.fragment_exception_display, null);
        initContentView();
        return contentView;
    }

    private void initContentView() {
        Throwable exception = (Throwable) getArguments().getSerializable(KEY_EXCEPTION);
        exception.printStackTrace();
        if (exception.getCause() != null) {
            exception.getCause().printStackTrace();
        }
        TextView exceptionTextView = (TextView) contentView.findViewById(R.id.exception_text_view);
        exceptionTextView.setText(exception.getMessage());
        View buttonRetry = contentView.findViewById(R.id.button_retry);
        buttonRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onButtonRetryClicked();
            }
        });
    }

    private void onButtonRetryClicked() {
        Callbacks callbacks = (Callbacks) getActivity();
        int code = getArguments().getInt(KEY_CODE);
        callbacks.onButtonRetryClicked(code);
    }

}
