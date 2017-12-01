package base;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import li.zwbase.R;

/**
 * 创建时间: 2017/11/29
 * 创建人: Administrator
 * 功能描述:RecyclerView基类(需要注意类型转换问题)
 */
public abstract class BaseRecyclerViewAdapter extends RecyclerView.Adapter<BaseRecyclerViewAdapter.BaseViewHolder> {
    protected ArrayList data;
    protected Context context;
    protected List<Integer> layoutIds;//布局集合
    protected boolean isMore = true;//上拉刷新时，是否有更多数据
    private boolean canLoad = true;

    /**
     * 适配器构造方法 #注意List
     *
     * @param data      数据集合
     * @param context   上下文对象
     * @param layoutIds 布局集合
     */
    public BaseRecyclerViewAdapter(List data, Context context, List<Integer> layoutIds) {
        this.data = new ArrayList<>();
        this.layoutIds = new ArrayList<>();
        this.data.addAll(data);
        this.layoutIds.addAll(layoutIds);
        this.context = context;
    }

    public void setData(List data) {
        this.data.clear();
        this.data.addAll(data);
        notifyDataSetChanged();
    }

    public void clearAll() {
        this.data.clear();
        notifyDataSetChanged();
    }

    public Object getDataByPos(int index) {
        if (data.size() <= index) {
            return null;
        }
        return data.get(index);
    }

    public void addData(Object t) {
        data.add(t);
        notifyDataSetChanged();
    }

    public void addDatas(List data) {
        this.data.addAll(data);
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if (canLoad) {
            if (position + 1 == getItemCount()) {
                return -1;//最后行时设为-1显示footLayout
            } else {
                return position;//其他设为位置
            }
        } else {
            return position;
        }

    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int pos) {
        Log.d("zww", "onCreateViewHolder " + pos);
        if (data.size() == 0) {//当data数据为0时
            View view = LayoutInflater.from(context).inflate(R.layout.zwbase_no_data, parent, false);
            //显示暂无数据布局
            return new BaseViewHolder(view);
        }
        if (pos == -1) {//pos为-1表示最后一行
            View view = LayoutInflater.from(context).inflate(R.layout.zwbase_footlayout, parent, false);
            //显示底部布局
            return new FootViewHolder(view);
        }
        int layout = getLayoutIdByPos(pos);
        View view = LayoutInflater.from(context).inflate(layout, parent, false);
        BaseViewHolder baseViewHolder = new BaseViewHolder(view);
        onCreate(baseViewHolder, data.get(pos), pos);
        return baseViewHolder;
    }

    /**
     * 定制要显示的布局
     *
     * @param pos 位置
     * @return 对应顺序下返回的布局
     */
    protected int getLayoutIdByPos(int pos) {
        int res = 0;
        if (pos < layoutIds.size()) {//layoutIds是存放布局的集合
            res = layoutIds.get(pos);
        } else {
            res = layoutIds.get(pos % layoutIds.size());
        }
        return res;
    }


    /**
     * 在oncreateViewHolder方法中设置点击事件
     * 避免重复调用
     *
     * @param baseViewHolder itme控件
     * @param o              itme    实体类
     * @param pos            位置
     */
    protected abstract void onCreate(BaseViewHolder baseViewHolder, Object o, int pos);

    protected abstract void onBind(BaseViewHolder baseViewHolder, Object itmeModule, int position) throws ClassCastException;

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        if (data.size() == 0) {
            ImageView nodataImage = (ImageView) holder.getViewById(R.id.zwbase_imageview);
            TextView nodataText = (TextView) holder.getViewById(R.id.zwbase_tvNoData);
            setNodataInfo(nodataImage, nodataText);
            return;
        }
        if (holder instanceof FootViewHolder) {
            //判断为底部布局时
            if (isMore) {//再次判断是否还有更多数据,加载相应的布局
                holder.getViewById(R.id.loadmore).setVisibility(View.VISIBLE);
                holder.getViewById(R.id.nodata).setVisibility(View.GONE);
            } else {
                holder.getViewById(R.id.loadmore).setVisibility(View.GONE);
                holder.getViewById(R.id.nodata).setVisibility(View.VISIBLE);
            }
            return;
        }
        try {

            onBind(holder, data.get(position), position);//设置显示数据
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
    }

    protected void setNodataInfo(ImageView nodataImage, TextView nodataText) {
    }


    @Override
    public int getItemCount() {
        if (canLoad) return data == null ? 0 : data.size() + 1;//数据为0或为空时返回0，否则data.size()+1
        else return data == null ? 0 : data.size();
    }

    public void setMore(boolean more) {
        //暴露方法给Activity
        this.isMore = more;
    }

    protected class FootViewHolder extends BaseViewHolder {
        public FootViewHolder(View itemView) {
            super(itemView);
        }
    }

    public void setCanLoad(boolean canLoad) {
        this.canLoad = canLoad;
    }

    public class BaseViewHolder extends RecyclerView.ViewHolder {
        //该类下部分方法可以自行添加
        View rootView;

        public BaseViewHolder(View itemView) {
            super(itemView);
            rootView = itemView;
        }

        public void setText(int viewId, int resourceId) {
            ((TextView) getViewById(viewId)).setText(resourceId);
        }

        public void setClickListent(int viewId, View.OnClickListener onClickListener) {
            getViewById(viewId).setOnClickListener(onClickListener);
        }

        public void setText(int viewId, String content) {
            ((TextView) getViewById(viewId)).setText(content);
        }

        public void setCheckChangeListen(int viewId, CompoundButton.OnCheckedChangeListener onCheckedChangeListener) {
            ((CheckBox) getViewById(viewId)).setOnCheckedChangeListener(onCheckedChangeListener);
        }

        public void setImageSource(int imageViewId, int sourceId) {
            ImageView imageView = (ImageView) getViewById(imageViewId);
            imageView.setImageResource(sourceId);
        }

        public View getViewById(int viewId) {
            return rootView.findViewById(viewId);
        }
    }
}

