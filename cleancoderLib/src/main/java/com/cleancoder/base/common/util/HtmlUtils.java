package com.cleancoder.base.common.util;

/**
 * Created by Leonid on 10.11.2014.
 */
public class HtmlUtils {

    public static boolean containsHtml(String text) {
        return text.matches(".*\\<[^>]+>.*");
    }

}
