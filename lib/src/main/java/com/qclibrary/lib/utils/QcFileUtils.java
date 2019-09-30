package com.qclibrary.lib.utils;

import android.util.Log;

import com.qclibrary.lib.io.gson.GsonSerializer;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class QcFileUtils {

    public static final int KB = 1024;
    public static final int MB = 1048576;
    public static final int GB = 1073741824;

    /*----------------------------------- Assets--------------------------------------------*/

    public static String getAssetsData(String path) {
        String result = "";
        try {
            InputStream mAssets = QcUtils.getApplication().getAssets().open(path);
            int lenght = mAssets.available();
            byte[] buffer = new byte[lenght];
            mAssets.read(buffer);
            mAssets.close();
            result = new String(buffer);
            return result;
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("error", e.getMessage());
            return result;
        }
    }

    public static <T> T getAssetWithGson(String path, Class<T> classOf) {
        String json = getAssetsData(path);
        T data = null;
        try {
            data = (T) GsonSerializer.getInstance().fromJson(json, classOf);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    /*----------------------------------- Files--------------------------------------------*/

    public static String getFileContent(File file){
        InputStream instream  = null;
        try {
            instream = new FileInputStream(file);
            if (instream != null) {
                InputStreamReader inputreader = new InputStreamReader(instream, "UTF-8");
                BufferedReader bufReader = new BufferedReader(inputreader);
                String line;
                StringBuilder result = new StringBuilder();
                while ((line = bufReader.readLine()) != null) {
                    result.append(line);
                }
                return result.toString();
            }
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (instream != null) {
                    instream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }



    public static boolean unzipFile(String zipFilePath, String destDirPath)
            throws IOException {
        return unzipFile(getFileByPath(zipFilePath), getFileByPath(destDirPath));
    }

    public static boolean unzipFile(File zipFile, File destDir)
            throws IOException {
        return unzipFileByKeyword(zipFile, destDir, null) != null;
    }

    public static List<File> unzipFileByKeyword(File zipFile, File destDir, String keyword)
            throws IOException {
        if (zipFile == null || destDir == null) return null;
        List<File> files = new ArrayList<>();
        ZipFile zf = new ZipFile(zipFile);
        Enumeration<?> entries = zf.entries();
        while (entries.hasMoreElements()) {
            ZipEntry entry = ((ZipEntry) entries.nextElement());
            String entryName = entry.getName();
            if (QcStringUtils.isEmpty(keyword) || getFileName(entryName).toLowerCase().contains(keyword.toLowerCase())) {
                String filePath = destDir + File.separator + entryName;
                File file = new File(filePath);
                files.add(file);
                if (entry.isDirectory()) {
                    if (!createOrExistsDir(file)) return null;
                } else {
                    if (!createOrExistsFile(file)) return null;
                    InputStream in = null;
                    OutputStream out = null;
                    try {
                        in = new BufferedInputStream(zf.getInputStream(entry));
                        out = new BufferedOutputStream(new FileOutputStream(file));
                        byte buffer[] = new byte[KB];
                        int len;
                        while ((len = in.read(buffer)) != -1) {
                            out.write(buffer, 0, len);
                        }
                    } finally {
                        closeIO(in, out);
                    }
                }
            }
        }
        return files;
    }

    public static String getFileName(String filePath) {
        if (QcStringUtils.isSpace(filePath)) return filePath;
        int lastSep = filePath.lastIndexOf(File.separator);
        return lastSep == -1 ? filePath : filePath.substring(lastSep + 1);
    }

    public static File getFileByPath(String filePath) {
        return QcStringUtils.isSpace(filePath) ? null : new File(filePath);
    }

    public static boolean createOrExistsDir(File file) {
        return file != null && (file.exists() ? file.isDirectory() : file.mkdirs());
    }

    public static boolean createOrExistsFile(File file) {
        if (file == null) return false;
        if (file.exists()) return file.isFile();
        if (!createOrExistsDir(file.getParentFile())) return false;
        try {
            return file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void closeIO(Closeable... closeables) {
        if (closeables == null) return;
        for (Closeable closeable : closeables) {
            if (closeable != null) {
                try {
                    closeable.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static List<File> listFilesInDirWithFilter(String dirPath, String suffix, boolean isRecursive) {
        return listFilesInDirWithFilter(getFileByPath(dirPath), suffix, isRecursive);
    }

    public static List<File> listFilesInDirWithFilter(File dir, String suffix, boolean isRecursive) {
        if (isRecursive) return listFilesInDirWithFilter(dir, suffix);
        if (dir == null || !isDirectory(dir)) return null;
        List<File> list = new ArrayList<>();
        File[] files = dir.listFiles();
        if (files != null && files.length != 0) {
            for (File file : files) {
                if (file.getName().toUpperCase().endsWith(suffix.toUpperCase())) {
                    list.add(file);
                }
            }
        }
        return list;
    }

    public static List<File> listFilesInDirWithFilter(File dir, String suffix) {
        if (dir == null || !isDirectory(dir)) return null;
        List<File> list = new ArrayList<>();
        File[] files = dir.listFiles();
        if (files != null && files.length != 0) {
            for (File file : files) {
                if (file.getName().toUpperCase().endsWith(suffix.toUpperCase())) {
                    list.add(file);
                }
                if (file.isDirectory()) {
                    list.addAll(listFilesInDirWithFilter(file, suffix));
                }
            }
        }
        return list;
    }

    public static boolean isDirectory(File file) {
        return isFileExists(file) && file.isDirectory();
    }

    public static boolean isFileExists(File file) {
        return file != null && file.exists();
    }


}
