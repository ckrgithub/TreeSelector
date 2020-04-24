package com.ckr.treeselector.common;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import java.util.ArrayList;
import java.util.List;


/**
 * Created on 2019/1/7
 *
 * @author ckr
 * @description
 */
public abstract class BaseAdapter<T, ViewHolder extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<ViewHolder> {
    private static final String TAG = "BaseAdapter";
    protected Context mContext;
    protected List<T> data;
    protected OnItemClickListener mOnItemClickListener;


    public BaseAdapter(Context mContext) {
        this.mContext = mContext;
        data = new ArrayList<>();
    }

    public BaseAdapter(Context mContext, @NonNull List<T> data) {
        this.mContext = mContext;
        this.data = data;
    }

    protected boolean hasHeaderItem() {
        return false;
    }

    public List<T> getData() {
        return data;
    }

    protected Context getContext() {
        return mContext;
    }

    /**
     * 清空所有数据
     */
    public void clear() {
        data.clear();
        notifyDataSetChanged();
    }

    /**
     * 更新所有数据
     *
     * @param newData
     */
    public void updateAll(List<T> newData) {
        if (checkNull(newData)) {
            return;
        }
        data.clear();
        data.addAll(newData);
        notifyDataSetChanged();
    }

    /**
     * 集合判空
     *
     * @param newData
     * @return
     */
    private boolean checkNull(List<T> newData) {
        if (newData == null || newData.size() == 0) {
            return true;
        }
        return false;
    }

    /**
     * 添加数据
     *
     * @param newData
     */
    public void updateItem(List<T> newData) {
        if (checkNull(newData)) {
            return;
        }
        int size = data.size();
        data.addAll(newData);
        notifyItemRangeChanged(size, newData.size());
    }

    /**
     * 更新start下标的item
     *
     * @param start
     * @param t
     */
    public void updateItem(int start, T t) {
        if (t == null) {
            return;
        }
        if (start < 0 && start > data.size()) {
            throw new ArrayIndexOutOfBoundsException(start);
        }
        data.add(start, t);
        int len = data.size() - start;
        notifyItemRangeChanged(start, len);
    }

    /**
     * 添加数据
     *
     * @param t
     */
    public void updateItem(T t) {
        if (t == null) {
            return;
        }
        int len = data.size();
        data.add(t);
        notifyItemRangeChanged(len, 1);
    }

    /**
     * 更新某个item
     *
     * @param position
     */
    public void updateItem(int position) {
        if (position < 0 && position >= data.size()) {
            throw new ArrayIndexOutOfBoundsException(position);
        }
        notifyItemChanged(position);
    }

    /**
     * 移除某个item
     *
     * @param position
     */
    public void removeItem(int position) {
        if (position < 0 && position >= data.size()) {
            throw new ArrayIndexOutOfBoundsException(position);
        }
        data.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(hasHeaderItem() ? position + 1 : position, data.size());
    }

    /**
     * 移除某个item
     *
     * @param position
     */
    public void removeItem2(int position) {
        if (position < 0 && position >= data.size()) {
            throw new ArrayIndexOutOfBoundsException(position);
        }
        int size = data.size();
        data.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged((size - 1) == position ? Math.max(0, position - 1) : position, data.size());
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layoutId = getLayoutId(viewType);
        View inflate = layoutId <= 0 ? null : LayoutInflater.from(mContext).inflate(layoutId, parent, false);
        return getViewHolder(inflate, viewType);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        T t = getItem(position);
        convert(holder, position, t);
    }

    protected T getItem(int position) {
        int size = data.size();
        return size > position ? data.get(position) : null;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    protected abstract int getLayoutId(int viewType);

    protected abstract ViewHolder getViewHolder(View itemView, int viewType);

    protected abstract void convert(ViewHolder holder, int position, T t);

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    public interface OnItemClickListener<T> {
        void onItemClick(int position, T t);
    }

}
