package com.dyn.base.utils

import com.blankj.utilcode.util.RegexUtils

object StrUtils {
    fun hidePhoneCenter(str: String?): String {
        return when {
            str.isNullOrEmpty() -> {
                ""
            }
            RegexUtils.isMobileSimple(str) -> {
                "${str.substring(0, 3)}****${str.substring(7)}"
            }
            else -> {
                str
            }
        }
    }
    /**
     * 校验密码 8-16位 英文和数字
     * */
    fun String.checkPass(minLength :Int = 8,maxLength:Int = 16):Boolean{
        return RegexUtils.isMatch("^(?![0-9]+\$)(?![a-zA-Z]+\$)[0-9A-Za-z]{$minLength,$maxLength}\$",this)
    }
}