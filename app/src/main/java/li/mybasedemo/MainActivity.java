package li.mybasedemo;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.webkit.MimeTypeMap;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import base.BaseActivity;
import util.FileUtil;

public class MainActivity extends BaseActivity {
    private RecyclerView myRview;
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

    @Override
    protected void initData() {
        data = new ArrayList<>();
        layoutIds = new ArrayList<>();
        layoutIds.add(R.layout.layout_rvitme);
        layoutIds.add(R.layout.layout_image);
        for (int i = 0; i < 100; i++) {
            data.add("sssss " + i);
        }
        baseRecyclerViewAdapter = new MyAdapter(data, this, layoutIds);
        myRview.setLayoutManager(new LinearLayoutManager(this));
        myRview.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        myRview.setAdapter(baseRecyclerViewAdapter);
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
