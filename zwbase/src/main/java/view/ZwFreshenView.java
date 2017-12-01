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
    private LinearLayoutManager linearLayoutManager;
    private static final int NODATA = 55;

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
        linearLayoutManager = new LinearLayoutManager(context);
        rvMyDataView.setLayoutManager(linearLayoutManager);
    }

    private void initEvent() {
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == NODATA) {
                    slReFreshView.setRefreshing(false);
                    return;
                }
                List data = (List) msg.obj;
                switch (msg.what) {
                    case LOADMORE:
                        if (data.size() == 0 || data == null) {
                            t.setMore(false);
                            t.notifyDataSetChanged();
                            return;
                        }
                        t.addDatas(data);
                        break;
                    case REFRESH:
                        slReFreshView.setRefreshing(false);
                        if (data != null)
                            t.setData(data);
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
                            Log.d("zww", "zwfreshen loadmore");

                            if (fresh != null)
                                fresh.onLoadMore();
                        }
                    }.start();
                }
            }
        });
    }

    @Override
    public void onRefresh() {
        Log.d("zww", "zw onRefresh");
        new Thread() {
            public void run() {
                super.run();
                fresh.onReFresh();
                t.setMore(true);
            }
        }.start();
    }

    public void initAdapter(T t) {
        this.t = t;
        slReFreshView.setRefreshing(false);
    }

    public void showData() {
        if (t != null) {
            Log.d("zww", "t size " + t.getItemCount());
            rvMyDataView.setAdapter(t);
        }
    }


    /**
     * 在setData前设置
     *
     * @param layoutManage
     */
    public void setLayoutManage(RecyclerView.LayoutManager layoutManage) {
        rvMyDataView.setLayoutManager(layoutManage);
    }

    /**
     * 在setData前设置
     * 是否能刷新
     *
     * @param loadMore true    false
     */
    public void canLoadMore(boolean loadMore) {
        t.setCanLoad(loadMore);
        slReFreshView.setEnabled(loadMore);

    }

    public void setData(List data) {

        message = new Message();
        if (data == null) {
            message.what = NODATA;
            handler.sendMessage(message);
            return;
        }
        try {
            this.t.setData(data);
            message.obj = data;
            message.what = REFRESH;
            handler.sendMessage(message);
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
