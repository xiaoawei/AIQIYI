package com.tsg.xutil.base;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * 所有BaseAdapter的直接父类
 *
 * @param <T>
 */
public abstract class BaseCustomAdapter<T> extends BaseAdapter {

    protected LayoutInflater layoutInflater;
    protected List<T> data;
    protected Context context;
    protected Resources res;

    public BaseCustomAdapter(Context context, List<T> data) {
        this.context = context;
        this.data = data;
        this.layoutInflater = LayoutInflater.from(context);
        this.res = context.getResources();
    }

    /**
     * 获取所有数据
     *
     * @return
     */
    public List<T> getData() {
        return data;
    }

    /**
     * 设置数据
     *
     * @param data
     */
    public void setData(List<T> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    /**
     * 添加集合数据，
     *
     * @param data
     */
    public void addAll(List<T> data) {
        this.data.addAll(data);
        notifyDataSetChanged();
    }

    /**
     * 添加集合，清除之前数据
     *
     * @param data
     */
    public void addAllClearOldList(List<T> data) {
        this.data.clear();
        this.data = data;
        notifyDataSetChanged();
    }

    public void addAll(int location, List<T> data) {
        this.data.addAll(location, data);
        notifyDataSetChanged();
    }

    public void add(T t) {
        this.data.add(t);
        notifyDataSetChanged();
    }

    public void add(int location, T t) {
        this.data.add(location, t);
        notifyDataSetChanged();
    }

    public void remove(int location) {
        this.data.remove(location);
        notifyDataSetChanged();
    }

    public void remove(T t) {
        this.data.remove(t);
        notifyDataSetChanged();
    }

    public void removeAll(List<T> data) {
        this.data.removeAll(data);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public T getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    public abstract View getCustomView(int position, View convertView, ViewGroup parent);

}