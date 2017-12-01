package li.mybasedemo.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import java.util.List;

import base.BaseRecyclerViewAdapter;
import li.mybasedemo.R;
import li.mybasedemo.activity.JianActivity;

/**
 * 创建时间: 2017/11/30
 * 创建人: Administrator
 * 功能描述:
 */

public class MyAdapter extends BaseRecyclerViewAdapter implements View.OnClickListener {

    public MyAdapter(List data, Context context, List<Integer> layoutIds) {
        super(data, context, layoutIds);
    }

    @Override
    protected void onCreate(BaseViewHolder baseViewHolder, Object o, int pos) {
        baseViewHolder.setClickListent(R.id.rootLayout, this);
    }

    @Override
    protected void onBind(BaseViewHolder baseViewHolder, Object itmeModule, int position) throws ClassCastException {
        if (!(baseViewHolder instanceof FootViewHolder)) {
            int layoutNum = (position % layoutIds.size());
            switch (layoutNum) {
                case 0:
                    baseViewHolder.setText(R.id.tvText, (String) itmeModule);
                    break;

            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rootLayout:
                Intent intent = new Intent(context, JianActivity.class);
                context.startActivity(intent);
        }
    }
}

