package com.cleancoder.base.common.util;

import java.io.Closeable;
import java.io.IOException;

/**
 * Created by Leonid on 09.11.2014.
 */
public class IOUtils {

    public static void close(Closeable closeable) {
        try {
            tryClose(closeable);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public static void tryClose(Closeable closeable) throws IOException {
        if (closeable != null) {
            closeable.close();
        }
    }

}
