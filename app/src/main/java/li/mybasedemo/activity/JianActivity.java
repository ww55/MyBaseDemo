package li.mybasedemo.activity;

import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import base.BaseActivity;
import li.mybasedemo.R;
import li.mybasedemo.adapter.JianAdapter;
import view.ZwFreshenView;

/**
 * 创建时间: 2017/12/1
 * 创建人: Administrator
 * 功能描述:
 */

public class JianActivity extends BaseActivity {
    private ZwFreshenView<JianAdapter> zvJian;
    private List<Integer> layoutIds;
    private JianAdapter jianAdapter;
    private List<String> data;

    @Override
    public int getLayoutId() {
        return R.layout.activity_jian;
    }

    @Override
    protected void initEvent() {

    }

    @Override
    protected void initData() {
        layoutIds = new ArrayList<>();
        data = new ArrayList();
        data.add("ssss");
        data.add("cccc");
        layoutIds.add(R.layout.layout_itme);
        layoutIds.add(R.layout.layout_list);
        jianAdapter = new JianAdapter(data, this, layoutIds);
        zvJian.setFocusable(false);
        zvJian.setFresh(new ZwFreshenView.Fresh() {
            @Override
            public void onReFresh() {
                zvJian.setData(null);
            }

            @Override
            public void onLoadMore() {
                Log.d("zww", "jian onLoadMore");
                jianAdapter.loadMore();

            }
        });
        zvJian.initAdapter(jianAdapter);
        zvJian.showData();
    }

    @Override
    protected void initView() {
        zvJian = findViewById(R.id.zvJian);

    }

    @Override
    public void onClick(View view) {

    }
}
