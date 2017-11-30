package util;

import android.content.Context;
import android.media.MediaMetadataRetriever;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 创建时间: 2017/10/18
 * 创建人: Administrator
 * 功能描述:文件工具类
 */

public class FileUtil {
    private SimpleDateFormat dateFormat, format;
    private Context context;
    private static FileUtil fileUtil;

    private FileUtil(Context context) {
        this.context = context;
        dateFormat = new SimpleDateFormat("yyyyMMddhhmmss");
        format = new SimpleDateFormat("yyyy年MM月dd日");

    }

    public static FileUtil getInstance(Context context) {
        if (fileUtil == null) {
            fileUtil = new FileUtil(context);
        }
        return fileUtil;
    }

    private String getLastModifyTimeByName(String path) {
        String time = "";
        File file = new File(path);
        time = format.format(new Date(file.lastModified()));
        return time;

    }

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


    public boolean isRename(String newName, String audioPath) {
        String path = audioPath;
        boolean res = false;

        File file = new File(path);
        File[] files = file.listFiles();
        for (File f : files
                ) {
            String fileName = f.getName();
            if (fileName.substring(0, fileName.length() - 4).trim().equals(newName.trim())) {
                res = true;
                break;
            }
        }
        return res;
    }

}
