package com.dyn.base.ui.fixedheightscrollview

import android.content.Context
import android.view.ViewConfiguration

class FlingHelper(context: Context) {
    private fun getSplineDeceleration(i: Int): Double {
        return Math.log((0.35f * Math.abs(i).toFloat() / (mFlingFriction * mPhysicalCoeff)).toDouble())
    }

    private fun getSplineDecelerationByDistance(d: Double): Double {
        return (DECELERATION_RATE.toDouble() - 1.0) * Math.log(d / (mFlingFriction * mPhysicalCoeff).toDouble()) / DECELERATION_RATE.toDouble()
    }

    fun getSplineFlingDistance(i: Int): Double {
        return Math.exp(getSplineDeceleration(i) * (DECELERATION_RATE.toDouble() / (DECELERATION_RATE.toDouble() - 1.0))) * (mFlingFriction * mPhysicalCoeff).toDouble()
    }

    fun getVelocityByDistance(d: Double): Int {
        return Math.abs((Math.exp(getSplineDecelerationByDistance(d)) * mFlingFriction.toDouble() * mPhysicalCoeff.toDouble() / 0.3499999940395355).toInt())
    }

    companion object {
        private val DECELERATION_RATE = (Math.log(0.78) / Math.log(0.9)).toFloat()
        private val mFlingFriction = ViewConfiguration.getScrollFriction()
        private var mPhysicalCoeff: Float = 0f
    }

    init {
        mPhysicalCoeff = context.resources.displayMetrics.density * 160.0f * 386.0878f * 0.84f
    }
}