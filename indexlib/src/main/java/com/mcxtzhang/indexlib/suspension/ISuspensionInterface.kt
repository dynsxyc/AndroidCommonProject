package com.mcxtzhang.indexlib.suspension

/**
 * 介绍：分类悬停的接口
 * 作者：zhangxutong
 * 邮箱：mcxtzhang@163.com
 * 主页：http://blog.csdn.net/zxt0601
 * 时间： 2016/11/7.
 */
interface ISuspensionInterface {
    //是否需要显示悬停title
    val isShowSuspension: Boolean

    //悬停的title
    val suspensionTag: String?
}