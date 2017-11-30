package li.mybasedemo;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.view.View;
import android.webkit.MimeTypeMap;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import base.BaseActivity;
import util.FileUtil;
import view.ZwFreshenView;

public class MainActivity extends BaseActivity {
    private view.ZwFreshenView myRview;
    private List<String> data;
    private MyAdapter baseRecyclerViewAdapter;
    private FileUtil fileUtil;
    private List<Integer> layoutIds;
    private static final String[] NEEDPERMISSION = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private static final int REQUESTCODE = 8;

    @Override
    public int getLayoutId() {
        setFullScreen();
        return R.layout.activity_main;
    }

    @Override
    protected void initEvent() {

    }

    int c = 0;

    @Override
    protected void initData() {
        data = new ArrayList<>();
        layoutIds = new ArrayList<>();
        layoutIds.add(R.layout.layout_rvitme);
        layoutIds.add(R.layout.layout_image);
        layoutIds.add(R.layout.layout_rvitme);
        for (int i = 0; i < 10; i++) {
            data.add("sssss " + i);
        }
        baseRecyclerViewAdapter = new MyAdapter(data, this, layoutIds);
        myRview.setFresh(new ZwFreshenView.Fresh() {
            @Override
            public void onReFresh() {

                data.clear();
                for (int i = 10; i < 20; i++) {
                    data.add("sssss " + i);
                }
                myRview.setData(data);
            }

            @Override
            public void onLoadMore() {
                data.clear();
                if (c < 3) {
                    for (int i = 20; i < 25; i++) {
                        data.add("sssss " + i);
                    }
                    c++;
                }
                myRview.addData(data);
            }
        });
        myRview.initDataToView(data, baseRecyclerViewAdapter);

    }

    @Override
    protected void initView() {
        myRview = findViewById(R.id.myRview);

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        boolean isPermission;
        if (Build.VERSION.SDK_INT >= 23) {
            isPermission = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED ?
                    true : false;//检查是否有读写权限
            if (!isPermission && hasFocus) {
                requestPermissions(NEEDPERMISSION, REQUESTCODE);
            }
        }
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

    @Override
    public void onClick(View view) {

    }
}
