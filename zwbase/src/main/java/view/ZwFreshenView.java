package view;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import java.util.List;

import base.BaseRecyclerViewAdapter;
import li.zwbase.R;

/**
 * 创建时间: 2017/11/30
 * 创建人: Administrator
 * 功能描述:
 */

public class ZwFreshenView<T extends BaseRecyclerViewAdapter> extends LinearLayout implements SwipeRefreshLayout.OnRefreshListener {
    private SwipeRefreshLayout slReFreshView;
    private RecyclerView rvMyDataView;
    private Fresh fresh;
    private Context context;
    private T t;
    private Handler handler;
    private static final int REFRESH = 33;
    private static final int LOADMORE = 44;
    private Message message;

    public ZwFreshenView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        View view = LayoutInflater.from(context).inflate(R.layout.zwbase_zwfreshen, null, false);
        initView(view);
        initEvent();
        addView(view);
    }

    public void setFresh(Fresh fresh) {
        this.fresh = fresh;
    }

    private void initView(View view) {
        slReFreshView = view.findViewById(R.id.slReFreshView);
        rvMyDataView = view.findViewById(R.id.rvMyDataView);
        rvMyDataView.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));
        rvMyDataView.setLayoutManager(new LinearLayoutManager(context));
    }

    private void initEvent() {
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                List data = (List) msg.obj;
                switch (msg.what) {
                    case LOADMORE:
                        if (data.size() == 0 || data == null) {
                            Log.d("zww", "add o data");
                            t.setMore(false);
                            t.notifyDataSetChanged();
                            return;
                        }
                        t.addDatas(data);
                        break;
                    case REFRESH:
                        rvMyDataView.setAdapter(t);
                        t.setData(data);
                        slReFreshView.setRefreshing(false);
                        break;
                }
            }
        };
        slReFreshView.setRefreshing(true);
        slReFreshView.setOnRefreshListener(this);
        rvMyDataView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (isSlideToBottom(recyclerView) && newState == 0
                        ) {
                    new Thread() {
                        public void run() {
                            super.run();
                            fresh.onLoadMore();
                        }
                    }.start();
                }
            }
        });
    }

    @Override
    public void onRefresh() {
        new Thread() {
            public void run() {
                super.run();
                fresh.onReFresh();
                t.setMore(true);
            }
        }.start();
    }

    public void initDataToView(List data, T t) {
        this.t = t;
        rvMyDataView.setAdapter(t);
        t.setData(data);
        slReFreshView.setRefreshing(false);
    }

    public void setData(List data) {
        try {
            this.t.setData(data);
            message = new Message();
            message.obj = data;
            message.what = REFRESH;
            handler.sendMessage(message);
        } catch (NullPointerException e) {
            Log.d("zww", "适配器为空");
        } catch (ClassCastException e) {
            Log.d("zww", "类型转换异常");
        }
    }

    public void addData(List data) {
        message = new Message();
        message.obj = data;
        message.what = LOADMORE;
        handler.sendMessage(message);
    }

    public interface Fresh {
        /**
         * 下拉刷新数据,加载完成之后调用 setData 方法
         */
        public void onReFresh();

        /**
         * 上拉加载,数据完成个之后调用 addData方法
         */
        public void onLoadMore();
    }

    /**
     * 判断是否滑动到了底部
     *
     * @param recyclerView 控件
     * @return boolean值
     */
    public boolean isSlideToBottom(RecyclerView recyclerView) {
        if (recyclerView == null) return false;
        if (recyclerView.computeVerticalScrollExtent() + recyclerView.computeVerticalScrollOffset()
                >= recyclerView.computeVerticalScrollRange())
            return true;
        return false;
    }
}
