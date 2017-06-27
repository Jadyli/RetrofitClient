package com.jady.sample.utils;

import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

/**
 * @Description: Created by jadyli on 2017/4/28.
 */
public class FileUtils {

    private static final String APP_NAME = "RetrofitClient";
    public static final String IMAGE_PATH = "image";
    public static final String THUMBNAIL_PATH = "image/thumbnail";
    public static final String THEME_PATH = "theme";
    public static final String HTTP_PATH = "http";
    public static final String TASK_PATH = "task";
    public static final int FILE_SELECT_CODE = 1000;
    public final static int REQUEST_CODE_GALLERY = 0x11;
    public final static int REQUEST_CODE_CAMERA = 0x12;
    public final static int REQUEST_CODE_CROP = 0x13;

    public static String getHttpDir(Context context) {
        return getInternalDir(context, HTTP_PATH, true);
    }

    /**
     * 获取当前可用文件夹
     *
     * @param context
     * @param path
     * @return
     */
    private static String getInternalDir(Context context, String path, boolean isCache) {
        File dir;
        if (isCache) {
            dir = new File(context.getCacheDir(), path);
        } else {
            dir = new File(context.getFilesDir(), path);
        }
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dir.getAbsolutePath();
    }

    /**
     * 获取外部存储卡文件夹
     *
     * @param context
     * @param path
     * @return
     */
    private static String getExternalDir(Context context, String path) {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equalsIgnoreCase(state)) {
            File root = Environment.getExternalStorageDirectory();

            File dir = new File(root, APP_NAME + File.separator + path);

            if (!dir.exists()) {
                dir.mkdirs();
            }
            return dir.getAbsolutePath();
        }
        return getInternalDir(context, path, false);
    }

    public static String getLocalImagePath(Context context, String url) {
        if (TextUtils.isEmpty(url)) {
            return "";
        }
        if (new File(url).exists()) {
            return url;
        }
        try {
            String imgName = url.substring(url.lastIndexOf("/") + 1);
            File imgFile = new File(getInternalDir(context, IMAGE_PATH, false), imgName);
            if (!imgFile.exists()) {
                return "";
            }
            return imgFile.getAbsolutePath();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }


    public static void showFileChooser(Activity activity) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        try {
            activity.startActivityForResult(Intent.createChooser(intent, "Select a File to Upload"), FILE_SELECT_CODE);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(activity, "Please install a File Manager.", Toast.LENGTH_SHORT).show();
        }
    }

    public static void showFileChooser(Fragment fragment, int requestCode) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        try {
            fragment.startActivityForResult(Intent.createChooser(intent, "Select a File to Upload"), requestCode);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(fragment.getActivity(), "Please install a File Manager.", Toast.LENGTH_SHORT).show();
        }
    }

    public static String getImageFromCamera(Fragment context) {
        File file = createNewFile(getExternalDir(context.getActivity(), IMAGE_PATH), createFileNmae(".jpg"));
        //通过FileProvider创建一个content类型的Uri
        Uri imageUri = FileProvider.getUriForFile(context.getActivity(), "com.jady.sample.fileprovider", file);
        Intent intent = new Intent();
        //添加这一句表示对目标应用临时授权该Uri所代表的文件
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        //设置Action为拍照
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        //将拍取的照片保存到指定URI
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        context.startActivityForResult(intent, REQUEST_CODE_CAMERA);
        return file.getAbsolutePath();
    }

    public static String getImageFromCamera(Activity context) {
        File file = createNewFile(getExternalDir(context, IMAGE_PATH), createFileNmae(".jpg"));
        //通过FileProvider创建一个content类型的Uri
        Uri imageUri = FileProvider.getUriForFile(context, "com.jady.sample.fileprovider", file);
        Intent intent = new Intent();
        //添加这一句表示对目标应用临时授权该Uri所代表的文件
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        //设置Action为拍照
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        //将拍取的照片保存到指定URI
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        context.startActivityForResult(intent, REQUEST_CODE_CAMERA);
        return file.getAbsolutePath();
    }

    public static String getImageFromGallery(Activity context) {
        File newFile = createNewFile(getExternalDir(context, IMAGE_PATH), createFileNmae(""));
        Uri uri = Uri.fromFile(newFile);
        Intent intent = new Intent("android.intent.action.PICK");
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                "image/*");
        intent.putExtra("output", uri);
        intent.putExtra("aspectX", 1);// 裁剪框比例
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 300);// 输出图片大小
        intent.putExtra("outputY", 300);
        context.startActivityForResult(intent, REQUEST_CODE_GALLERY);
        return newFile.getAbsolutePath();
    }

    public static String getImageFromGallery(Fragment context) {
        File newFile = createNewFile(getExternalDir(context.getActivity(), IMAGE_PATH), createFileNmae(".jpg"));
        Uri uri = Uri.fromFile(newFile);
        Intent intent = new Intent("android.intent.action.PICK");
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                "image/*");
        intent.putExtra("output", uri);
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);// 裁剪框比例
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 300);// 输出图片大小
        intent.putExtra("outputY", 300);
        context.startActivityForResult(intent, REQUEST_CODE_GALLERY);
        return newFile.getAbsolutePath();
    }

    public static void getImageFromCrop(Activity context, File file) {
        //通过FileProvider创建一个content类型的Uri
        Uri imageUri = FileProvider.getUriForFile(context, "com.jady.sample.fileprovider", file);
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setDataAndType(imageUri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("scale", true);
        intent.putExtra("outputX", 300);// 输出图片大小
        intent.putExtra("outputY", 300);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true); // no face detection
        context.startActivityForResult(intent, REQUEST_CODE_CROP);
    }

    public static void getImageFromCrop(Fragment context, File file) {
        //通过FileProvider创建一个content类型的Uri
        Uri imageUri = FileProvider.getUriForFile(context.getActivity(), "com.jady.sample.fileprovider", file);
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setDataAndType(imageUri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("scale", true);
        intent.putExtra("outputX", 300);// 输出图片大小
        intent.putExtra("outputY", 300);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true); // no face detection
        context.startActivityForResult(intent, REQUEST_CODE_CROP);
    }

    /**
     * 获取可读的文件大小
     */
    public static String getReadableFileSize(int size) {
        final int BYTES_IN_KILOBYTES = 1024;
        final DecimalFormat dec = new DecimalFormat("###.#");
        final String KILOBYTES = " KB";
        final String MEGABYTES = " MB";
        final String GIGABYTES = " GB";
        float fileSize = 0;
        String suffix = KILOBYTES;
        if (size > BYTES_IN_KILOBYTES) {
            fileSize = size / BYTES_IN_KILOBYTES;
            if (fileSize > BYTES_IN_KILOBYTES) {
                fileSize = fileSize / BYTES_IN_KILOBYTES;
                if (fileSize > BYTES_IN_KILOBYTES) {
                    fileSize = fileSize / BYTES_IN_KILOBYTES;
                    suffix = GIGABYTES;
                } else {
                    suffix = MEGABYTES;
                }
            }
        }
        return String.valueOf(dec.format(fileSize) + suffix);
    }

    /**
     * 获取文件的文件名(不包括扩展名)
     */
    public static String getFileNameWithoutExtension(String path) {
        if (path == null) {
            return null;
        }
        int separatorIndex = path.lastIndexOf(File.separator);
        if (separatorIndex < 0) {
            separatorIndex = 0;
        }
        int dotIndex = path.lastIndexOf(".");
        if (dotIndex < 0) {
            dotIndex = path.length();
        } else if (dotIndex < separatorIndex) {
            dotIndex = path.length();
        }
        return path.substring(separatorIndex + 1, dotIndex);
    }

    /**
     * 获取文件名
     */
    public static String getFileName(String path) {
        if (path == null) {
            return null;
        }
        int separatorIndex = path.lastIndexOf(File.separator);
        return (separatorIndex < 0) ? path : path.substring(separatorIndex + 1, path.length());
    }

    /**
     * 获取扩展名
     */
    public static String getExtension(String path) {
        if (path == null) {
            return null;
        }
        int dot = path.lastIndexOf(".");
        if (dot >= 0) {
            return path.substring(dot);
        } else {
            return "";
        }
    }

    public static File getUriFile(Context context, Uri uri) {
        String path = getUriPath(context, uri);
        if (path == null) {
            return null;
        }
        return new File(path);
    }

    public static String getUriPath(Context context, Uri uri) {
        if (uri == null) {
            return null;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && DocumentsContract.isDocumentUri(context, uri)) {
            if ("com.android.externalstorage.documents".equals(uri.getAuthority())) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                return getDataColumn(context, contentUri, null, null);
            } else if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{split[1]};
                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            if ("com.google.android.apps.photos.content".equals(uri.getAuthority())) {
                return uri.getLastPathSegment();
            }
            return getDataColumn(context, uri, null, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null) cursor.close();
        }
        return null;
    }

    public static File createNewFile(String dir, String fileName) {
        return createNewFile(dir + File.separator + fileName);
    }

    public static File createNewFile(String absolutePath) {
//        if (!Environment.MEDIA_MOUNTED.equalsIgnoreCase(Environment.getExternalStorageState())) {
//            return null;
//        }

        if (TextUtils.isEmpty(absolutePath)) {
            return null;
        }

        File file = new File(absolutePath);
        if (file.exists()) {
            return file;
        } else {
            File dir = file.getParentFile();
            if (!dir.exists()) {
                dir.mkdirs();
            }

            try {
                if (file.createNewFile()) {
                    return file;
                }
            } catch (IOException e) {
                return null;
            }
        }
        return null;
    }

    /**
     * @param extension 后缀名 如".jpg"
     * @return
     */
    public static String createFileNmae(String extension) {
//        //注意必须加上毫秒,因为有时候代码会创建两个文件,时间短,文件会重名
        DateFormat format = new SimpleDateFormat("yyyyMMddHHmmssSSS", Locale.getDefault());
        // 转换为字符串
        String formatDate = format.format(new Date());

        String name = formatDate + new Random().nextInt(10000);
        //查看是否带"."
        if (!TextUtils.isEmpty(extension) && !extension.startsWith("."))
            extension = "." + extension;
        return name + extension;
    }
}
