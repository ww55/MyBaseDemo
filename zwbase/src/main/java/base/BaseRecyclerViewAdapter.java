package base;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import li.zwbase.R;

/**
 * 创建时间: 2017/11/29
 * 创建人: Administrator
 * 功能描述:RecyclerView基类
 */
public abstract class BaseRecyclerViewAdapter<T> extends RecyclerView.Adapter<BaseRecyclerViewAdapter.BaseViewHolder> {


    protected List<T> datas;
    protected boolean isShowFoot;
    protected boolean isMore = true;
    private static final int FOOTVIEWTYPE = -12;
    public BaseRecyclerViewAdapter(List<T> datas) {
        this.datas = datas;
    }

    public boolean isShowFoot() {
        return isShowFoot;
    }

    public void setShowFoot(boolean showFoot) {
        isShowFoot = showFoot;
    }

    public boolean isMore() {
        return isMore;
    }

    public void setMore(boolean more) {
        isMore = more;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (getItemCount() == 0) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.zwbase_no_data, parent, false);
        } else if (isShowFoot && viewType == FOOTVIEWTYPE) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.zwbase_footlayout, parent, false);

        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(getLayoutIdByViewType(viewType), parent, false);

        }
        return new BaseViewHolder(view);
    }

    @Override
    public int getItemViewType(int position) {
        if (isShowFoot && datas.size() == position + 2) {
            return FOOTVIEWTYPE;
        }
        return super.getItemViewType(position);
    }

    @Override
    public void onBindViewHolder(BaseRecyclerViewAdapter.BaseViewHolder holder, final int position) {
        if (getItemCount() == 0) {
            return;
        }
        if (datas.size() == position + 2) {
            setFootViewStyle(holder);
            return;
        }
        bindData(holder, position);
    }


    /**
     * 刷新数据
     *
     * @param datas
     */
    public void refresh(List<T> datas) {
        this.datas.clear();
        this.datas.addAll(datas);
        notifyDataSetChanged();
    }


    /**
     * 添加数据
     *
     * @param datas
     */
    public void addData(List<T> datas) {
        this.datas.addAll(datas);
        notifyDataSetChanged();
    }

    /**
     * 绑定数据
     *
     * @param holder   具体的viewHolder
     * @param position 对应的索引
     */
    protected abstract void bindData(BaseViewHolder holder, int position);


    @Override
    public int getItemCount() {
        return datas == null ? 0 : isShowFoot ? datas.size() + 1 : datas.size();
    }

    public void setFootViewStyle(BaseViewHolder footViewStyle) {
        if (isMore) {
            footViewStyle.getView(R.id.pbLoadView).setVisibility(View.VISIBLE);
            ((TextView) footViewStyle.getView(R.id.tvFootContent)).setText("加载中...");
        } else {
            footViewStyle.getView(R.id.pbLoadView).setVisibility(View.GONE);
            ((TextView) footViewStyle.getView(R.id.tvFootContent)).setText("——我是有底线的——");

        }
    }


    /**
     * 封装ViewHolder ,子类可以直接使用
     */
    public class BaseViewHolder extends RecyclerView.ViewHolder {


        private Map<Integer, View> mViewMap;

        public BaseViewHolder(View itemView) {
            super(itemView);
            mViewMap = new HashMap<>();
        }

        /**
         * 获取设置的view
         *
         * @param id
         * @return
         */
        public View getView(int id) {
            View view = mViewMap.get(id);
            if (view == null) {
                view = itemView.findViewById(id);
                mViewMap.put(id, view);
            }
            return view;
        }
    }

    /**
     * 获取子item
     *
     * @return
     */
    public abstract int getLayoutIdByViewType(int viewType);


    /**
     * 设置文本属性
     *
     * @param view
     * @param text
     */
    public void setItemText(View view, String text) {
        if (view instanceof TextView) {
            ((TextView) view).setText(text);
        }
    }
}