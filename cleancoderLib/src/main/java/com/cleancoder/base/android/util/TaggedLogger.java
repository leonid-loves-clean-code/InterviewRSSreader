package com.cleancoder.base.android.util;

import android.util.Log;

/**
 * Created by Leonid Semyonov (clean-coder-xyz) on 03.11.2014.
 */
public class TaggedLogger {

    private final String tag;

    public static TaggedLogger forClass(Class<?> someClass) {
        return withTag(someClass.getName());
    }

    public static TaggedLogger withTag(String tag) {
        return new TaggedLogger(tag);
    }

    public static TaggedLogger forInstance(Object instance) {
        return forClass(instance.getClass());
    }

    private TaggedLogger(String tag) {
        this.tag = tag;
    }

    public void debug(String message) {
        Log.d(tag, message);
    }

    public void info(String message) {
        Log.i(tag, message);
    }

    public void exception(String message) {
        Log.e(tag, message);
    }

    public void exception(String message, Throwable exception) {
        Log.e(tag, message, exception);
    }

}
