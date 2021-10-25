package com.dyn.base.ui.magicindicator

import android.content.Context
import androidx.annotation.IntRange
import androidx.annotation.NonNull
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView

abstract class BaseMagicIndicatorAdapter<T>: CommonNavigatorAdapter() {
    /**
     * data, Only allowed to get.
     * 数据, 只允许 get。
     */
    var data: MutableList<T> =  arrayListOf()
        internal set
    override fun getCount(): Int {
        return data.size
    }

    override fun getTitleView(context: Context, index: Int): IPagerTitleView {
        return createTitleView(context, index)
    }

    override fun getIndicator(context: Context): IPagerIndicator {
        return createPagerIndicator(context)
    }

    abstract fun createTitleView(context: Context, index: Int): IPagerTitleView

    abstract fun createPagerIndicator(context: Context): IPagerIndicator

    /**
     * setting up a new instance to data;
     * 设置新的数据实例，替换原有内存引用。
     * 通常情况下，如非必要，请使用[setList]修改内容
     *
     * @param list
     */
    open fun setNewInstance(list: MutableList<T>?) {
        if (list === this.data) {
            return
        }
        this.data = list ?: arrayListOf()
        notifyDataSetChanged()
    }

    /**
     * 使用新的数据集合，改变原有数据集合内容。
     * 注意：不会替换原有的内存引用，只是替换内容
     *
     * @param list Collection<T>?
     */
    open fun setList(list: Collection<T>?) {
        if (list !== this.data) {
            this.data.clear()
            if (!list.isNullOrEmpty()) {
                this.data.addAll(list)
            }
        } else {
            if (!list.isNullOrEmpty()) {
                val newList = ArrayList(list)
                this.data.clear()
                this.data.addAll(newList)
            } else {
                this.data.clear()
            }
        }
        notifyDataSetChanged()
    }

    /**
     * change data
     * 改变某一位置数据
     */
    open fun setData(@IntRange(from = 0) index: Int, data: T) {
        if (index >= this.data.size) {
            return
        }
        this.data[index] = data
        notifyDataSetChanged()
    }

    /**
     * add one new data in to certain location
     * 在指定位置添加一条新数据
     *
     * @param position
     */
    open fun addData(@IntRange(from = 0) position: Int, data: T) {
        this.data.add(position, data)
        notifyDataSetChanged()
    }

    /**
     * add one new data
     * 添加一条新数据
     */
    open fun addData(@NonNull data: T) {
        this.data.add(data)
        notifyDataSetChanged()
    }

    /**
     * add new data in to certain location
     * 在指定位置添加数据
     *
     * @param position the insert position
     * @param newData  the new data collection
     */
    open fun addData(@IntRange(from = 0) position: Int, newData: Collection<T>) {
        this.data.addAll(position, newData)
        notifyDataSetChanged()
    }

    open fun addData(@NonNull newData: Collection<T>) {
        this.data.addAll(newData)
        notifyDataSetChanged()
    }

    /**
     * remove the item associated with the specified position of adapter
     * 删除指定位置的数据
     *
     * @param position
     */
    open fun removeAt(@IntRange(from = 0) position: Int) {
        if (position >= data.size) {
            return
        }
        this.data.removeAt(position)
        notifyDataSetChanged()
    }

    open fun remove(data: T) {
        val index = this.data.indexOf(data)
        if (index == -1) {
            return
        }
        removeAt(index)
    }

    fun getItem(index:Int):T?{
       return if (index>=0 && index<data.size){
             data[index]
        } else {
             null
        }
    }
    private var mOnPageSelectedListeners:MutableList<OnPageSelectedListener>? = null
    fun addOnPageSelectedListener(listener: OnPageSelectedListener) {
        if (mOnPageSelectedListeners == null){
            mOnPageSelectedListeners = ArrayList()
        }
        mOnPageSelectedListeners!!.add(listener)
    }

    fun onPageSelected(position: Int){
        mOnPageSelectedListeners?.let { data->
            data.forEach {
                it.onItemSelected(position)
            }
        }
    }

}