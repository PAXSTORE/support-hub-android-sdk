package com.pax.android.supporthub.sdk.apis.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class FileUtil {
    private static final String UPLOAD_COMPRESS_TMP = "upload_compress_tmp";

    public static List<File> compressImagesIfNeeded(Context context, List<File> originalFiles) {
        List<File> compressedFiles = new ArrayList<>();

        for (File originalFile : originalFiles) {
            // 如果文件小于500KB，不压缩
            if (originalFile.length() < 500 * 1024) {
                compressedFiles.add(originalFile);
                Log.w("SupportHub SDK FileUtil", "The file size is less than 5MB. Skipping compression. "
                        + originalFile.getName() + " (" + originalFile.length() + " bytes)");
                continue;
            }
            try {
                // 获取原始文件名和扩展名
                String originalName = originalFile.getName();
                Log.w("SupportHub SDK FileUtil", "originalFile size: " + originalFile.length() + " bytes");
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
                Log.w("SupportHub SDK FileUtil", "compress picture -" + originalFile.getName() + " err: " + e);
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

        File tmpFile = fileName.contains(".")
                ? new File(tmpDir, fileName)
                : new File(tmpDir, fileName + ext);

        if (tmpFile.exists()) {
            Log.w("SupportHub SDK FileUtil", "compress picture is exist");
            return tmpFile;
        }

        // 创建临时文件保存原始数据
        File tempOriginalFile = new File(tmpDir, "temp_original_" + System.currentTimeMillis());
        try (in; FileOutputStream tempOut = new FileOutputStream(tempOriginalFile)) {
            // 1. 将输入流复制到临时文件
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = in.read(buffer)) != -1) {
                tempOut.write(buffer, 0, bytesRead);
            }

            // 2. 从临时文件读取边界
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(tempOriginalFile.getAbsolutePath(), options);

            // 3. 计算采样率
            options.inJustDecodeBounds = false;
            options.inSampleSize = calculateInSampleSize(options, options.outWidth / 2, options.outHeight / 2);
            options.inPreferredConfig = Bitmap.Config.RGB_565;

            // 4. 解码图片
            Bitmap bitmap = BitmapFactory.decodeFile(tempOriginalFile.getAbsolutePath(), options);
            if (bitmap == null) {
                Log.w("SupportHub SDK FileUtil", "decode bitmap failed");
                return null;
            }

            // 5. 压缩并保存
            Bitmap.CompressFormat format = (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg"))
                    ? Bitmap.CompressFormat.JPEG
                    : Bitmap.CompressFormat.PNG;
            int compressQuality = (format == Bitmap.CompressFormat.JPEG) ? 90 : 100;

            try (FileOutputStream fos = new FileOutputStream(tmpFile)) {
                boolean compressResult = bitmap.compress(format, compressQuality, fos);
                Log.w("SupportHub SDK FileUtil", (format == Bitmap.CompressFormat.JPEG ? "JPEG" : "PNG")
                        + " compressResult: " + compressResult);

                long compressedFileSize = tmpFile.length();
                Log.w("SupportHub SDK FileUtil", fileName + " Compressed image size: " + compressedFileSize + " bytes");

                return tmpFile;
            } catch (IOException e) {
                Log.w("SupportHub SDK FileUtil", "write to file error: " + e);
                if (tmpFile.exists()) tmpFile.delete();
                return null;
            } finally {
                bitmap.recycle();
            }

        } catch (IOException e) {
            Log.w("SupportHub SDK FileUtil", "process image error: " + e);
            return null;
        } finally {
            // 清理临时文件
            if (tempOriginalFile.exists()) {
                tempOriginalFile.delete();
            }
        }
    }
    private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // 原始图片尺寸
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // 计算最大的inSampleSize值，使高度和宽度都大于等于要求的值
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
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
