package com.cleancoder.interviews.rssreader.data;

import com.cleancoder.base.common.data.TableRow;
import com.google.common.base.Objects;

import java.util.List;

/**
 * Created by Leonid on 09.11.2014.
 */
public class RssAdapter {
    private final TableRow feedMetadata;
    private final List<TableRow> items;

    public RssAdapter(TableRow feedMetadata, List<TableRow> items) {
        this.feedMetadata = feedMetadata;
        this.items = items;
    }

    public TableRow getFeedMetadata() {
        return feedMetadata;
    }

    public List<TableRow> getItems() {
        return items;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof RssAdapter)) {
            return false;
        }
        RssAdapter other = (RssAdapter) obj;
        return Objects.equal(getFeedMetadata(), other.getFeedMetadata()) &&
                Objects.equal(getItems(), other.getItems());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getFeedMetadata(), getItems());
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("feedMetadata", getFeedMetadata())
                .add("items", getItems())
                .toString();
    }

}
