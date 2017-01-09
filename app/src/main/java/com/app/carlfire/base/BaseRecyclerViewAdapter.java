package com.app.carlfire.base;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.app.carlfire.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 适用于RecyclerView的抽象Adapter，封装了数据集、ViewHolder的创建与绑定过程,简化子类的操作
 *
 * @param <D> 数据集中的类型，例如Article等
 * @param <V> ViewHolder类型
 */
public abstract class BaseRecyclerViewAdapter<D, V extends ViewHolder> extends Adapter<V> {
    private static final int TYPE_ITEM = 0;
    private static final int TYPE_FOOTER = 1;

    static final int STATE_NORMAL = 0;//正常状态
    static final int STATE_LOADING = 1;//加载中
    static final int STATE_LASTED = 2;//没有更多

    private int state = STATE_NORMAL;//当前footer状态

    private boolean enableLoadMore = false;

    /**
     * RecyclerView中的数据集
     */
    private final List<D> mDataSet = new ArrayList<>();

    @Override
    public final int getItemCount() {
        return mDataSet.size() == 0? mDataSet.size(): mDataSet.size() + (enableLoadMore ? 1 : 0);
    }

    @Override
    public final int getItemViewType(int position) {
        if (enableLoadMore && position + 1 == getItemCount()) {
            return TYPE_FOOTER;
        } else {
            return TYPE_ITEM;
        }
    }

    @Override
    public final V onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            return bindViewHolder(parent, viewType);
        } else {
            return (V) new FooterViewHolder(inflateItemView(parent, R.layout.footer));
        }
    }

    /**
     * 绑定数据,主要分为两步,绑定数据与设置每项的点击事件处理
     */
    @Override
    public final void onBindViewHolder(V viewHolder, int position) {
        if (getItemViewType(position) == TYPE_ITEM) {
            final D item = mDataSet.get(position);
            bindDataToItemView(viewHolder, item);
            setupItemViewListener(viewHolder, item);
        } else {
            ((FooterViewHolder) viewHolder).changeState(state);
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if (manager instanceof GridLayoutManager) {
            final GridLayoutManager gridManager = ((GridLayoutManager) manager);
            gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    return (getItemViewType(position) == TYPE_ITEM)
                            ? 1 : gridManager.getSpanCount();
                }
            });
        }
    }

    @Override
    public void onViewAttachedToWindow(V holder) {
        super.onViewAttachedToWindow(holder);
        ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
        if (lp != null && lp instanceof StaggeredGridLayoutManager.LayoutParams) {
            StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) lp;
            //非item项都独占一行
            if (holder.getItemViewType() != TYPE_ITEM) {
                p.setFullSpan(true);
            }
        }
    }

    protected abstract V bindViewHolder(ViewGroup parent, int viewType);

    /**
     * ItemView的UI显示绑定
     *
     * @param viewHolder 视图组件
     * @param item       对象实例
     */
    protected abstract void bindDataToItemView(V viewHolder, D item);

    /**
     * ItemView的监听事件绑定
     *
     * @param viewHolder 视图组件
     * @param item       对象实例
     */
    protected abstract void setupItemViewListener(V viewHolder, final D item);

    /**
     * 提供给子类实现onCreateViewHolder时调用
     *
     * @param viewGroup
     * @param layoutId
     * @return
     */
    protected final View inflateItemView(ViewGroup viewGroup, int layoutId) {
        return LayoutInflater.from(viewGroup.getContext()).inflate(layoutId, viewGroup, false);
    }

    /**
     * 新增数据,自动完成显示刷新
     *
     * @param items
     */
    public void addItems(List<D> items) {
        // 移除已经存在的数据,避免数据重复
        items.removeAll(mDataSet);
        // 添加新数据
        mDataSet.addAll(items);
        notifyDataSetChanged();
    }

    /**
     * 移除数据,自动完成显示刷新
     */
    public void clear() {
        mDataSet.clear();
        notifyDataSetChanged();
    }

    void setEnableLoadMore(boolean enableLoadMore) {
        this.enableLoadMore = enableLoadMore;
    }

    void notifyLoadMoreState(int state) {
        if(this.state == STATE_LASTED)
            return;
        this.state = state;
        notifyItemChanged(getItemCount() - 1);
    }

    private class FooterViewHolder extends ViewHolder {
        private ProgressBar progressBar;
        private TextView context;

        FooterViewHolder(View itemView) {
            super(itemView);
            progressBar = (ProgressBar) itemView.findViewById(R.id.footer_pb);
            context = (TextView) itemView.findViewById(R.id.more_data_msg);
        }

        void changeState(int state) {
            Log.e("wyj", "changeState: "+state );
            switch (state) {
                case STATE_LOADING:
                    progressBar.setVisibility(View.VISIBLE);
                    context.setText("加载中...");
                    break;
                case STATE_LASTED:
                    progressBar.setVisibility(View.GONE);
                    context.setText("没有更多了");
                    break;
                case STATE_NORMAL:
                    progressBar.setVisibility(View.GONE);
                    context.setText("上拉查看更多");
                    break;
            }
        }
    }
}
