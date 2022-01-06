package com.mcxtzhang.indexlib.indexbar.helper

import com.mcxtzhang.indexlib.indexbar.bean.BaseIndexPinyinBean
import com.github.promeg.pinyinhelper.Pinyin
import java.lang.StringBuilder
import java.util.*

/**
 * 介绍：IndexBar 的 数据相关帮助类 实现
 * * 1 将汉语转成拼音(利用tinyPinyin)
 * 2 填充indexTag (取拼音首字母)
 * 3 排序源数据源
 * 4 根据排序后的源数据源->indexBar的数据源
 * 作者：zhangxutong
 * 邮箱：mcxtzhang@163.com
 * 主页：http://blog.csdn.net/zxt0601
 * 时间： 2016/11/28.
 */
class IndexBarDataHelperImpl : IIndexBarDataHelper {
    /**
     * 如果需要，
     * 字符->拼音，
     *
     * @param datas
     */
    override fun convert(datas: MutableList<out BaseIndexPinyinBean?>?): IIndexBarDataHelper? {
        if (null == datas || datas.isEmpty()) {
            return this
        }
        val size = datas.size
        for (i in 0 until size) {
            val indexPinyinBean = datas[i]
            val pySb = StringBuilder()
            //add by zhangxutong 2016 11 10 如果不是top 才转拼音，否则不用转了
            if (indexPinyinBean!!.isNeedToPinyin) {
                val target = indexPinyinBean.target //取出需要被拼音化的字段
                //遍历target的每个char得到它的全拼音
                for (i1 in 0 until target!!.length) {
                    //利用TinyPinyin将char转成拼音
                    //查看源码，方法内 如果char为汉字，则返回大写拼音
                    //如果c不是汉字，则返回String.valueOf(c)
                    pySb.append(Pinyin.toPinyin(target[i1]).toUpperCase())
                }
                indexPinyinBean.setBaseIndexPinyin(pySb.toString()) //设置城市名全拼音
            } else {
                //pySb.append(indexPinyinBean.getBaseIndexPinyin());
            }
        }
        return this
    }

    /**
     * 如果需要取出，则
     * 取出首字母->tag,或者特殊字母 "#".
     * 否则，用户已经实现设置好
     *
     * @param datas
     */
    override fun fillInexTag(datas: MutableList<out BaseIndexPinyinBean?>?): IIndexBarDataHelper? {
        if (null == datas || datas.isEmpty()) {
            return this
        }
        val size = datas.size
        for (i in 0 until size) {
            val indexPinyinBean = datas[i]
            if (indexPinyinBean!!.isNeedToPinyin) {
                //以下代码设置城市拼音首字母
                val tagString = indexPinyinBean.baseIndexPinyin.toString().substring(0, 1)
                if (tagString.matches(Regex("[A-Z]"))) { //如果是A-Z字母开头
                    indexPinyinBean.setBaseIndexTag(tagString)
                } else { //特殊字母这里统一用#处理
                    indexPinyinBean.setBaseIndexTag("#")
                }
            }
        }
        return this
    }

    override fun sortSourceDatas(datas: MutableList<out BaseIndexPinyinBean?>?): IIndexBarDataHelper? {
        if (null == datas || datas.isEmpty()) {
            return this
        }
        convert(datas)
        fillInexTag(datas)
        //对数据源进行排序
        datas.sortWith(Comparator { lhs, rhs ->
            if (lhs == null) {
                0
            } else
                if (!lhs.isNeedToPinyin) {
                    0
                } else if (!rhs!!.isNeedToPinyin) {
                    0
                } else if (lhs.baseIndexTag == "#") {
                    1
                } else if (rhs.baseIndexTag == "#") {
                    -1
                } else {
                    lhs.baseIndexPinyin!!.compareTo(rhs.baseIndexPinyin!!)
                }
        })
        return this
    }

    override fun getSortedIndexDatas(
        sourceDatas: MutableList<out BaseIndexPinyinBean?>?,
        indexDatas: MutableList<String?>?
    ): IIndexBarDataHelper? {
        if (null == sourceDatas || sourceDatas.isEmpty()) {
            return this
        }
        //按数据源来 此时sourceDatas 已经有序
        val size = sourceDatas.size
        var baseIndexTag: String?
        for (i in 0 until size) {
            baseIndexTag = sourceDatas[i]!!.baseIndexTag
            if (!indexDatas!!.contains(baseIndexTag)) { //则判断是否已经将这个索引添加进去，若没有则添加
                indexDatas.add(baseIndexTag)
            }
        }
        return this
    }
}