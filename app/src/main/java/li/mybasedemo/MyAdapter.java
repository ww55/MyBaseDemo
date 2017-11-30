package li.mybasedemo;

import android.content.Context;

import java.util.List;

import base.BaseRecyclerViewAdapter;

/**
 * 创建时间: 2017/11/30
 * 创建人: Administrator
 * 功能描述:
 */

public class MyAdapter extends BaseRecyclerViewAdapter {

    public MyAdapter(List data, Context context, List<Integer> layoutIds) {
        super(data, context, layoutIds);
    }

    @Override
    protected void clickView(BaseViewHolder baseViewHolder, Object o, int pos) {

    }

    @Override
    protected void setItmeData(BaseViewHolder baseViewHolder, Object itmeModule, int position) throws ClassCastException {
        if (!(baseViewHolder instanceof FootViewHolder)) {
            int layoutNum = (position % layoutIds.size());
            switch (layoutNum) {
                case 0:
                    baseViewHolder.setText(R.id.tvText, (String) itmeModule);
                    break;
                case 1:
                    baseViewHolder.setImageSource(R.id.ivImage, R.mipmap.image);
                    break;
                case 2:
                    baseViewHolder.setText(R.id.tvText, "aaaa " + position);
                    break;

            }
        }
    }

}

