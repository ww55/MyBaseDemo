package li.mybasedemo.adapter;

import android.content.Context;
import android.graphics.drawable.Animatable;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import base.BaseRecyclerViewAdapter;
import li.mybasedemo.R;

/**
 * 创建时间: 2017/12/1
 * 创建人: Administrator
 * 功能描述:
 */

public class JianAdapter extends BaseRecyclerViewAdapter implements View.OnClickListener {
    private static final int LOADMORESUCEEE = 32;
    private RecyclerView cardView;
    private CardAdapter cardAdapter;
    private List<Integer> headResources;
    private List<Integer> cardLayouts;
    private List<Integer> listLayout;
    private Handler handler;
    private static final int SUCESS = 45;
    private static final int FAUILRE = 55;
    private Animatable authAnimatable;
    private BaseRecyclerViewAdapter lastViewAdapter;

    public JianAdapter(List data, Context context, List<Integer> layoutIds) {
        super(data, context, layoutIds);
        cardLayouts = new ArrayList<>();
        headResources = new ArrayList<>();
        listLayout = new ArrayList<>();
        listLayout.add(R.layout.layout_rvitme);
        cardLayouts.add(R.layout.layout_cardview);
        for (int i = 0; i < 15; i++) {
            headResources.add(R.drawable.head);
        }
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                List data = (List) msg.obj;

                switch (msg.what) {
                    case LOADMORESUCEEE:
                        if (data != null)
                            lastViewAdapter.addDatas(data);
                        break;
                    case SUCESS:
                        operateViewAni();
                        cardAdapter.setData(data);
                        break;
                    case FAUILRE:
                        operateViewAni();
                        break;
                }
            }
        };
    }

    @Override
    protected void onCreate(BaseViewHolder baseViewHolder, Object o, int pos) {
        Log.d("zww", "jian adapter onCreate");
        int layoutNum = (pos % layoutIds.size());
        switch (layoutNum) {
            case 0:
                initCardViewInfo(baseViewHolder, o, pos);
                break;
            case 1:
                initDataViewInfo(baseViewHolder, o, pos);
                break;
        }
    }

    private void initDataViewInfo(BaseViewHolder baseViewHolder, Object o, int pos) {
        RecyclerView recyclerView = (RecyclerView) baseViewHolder.getViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        lastViewAdapter = new BaseRecyclerViewAdapter(headResources, context, listLayout) {
            @Override
            protected void onCreate(BaseViewHolder baseViewHolder, Object o, int pos) {

            }

            @Override
            protected void onBind(BaseViewHolder baseViewHolder, Object itmeModule, int position) throws ClassCastException {
                baseViewHolder.setText(R.id.tvText, "sssss");
            }
        };
        recyclerView.setAdapter(lastViewAdapter);
        recyclerView.setFocusable(true);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                Log.d("zww", "jian onScrollStateChanged");
                if (isSlideToBottom(recyclerView) && newState == 0
                        ) {
                    for (int i = 0; i < 11; i++) {
                        headResources.add(R.drawable.head);
                    }
                    lastViewAdapter.setData(headResources);
                }
            }
        });
    }

    private void initCardViewInfo(BaseViewHolder baseViewHolder, Object o, int pos) {
        cardAdapter = new CardAdapter(headResources, context, cardLayouts);
        cardAdapter.setCanLoad(false);
        cardView = (RecyclerView) baseViewHolder.getViewById(R.id.zvCardView);
        cardView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, true));
        cardView.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));
        cardView.setAdapter(cardAdapter);
        TextView textView = (TextView) baseViewHolder.getViewById(R.id.tvRefreshen);
        textView.setOnClickListener(this);
        authAnimatable = (Animatable) textView.getCompoundDrawables()[0];
    }

    @Override
    protected void onBind(BaseViewHolder baseViewHolder, Object itmeModule, int position) throws ClassCastException {
        int layoutNum = (position % layoutIds.size());
        switch (layoutNum) {
            case 0:
                break;
            case 1:
                break;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvRefreshen:
                operateViewAni();
                refreshData();
                break;
        }
    }

    private void refreshData() {
        new Thread() {
            public void run() {
                super.run();
                headResources.clear();
                {
                    for (int i = 0; i < 6; i++) {
                        headResources.add(R.drawable.head);
                    }
                    Message message = new Message();
                    message.obj = headResources;
                    message.what = SUCESS;
                    handler.sendMessage(message);
                }
            }
        }.start();
    }

    private void operateViewAni() {
        if (!((Animatable) authAnimatable).isRunning()) {
            ((Animatable) authAnimatable).start();
        } else {
            ((Animatable) authAnimatable).stop();
        }
    }

    public void loadMore() {
        headResources.clear();
        for (int i = 0; i < 10;
             i++) {
            headResources.add(R.drawable.head);
        }
        Message message = new Message();
        message.obj = headResources;
        message.what = LOADMORESUCEEE;
        handler.sendMessage(message);
    }

    public boolean isSlideToBottom(RecyclerView recyclerView) {
        if (recyclerView == null) return false;
        if (recyclerView.computeVerticalScrollExtent() + recyclerView.computeVerticalScrollOffset()
                >= recyclerView.computeVerticalScrollRange())
            return true;
        return false;
    }
}
