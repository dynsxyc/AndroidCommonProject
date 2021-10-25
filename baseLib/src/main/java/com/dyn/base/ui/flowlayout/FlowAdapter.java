package com.dyn.base.ui.flowlayout;

import android.view.View;

import java.util.ArrayList;
import java.util.List;


/**
 * @date on 2019/04/15 14:25
 * @packagename tsou.uxuan.user.widget
 * @author dyn
 * @org com.zhongjiang.youxuan
 * @describe GridView 自动内容宽高布局  autoFlowLayout适配器
 * @email 583454199@qq.com
 **/
public abstract class FlowAdapter<T extends IStringContent> {
    private List<T> mList;

    private AutoFlowLayout mAutoFlowLayout;
    public FlowAdapter(AutoFlowLayout autoFlowLayout) {
        mAutoFlowLayout = autoFlowLayout;
        mAutoFlowLayout.setAdapter(this);
    }

    public AutoFlowLayout getAutoFlowLayout() {
        return mAutoFlowLayout;
    }

    public T getItem(int position) {
        return mList.get(position);
    }

    public int getCount() {
        return mList == null ? 0 : mList.size();
    }
    /**
     * @param list 数据
     * @Description: 刷新数据，对应界面下拉
     */
    public void resetList(List<T> list) {
        if (mList == null) {
            mList = new ArrayList<T>();
        }
        if (list == null) {
            list = new ArrayList<T>();
        }
        mList.clear();
        mAutoFlowLayout.clearViews();
        addList(list);
    }
    /**
     * @param list 数据
     * @Description: 添加数据到列表 ，对应界面上拉
     */
    public void addList(List<T> list) {
        if (mList == null) {
            mList = new ArrayList<T>();
        }
        if (list == null) {
            list = new ArrayList<T>();
        }
        mList.addAll(list);
        mAutoFlowLayout.notifyDataSetChange();
    }
    public void addData(T data){
        if (mList == null) {
            mList = new ArrayList<T>();
        }
        if (data == null){
            return;
        }
        mList.add(data);
        mAutoFlowLayout.addView(getView(data));
    }
    public List<T> getData(){
        return mList;
    }
    public void removePosition(int position){
        if (position< mList.size()) {
            mList.remove(position);
            mAutoFlowLayout.removeViewAt(position);
        }
    }
    public void notifyDataSetChange(){
        if (mAutoFlowLayout != null){
            mAutoFlowLayout.clearViews();
            mAutoFlowLayout.notifyDataSetChange();
        }
    }
    public boolean isEmpty(){
        return mList == null || mList.isEmpty();
    }
    public abstract View getView(T data);
}