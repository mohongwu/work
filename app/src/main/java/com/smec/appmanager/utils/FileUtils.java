package com.smec.appmanager.utils;

import android.annotation.TargetApi;
import android.os.Build;
import android.support.annotation.RequiresApi;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;

/**
 * Create by mohongwu on 2018/11/12
 */
public class FileUtils {

    /**
     * Counts the size of a directory recursively (sum of the length of all files).
     * <p>
     * Note that overflow is not detected, and the return value may be negative if
     * overflow occurs.
     *
     * @param directory directory to inspect, must not be {@code null}
     * @return size of directory in bytes, 0 if directory is security restricted, a negative number when the real total
     * is greater than {@link Long#MAX_VALUE}.
     * @throws NullPointerException if the directory is {@code null}
     */
    public static long sizeOfDirectory(final File directory) {
        checkDirectory(directory);
        return sizeOfDirectory0(directory);
    }

    // Private method, must be invoked will a directory parameter

    /**
     * the size of a director
     * @param directory the directory to check
     * @return the size
     */
    private static long sizeOfDirectory0(final File directory) {
        final File[] files = directory.listFiles();
        if (files == null) {  // null if security restricted
            return 0L;
        }
        long size = 0;

        for (final File file : files) {
            //try {
                //if (!isSymlink(file)) {
                if (true){
                    size += sizeOf0(file); // internal method
                    if (size < 0) {
                        break;
                    }
                }
            //} catch (final IOException ioe) {
                // Ignore exceptions caught when asking if a File is a symlink.
            //}
        }

        return size;
    }

    // Internal method - does not check existence

    /**
     * the size of a file
     * @param file the file to check
     * @return the size of the file
     */
    private static long sizeOf0(final File file) {
        if (file.isDirectory()) {
            return sizeOfDirectory0(file);
        } else {
            return file.length(); // will be 0 if file does not exist
        }
    }

    /**
     * Checks that the given {@code File} exists and is a directory.
     *
     * @param directory The {@code File} to check.
     * @throws IllegalArgumentException if the given {@code File} does not exist or is not a directory.
     */
    private static void checkDirectory(final File directory) {
        if (!directory.exists()) {
            throw new IllegalArgumentException(directory + " does not exist");
        }
        if (!directory.isDirectory()) {
            throw new IllegalArgumentException(directory + " is not a directory");
        }
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (String aChildren : children) {
                boolean success = deleteDir(new File(dir, aChildren));
                if (!success) {
                    return false;
                }
            }
        }
        assert dir != null;
        return dir.delete();
    }
}
