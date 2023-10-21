package com.dyn.base.binding_adapter

import android.view.View
import androidx.databinding.BindingAdapter
import com.allen.library.shape.ShapeConstraintLayout
import com.allen.library.shape.ShapeFrameLayout
import com.allen.library.shape.ShapeTextView

object BindingShapeViewAdapter {
    @BindingAdapter(value = ["shapeGradientStartColor","shapeGradientCenterColor","shapeGradientEndColor"], requireAll = false)
    @JvmStatic
    fun shapeGradientColor(view:View,startColor:Int?,centerColor:Int?,endColor:Int?){
        when (view){
            is ShapeTextView->{
                if (startColor != null) {
                    view.attributeSetData.gradientStartColor = startColor
                }
                if (centerColor != null) {
                    view.attributeSetData.gradientCenterColor = centerColor
                }

                if (endColor != null) {
                    view.attributeSetData.gradientEndColor = endColor
                }
                view.shapeBuilder?.init(view, view.attributeSetData)
            }
            is ShapeFrameLayout->{
                if (startColor != null) {
                    view.attributeSetData.gradientStartColor = startColor
                }
                if (centerColor != null) {
                    view.attributeSetData.gradientCenterColor = centerColor
                }

                if (endColor != null) {
                    view.attributeSetData.gradientEndColor = endColor
                }
                view.shapeBuilder?.init(view, view.attributeSetData)
            }
            is ShapeConstraintLayout->{
                if (startColor != null) {
                    view.attributeSetData.gradientStartColor = startColor
                }
                if (centerColor != null) {
                    view.attributeSetData.gradientCenterColor = centerColor
                }

                if (endColor != null) {
                    view.attributeSetData.gradientEndColor = endColor
                }
                view.shapeBuilder?.init(view, view.attributeSetData)
            }
        }
    }
}