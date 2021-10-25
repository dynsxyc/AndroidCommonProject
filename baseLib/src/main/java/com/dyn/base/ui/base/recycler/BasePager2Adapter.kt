package com.dyn.base.ui.base.recycler

import androidx.annotation.IntRange
import androidx.annotation.NonNull
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.chad.library.adapter.base.BaseQuickAdapter

abstract class BasePager2Adapter<T>(fragment: Fragment) : FragmentStateAdapter(fragment) {
    /**
     * data, Only allowed to get.
     * 数据, 只允许 get。
     */
    var data: MutableList<T> =  arrayListOf()
        internal set
    override fun getItemCount(): Int {
        return data.count()
    }

    override fun createFragment(position: Int): Fragment {
        if (position<data.size){
            return createFragment(position, data[position])
        }
        return createFragment(position, null)
    }
    abstract fun createFragment(position: Int,data: T?):Fragment
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
        notifyItemChanged(index )
    }

    /**
     * add one new data in to certain location
     * 在指定位置添加一条新数据
     *
     * @param position
     */
    open fun addData(@IntRange(from = 0) position: Int, data: T) {
        this.data.add(position, data)
        notifyItemInserted(position )
        compatibilityDataSizeChanged(1)
    }

    /**
     * add one new data
     * 添加一条新数据
     */
    open fun addData(@NonNull data: T) {
        this.data.add(data)
        notifyItemInserted(this.data.size )
        compatibilityDataSizeChanged(1)
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
        notifyItemRangeInserted(position , newData.size)
        compatibilityDataSizeChanged(newData.size)
    }

    open fun addData(@NonNull newData: Collection<T>) {
        this.data.addAll(newData)
        notifyItemRangeInserted(this.data.size - newData.size , newData.size)
        compatibilityDataSizeChanged(newData.size)
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
        notifyItemRemoved(position)
        compatibilityDataSizeChanged(0)
        notifyItemRangeChanged(position, this.data.size - position)
    }

    open fun remove(data: T) {
        val index = this.data.indexOf(data)
        if (index == -1) {
            return
        }
        removeAt(index)
    }


    /**
     * compatible getLoadMoreViewCount and getEmptyViewCount may change
     *
     * @param size Need compatible data size
     */
    protected fun compatibilityDataSizeChanged(size: Int) {
        if (this.data.size == size) {
            notifyDataSetChanged()
        }
    }
}