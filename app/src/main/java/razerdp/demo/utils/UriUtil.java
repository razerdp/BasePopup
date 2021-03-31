package razerdp.demo.utils;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;

import java.io.File;

import razerdp.demo.app.AppContext;
import razerdp.util.log.PopupLog;

/**
 * Created by 大灯泡 on 2019/4/19.
 */
public class UriUtil {

    private static final String PARSE_RES = "android.resource://" + AppContext.getAppContext()
            .getPackageName() + "/";


    public static Uri getUri(@NonNull final File file) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                String authority = AppContext.getAppInstance().getPackageName() + ".fileprovider";
                return FileProvider.getUriForFile(AppContext.getAppContext(), authority, file);
            } else {
                return Uri.fromFile(file);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Uri getUri(int resid) {
        if (resid == 0) return null;
        return Uri.parse(PARSE_RES + resid);
    }

    public static File toFile(@NonNull final Uri uri) {
        PopupLog.d("UriUtil", uri.toString());
        String authority = uri.getAuthority();
        String scheme = uri.getScheme();
        String path = uri.getPath();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N
                && path != null && path.startsWith("/external")) {
            int secondSeparator = path.indexOf("/", "/external".length());
            path = path.substring(secondSeparator);
            return new File(Environment.getExternalStorageDirectory().getAbsolutePath()
                    + path.replace("/external", ""));
        }
        if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            if (path != null) return new File(path);
            PopupLog.e("UriUtil", uri.toString() + " parse failed. -> 0");
            return null;
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT
                && DocumentsContract.isDocumentUri(AppContext.getAppContext(), uri)) {
            if ("com.android.externalstorage.documents".equals(authority)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                if ("primary".equalsIgnoreCase(type)) {
                    return new File(Environment.getExternalStorageDirectory() + "/" + split[1]);
                }
                PopupLog.e("UriUtil", uri.toString() + " parse failed. -> 1");
                return null;
            } else if ("com.android.providers.downloads.documents".equals(authority)) {
                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"),
                        Long.valueOf(id)
                );
                return getFileFromUri(contentUri, 2);
            } else if ("com.android.providers.media.documents".equals(authority)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                Uri contentUri;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                } else {
                    PopupLog.d("UriUtil", uri.toString() + " parse failed. -> 3");
                    return null;
                }
                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{split[1]};
                return getFileFromUri(contentUri, selection, selectionArgs, 4);
            } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
                return getFileFromUri(uri, 5);
            } else {
                PopupLog.e("UriUtil", uri.toString() + " parse failed. -> 6");
                return null;
            }
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            return getFileFromUri(uri, 7);
        } else {
            PopupLog.e("UriUtil", uri.toString() + " parse failed. -> 8");
            return null;
        }
    }

    private static File getFileFromUri(final Uri uri, final int code) {
        return getFileFromUri(uri, null, null, code);
    }

    private static File getFileFromUri(final Uri uri,
                                       final String selection,
                                       final String[] selectionArgs,
                                       final int code) {
        final Cursor cursor = AppContext.getAppContext().getContentResolver().query(
                uri, new String[]{"_data"}, selection, selectionArgs, null);
        if (cursor == null) {
            PopupLog.e("UriUtil", uri.toString() + " parse failed(cursor is null). -> " + code);
            return null;
        }
        try {
            if (cursor.moveToFirst()) {
                final int columnIndex = cursor.getColumnIndex("_data");
                if (columnIndex > -1) {
                    return new File(cursor.getString(columnIndex));
                } else {
                    PopupLog.e("UriUtil", uri.toString() + " parse failed(columnIndex: " + columnIndex + " is wrong). -> " + code);
                    return null;
                }
            } else {
                PopupLog.e("UriUtil", uri.toString() + " parse failed(moveToFirst return false). -> " + code);
                return null;
            }
        } catch (Exception e) {
            PopupLog.e("UriUtil", uri.toString() + " parse failed. -> " + code);
            return null;
        } finally {
            cursor.close();
        }
    }


    public static String getFilePath(final Uri uri) {
        File file = toFile(uri);
        if (file == null || !file.exists()) {
            return null;
        }
        return file.getAbsolutePath();
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    public static String selectImage(Context context, Intent data) {
        Uri selectedImage = data.getData();
//		PopupLog.e(TAG, selectedImage.toString());
        if (selectedImage != null) {
            String uriStr = selectedImage.toString();
            String path = uriStr.substring(10, uriStr.length());
            if (path.startsWith("com.sec.android.gallery3d")) {
                return null;
            }
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            String picturePath = null;
            Cursor cursor = context.getContentResolver()
                    .query(selectedImage, filePathColumn, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                picturePath = cursor.getString(columnIndex);
                cursor.close();
            }
            return picturePath;
        } else {
            return null;
        }
    }
}
