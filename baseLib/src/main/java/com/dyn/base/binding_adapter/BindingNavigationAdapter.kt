package com.dyn.base.binding_adapter

import android.content.res.ColorStateList
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.core.view.forEachIndexed
import androidx.core.view.get
import androidx.databinding.BindingAdapter
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView.OnItemSelectedListener

object BindingNavigationAdapter {

    @BindingAdapter(value = ["navigationSelectedListener"])
    @JvmStatic
    fun navigationSelectedListener(
        bottomNavigationView: BottomNavigationView,
       listener: OnItemSelectedListener?
    ) {
        listener?.let {
            bottomNavigationView.setOnItemSelectedListener(it)
        }
    }
    @BindingAdapter(value = ["navigationItemIconTintList"])
    @JvmStatic
    fun itemIconTintList(
        bottomNavigationView: BottomNavigationView,
        colorStateList: ColorStateList? = null
    ) {

        bottomNavigationView.itemIconTintList = colorStateList
    }

    @BindingAdapter(value = ["navigationSelectedId"])
    @JvmStatic
    fun navigationSelectedId(
        bottomNavigationView: BottomNavigationView,
        @IdRes newId: Int
    ) {
        if (bottomNavigationView.selectedItemId != newId){
            bottomNavigationView.selectedItemId = newId
        }
    }

    @BindingAdapter(value = ["navigationViewHideLongTips"])
    @JvmStatic
    fun navigationViewHideLongTips(
        bottomNavigationView: BottomNavigationView,
        hide: Boolean
    ) {
            if (hide) {
                bottomNavigationView.menu.forEachIndexed { index, _ ->
                    (bottomNavigationView.getChildAt(0) as ViewGroup?)?.getChildAt(index)?.setOnLongClickListener { true }
                }
        }

    }
    @BindingAdapter(value = ["hasIconTint"])
    @JvmStatic
    fun hasIconTint(
        bottomNavigationView: BottomNavigationView,
        hide: Boolean
    ) {
            if (hide.not()) {
                bottomNavigationView.itemIconTintList = null
        }

    }
    data class NavShowHideData(var isShow:Boolean,val position:Int)
    @BindingAdapter(value = ["navigationViewHideData"])
    @JvmStatic
    fun navigationViewHideByPosition(
        bottomNavigationView: BottomNavigationView,
        data:NavShowHideData?
    ) {
        data?.let {
            val position = data.position
            val positionItemMenu = bottomNavigationView.menu[position]
            val currentVisible = positionItemMenu.isVisible
            if (currentVisible != data.isShow){
                positionItemMenu.isVisible = data.isShow
            }
        }

    }
    @BindingAdapter(value = ["navigationViewHideDataList"])
    @JvmStatic
    fun navigationViewHideByPosition(
        bottomNavigationView: BottomNavigationView,
        data:List<NavShowHideData>?
    ) {
        data?.let {
          it.forEach { item->
              val position = item.position
              val positionItemMenu = bottomNavigationView.menu[position]
              val currentVisible = positionItemMenu.isVisible
              if (currentVisible != item.isShow){
                  positionItemMenu.isVisible = item.isShow
              }
          }
        }

    }
}