package com.cleancoder.base.android.data;

import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.content.Context;
import android.content.CursorLoader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Leonid on 04.11.2014.
 */
public class Query implements Parcelable {

    public static final Creator<Query> CREATOR = new Creator<Query>() {
        @Override
        public Query createFromParcel(Parcel source) {
            return new Query(source);
        }

        @Override
        public Query[] newArray(int size) {
            return new Query[size];
        }
    };

    private Uri uri;
    private String[] projection;
    private String selection;
    private String[] selectionArgs;
    private String sortOrder;

    public Query() {
        uri = null;
        projection = null;
        selection = null;
        selectionArgs = null;
        sortOrder = null;
    }

    public Cursor query(ContentResolver contentResolver) {
        return contentResolver.query(
                getUri(),
                getProjection(),
                getSelection(),
                getSelectionArgs(),
                getSortOrder()
        );
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public CursorLoader createNotSupportCursorLoader(Context context) {
        return new CursorLoader(
                context,
                getUri(),
                getProjection(),
                getSelection(),
                getSelectionArgs(),
                getSortOrder()
        );
    }

    public android.support.v4.content.CursorLoader createSupportCursorLoader(Context context) {
        return new android.support.v4.content.CursorLoader(
                context,
                getUri(),
                getProjection(),
                getSelection(),
                getSelectionArgs(),
                getSortOrder()
        );
    }

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }

    public String[] getProjection() {
        return projection;
    }

    public void setProjection(String[] projection) {
        this.projection = projection;
    }

    public String getSelection() {
        return selection;
    }

    public void setSelection(String selection) {
        this.selection = selection;
    }

    public String[] getSelectionArgs() {
        return selectionArgs;
    }

    public void setSelectionArgs(String[] selectionArgs) {
        this.selectionArgs = selectionArgs;
    }

    public String getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(String sortOrder) {
        this.sortOrder = sortOrder;
    }

    public Query(Parcel in) {
        uri = in.readParcelable(Uri.class.getClassLoader());
        projection = in.createStringArray();
        selection = in.readString();
        selectionArgs = in.createStringArray();
        sortOrder = in.readString();
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeParcelable(uri, flags);
        out.writeStringArray(projection);
        out.writeString(selection);
        out.writeStringArray(selectionArgs);
        out.writeString(sortOrder);
    }

    @Override
    public int describeContents() {
        return 0;
    }

}
