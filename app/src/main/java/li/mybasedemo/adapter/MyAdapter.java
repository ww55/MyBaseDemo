package li.mybasedemo.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import java.util.List;

import base.BaseRecyclerViewAdapter;
import li.mybasedemo.R;
import li.mybasedemo.activity.JianActivity;
import li.mybasedemo.activity.RxActivity;

/**
 * 创建时间: 2017/11/30
 * 创建人: Administrator
 * 功能描述:
 */

public class MyAdapter extends BaseRecyclerViewAdapter<String> implements View.OnClickListener {

    public MyAdapter(List data, Context context, List<Integer> layoutIds) {
        super(data, context, layoutIds);
    }

    @Override
    protected void onCreate(BaseViewHolder baseViewHolder, String o, int pos) {
        baseViewHolder.setClickListent(R.id.rootLayout, this);
        baseViewHolder.getViewById(R.id.rootLayout).setTag(pos);
    }

    @Override
    protected void onBind(BaseViewHolder baseViewHolder, String itmeModule, int position) throws ClassCastException {
        if (!(baseViewHolder instanceof BaseRecyclerViewAdapter.FootViewHolder)) {
            int layoutNum = (position % layoutIds.size());
            switch (layoutNum) {
                case 0:
                    baseViewHolder.setText(R.id.tvText, itmeModule);
                    break;

            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rootLayout:
                int pos = (int) view.getTag();
                Intent intent = null;
                switch (pos) {
                    case 0:
                        intent = new Intent(context, JianActivity.class);
                        context.startActivity(intent);
                        break;
                    case 1:
                        intent = new Intent(context, RxActivity.class);
                        context.startActivity(intent);
                        break;
                }
                break;
        }
    }
}

