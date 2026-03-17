package com.pax.android.supporthub.app.util;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.OpenableColumns;
import android.util.Log;

import androidx.annotation.Nullable;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import okio.BufferedSink;
import okio.Okio;

public class FileUtils {
    private static final String TAG = FileUtils.class.getSimpleName();
    private static final String TEMP_DIR = "upload_tmp";

    /**
     * 把任意 InputStream 写入应用内部缓存目录，返回临时 File
     *
     * @param context 上下文
     * @param in      输入流（方法内部关闭）
     * @param fileName     文件名
     * @param ext     如果文件不包含.的话，就加入扩展名，例如 ".png"
     * @return 临时文件对象；失败返回 null
     */
    @Nullable
    public static File streamToTempFile(Context context, InputStream in, String fileName, String ext) {
        if (in == null) return null;

        File tmpDir = new File(context.getCacheDir(), TEMP_DIR);
        if (!tmpDir.exists() && !tmpDir.mkdirs()) {
            return null;
        }
        File tmpFile;
        if (fileName.contains(".")){
            tmpFile = new File(tmpDir, fileName);
        } else {
            tmpFile = new File(tmpDir, fileName + ext);
        }
        if (tmpFile.exists()) {
            try {
                in.close();
            } catch (IOException ignore) {
            }
            return tmpFile;
        }
        try (in; BufferedSink sink = Okio.buffer(Okio.sink(tmpFile))) {
            sink.writeAll(Okio.source(in));
            sink.flush();
            return tmpFile;
        } catch (IOException e) {
            // 失败时清理半写入文件
            if (tmpFile.exists()) tmpFile.delete();
            return null;
        }
    }

    public static void clearAllTempFiles(Context context) {
        File tmpDir = new File(context.getCacheDir(), TEMP_DIR);
        if (!tmpDir.exists()) return;
        deleteRecursive(tmpDir);
    }

    public static void deleteRecursive(File file) {
        if (file.isDirectory()) {
            File[] children = file.listFiles();
            if (children != null) {
                for (File child : children) deleteRecursive(child);
            }
        }
        //noinspection ResultOfMethodCallIgnored
        file.delete();
    }

    public static String getFileName(Context context, Uri uri) {
        if (uri == null) return null;
        String result = null;

        // 1. 如果是 file://  scheme，直接截
        if (ContentResolver.SCHEME_FILE.equals(uri.getScheme())) {
            result = uri.getLastPathSegment();
        }
        // 2. 如果是 content://  scheme（常见）
        else if (ContentResolver.SCHEME_CONTENT.equals(uri.getScheme())) {
            try (Cursor cursor = context.getContentResolver()
                    .query(uri, new String[]{OpenableColumns.DISPLAY_NAME},
                            null, null, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(
                            cursor.getColumnIndexOrThrow(OpenableColumns.DISPLAY_NAME));
                }
            }
        }
        // 3. 兜底
        if (result == null) {
            result = uri.getLastPathSegment();
        }
        return result;
    }


    // 获取 Uri 对应的文件大小，查不到返回-1
    public static long getUriFileSize(Context ctx, Uri uri) {
        Cursor cursor = ctx.getContentResolver()
                .query(uri, new String[]{OpenableColumns.SIZE}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            long size = cursor.getLong(0);
            cursor.close();
            Log.w(TAG, "Compressed original image size: " + size + "bytes");
            return size;
        }
        return -1;
    }

    public static boolean filterPictureType(String file) {
        String fileName = file.toLowerCase();
        return fileName.endsWith(".jpg") || fileName.endsWith(".jpeg")
                || fileName.endsWith(".png");
    }
}
