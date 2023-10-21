package com.dyn.base.ui.base.recycler.addImage

import androidx.lifecycle.MutableLiveData

class AddImageLiveData(private val mMaxCount: Int = 9) {
    private val mDatas = mutableListOf<AddImageItemBean>()
    val mItems = MutableLiveData<MutableList<AddImageItemBean>>()
    private val mAddItemBean = AddImageItemBean(isAdd = true)


    init {
        mAddItemBean.isAdd = true
        mDatas.add(mAddItemBean)
        mItems.value = mDatas
    }

    fun addData(newData: MutableList<AddImageItemBean>?) {
        if (newData.isNullOrEmpty()){
            return
        }
        mDatas.addAll(mDatas.size - 1, newData)
        if (mDatas.size - 1 >= mMaxCount) {
            mDatas.remove(mAddItemBean)
        }
        mItems.value = mDatas
    }

    fun removeAddBean(){
        if (mDatas.contains(mAddItemBean)){
            mDatas.remove(mAddItemBean)
        }
        mItems.value = mDatas
    }

    fun addData(itemData: AddImageItemBean) {
        mDatas.add(mDatas.size - 1, itemData)
        if (mDatas.size - 1 >= mMaxCount) {
            mDatas.remove(mAddItemBean)
        }
        mItems.value = mDatas
    }

    /**
     * 移除数据
     * @param checkAdd 检查是否有add添加按钮  true 如果列表中没有添加按钮 则添加一个 false 则不添加
     * */
    fun remove(position: Int) {
        mDatas.removeAt(position)
        if (!mDatas.contains(mAddItemBean)) {
            mDatas.add(mAddItemBean)
        }
        mItems.value = mDatas
    }

    fun remove(itemData: AddImageItemBean) {
        if (mDatas.contains(itemData)) {
            remove(mDatas.indexOf(itemData))
        }
    }

    /**
     * 可以添加的数量
     * */
    fun getAddCount(): Int {
        return mMaxCount - mDatas.size + 1
    }

    fun getAddedData():MutableList<AddImageItemBean>?{
        return mDatas?.filter {
            it.isAdd.not()
        }?.toMutableList()
    }

    fun isAddEmpty():Boolean{
        return mDatas?.filter {
            it.isAdd.not()
        }.isNullOrEmpty()
    }
    fun getImageStr(): String {
        return if (isAddEmpty()){
            ""
        }else{
            mDatas?.filter {
                it.isAdd.not()
            }?.joinToString(",") {
                it.successUrl?:""
            }?:""
        }
    }
}