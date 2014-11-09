package com.cleancoder.base.common.data;

import com.google.common.base.Objects;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by Leonid Semyonov (clean-coder-xyz) on 03.11.2014.
 */
public class TableRow implements Serializable {
    private final int numberOfColumns;
    private final Map<String,Object> fields;

    public TableRow(String... columns) {
        this(Arrays.asList(columns));
    }

    public TableRow(Collection<String> columns) {
        checkColumns(columns);
        fields = new HashMap<String, Object>(columns.size());
        for (String column : columns) {
            fields.put(column, null);
        }
        numberOfColumns = columns.size();
    }

    private static void checkColumns(Collection<String> columns) {
        if (columns == null) {
            throw new IllegalArgumentException("You can't pass <null> instead of column names");
        }
        if (columns.isEmpty()) {
            throw new IllegalArgumentException(
                    "You can't pass no columns. You should pass at least 1 column name");
        }
        Set<String> columnSet = new HashSet<String>(columns);
        if (columnSet.contains(null)) {
            throw new IllegalArgumentException("You can't pass <null> instead of column name");
        }
        if (columnSet.size() < columns.size()) {
            throw new IllegalArgumentException("Table can't have duplicate column names");
        }
    }

    public void set(String column, Object value) {
        checkColumn(column);
        fields.put(column, value);
    }

    private void checkColumn(String column) {
        if (!fields.containsKey(column)) {
            throw new IllegalArgumentException("There is no column <" + column + ">");
        }
    }

    public <T> T get(String column) {
        checkColumn(column);
        return (T) fields.get(column);
    }

    public int getNumberOfColumns() {
        return numberOfColumns;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof TableRow)) {
            return false;
        }
        TableRow other = (TableRow) obj;
        return Objects.equal(fields, other.fields);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(fields);
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("fields", fields)
                .toString();
    }

}
