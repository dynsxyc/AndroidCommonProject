package com.dyn.base.utils

import java.math.BigDecimal
import java.math.RoundingMode

object DigitUtils {

    private val ONE_WAN = BigDecimal(10000)
    fun Int.fenToYuan(): String {
        return this.toBigDecimal().divide(BigDecimal(100), 2, RoundingMode.HALF_UP).toString()
    }

    fun String.fenToYuan(): String {
        if (this.isNullOrEmpty()) {
            return "0.00"
        }
        return BigDecimal(this).divide(BigDecimal(100), 2, RoundingMode.HALF_UP).toString()
    }

    fun String?.toMBigDecimal(dotLength :Int = 2): BigDecimal {
        return try {
            if (this.isNullOrEmpty()){
                BigDecimal.ZERO.setScale(dotLength, RoundingMode.HALF_UP)
            }else{
                this.toBigDecimal().setScale(dotLength, RoundingMode.HALF_UP)
            }
        } catch (e: Exception) {
            BigDecimal.ZERO.setScale(dotLength, RoundingMode.HALF_UP)
        }
    }
    /**
     * 字符串元转分
     * */
    fun String?.yuan2Wan():String{
        val b = this.toMBigDecimal()
        if (b < ONE_WAN){
            return b.toString()
        }
        val remainder = b.remainder(ONE_WAN)
        return if (remainder<= BigDecimal.ZERO){
            b.divide(ONE_WAN).setScale(0).toString().plus("万")
        }else{
            b.divide(ONE_WAN).setScale(2,RoundingMode.HALF_UP).toString().plus("万")
        }
    }
    /**
     * 金额显示  保留两位小数显示
     * */
    fun String?.toShowMoney():String{
        if (this.isNullOrEmpty()){
            return "0.00"
        }
        return this.toMBigDecimal().toString()
    }
    private val hundredBig = BigDecimal(100)
    private val tenBig = BigDecimal(10)
    /**
     * 金额显示  保留有效位数显示
     * */
    fun String?.toShowEffectivePrice():String{
        if (this.isNullOrEmpty()){
            return "0.0"
        }
        return try {
            val oBig = this.toMBigDecimal().multiply(hundredBig).setScale(0, RoundingMode.DOWN)
            val hundred = oBig.divideAndRemainder(hundredBig)[1]//求余 数组0位为商  1位为余
            val ten = oBig.divideAndRemainder(tenBig)[1]
            if (hundred == BigDecimal.ZERO) {
                this.toMBigDecimal(0).toString()
            } else if (ten == BigDecimal.ZERO) {
                this.toMBigDecimal(1).toString()
            } else {
                this.toMBigDecimal().toString()
            }
        }catch (e:Exception){
            this
        }
    }

    fun BigDecimal.fenToYuan(): String {
        return this.divide(BigDecimal(100), 2, RoundingMode.HALF_UP).toString()
    }

    /**
     * 计算两个单位为分的数据相减后显示的金钱数
     * @param reduction 减数
     * @param minuend 被减数
     * */
    fun minuendMoney(reduction: String?, minuend: String?): String {
        if (reduction.isNullOrEmpty()) {
            return "0.00"
        }
        val minuendBigDecimal = if (minuend.isNullOrEmpty()) {
            BigDecimal.ZERO
        } else {
            BigDecimal(minuend)
        }
        val reductionBigDecimal = BigDecimal(reduction)
        return reductionBigDecimal.subtract(minuendBigDecimal).setScale(2,RoundingMode.HALF_UP).toString()
    }

    /**
     * 多个钱相乘
     * */
    fun multiplyMorMoney(vararg moneys: String?): BigDecimal {
        val convertBigDecimals = moneys.map {
            if (it.isNullOrEmpty()) {
                BigDecimal.ZERO
            } else {
                try {
                    BigDecimal(it)
                } catch (e: Exception) {
                    BigDecimal.ZERO
                }
            }
        }
        var result = BigDecimal.ONE
        convertBigDecimals.forEach {
            result = result.multiply(it)
        }
        return result
    }

    /**
     * price 单价元
     * */
    fun Int.countToTotalMoney(price: String?): String {
        if (price.isNullOrEmpty()) {
            return ""
        }
        return BigDecimal(this).multiply(BigDecimal(price)).setScale(2, RoundingMode.HALF_UP)
            .toString()
    }
}