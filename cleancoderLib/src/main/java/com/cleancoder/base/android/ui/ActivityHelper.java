package com.cleancoder.base.android.ui;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

/**
 * Created by Leonid Semyonov (clean-coder-xyz) on 03.11.2014.
 */
public class ActivityHelper extends ActionBarActivity {

    private final Object LOCK_DESTROYED = new Object();

    private boolean destroyed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setDestroyed(false);
    }

    private void setDestroyed(boolean destroyed) {
        synchronized (LOCK_DESTROYED) {
            this.destroyed = destroyed;
        }
    }

    @Override
    protected void onResume() {
        setDestroyed(false);
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        setDestroyed(true);
        super.onDestroy();
    }

    public boolean isDestroyed() {
        synchronized (LOCK_DESTROYED) {
            return destroyed;
        }
    }

    public void refreshActivity() {
        finish();
        startActivity(getIntent());
    }

}
