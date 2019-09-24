package razerdp.demo.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Environment;
import android.os.storage.StorageManager;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import razerdp.demo.app.AppContext;
import razerdp.util.log.PopupLog;

public class FileUtil {

    private static final int IO_BUFFER_SIZE = 1024;

    public static boolean hasSDCard() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    private void writeFile(String fileName, String writeStr) {
        try {

            FileOutputStream fout = new FileOutputStream(fileName);
            byte[] bytes = writeStr.getBytes();

            fout.write(bytes);
            fout.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String readFile(String filePath) {
        String result = "";
        try {
            FileInputStream fileInputStream = new FileInputStream(filePath);
            int length = fileInputStream.available();
            byte[] buffered = new byte[length];
            fileInputStream.read(buffered);

            result = new String(buffered, Charset.defaultCharset());

            fileInputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;

    }

    public static String getFileName(String path) {
        if (TextUtils.isEmpty(path)) return null;
        int start = path.lastIndexOf("/");
        if (start != -1) {
            return path.substring(start + 1);
        } else {
            return null;
        }
    }

    public static String getFileSuffix(String path) {
        if (TextUtils.isEmpty(path)) return null;
        int start = path.lastIndexOf(".");
        if (start != -1) {
            return path.substring(start + 1);
        } else {
            return null;
        }

    }

    /**
     * 如果文件末尾有了"/"则判断是否有多个"/"，是则保留一个，没有则添加
     *
     * @param path
     * @return
     */
    public static String checkFileSeparator(String path) {
        if (path != null && path.length() != 0) {
            if (!path.endsWith(File.separator)) {
                return path.concat(File.separator);
            } else {
                final int sourceStringLength = path.length();
                int index = sourceStringLength - 1;
                while (path.charAt(index) == File.separatorChar) {
                    index--;
                }
                path = path.substring(0, index + 1);
                return path.concat(File.separator);
            }
        }
        return path;
    }

    /**
     * 判断文件是否可读可写
     */
    public static boolean isFileCanReadAndWrite(String filePath) {
        if (null != filePath && filePath.length() > 0) {
            File f = new File(filePath);
            if (null != f && f.exists()) {
                return f.canRead() && f.canWrite();
            }
        }
        return false;
    }

    public static void copy(InputStream in, OutputStream out) throws IOException {
        byte[] b = new byte[IO_BUFFER_SIZE];
        int read;
        while ((read = in.read(b)) != -1) {
            out.write(b, 0, read);
        }
    }

    /**
     * 复制单个文件
     *
     * @param oldPath String 原文件路径
     * @param newPath String 复制后路径
     * @return boolean
     */
    public static boolean copyFile(String oldPath, String newPath) {
        try {
            int byteread = 0;
            File oldfile = new File(oldPath);
            if (oldfile.exists()) { // 文件存在时
                InputStream inStream = new FileInputStream(oldPath); // 读入原文件
                FileOutputStream fs = new FileOutputStream(newPath);
                byte[] buffer = new byte[IO_BUFFER_SIZE];

                while ((byteread = inStream.read(buffer)) != -1) {
                    fs.write(buffer, 0, byteread);
                }
                inStream.close();
                fs.close();
            }
        } catch (Exception e) {
            PopupLog.e(e);
            return false;
        }
        return true;
    }

    /**
     * 写入文件
     *
     * @param strFileName 文件名
     * @param ins         流
     */
    public static void writeToFile(String strFileName, InputStream ins) {
        try {
            File file = new File(strFileName);
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }

            FileOutputStream fouts = new FileOutputStream(file);
            int len;
            int maxSize = 1024 * 1024;
            byte buf[] = new byte[maxSize];
            while ((len = ins.read(buf, 0, maxSize)) != -1) {
                fouts.write(buf, 0, len);
                fouts.flush();
            }

            fouts.close();
        } catch (IOException e) {
            PopupLog.e(e);
        }
    }

    /**
     * 写入文件
     *
     * @param strFileName 文件名
     * @param bytes       bytes
     */
    public static boolean writeToFile(String strFileName, byte[] bytes) {
        try {
            File file = new File(strFileName);

            FileOutputStream fouts = new FileOutputStream(file);
            fouts.write(bytes, 0, bytes.length);
            fouts.flush();
            fouts.close();
            return true;
        } catch (IOException e) {
            PopupLog.e(e);
        }
        return false;
    }

    /**
     * Prints some data to a file using a BufferedWriter
     */
    public static boolean writeToFile(String filename, String data) {
        BufferedWriter bufferedWriter = null;
        try {
            // Construct the BufferedWriter object
            bufferedWriter = new BufferedWriter(new FileWriter(filename));
            // Start writing to the output stream
            bufferedWriter.write(data);
            return true;
        } catch (FileNotFoundException e) {
            PopupLog.e(e);
        } catch (IOException e) {
            PopupLog.e(e);
        } finally {
            // Close the BufferedWriter
            try {
                if (bufferedWriter != null) {
                    bufferedWriter.flush();
                    bufferedWriter.close();
                }
            } catch (IOException e) {
                PopupLog.e(e);
            }
        }
        return false;
    }


    public static void Write(String fileName, String message) {

        try {
            FileOutputStream outSTr = null;
            try {
                outSTr = new FileOutputStream(new File(fileName));
            } catch (FileNotFoundException e) {
                PopupLog.e(e);
            }
            BufferedOutputStream Buff = new BufferedOutputStream(outSTr);
            byte[] bs = message.getBytes();
            Buff.write(bs);
            Buff.flush();
            Buff.close();
        } catch (MalformedURLException e) {
            PopupLog.e(e);
        } catch (IOException e) {
            PopupLog.e(e);
        }
    }

    public static void Write(String fileName, String message, boolean append) {
        try {
            FileOutputStream outSTr = null;
            try {
                outSTr = new FileOutputStream(new File(fileName), append);
            } catch (FileNotFoundException e) {
                PopupLog.e(e);
            }
            BufferedOutputStream Buff = new BufferedOutputStream(outSTr);
            byte[] bs = message.getBytes();
            Buff.write(bs);
            Buff.flush();
            Buff.close();
        } catch (MalformedURLException e) {
            PopupLog.e(e);
        } catch (IOException e) {
            PopupLog.e(e);
        }
    }

    /**
     * 删除文件 删除文件夹里面的所有文件
     *
     * @param path String 路径
     */
    public static void deleteFile(String path) {
        File file = new File(path);
        if (!file.exists()) {
            return;
        }
        if (!file.isDirectory()) {// 如果是文件，则删除文件
            file.delete();
            return;
        }
        File files[] = file.listFiles();
        for (int i = 0; i < files.length; i++) {
            if (files[i].isDirectory()) {
                deleteFile(files[i].getAbsolutePath());// 先删除文件夹里面的文件
            }
            files[i].delete();
        }
        file.delete();
    }

    /**
     * 删除文件 删除文件夹里面的所有文件(此方法和deleteFile(String path)这个方法总体是一样的，只是删除代码部分用的是先改名再删除的方法删除的，为了避免EBUSY (Device or resource busy)的错误)
     *
     * @param path String 路径
     */
    public static void deleteFileSafely(String path) {
        File file = new File(path);
        if (!file.exists()) {
            return;
        }
        if (!file.isDirectory()) {// 如果是文件，则删除文件
            safelyDelete(file);
            return;
        }
        File files[] = file.listFiles();
        for (int i = 0; i < files.length; i++) {
            if (files[i].isDirectory()) {
                deleteFileSafely(files[i].getAbsolutePath());// 先删除文件夹里面的文件
            }
            safelyDelete(files[i]);
        }
        safelyDelete(file);
    }

    /**
     * 先改名，在删除（为了避免EBUSY (Device or resource busy)的错误）
     */
    public static void safelyDelete(File file) {
        if (file == null || !file.exists()) return;
        try {
            final File to = new File(file.getAbsolutePath() + System.currentTimeMillis());
            file.renameTo(to);
            to.delete();
        } catch (Exception e) {
            PopupLog.e(e);
        }
    }

    /**
     * 文件大小
     *
     * @throws Exception
     */
    public static long getFileSize(File file) {
        long size = 0;
        try {
            if (!file.exists()) {
                return size;
            }
            if (!file.isDirectory()) {
                size = file.length();
            } else {
                File[] fileList = file.listFiles();
                for (int i = 0; i < fileList.length; i++) {
                    if (fileList[i].isDirectory()) {
                        size = size + getFileSize(fileList[i]);
                    } else {
                        size = size + fileList[i].length();
                    }
                }
            }

        } catch (Exception e) {
            PopupLog.e(e);
        }
        return size;
    }

    /**
     * @return 文件的大小，带单位(MB、KB等)
     */
    public static String getFileLength(String filePath) {
        try {
            File file = new File(filePath);
            return fileLengthFormat(getFileSize(file));
        } catch (Exception e) {
            PopupLog.e(e);
            return "";
        }
    }

    /**
     * @return 文件的大小，带单位(MB、KB等)
     */
    public static String fileLengthFormat(long length) {
        String lenStr = "";
        DecimalFormat formater = new DecimalFormat("#0.##");
        if (length > 0 && length < 1024) {
            lenStr = formater.format(length) + " Byte";
        } else if (length < 1024 * 1024) {
            lenStr = formater.format(length / 1024.0f) + " KB";
        } else if (length < 1024 * 1024 * 1024) {
            lenStr = formater.format(length / (1024 * 1024.0f)) + " MB";
        } else {
            lenStr = formater.format(length / (1024 * 1024 * 1024.0f)) + " GB";
        }
        return lenStr;
    }

    /**
     * 得到文件的类型。 实际上就是得到文件名中最后一个“.”后面的部分。
     *
     * @param fileName 文件名
     * @return 文件名中的类型部分
     */
    public static String pathExtension(String fileName) {
        int point = fileName.lastIndexOf('.');
        int length = fileName.length();
        if (point == -1 || point == length - 1) {
            return "";
        } else {
            return fileName.substring(point, length);
        }
    }

    /**
     * 根据文件后缀名获得对应的MIME类型。
     */
    @SuppressLint("DefaultLocale")
    public static String getMIMEType(File file) {

        String type = "*/*";
        String fName = file.getName();
        // 获取后缀名前的分隔符"."在fName中的位置。
        int dotIndex = fName.lastIndexOf(".");
        if (dotIndex < 0) {
            return type;
        }
        /* 获取文件的后缀名 */
        String end = fName.substring(dotIndex, fName.length()).toLowerCase();
        if (end == "") {
            return type;
        }
        // 在MIME和文件类型的匹配表中找到对应的MIME类型。
        for (int i = 0; i < MIME_MapTable.length; i++) {
            if (end.equals(MIME_MapTable[i][0])) {
                type = MIME_MapTable[i][1];
            }
        }
        return type;
    }

    public static void moveFile(String oldPath, String newPath) {
        copyFile(oldPath, newPath);
        delFile(oldPath);
    }

    public static void delFile(String filePathAndName) {
        try {
            File myDelFile = new File(filePathAndName);
            myDelFile.delete();
        } catch (Exception e) {
            System.out.println("删除文件操作出错");
            e.printStackTrace();
        }
    }

    private static final String[][] MIME_MapTable = {
            // {后缀名， MIME类型}
            {".doc", "application/msword"}, {".docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document"},
            {".xls", "application/vnd.ms-excel"}, {".xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"},
            {".pdf", "application/pdf"}, {".ppt", "application/vnd.ms-powerpoint"},
            {".pptx", "application/vnd.openxmlformats-officedocument.presentationml.presentation"}, {".txt", "text/plain"},
            {".wps", "application/vnd.ms-works"}, {"", "*/*"}
    };

    /**
     * 解压文件
     */
    public static boolean Unzip(String zipFile, String targetDir) {
        try {
            int BUFFER = 4096; // 这里缓冲区我们使用4KB，
            String strEntry; // 保存每个zip的条目名称

            BufferedOutputStream dest = null; // 缓冲输出流
            FileInputStream fis = new FileInputStream(zipFile);
            ZipInputStream zis = new ZipInputStream(new BufferedInputStream(fis));
            ZipEntry entry; // 每个zip条目的实例

            while ((entry = zis.getNextEntry()) != null) {

                int count;
                byte data[] = new byte[BUFFER];
                strEntry = entry.getName();

                File entryFile = new File(targetDir + strEntry);
                File entryDir = new File(entryFile.getParent());
                if (!entryDir.exists()) {
                    entryDir.mkdirs();
                }

                FileOutputStream fos = new FileOutputStream(entryFile);
                dest = new BufferedOutputStream(fos, BUFFER);
                while ((count = zis.read(data, 0, BUFFER)) != -1) {
                    dest.write(data, 0, count);
                }
                dest.flush();
                dest.close();
            }
            zis.close();
            return true;
        } catch (IOException e) {
            return true;
        }
    }

    public void listMemoFolder() {

    }

    /**
     * 删除指定目录下文件及目录
     */
    public static void deleteFolderFile(String filePath, boolean deleteThisPath) {
        if (!TextUtils.isEmpty(filePath)) {
            File file = new File(filePath);

            if (file.isDirectory()) {
                File files[] = file.listFiles();
                for (int i = 0; i < files.length; i++) {
                    deleteFolderFile(files[i].getAbsolutePath(), true);
                }
            }
            if (deleteThisPath) {
                if (!file.isDirectory()) {
                    file.delete();
                } else {
                    if (file.listFiles().length == 0) {
                        file.delete();
                    }
                }
            }
        }
    }

    public static String getFromAssets(String fileName) {
        String result = "";
        try {
            InputStream in = AppContext.getResources().getAssets().open(fileName);
            byte[] buffered = new byte[in.available()];
            in.read(buffered);
            result = new String(buffered, StandardCharsets.UTF_8);
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static InputStream getAssetsInputStream(String fileName) {
        try {
            return AppContext.getAppContext().getResources().getAssets().open(fileName);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 得到内置或外置SD卡的路径
     *
     * @param mContext
     * @param is_removale true=外置SD卡
     * @return
     */
    public static String getStoragePath(Context mContext, boolean is_removale) {
        StorageManager mStorageManager = (StorageManager) mContext.getSystemService(Context.STORAGE_SERVICE);
        Class<?> storageVolumeClazz = null;
        try {
            storageVolumeClazz = Class.forName("android.os.storage.StorageVolume");
            Method getVolumeList = mStorageManager.getClass().getMethod("getVolumeList");
            Method getPath = storageVolumeClazz.getMethod("getUrl");
            Method isRemovable = storageVolumeClazz.getMethod("isRemovable");
            Object result = getVolumeList.invoke(mStorageManager);
            final int length = Array.getLength(result);
            for (int i = 0; i < length; i++) {
                Object storageVolumeElement = Array.get(result, i);
                String path = (String) getPath.invoke(storageVolumeElement);
                boolean removable = (Boolean) isRemovable.invoke(storageVolumeElement);
                if (is_removale == removable) {
                    return path;
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean createOrExistsDir(final File file) {
        return file != null && (file.exists() ? file.isDirectory() : file.mkdirs());
    }

    public static boolean createFile(final File file, boolean deleteOld) {
        if (file == null) return false;
        if (deleteOld && file.exists() && !file.delete()) return false;
        if (!createOrExistsDir(file.getParentFile())) return false;
        try {
            return file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static File createFile(File zipdir, File zipfile) {
        if (!zipdir.exists()) {
            boolean result = zipdir.mkdirs();
            PopupLog.d("TAG", "zipdir.mkdirs() = " + result);
        }
        if (!zipfile.exists()) {
            try {
                boolean result = zipfile.createNewFile();
                PopupLog.d("zipdir.createNewFile() = " + result);
            } catch (IOException e) {
                e.printStackTrace();
                Log.e("TAG", e.getMessage());
            }
        }
        return zipfile;
    }

    /**
     * 递归删除目录下的所有文件及子目录下所有文件
     *
     * @param dir 将要删除的文件目录
     * @return 删除成功返回true，否则返回false
     */
    public static boolean deleteDir(File dir) {
        if (dir == null || !dir.exists()) return true;
        if (dir.isDirectory()) {
            String[] children = dir.list();
            // 递归删除目录中的子目录下
            for (String aChildren : children) {
                boolean success = deleteDir(new File(dir, aChildren));
                if (!success) {
                    return false;
                }
            }
        }
        // 目录此时为空，可以删除
        return dir.delete();
    }

    /**
     * 获取文件夹的大小
     *
     * @param directory 需要测量大小的文件夹
     * @return 返回文件夹大小，单位byte
     */
    public static long folderSize(File directory) {
        long length = 0;
        for (File file : directory.listFiles()) {
            if (file.isFile())
                length += file.length();
            else
                length += folderSize(file);
        }
        return length;
    }


    public static int getInputStreamLength(InputStream in) throws IOException {
        int ch1 = in.read();
        int ch2 = in.read();
        int ch3 = in.read();
        int ch4 = in.read();
        if ((ch1 | ch2 | ch3 | ch4) < 0)
            throw new EOFException();
        return ((ch1 << 24) + (ch2 << 16) + (ch3 << 8) + (ch4 << 0));
    }
}
