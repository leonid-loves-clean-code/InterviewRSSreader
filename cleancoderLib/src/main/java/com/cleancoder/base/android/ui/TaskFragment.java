package com.cleancoder.base.android.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cleancoder.base.R;

/**
 * Created by Leonid Semyonov (clean-coder-xyz) on 03.11.2014.
 */
public abstract class TaskFragment extends android.support.v4.app.Fragment {

    private final Object LOCK_IS_TASK_STARTED = "LOCK_IS_TASK_STARTED";

    private boolean isTaskStarted = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_task, null);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        startTaskIfNotStarted();
    }

    private void startTaskIfNotStarted() {
        synchronized (LOCK_IS_TASK_STARTED) {
            if (isTaskStarted) {
                return;
            }
            isTaskStarted = true;
        }
        startTask();
    }

    protected abstract void startTask();

    public void setStatus(final String text) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                getStatusTextView().setText(text);
            }
        });
    }

    private TextView getStatusTextView() {
        return (TextView) getView().findViewById(R.id.status_text_view);
    }

    public void setStatus(final int resId) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                getStatusTextView().setText(resId);
            }
        });
    }

}
