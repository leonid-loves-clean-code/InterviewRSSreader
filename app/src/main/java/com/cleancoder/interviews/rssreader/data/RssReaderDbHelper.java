package com.cleancoder.interviews.rssreader.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.cleancoder.interviews.rssreader.data.RssReaderContract.*;

/**
 * Created by Leonid on 09.11.2014.
 */
public class RssReaderDbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "rssreader.db";
    private static final int DATABASE_VERSION = 1;

    public RssReaderDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_RSS_FEED_URLS_TABLE =
                "CREATE TABLE " + RssFeedUrlEntry.TABLE_NAME + " (" +
                        RssFeedUrlEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        RssFeedUrlEntry.COLUMN_URL + " TEXT NOT NULL, " +
                        " UNIQUE (" + RssFeedUrlEntry.COLUMN_URL + ") ON CONFLICT IGNORE " +
                ");";
        final String SQL_CREATE_RSS_FEEDS_TABLE =
                "CREATE TABLE " + RssFeedEntry.TABLE_NAME + " (" +
                        RssFeedEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        RssFeedEntry.COLUMN_URL + " TEXT NOT NULL, " +
                        RssFeedEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                        RssFeedEntry.COLUMN_DESCRIPTION + " TEXT NOT NULL, " +
                        RssFeedEntry.COLUMN_TIME_STAMP + " INTEGER NOT NULL, " +
                        " UNIQUE (" + RssFeedEntry.COLUMN_URL + ") ON CONFLICT IGNORE " +
                ");";
        final String SQL_CREATE_RSS_ITEMS_TABLE =
                "CREATE TABLE " + RssItemEntry.TABLE_NAME + " (" +
                        RssItemEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        RssItemEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                        RssItemEntry.COLUMN_DESCRIPTION + " TEXT NOT NULL, " +
                        RssItemEntry.COLUMN_LINK + " TEXT NOT NULL, " +
                        RssItemEntry.COLUMN_FEED_ID + " INTEGER NOT NULL, " +
                        RssItemEntry.COLUMN_TIME_STAMP + " INTEGER NOT NULL, " +
                        " UNIQUE (" + RssItemEntry.COLUMN_LINK + ", " +
                                    RssItemEntry.COLUMN_FEED_ID + ") ON CONFLICT IGNORE " +
                ");";
        db.execSQL(SQL_CREATE_RSS_FEED_URLS_TABLE);
        db.execSQL(SQL_CREATE_RSS_FEEDS_TABLE);
        db.execSQL(SQL_CREATE_RSS_ITEMS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + RssFeedUrlEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + RssFeedEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + RssItemEntry.TABLE_NAME);
        onCreate(db);
    }
}
