package com.fls.metro.core.util

/**
 * User: NFadin
 * Date: 06.06.2014
 * Time: 16:27
 */
class FileUtils {
    public static String getExtension(String fileName) {
        int pos = fileName.lastIndexOf('.');
        if (pos < 0) {
            return "";
        }
        return fileName.substring(pos);
    }
}
