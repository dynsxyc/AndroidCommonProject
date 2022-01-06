package com.mcxtzhang.indexlib.indexbar.bean

import com.mcxtzhang.indexlib.indexbar.bean.BaseIndexBean
import com.mcxtzhang.indexlib.indexbar.bean.BaseIndexPinyinBean

/**
 * 介绍：索引类的汉语拼音的接口
 * 作者：zhangxutong
 * 邮箱：mcxtzhang@163.com
 * CSDN：http://blog.csdn.net/zxt0601
 * 时间： 16/09/04.
 */
abstract class BaseIndexPinyinBean : BaseIndexBean() {
    open var baseIndexPinyin: String? = null //城市的拼音


    fun setBaseIndexPinyin(baseIndexPinyin: String?): BaseIndexPinyinBean {
        this.baseIndexPinyin = baseIndexPinyin
        return this
    }

    //是否需要被转化成拼音， 类似微信头部那种就不需要 美团的也不需要
    //微信的头部 不需要显示索引
    //美团的头部 索引自定义
    //默认应该是需要的
    open var isNeedToPinyin: Boolean = true

    //需要转化成拼音的目标字段
    abstract val target: String?
}