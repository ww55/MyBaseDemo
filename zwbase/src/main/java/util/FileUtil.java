package util;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;
import android.webkit.MimeTypeMap;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import li.zwbase.R;

/**
 * 创建时间: 2017/10/18
 * 创建人: Administrator
 * 功能描述:文件工具类
 */

public class FileUtil {
    private SimpleDateFormat dateFormat, format;
    private Context context;
    private static FileUtil fileUtil;
    private ToastUtil toastUtil;
    private String FilePermission = Manifest.permission.WRITE_EXTERNAL_STORAGE;

    private FileUtil(Context context) {
        this.context = context;
        toastUtil = ToastUtil.getInstance(context);
        dateFormat = new SimpleDateFormat("yyyyMMddhhmmss");
        format = new SimpleDateFormat("yyyy年MM月dd日");

    }

    /**
     * 初始化
     *
     * @param context
     * @return
     */
    public static FileUtil getInstance(Context context) {
        if (fileUtil == null) {
            fileUtil = new FileUtil(context);
        }
        return fileUtil;
    }

    /**
     * 获取文件最后修改时间
     *
     * @param path 文件路径
     * @return
     */
    private String getLastModifyTimeByName(String path) {
        String time = "";
        File file = new File(path);
        time = format.format(new Date(file.lastModified()));
        return time;

    }

    /**
     * 获取视频时长
     *
     * @param mUri 文件路径
     * @return 时长
     */
    public static String getRingDuring(String mUri) {
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        mmr.setDataSource(mUri);
        String duration = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION); // 播
        mmr.release();
        int time = Integer.parseInt(duration);
        int hour = time / 1000 / 3600;
        int minute = time / 1000 / 60;
        int second = time / 1000;
        duration =
                hour + " 时 " + minute + " 分 " + second + " 秒";
        return duration;
    }

    /**
     * 适配android7.0 需要在mainfest文件下配置FileProvider（authority = 包名+".provide"）
     *
     * @param file
     */
    public void openFile(File file) {
        if (!checkPermission()) {
            toastUtil.showToast(R.string.zwbase_no_permission);
            return;
        }
        if (!file.exists()) {
            toastUtil.showToast(R.string.zwbase_no_file);
            return;
        }
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        String type = getMimeType(file);
        Uri u = null;
        if (Build.VERSION.SDK_INT >= 24) {
            u = FileProvider.getUriForFile(context, context.getPackageName() + ".provider", file);
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        } else {
            u = Uri.fromFile(file);
        }

        intent.setDataAndType(u, type);
        context.startActivity(intent);
    }

    /**
     * 适配android7.0 需要在mainfest文件下配置FileProvider（authority = 包名+".provide"）
     *
     * @param file
     */
    public void shareFile(File file) {
        if (!file.exists()) {
            return;
        }
        Intent intent = new Intent();
        Uri u = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".provider", file);
        intent.putExtra(Intent.EXTRA_STREAM, u);
        intent.setAction(Intent.ACTION_SEND);
        intent.setType(getMimeType(file));                    //设置类型
        context.startActivity(intent);
    }

    public boolean checkPermission() {
        boolean res = true;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            res = (context.checkSelfPermission(FilePermission) == PackageManager.PERMISSION_GRANTED);
        }
        return res;
    }

    /**
     * 是否重名
     *
     * @param newName   新文件名
     * @param audioPath 文件路径
     * @return
     */
    public boolean isRename(String newName, String audioPath) {
        String path = audioPath;
        boolean res = false;

        File file = new File(path);
        File[] files = file.listFiles();
        for (File f : files
                ) {
            String fileName = f.getName();
            if (fileName.substring(fileName.lastIndexOf("." + 1)).trim().equals(newName.trim())) {
                res = true;
                break;
            }
        }
        return res;
    }

    /**
     * 获取文件扩展名
     *
     * @param file 文件
     * @return 扩展名
     */
    public String getExtension(final File file) {
        final String name = file.getName();
        final int idx = name.lastIndexOf(".");
        String suffix = "";
        if (idx > 0) {
            suffix = name.substring(idx + 1);
        }
        return suffix;
    }

    /**
     * 获取Mime类型
     *
     * @param file 文件
     * @return 获取Mime类型
     */
    public String getMimeType(final File file) {
        final String extension = getExtension(file);
        return MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
    }
}
