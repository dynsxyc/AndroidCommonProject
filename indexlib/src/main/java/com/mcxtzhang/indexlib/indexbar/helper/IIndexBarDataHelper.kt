package com.mcxtzhang.indexlib.indexbar.helper

import com.mcxtzhang.indexlib.indexbar.bean.BaseIndexPinyinBean
import com.mcxtzhang.indexlib.indexbar.helper.IIndexBarDataHelper

/**
 * 介绍：IndexBar 的 数据相关帮助类
 * 1 将汉语转成拼音
 * 2 填充indexTag
 * 3 排序源数据源
 * 4 根据排序后的源数据源->indexBar的数据源
 * 作者：zhangxutong
 * 邮箱：mcxtzhang@163.com
 * 主页：http://blog.csdn.net/zxt0601
 * 时间： 2016/11/28.
 */
interface IIndexBarDataHelper {
    //汉语-》拼音
    fun convert(data: MutableList<out BaseIndexPinyinBean?>?): IIndexBarDataHelper?

    //拼音->tag
    fun fillInexTag(data: MutableList<out BaseIndexPinyinBean?>?): IIndexBarDataHelper?

    //对源数据进行排序（RecyclerView）
    fun sortSourceDatas(datas: MutableList<out BaseIndexPinyinBean?>?): IIndexBarDataHelper?

    //对IndexBar的数据源进行排序(右侧栏),在 sortSourceDatas 方法后调用
    fun getSortedIndexDatas(
        sourceDatas: MutableList<out BaseIndexPinyinBean?>?,
        datas: MutableList<String?>?
    ): IIndexBarDataHelper?
}