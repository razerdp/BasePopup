package razerdp.demo.utils;

import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.provider.MediaStore;
import android.view.View;


import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import razerdp.demo.app.AppContext;

import static android.graphics.Bitmap.CompressFormat;
import static android.graphics.Bitmap.Config;
import static android.graphics.Bitmap.createBitmap;

/**
 * Created by 大灯泡 on 2019/5/9
 * <p>
 * Description：
 */
public class ImageUtil {

    public enum ImageType {
        NORMAL,
        GIF,
        WEBP,
        ANIMATED_WEBP
    }

    public static ImageType getImageType(File file) {
        ImageType type = ImageType.NORMAL;
        try {
            FileInputStream inputStream = new FileInputStream(file);

            byte[] header = new byte[20];
            int read = inputStream.read(header);
            if (read >= 3 && isGifHeader(header)) {
                type = ImageType.GIF;
            } else if (read >= 12 && isWebpHeader(header)) {
                if (read >= 17 && isExtendedWebp(header)
                        && (header[16] & 0x02) != 0) {
                    type = ImageType.ANIMATED_WEBP;
                } else {
                    type = ImageType.WEBP;
                }
            }

            inputStream.close();
        } catch (IOException e) {
        }

        return type;
    }


    private static boolean isGifHeader(byte[] header) {
        return header[0] == 'G' && header[1] == 'I' && header[2] == 'F';
    }

    private static boolean isWebpHeader(byte[] header) {
        return header[0] == 'R' && header[1] == 'I' && header[2] == 'F' && header[3] == 'F'
                && header[8] == 'W' && header[9] == 'E' && header[10] == 'B' && header[11] == 'P';
    }

    private static boolean isExtendedWebp(byte[] header) {
        return header[12] == 'V' && header[13] == 'P' && header[14] == '8' && header[15] == 'X';
    }


    public static boolean saveBitmap(final Bitmap src,
                                     final String filePath,
                                     final CompressFormat format,
                                     final boolean sendBoardcast) {
        return saveBitmap(src, new File(filePath), format, false, sendBoardcast);
    }

    public static boolean saveBitmap(final Bitmap src, final File file, final CompressFormat format, final boolean sendBoardcast) {
        return saveBitmap(src, file, format, false, sendBoardcast);
    }

    public static boolean saveBitmap(final Bitmap src,
                                     final String filePath,
                                     final CompressFormat format,
                                     final boolean recycle,
                                     final boolean sendBoardcast) {
        return saveBitmap(src, new File(filePath), format, recycle, sendBoardcast);
    }

    public static boolean saveBitmap(final Bitmap src,
                                     final File file,
                                     final CompressFormat format,
                                     final boolean recycle,
                                     final boolean sendBoardcast) {
        return saveBitmap(src, file, format, 100, recycle, sendBoardcast);
    }

    public static boolean saveBitmap(final Bitmap src,
                                     final File file,
                                     final CompressFormat format,
                                     final int quality,
                                     final boolean recycle,
                                     final boolean sendBoardcast) {
        if (isEmptyBitmap(src) || !FileUtil.createFile(file, true)) return false;
        OutputStream os = null;
        boolean ret = false;
        try {
            os = new BufferedOutputStream(new FileOutputStream(file));
            ret = src.compress(format, quality, os);
            if (recycle && !src.isRecycled()) src.recycle();
            if (sendBoardcast) {
                if (AppUtil.isOver10()) {
                    ContentValues values = new ContentValues();
                    values.put(MediaStore.Images.Media.DATA, file.getAbsolutePath());
                    values.put(MediaStore.Images.Media.MIME_TYPE, "image/*");
                    AppContext.getAppContext().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                } else {
                    MediaStore.Images.Media.insertImage(AppContext.getAppContext().getContentResolver(), file.getAbsolutePath(), file.getName(), null);
                }
                // 通知图库更新（android 10适配）
                AppContext.getAppContext().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, UriUtil.getUri(file)));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return ret;
    }

    private static boolean isEmptyBitmap(final Bitmap src) {
        return src == null || src.getWidth() == 0 || src.getHeight() == 0;
    }

    public static Bitmap drawable2Bitmap(final Drawable drawable) {
        return drawable2Bitmap(drawable, null);
    }

    public static Bitmap drawable2Bitmap(final Drawable drawable, Bitmap bitmap) {
        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if (bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }
        if (bitmap == null) {
            if (drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
                bitmap = createBitmap(1, 1,
                        drawable.getOpacity() != PixelFormat.OPAQUE
                                ? Config.ARGB_8888
                                : Config.RGB_565);
            } else {
                bitmap = createBitmap(drawable.getIntrinsicWidth(),
                        drawable.getIntrinsicHeight(),
                        drawable.getOpacity() != PixelFormat.OPAQUE
                                ? Config.ARGB_8888
                                : Config.RGB_565);
            }
        }
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    public static Bitmap view2Bitmap(final View view) {
        if (view == null) return null;
        Bitmap result = createBitmap(view.getWidth(),
                view.getHeight(),
                Config.ARGB_8888);
        Canvas canvas = new Canvas(result);
        Drawable bgDrawable = view.getBackground();
        if (bgDrawable != null) {
            bgDrawable.draw(canvas);
        } else {
            canvas.drawColor(Color.WHITE);
        }
        view.draw(canvas);
        return result;
    }

    public static Bitmap getBitmap(final File file, final int maxWidth, final int maxHeight) {
        if (file == null) return null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(file.getAbsolutePath(), options);
        options.inSampleSize = calculateInSampleSize(options, maxWidth, maxHeight);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(file.getAbsolutePath(), options);
    }

    private static int calculateInSampleSize(final BitmapFactory.Options options,
                                             final int maxWidth,
                                             final int maxHeight) {
        int height = options.outHeight;
        int width = options.outWidth;
        int inSampleSize = 1;
        while (height > maxHeight || width > maxWidth) {
            height >>= 1;
            width >>= 1;
            inSampleSize <<= 1;
        }
        return inSampleSize;
    }

    public static Bitmap compressSampleSize(final Bitmap src,
                                            final int maxWidth,
                                            final int maxHeight,
                                            final boolean recycle) {
        if (isEmptyBitmap(src)) return null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        src.compress(CompressFormat.JPEG, 100, baos);
        byte[] bytes = baos.toByteArray();
        BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
        options.inSampleSize = calculateInSampleSize(options, maxWidth, maxHeight);
        options.inJustDecodeBounds = false;
        if (recycle && !src.isRecycled()) src.recycle();
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
    }

    public static Bitmap compressQuality(final Bitmap src,
                                         final long maxByteSize,
                                         final boolean recycle) {
        if (isEmptyBitmap(src) || maxByteSize <= 0) return null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        src.compress(CompressFormat.JPEG, 100, baos);
        byte[] bytes;
        if (baos.size() <= maxByteSize) {
            bytes = baos.toByteArray();
        } else {
            baos.reset();
            src.compress(CompressFormat.JPEG, 0, baos);
            if (baos.size() >= maxByteSize) {
                bytes = baos.toByteArray();
            } else {
                // find the best quality using binary search
                int st = 0;
                int end = 100;
                int mid = 0;
                while (st < end) {
                    mid = (st + end) / 2;
                    baos.reset();
                    src.compress(CompressFormat.JPEG, mid, baos);
                    int len = baos.size();
                    if (len == maxByteSize) {
                        break;
                    } else if (len > maxByteSize) {
                        end = mid - 1;
                    } else {
                        st = mid + 1;
                    }
                }
                if (end == mid - 1) {
                    baos.reset();
                    src.compress(CompressFormat.JPEG, st, baos);
                }
                bytes = baos.toByteArray();
            }
        }
        if (recycle && !src.isRecycled()) src.recycle();
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    public static Bitmap scale(final Bitmap src,
                               final float scaleWidth,
                               final float scaleHeight,
                               final boolean recycle) {
        if (isEmptyBitmap(src)) return null;
        Matrix matrix = new Matrix();
        matrix.setScale(scaleWidth, scaleHeight);
        Bitmap ret = Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(), matrix, true);
        if (recycle && !src.isRecycled() && ret != src) src.recycle();
        return ret;
    }

    public static Bitmap crop(final Bitmap src,
                              final int x,
                              final int y,
                              final int width,
                              final int height,
                              final boolean recycle) {
        if (isEmptyBitmap(src)) return null;
        Bitmap ret = Bitmap.createBitmap(src, x, y, width, height);
        if (recycle && !src.isRecycled() && ret != src) src.recycle();
        return ret;
    }
}
