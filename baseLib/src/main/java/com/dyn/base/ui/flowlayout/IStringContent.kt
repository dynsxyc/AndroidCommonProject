package com.dyn.base.ui.flowlayout

import java.io.Serializable

/**
 * @date on 2019/07/24 11:55
 * @author Administrator
 * @describe 文本选择数据适配接口
 * @email 583454199@qq.com
 */
interface IStringContent : Serializable {
    var content: String
    var isSelected: Boolean
}
