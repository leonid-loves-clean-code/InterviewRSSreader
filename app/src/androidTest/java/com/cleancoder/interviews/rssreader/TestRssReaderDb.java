package com.cleancoder.interviews.rssreader;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

import com.cleancoder.base.android.util.TaggedLogger;
import com.cleancoder.interviews.rssreader.data.RssReaderContract.RssFeedEntry;
import com.cleancoder.interviews.rssreader.data.RssReaderContract.RssFeedUrlEntry;
import com.cleancoder.interviews.rssreader.data.RssReaderContract.RssItemEntry;
import com.cleancoder.interviews.rssreader.data.RssReaderDbHelper;

/**
 * Created by Leonid on 09.11.2014.
 */
public class TestRssReaderDb extends AndroidTestCase {
    private static final TaggedLogger logger = TaggedLogger.forClass(TestRssReaderDb.class);

    public void testCreateDb() throws Throwable {
        mContext.deleteDatabase(RssReaderDbHelper.DATABASE_NAME);
        SQLiteDatabase db = new RssReaderDbHelper(mContext).getWritableDatabase();
        assertTrue(db.isOpen());
        db.close();
    }



    public void testInsertReadOneRssFeedUrl() {
        mContext.deleteDatabase(RssReaderDbHelper.DATABASE_NAME);

        String testUrl = "http://news.nationalgeographic.com/index.rss";

        RssReaderDbHelper dbHelper = new RssReaderDbHelper(mContext);
        SQLiteDatabase writableDatabase = dbHelper.getWritableDatabase();

        ContentValues rowRssUrl = new ContentValues();
        rowRssUrl.put(RssFeedUrlEntry.COLUMN_URL, testUrl);

        long insertedRowId = writableDatabase.insert(RssFeedUrlEntry.TABLE_NAME, null, rowRssUrl);

        writableDatabase.close();

        assertTrue(insertedRowId != -1);

        logger.debug("inserted row id:  " + insertedRowId);

        String[] columns = {
                RssFeedUrlEntry._ID,
                RssFeedUrlEntry.COLUMN_URL
        };

        SQLiteDatabase readableDatabase = dbHelper.getReadableDatabase();

        Cursor cursor = readableDatabase.query(
                RssFeedUrlEntry.TABLE_NAME,
                columns,
                null, null, null, null, null
        );

        assertTrue(cursor.getCount() == 1);

        boolean movedToFirst = cursor.moveToFirst();

        if (!movedToFirst) {
            fail("No values returned");
            return;
        }

        int columnId = cursor.getColumnIndexOrThrow(RssFeedUrlEntry._ID);
        int columnUrl = cursor.getColumnIndexOrThrow(RssFeedUrlEntry.COLUMN_URL);

        int id = cursor.getInt(columnId);
        String url = cursor.getString(columnUrl);

        cursor.close();
        readableDatabase.close();

        assertEquals(insertedRowId, id);
        assertEquals(testUrl, url);
    }



    public void testInsertReadOneRssFeed() {
        mContext.deleteDatabase(RssReaderDbHelper.DATABASE_NAME);

        String testUrl = "http://news.nationalgeographic.com/index.rss";
        String testTitle = "National Geographic";
        String testDescription = "About nature";
        long testTimeStamp = 1923;

        RssReaderDbHelper dbHelper = new RssReaderDbHelper(mContext);
        SQLiteDatabase writableDatabase = dbHelper.getWritableDatabase();

        ContentValues rowRssFeed = new ContentValues();
        rowRssFeed.put(RssFeedEntry.COLUMN_URL, testUrl);
        rowRssFeed.put(RssFeedEntry.COLUMN_TITLE, testTitle);
        rowRssFeed.put(RssFeedEntry.COLUMN_DESCRIPTION, testDescription);
        rowRssFeed.put(RssFeedEntry.COLUMN_TIME_STAMP, testTimeStamp);

        long insertedRowId = writableDatabase.insert(RssFeedEntry.TABLE_NAME, null, rowRssFeed);

        writableDatabase.close();

        assertTrue(insertedRowId != -1);

        logger.debug("inserted row id:  " + insertedRowId);

        String[] columns = {
                RssFeedEntry._ID,
                RssFeedEntry.COLUMN_URL,
                RssFeedEntry.COLUMN_TITLE,
                RssFeedEntry.COLUMN_DESCRIPTION,
                RssFeedEntry.COLUMN_TIME_STAMP
        };

        SQLiteDatabase readableDatabase = dbHelper.getReadableDatabase();

        Cursor cursor = readableDatabase.query(
                RssFeedEntry.TABLE_NAME,
                columns,
                null, null, null, null, null
        );

        assertTrue(cursor.getCount() == 1);

        boolean movedToFirst = cursor.moveToFirst();

        if (!movedToFirst) {
            fail("No values returned");
            return;
        }

        int columnId = cursor.getColumnIndexOrThrow(RssFeedEntry._ID);
        int columnUrl = cursor.getColumnIndexOrThrow(RssFeedEntry.COLUMN_URL);
        int columnTitle = cursor.getColumnIndexOrThrow(RssFeedEntry.COLUMN_TITLE);
        int columnDescription = cursor.getColumnIndexOrThrow(RssFeedEntry.COLUMN_DESCRIPTION);
        int columnTimeStamp = cursor.getColumnIndexOrThrow(RssFeedEntry.COLUMN_TIME_STAMP);

        int id = cursor.getInt(columnId);
        String url = cursor.getString(columnUrl);
        String title = cursor.getString(columnTitle);
        String description = cursor.getString(columnDescription);
        long timeStamp = cursor.getLong(columnTimeStamp);

        cursor.close();
        readableDatabase.close();

        assertEquals(insertedRowId, id);
        assertEquals(testUrl, url);
        assertEquals(testTitle, title);
        assertEquals(testDescription, description);
        assertEquals(testTimeStamp, timeStamp);
    }



    public void testInsertReadOneRssItem() {
        mContext.deleteDatabase(RssReaderDbHelper.DATABASE_NAME);

        String testItemTitle = "Tiger is in Red Book";
        String testItemDescription = "Did you know that?";
        String testItemLink = "http://news.nationalgeographic.com/dummylink";
        long testItemTimeStamp = 135;

        RssReaderDbHelper dbHelper = new RssReaderDbHelper(mContext);
        SQLiteDatabase writableDatabase = dbHelper.getWritableDatabase();

        ContentValues rowRssFeed = new ContentValues();
        rowRssFeed.put(RssFeedEntry.COLUMN_URL, "http://news.nationalgeographic.com/index.rss");
        rowRssFeed.put(RssFeedEntry.COLUMN_TITLE, "National Geographic");
        rowRssFeed.put(RssFeedEntry.COLUMN_DESCRIPTION, "About nature");
        rowRssFeed.put(RssFeedEntry.COLUMN_TIME_STAMP, 153592);

        long insertedFeedRowId = writableDatabase.insert(RssFeedEntry.TABLE_NAME, null, rowRssFeed);

        assertTrue(insertedFeedRowId != -1);

        logger.debug("inserted row id:  " + insertedFeedRowId);

        ContentValues rowRssItem = new ContentValues();
        rowRssItem.put(RssItemEntry.COLUMN_TITLE, testItemTitle);
        rowRssItem.put(RssItemEntry.COLUMN_DESCRIPTION, testItemDescription);
        rowRssItem.put(RssItemEntry.COLUMN_LINK, testItemLink);
        rowRssItem.put(RssItemEntry.COLUMN_FEED_ID, insertedFeedRowId);
        rowRssItem.put(RssItemEntry.COLUMN_TIME_STAMP, testItemTimeStamp);

        long insertedItemRowId = writableDatabase.insert(RssItemEntry.TABLE_NAME, null, rowRssItem);

        assertTrue(insertedItemRowId != -1);

        writableDatabase.close();

        String[] columns = {
                RssItemEntry._ID,
                RssItemEntry.COLUMN_TITLE,
                RssItemEntry.COLUMN_DESCRIPTION,
                RssItemEntry.COLUMN_LINK,
                RssItemEntry.COLUMN_FEED_ID,
                RssItemEntry.COLUMN_TIME_STAMP
        };

        SQLiteDatabase readableDatabase = dbHelper.getReadableDatabase();

        Cursor cursor = readableDatabase.query(
                RssItemEntry.TABLE_NAME,
                columns,
                null, null, null, null, null
        );

        assertTrue(cursor.getCount() == 1);

        boolean movedToFirst = cursor.moveToFirst();

        if (!movedToFirst) {
            fail("No values returned");
            return;
        }

        int columnId = cursor.getColumnIndexOrThrow(RssItemEntry._ID);
        int columnTitle = cursor.getColumnIndexOrThrow(RssItemEntry.COLUMN_TITLE);
        int columnDescription = cursor.getColumnIndexOrThrow(RssItemEntry.COLUMN_DESCRIPTION);
        int columnLink = cursor.getColumnIndexOrThrow(RssItemEntry.COLUMN_LINK);
        int columnFeedId = cursor.getColumnIndexOrThrow(RssItemEntry.COLUMN_FEED_ID);
        int columnTimeStamp = cursor.getColumnIndexOrThrow(RssItemEntry.COLUMN_TIME_STAMP);

        long id = cursor.getLong(columnId);
        String title = cursor.getString(columnTitle);
        String description = cursor.getString(columnDescription);
        String link = cursor.getString(columnLink);
        long timeStamp = cursor.getLong(columnTimeStamp);
        long feedId = cursor.getLong(columnFeedId);

        cursor.close();
        readableDatabase.close();

        assertEquals(insertedItemRowId, id);
        assertEquals(testItemTitle, title);
        assertEquals(testItemDescription, description);
        assertEquals(testItemLink, link);
        assertEquals(testItemTimeStamp, timeStamp);
        assertEquals(insertedFeedRowId, feedId);
    }

}
