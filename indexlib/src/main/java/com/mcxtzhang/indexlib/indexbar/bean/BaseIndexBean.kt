package com.mcxtzhang.indexlib.indexbar.bean

import com.mcxtzhang.indexlib.suspension.ISuspensionInterface
import com.mcxtzhang.indexlib.indexbar.bean.BaseIndexBean

/**
 * 介绍：索引类的标志位的实体基类
 * 作者：zhangxutong
 * 邮箱：mcxtzhang@163.com
 * CSDN：http://blog.csdn.net/zxt0601
 * 时间： 16/09/04.
 */
abstract class BaseIndexBean : ISuspensionInterface {
    open var baseIndexTag: String? = null//所属的分类（城市的汉语拼音首字母）

    fun setBaseIndexTag(baseIndexTag: String?): BaseIndexBean {
        this.baseIndexTag = baseIndexTag
        return this
    }

    override val suspensionTag: String?
        get() = baseIndexTag

    override val isShowSuspension: Boolean
        get() = true
}