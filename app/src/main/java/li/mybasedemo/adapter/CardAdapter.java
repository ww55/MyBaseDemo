package li.mybasedemo.adapter;

import android.content.Context;
import android.util.Log;

import java.util.List;

import base.BaseRecyclerViewAdapter;

/**
 * 创建时间: 2017/12/1
 * 创建人: Administrator
 * 功能描述:
 */

public class CardAdapter extends BaseRecyclerViewAdapter {
    public CardAdapter(List data, Context context, List<Integer> layoutIds) {
        super(data, context, layoutIds);
    }

    @Override
    protected void onCreate(BaseViewHolder baseViewHolder, Object o, int pos) {
        Log.d("zww", "cardView onvcreate");
    }

    @Override
    public int getItemCount() {
        Log.d("zww", "card getItemCount " + super.getItemCount());
        return super.getItemCount();
    }

    @Override
    protected void onBind(BaseViewHolder baseViewHolder, Object itmeModule, int position) throws ClassCastException {

    }
}
