package com.qysd.lawtree.lawtreeutils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Environment;
import android.text.TextUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 文件工具类
 */
public class FileUtils {
    //分隔符
    public static final String FILE_SEPARATOR = File.separator;
    //文件缓冲的大小
    private static final int BYTE_SIZE = 1024;

    private static final String TXT = ".TXT";//TXT文档

    private static final String DOC = ".DOC";//DOC文档

    private static final String XLS = ".XLS";//XLS文档

    private static final String PPT = ".PPT";//PPT文档

    private static final String PDF = ".PDF";//PDF文档

    private static final String[] filterFields = new String[]{TXT, DOC, XLS, PPT, PDF};

    /**
     * 判断外部储存是否挂载（判断手机sd卡是否存在）
     *
     * @return true表示存在
     */
    private static boolean externalMemoryAvailable() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    /**
     * @param fileName    文件名
     * @param isImagePath 是否是图片路径
     * @return 文件路径
     */
    public static String getExternalDestination(Context context, String fileName, boolean isImagePath) {
        if (externalMemoryAvailable()) {
            //判断sdk卡是否有写的权限
            if (Environment.getExternalStorageDirectory().canWrite()) {
                if (isImagePath) {
                    return Environment.getExternalStorageDirectory().getAbsolutePath() + FILE_SEPARATOR +
                            Constant.SD_PATH + FILE_SEPARATOR;
                } else {
                    return Environment.getExternalStorageDirectory().getAbsolutePath() + FILE_SEPARATOR +
                            Constant.SD_PATH + FILE_SEPARATOR + fileName;
                }
            }
        } else {
            return createIfNotExist(context, fileName);
        }
        return "";
    }


    /**
     * 获取应用的内部文件目录
     *
     * @param context 上下文
     * @return 应用的内部储存
     */
    public static String getFilesDir(Context context) {
        String filePath = context.getFilesDir().getAbsolutePath();
        return filePath;
    }

    /**
     * 获取下载路径
     *
     * @return 文件的路径
     */
    public static String getDownloadPath(Context context) {
        return createIfNotExist(context);
    }

    /**
     * 创建文件夹如果文件夹不存在
     *
     * @param context 上下文
     * @return
     */
    private static String createIfNotExist(Context context) {
        String filePath = getFilesDir(context) + FILE_SEPARATOR + Constant.DOWNLOAD;
        //判断文件夹是否存在
        boolean exist = isPathExist(filePath);
        if (!exist) {
            File file = new File(filePath);
            if (!file.exists()) {
                //创建文件夹
                file.mkdir();
                filePath = file.getAbsolutePath();
            }
        }
        return filePath + FILE_SEPARATOR;
    }

    /**
     * 创建文件如果文件不存在
     *
     * @param context  上下文
     * @param fileName 文件名
     * @return
     */
    private static String createIfNotExist(Context context, String fileName) {
        if (TextUtils.isEmpty(fileName)) {
            return "";
        }

        String filePath = getFilesDir(context);
        String tempFilePath = filePath + FILE_SEPARATOR + Constant.COMMON_FILE;
        filePath = tempFilePath + FILE_SEPARATOR + fileName;
        //判断文件是否存在
        boolean exist = isFileExist(filePath);
        if (!exist) {
            File file = new File(tempFilePath);
            try {
                if (!file.exists()) {
                    file.mkdir();
                }
                File resultFile = new File(file, fileName);
                if (!resultFile.exists()) {
                    resultFile.createNewFile();
                }
                filePath = resultFile.getAbsolutePath();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return filePath;
    }

    /**
     * 文件重命名
     *
     * @param sourceName 源文件
     * @param targetName 目标文件
     */
    public static void renameFile(String sourceName, String targetName) {
        if (TextUtils.isEmpty(sourceName)) {
            return;
        }
        if (TextUtils.isEmpty(targetName)) {
            return;
        }

        File file = new File(sourceName);
        File newFile = new File(targetName);
        //如果文件已经存在则先删除
        deleteFile(targetName);
        file.renameTo(newFile);
    }

    /**
     * 判断文件是否存在
     *
     * @param fileName
     * @return true表示存在
     */
    private static boolean isFileExist(String fileName) {
        File file = new File(fileName);
        //检查一个对象是否是文件夹
        boolean isFile = file.isFile();
        boolean exists = file.exists();
        if (isFile && exists) {
            return true;
        }
        return false;
    }

    /**
     * 判断文件夹是否存在
     *
     * @param filePath
     * @return true表示存在
     */
    private static boolean isPathExist(String filePath) {
        File file = new File(filePath);
        //检查一个对象是否是文件夹
        boolean isDirectory = file.isDirectory();
        boolean exists = file.exists();
        if (isDirectory && exists) {
            return true;
        }
        return false;
    }

    /**
     * 删除文件
     *
     * @param filePath 需要删除的文件或者文件夹
     */
    public static void deleteFile(String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            return;
        }
        File file = new File(filePath);
        //是文件并且已经存在
        if (file.exists()) {
            if (file.isFile()) {
                file.delete();
            } else if (file.isDirectory()) {
                //是文件夹就获取文件的列表
                File[] files = file.listFiles();
                for (int i = 0, count = files.length; i < count; i++) {
                    if (files[i].isFile()) {
                        files[i].delete();
                    } else if (files[i].isDirectory()) {
                        deleteFile(files[i].getPath());
                    }
                }

                //目录下没有文件或者文件夹，则删除
                if (file.listFiles().length == 0) {
                    file.delete();
                }
            }
        }
    }

    /**
     * 获取sd卡下所有的文件
     *
     * @return
     */
    public static List<File> getFileList(Context context) {
        String filePath = "";
        List<File> datas = new ArrayList<>();
        if (externalMemoryAvailable()) {
            filePath = Environment.getExternalStorageDirectory().getAbsolutePath();
            File file = new File(filePath);
            //是文件并且已经存在
            if (file.exists()) {
                if (file.isDirectory()) {
                    //是文件夹就获取文件的列表
                    File[] files = file.listFiles();
                    for (int i = 0, count = files.length; i < count; i++) {
                        if (files[i].isFile()) {
                            //过滤字段
                            for (int k = 0, number = filterFields.length; k < number; k++) {
                                if (files[i].getName().endsWith(filterFields[k].toLowerCase())
                                        || files[i].getName().endsWith(filterFields[k].toUpperCase())) {
                                    datas.add(files[i]);
                                }
                            }
                        } else if (files[i].isDirectory()) {
                            getFileList(datas, files[i].getPath());
                        }
                    }
                }
            }
            //获取手机应用下的文件路径
            filePath = getFilesDir(context);
            getFileList(datas, filePath);
        } else {
            //获取手机应用下的文件路径
            filePath = getFilesDir(context);
            getFileList(datas, filePath);
        }

        return datas;
    }

    /**
     * 从手机应用获取文件
     *
     * @return
     */
    private static void getFileList(List<File> datas, String filePath) {
        File file = new File(filePath);
        //是文件并且已经存在
        if (file.exists()) {
            if (file.isDirectory()) {
                //是文件夹就获取文件的列表
                File[] files = file.listFiles();
                for (int i = 0, count = files.length; i < count; i++) {
                    if (files[i].isFile()) {
                        //过滤字段
                        for (int k = 0, number = filterFields.length; k < number; k++) {
                            if (files[i].getName().endsWith(filterFields[k].toLowerCase())
                                    || files[i].getName().endsWith(filterFields[k].toUpperCase())) {
                                //装入集合zhong
                                datas.add(files[i]);
                            }
                        }
                    } else if (files[i].isDirectory()) {
                        getFileList(datas, files[i].getPath());
                    }
                }
            }
        }
    }

    /**
     * 文件的复制
     *
     * @param sourceFile 源文件
     * @param targetFile 目标文件
     */
    public static void copyFile(File sourceFile, File targetFile) {
        if (sourceFile == null || targetFile == null) {
            return;
        }

        InputStream is = null;
        OutputStream os = null;
        try {
            is = new FileInputStream(sourceFile);
            os = new FileOutputStream(targetFile);
            int length = 0;
            byte[] buffer = new byte[BYTE_SIZE];
            while ((length = is.read(buffer)) != -1) {
                os.write(buffer, 0, length);
                os.flush();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                    is = null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (os != null) {
                try {
                    os.close();
                    os = null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 计算缓存文件的大小
     *
     * @param cacheFile 缓存的文件
     */
    private static double calculateFileSize(File cacheFile) {
        double size = 0.0d;
        if (cacheFile.exists()) {
            if (cacheFile.isFile()) {
                size = cacheFile.length();
            } else if (cacheFile.isDirectory()) {
                File[] cacheFiles = cacheFile.listFiles();
                for (int i = 0, count = cacheFiles.length; i < count; i++) {
                    if (cacheFiles[i].isFile()) {
                        size += cacheFiles[i].length();
                    } else if (cacheFiles[i].isDirectory()) {
                        size += calculateFileSize(cacheFiles[i]);
                    }
                }
            }
        }
        return size;
    }

    /**
     * 清除缓存
     */
    public static void clearCache(Context context) {
        //先判断是否有SD卡的挂载
        if (externalMemoryAvailable()) {
            //获取sd卡的缓存
            File cacheFile = context.getExternalCacheDir();
            if (cacheFile != null && cacheFile.exists()) {
                //删掉文件
                deleteFile(cacheFile.getAbsolutePath());
            }
        } else {
            //获取应用内部的缓存
            File cacheFile = context.getCacheDir();
            if (cacheFile != null && cacheFile.exists()) {
                //删掉文件
                deleteFile(cacheFile.getAbsolutePath());
            }
        }
    }

    /**
     * 格式化单位
     *
     * @param size 文件的大小
     * @return 格式化后的文件大小
     */
    public static String getFormatSize(double size) {
        double kiloByte = size / 1024;
        if (kiloByte < 1) {
            return size + "Byte";
        }

        double megaByte = kiloByte / 1024;
        if (megaByte < 1) {
            BigDecimal result1 = new BigDecimal(Double.toString(kiloByte));
            return result1.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "KB";
        }

        double gigaByte = megaByte / 1024;
        if (gigaByte < 1) {
            BigDecimal result2 = new BigDecimal(Double.toString(megaByte));
            return result2.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "MB";
        }

        double teraBytes = gigaByte / 1024;
        if (teraBytes < 1) {
            BigDecimal result3 = new BigDecimal(Double.toString(gigaByte));
            return result3.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "GB";
        }
        BigDecimal result4 = new BigDecimal(teraBytes);
        return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString()
                + "TB";
    }

    /**
     * 获取格式后的文件大小
     *
     * @param file 需要计算的文件
     * @return
     */
    public static String getCacheSize(File file) {
        return getFormatSize(calculateFileSize(file));
    }

    /**
     * 拍照之前保存图片路径
     */
    public static File saveBeforeTakePhotoImagePath(Context context) {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd-HHmmss");
        //图片名字
        String imageName = "IMG_" + format.format(new Date()) + ".jpg";
        //获取图片保存的路径
        String imagePath = FileUtils.getExternalDestination(context, imageName, true);
        File file = new File(imagePath);
        if (!file.exists()) {
            //如果文件夹不存在则创建文件夹
            file.mkdirs();
        }
        //如果是文件
        if (!file.isFile()) {
            file = new File(file, imageName);//创建文件
        }
        return file;
    }

    /**
     * 拍照之后保存图片路径
     *
     * @param data intent
     * @return 得到的是图片的缩略图（会在系统相册文件夹的位置进行展示）
     */
    public static void saveAfterTakePhotoPath(Context context, Intent data) {
        Bitmap bitmap = data.getParcelableExtra("data");
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd-HHmmss");
        //图片名字
        String imageName = "IMG_" + format.format(new Date()) + ".jpg";
        //获取图片保存的路径
        String imagePath = FileUtils.getExternalDestination(context, imageName, true);
        File file = new File(imagePath);
        if (!file.exists()) {
            //如果文件夹不存在则创建文件夹
            file.mkdirs();
        }
        //如果是文件
        if (!file.isFile()) {
            file = new File(file, imageName);//创建文件
        }
        FileOutputStream fos = null;
        if (!TextUtils.isEmpty(imagePath)) {
            try {
                fos = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } finally {
                if (null != fos) {
                    try {
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        //判断bitmap是否回收，没有回收则回收并且置空(防止OOM)
        if (null != bitmap && !bitmap.isRecycled()) {
            bitmap.recycle();
            bitmap = null;
            //通知垃圾回收机制进行垃圾回收
            System.gc();
        }
    }

    /**
     * 录像之前保存图片路径
     */
    public static File saveBeforeTakeVideoPath(Context context) {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd-HHmmss");
        //视频名字
        String videoName = "VIDEO_" + format.format(new Date()) + ".mp4";
        //获取图片保存的路径
        String videoPath = FileUtils.getExternalDestination(context, videoName, true);
        File file = new File(videoPath);
        if (!file.exists()) {
            //如果文件夹不存在则创建文件夹
            file.mkdirs();
        }
        //如果不是文件
        if (!file.isFile()) {
            file = new File(file, videoName);//创建文件
        }
        return file;
    }

    /**
     * 录像之后保存图片路径
     *
     * @param data intent
     * @return 得到的是图片的缩略图（会在系统相册文件夹的位置进行展示）
     */
    public static void saveAfterTakeVideoPath(Context context, Intent data) {
        Bitmap bitmap = data.getParcelableExtra("data");
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd-HHmmss");
        //视频名字
        String videoName = "VIDEO_" + format.format(new Date()) + ".MP4";
        //获取视频保存的路径
        String videoPath = FileUtils.getExternalDestination(context, videoName, true);
        File file = new File(videoPath);
        if (!file.exists()) {
            //如果文件夹不存在则创建文件夹
            file.mkdirs();
        }
        //如果是文件
        if (!file.isFile()) {
            file = new File(file, videoName);//创建文件
        }
        FileOutputStream fos = null;
        if (!TextUtils.isEmpty(videoPath)) {
            try {
                fos = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } finally {
                if (null != fos) {
                    try {
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        //判断bitmap是否回收，没有回收则回收并且置空(防止OOM)
        if (null != bitmap && !bitmap.isRecycled()) {
            bitmap.recycle();
            bitmap = null;
            //通知垃圾回收机制进行垃圾回收
            System.gc();
        }
    }
}
