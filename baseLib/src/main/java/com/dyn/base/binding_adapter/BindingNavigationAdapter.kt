package com.dyn.base.binding_adapter

import android.content.res.ColorStateList
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.annotation.NonNull
import androidx.core.view.forEach
import androidx.core.view.forEachIndexed
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph
import androidx.navigation.NavOptions
import com.dyn.base.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.lang.ref.WeakReference

object BindingNavigationAdapter {

    @BindingAdapter(value = ["navigationSelectedListener"])
    @JvmStatic
    fun navigationSelectedListener(
        bottomNavigationView: BottomNavigationView,
       listener: BottomNavigationView.OnNavigationItemSelectedListener?
    ) {
        listener?.let {
            bottomNavigationView.setOnNavigationItemSelectedListener(it)
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
            bottomNavigationView.menu.forEachIndexed { index, item ->
                (bottomNavigationView.getChildAt(0) as ViewGroup).getChildAt(index)
                    .setOnLongClickListener { true }
            }
        }
    }
}