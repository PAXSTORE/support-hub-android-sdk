package com.pax.android.supporthub.sdk.apis.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import okio.BufferedSink;
import okio.Okio;

public class FileUtil {
    private static final String UPLOAD_COMPRESS_TMP = "upload_compress_tmp";

    public static List<File> compressImagesIfNeeded(Context context, List<File> originalFiles) {
        List<File> compressedFiles = new ArrayList<>();

        for (File originalFile : originalFiles) {
            // 如果文件小于500KB，不压缩
            if (originalFile.length() < 500 * 1024) {
                compressedFiles.add(originalFile);
                Log.w("SupportHub SDK FileUtil", "文件过小，跳过压缩: "
                        + originalFile.getName() + " (" + originalFile.length() + " bytes)");
                continue;
            }
            try {
                // 获取原始文件名和扩展名
                String originalName = originalFile.getName();
                String ext;
                int dotIndex = originalName.lastIndexOf('.');
                if (dotIndex > 0) {
                    ext = originalName.substring(dotIndex);
                } else {
                    ext = ".jpg"; // 默认扩展名
                }

                // 生成压缩后的文件名（添加压缩标识）
                String baseName = originalName.substring(0, dotIndex > 0 ? dotIndex : originalName.length());
                String compressedFileName = baseName + "_compressed" + ext;

                // 创建输入流
                InputStream fileInputStream = new FileInputStream(originalFile);

                // 调用 streamToTempFile 进行压缩
                File compressedFile = streamToTempFile(context, fileInputStream, compressedFileName, ext);

                if (compressedFile != null && compressedFile.exists()) {
                    compressedFiles.add(compressedFile);
                    Log.w("SupportHub SDK FileUtil", "compress success:" + originalFile.getName() +
                "-> " + compressedFile.getName() + " (" + compressedFile.length() +" bytes)");
                } else {
                    // 压缩失败，使用原始文件
                    compressedFiles.add(originalFile);
                    Log.w("SupportHub SDK FileUtil", "compress fail, use original file: " + originalFile.getName());
                }

                // 关闭输入流
                fileInputStream.close();

            } catch (Exception e) {
                Log.w("SupportHub SDK FileUtil", "处理图片:" + originalFile.getName() + " 时出错: " + e);
                // 发生异常时使用原始文件
                compressedFiles.add(originalFile);
            }
        }

        return compressedFiles;
    }

    public static File streamToTempFile(Context context, InputStream in, String fileName, String ext) {
        if (in == null) return null;
        File tmpDir = new File(context.getCacheDir(), UPLOAD_COMPRESS_TMP);
        if (!tmpDir.exists() && !tmpDir.mkdirs()) {
            return null;
        }
        File tmpFile;
        if (fileName.contains(".")) {
            tmpFile = new File(tmpDir, fileName);
        } else {
            tmpFile = new File(tmpDir, fileName + ext);
        }
        if (tmpFile.exists()) {
            Log.w("SupportHub SDK FileUtil", "compress picture is exist");
            try {
                in.close();
            } catch (IOException e) {
                Log.w("SupportHub SDK FileUtil", "streamToTempFile err: " + e);
            }
            return tmpFile;
        }
        // 1. 将输入流转为 Bitmap
        Bitmap originalBitmap = BitmapFactory.decodeStream(in);
        if (originalBitmap == null) return null;  // 如果无法解析为图片，返回 null
        // 2. 缩小图片尺寸 原来的1/2
        int newWidth = originalBitmap.getWidth() / 2;
        int newHeight = originalBitmap.getHeight() / 2;
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(originalBitmap, newWidth, newHeight, false);
        // 3. 将图片流压缩
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        if (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg")) {
            boolean compressJpgResult = resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 90, byteArrayOutputStream);
            Log.w("SupportHub SDK FileUtil", "JPEG compressJpgResult: " + compressJpgResult);
        } else {
            boolean compressPngResult = resizedBitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
            Log.w("SupportHub SDK FileUtil", "PNG compressPngResult: " + compressPngResult);
        }
        // 4. 写入临时文件
        try (BufferedSink sink = Okio.buffer(Okio.sink(tmpFile))) {
            byte[] byteArray = byteArrayOutputStream.toByteArray();
            sink.write(byteArray);
            sink.flush();
            // 输出压缩后图片的大小
            long compressedFileSize = tmpFile.length();
            Log.w("SupportHub SDK FileUtil", fileName + " Compressed image size: " + compressedFileSize + " bytes");
            return tmpFile;
        } catch (IOException e) {
            // 失败时清理半写入文件
            Log.w("SupportHub SDK FileUtil", "streamToTempFile e:" + e);
            if (tmpFile.exists()) tmpFile.delete();
            return null;
        } finally {
            resizedBitmap.recycle();
            originalBitmap.recycle();
        }
    }

    public static void clearCompressTempFiles(Context context) {
        File tmpDir = new File(context.getCacheDir(), UPLOAD_COMPRESS_TMP);
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

    public static boolean filterPictureType(String file) {
        String fileName = file.toLowerCase();
        return fileName.endsWith(".jpg") || fileName.endsWith(".jpeg")
                || fileName.endsWith(".png");
    }

}
