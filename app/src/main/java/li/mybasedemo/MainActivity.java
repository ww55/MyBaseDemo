package li.mybasedemo;

import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import java.util.ArrayList;
import java.util.List;

import base.BaseActivity;
import base.BaseRecyclerView;

public class MainActivity extends BaseActivity {
    private RecyclerView myRview;
    private List<String> data;
    private BaseRecyclerView baseRecyclerView;

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
        for (int i = 0; i < 100; i++) {
            data.add("text " + i);
        }
        baseRecyclerView = new BaseRecyclerView(data, MainActivity.this, R.layout.layout_rvitme) {

            @Override
            protected void clickView(BaseViewHolder baseViewHolder, Object o, final int pos) {
                ((CheckBox) baseViewHolder.getViewById(R.id.cbChoose)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        showToast("CHEKC " + pos);
                    }
                });
            }

            @Override
            protected void setItmeData(BaseViewHolder baseViewHolder, Object itmeModule) throws ClassCastException {
                String content = (String) itmeModule;
                baseViewHolder.setText(R.id.tvText, content);
            }
        };
        myRview.setLayoutManager(new LinearLayoutManager(this));
        myRview.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        myRview.setAdapter(baseRecyclerView);
    }

    @Override
    protected void initView() {
        myRview = findViewById(R.id.myRview);

    }
}
