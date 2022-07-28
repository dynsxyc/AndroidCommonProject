/*
 * Copyright 2018-present KunMinX
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.dyn.base.binding_adapter

import android.animation.ObjectAnimator
import android.app.Activity
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.text.InputFilter
import android.text.InputFilter.LengthFilter
import android.text.InputType
import android.text.method.HideReturnsTransformationMethod
import android.text.method.LinkMovementMethod
import android.text.method.PasswordTransformationMethod
import android.text.method.ScrollingMovementMethod
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.view.ViewOutlineProvider
import android.view.animation.LinearInterpolator
import android.view.inputmethod.EditorInfo
import android.webkit.WebView
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.appcompat.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.ViewCompat
import androidx.core.widget.TextViewCompat
import androidx.databinding.BindingAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModel
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat
import androidx.viewpager.widget.ViewPager
import com.blankj.utilcode.util.KeyboardUtils
import com.blankj.utilcode.util.SpanUtils
import com.dyn.base.R
import com.dyn.base.customview.ICustomViewActionListener
import com.dyn.base.ui.navigationbar.DefaultNavigationBar
import com.dyn.base.ui.weight.ClearEditText
import com.dyn.base.ui.weight.MessageCountTextView
import com.dyn.base.ui.weight.header.CommonHeaderModel
import com.dyn.base.ui.weight.header.CommonHeaderView
import com.dyn.base.ui.weight.passedit.PasswordEditText
import com.dyn.base.utils.ImageLoaderManager
import com.dyn.base.utils.StarTransformationMethod
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.tabs.TabLayout
import com.gyf.immersionbar.ImmersionBar
import com.jakewharton.rxbinding4.view.clicks
import com.jakewharton.rxbinding4.view.scrollChangeEvents
import com.jakewharton.rxbinding4.widget.editorActions
import java.util.concurrent.TimeUnit


/**
 */
object BindingCommonAdapter {
    @BindingAdapter(value = ["imageUrl", "placeHolder"], requireAll = false)
    @JvmStatic
    fun loadUrl(
        view: ImageView,
        url: Any?,
        placeHolder: Drawable?
    ) {
        url?.let {
            ImageLoaderManager.displayImage(view, it, placeHolder)
        }
    }


    @BindingAdapter(value = ["imageBitmap"], requireAll = false)
    @JvmStatic
    fun imageBitmap(view: ImageView, bitmap: Bitmap?) {
        bitmap?.let {
            view.setImageBitmap(it)
        }
    }

    @BindingAdapter(value = ["imageDrawable"], requireAll = false)
    @JvmStatic
    fun imageDrawable(view: ImageView, bitmap: Drawable?) {
        bitmap?.let {
            view.setImageDrawable(it)
        }
    }


    @BindingAdapter(value = ["visible"], requireAll = false)
    @JvmStatic
    fun visible(view: View, visible: Boolean) {
        view.visibility = if (visible) View.VISIBLE else View.GONE
    }

//    @BindingAdapter(value = ["visibleCustomView"], requireAll = false)
//    @JvmStatic
//    fun visibleCustomView(view: BaseCustomView<*, *>, visible: Boolean) {
//        view.visibility = if (visible) View.VISIBLE else View.GONE
//        view.invalidateAll()
//    }

    @BindingAdapter(value = ["rotate"], requireAll = false)
    @JvmStatic
    fun rotate(view: View, rotation: Float) {
        view.rotation = rotation
//        val animator = ObjectAnimator.ofFloat(view,"rotation",view.rotation,rotate)
//        animator.duration = 300
//        animator.interpolator = LinearInterpolator()
//        animator.start()
    }

    @BindingAdapter(value = ["rotationAnimation"], requireAll = false)
    @JvmStatic
    fun rotationAnimation(view: View, rotation: Float) {
        val animator = ObjectAnimator.ofFloat(view, "rotation", view.rotation, rotation)
        animator.duration = 300
        animator.interpolator = LinearInterpolator()
        animator.start()
    }

    @BindingAdapter(value = ["inputType"], requireAll = false)
    @JvmStatic
    fun inputType(view: EditText, type: Int) {
        view.inputType = type
        if (isAnyPasswordInputType(type)) {
            InputType.TYPE_CLASS_TEXT
            view.transformationMethod = PasswordTransformationMethod.getInstance()
        } else {
            view.transformationMethod = HideReturnsTransformationMethod.getInstance()
        }
    }

    @BindingAdapter(value = ["textViewSingleLine"], requireAll = false)
    @JvmStatic
    fun textViewSingleLine(view: EditText, isSingleLine: Boolean) {
        view.isSingleLine = isSingleLine
    }

    @BindingAdapter(value = ["isStarVisible"], requireAll = false)
    @JvmStatic
    fun isStarVisible(view: TextView, isShow: Boolean) {
        if (isShow) {
            view.transformationMethod = StarTransformationMethod.getInstance()
        } else {
            view.transformationMethod = HideReturnsTransformationMethod.getInstance()
        }
    }

    @BindingAdapter(value = ["maxLength"], requireAll = false)
    @JvmStatic
    fun maxLength(view: TextView, maxlength: Int) {
        if (maxlength >= 0) {
            view.filters = arrayOf<InputFilter>(LengthFilter(maxlength))
        } else {
            view.filters = arrayOf()
        }
    }

    @JvmStatic
    fun isAnyPasswordInputType(inputType: Int): Boolean {
        return isPasswordInputType(inputType) || isVisiblePasswordInputType(inputType)
    }

    private fun isPasswordInputType(inputType: Int): Boolean {
        val variation = inputType and (EditorInfo.TYPE_MASK_CLASS or EditorInfo.TYPE_MASK_VARIATION)
        return (variation
                == EditorInfo.TYPE_CLASS_TEXT or EditorInfo.TYPE_TEXT_VARIATION_PASSWORD) || (variation
                == EditorInfo.TYPE_CLASS_TEXT or EditorInfo.TYPE_TEXT_VARIATION_WEB_PASSWORD) || (variation
                == EditorInfo.TYPE_CLASS_NUMBER or EditorInfo.TYPE_NUMBER_VARIATION_PASSWORD)
    }

    private fun isVisiblePasswordInputType(inputType: Int): Boolean {
        val variation = inputType and (EditorInfo.TYPE_MASK_CLASS or EditorInfo.TYPE_MASK_VARIATION)
        return (variation
                == EditorInfo.TYPE_CLASS_TEXT or EditorInfo.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD)
    }

    @BindingAdapter(value = ["inVisible"], requireAll = false)
    @JvmStatic
    fun inVisible(view: View, visible: Boolean) {
        view.visibility = if (visible) View.VISIBLE else View.INVISIBLE
    }

    @BindingAdapter(value = ["imageRes"], requireAll = false)
    @JvmStatic
    fun setImageRes(imageView: ImageView, imageRes: Int) {
        imageView.setImageResource(imageRes)
    }

    @BindingAdapter(value = ["isShowHidePassMode"], requireAll = false)
    @JvmStatic
    fun isShowHidePassMode(et: ClearEditText, isPass: Boolean) {
        et.isShowHidePassMode = isPass
    }

    @BindingAdapter(value = ["selected"], requireAll = false)
    @JvmStatic
    fun selected(view: View, select: Boolean) {
        view.isSelected = select
    }

    @BindingAdapter(value = ["onClickWithDebouncing"], requireAll = false)
    @JvmStatic
    fun onClickWithDebouncing(view: View, clickListener: View.OnClickListener?) {
        view.clicks().throttleFirst(800, TimeUnit.MILLISECONDS).subscribe {
            try {
                clickListener?.onClick(view)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    interface IActionSearchListener {
        fun onSearch(str: String)
    }

    @BindingAdapter(value = ["editorSearchAction"], requireAll = false)
    @JvmStatic
    fun onTextEditorActions(editText: AppCompatEditText, searchAction: IActionSearchListener) {
        editText.editorActions {
            when (it) {
                EditorInfo.IME_ACTION_SEARCH -> {
                    if (editText.text.isNullOrEmpty().not()) {
                        searchAction.onSearch(editText.text.toString())
                    }
                    true
                }
                else -> {
                    false
                }
            }
        }.subscribe()
    }

    @BindingAdapter(value = ["editorSearchActionClick"], requireAll = false)
    @JvmStatic
    fun editorSearchActionClick(editText: AppCompatEditText, clickListener: View.OnClickListener) {
        editText.editorActions {
            when (it) {
                EditorInfo.IME_ACTION_SEARCH -> {
                    clickListener.onClick(editText)
                    true
                }
                else -> {
                    false
                }
            }
        }.subscribe()
    }

    @BindingAdapter(value = ["adjustWidth"])
    @JvmStatic
    fun adjustWidth(view: View, adjustWidth: Int) {
        val params = view.layoutParams
        params.width = adjustWidth
        view.layoutParams = params
    }

    @BindingAdapter(value = ["adjustHeight"])
    @JvmStatic
    fun adjustHeight(view: View, adjustHeight: Int) {
        val params = view.layoutParams
        params.height = adjustHeight
        view.layoutParams = params
    }

    @BindingAdapter(value = ["onScrollChangeListener"])
    @JvmStatic
    fun onScrollChangeListener(view: View, scrollChangeListener: View.OnScrollChangeListener) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            view.scrollChangeEvents().subscribe {
                scrollChangeListener.onScrollChange(
                    it.view,
                    it.scrollX,
                    it.scrollY,
                    it.oldScrollX,
                    it.oldScrollY
                )
            }
        }
    }

    interface OnPositionListener {
        fun onSelect(position: Int)
    }


    @BindingAdapter(value = ["tabLayoutBindViewPager"], requireAll = false)
    @JvmStatic
    fun initTabLayout(view: TabLayout, viewPager: ViewPager) {
        view.setupWithViewPager(viewPager)
    }

//    @JvmStatic
//    fun findStartDestination(graph: NavGraph?): NavDestination? {
//        var startDestination: NavDestination? = graph
//        while (startDestination is NavGraph) {
//            val parent = startDestination
//            startDestination = parent?.findNode(parent?.startDestination)
//        }
//        return startDestination
//    }

    @BindingAdapter(
        value = ["android:drawableStart", "android:drawableTop", "android:drawableEnd", "android:drawableBottom"],
        requireAll = false
    )
    @JvmStatic
    fun drawable(
        view: TextView,
        start: Drawable?,
        top: Drawable?,
        end: Drawable?,
        bottom: Drawable?
    ) {
        view.setCompoundDrawablesRelativeWithIntrinsicBounds(start, top, end, bottom)
    }

    @BindingAdapter(value = ["android:textColor"], requireAll = false)
    @JvmStatic
    fun text(view: TextView, textColor: Int) {
        view.setTextColor(textColor)
    }

    @BindingAdapter(value = ["dTextSize"], requireAll = false)
    @JvmStatic
    fun textSize(view: TextView, textSize: Float) {
        view.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize)
    }

//    @BindingAdapter(value = ["drawableTintRes"],requireAll = false)
//    @JvmStatic
//    fun drawableTintRes(textView: AppCompatCheckBox,@ColorRes color:Int){
//        TextViewCompat.setCompoundDrawableTintList(textView, ColorStateList.valueOf(ContextCompat.getColor(textView.context,color)))
//    }

    @BindingAdapter(value = ["drawableTint"], requireAll = false)
    @JvmStatic
    fun drawableTint(textView: AppCompatTextView, @ColorInt color: Int?) {
        color?.let {
            TextViewCompat.setCompoundDrawableTintList(textView, ColorStateList.valueOf(it))
        }
    }

    @BindingAdapter(value = ["drawableTint"], requireAll = false)
    @JvmStatic
    fun drawableTintCheckBox(textView: AppCompatCheckBox, @ColorInt color: Int) {
        TextViewCompat.setCompoundDrawableTintList(textView, ColorStateList.valueOf(color))
    }

    @BindingAdapter(
        value = ["gradientBgStartColor", "gradientBgEndColor", "gradientBgRadiusSize", "gradientOrientation"],
        requireAll = false
    )
    @JvmStatic
    fun gradientBgColor(
        view: View,
        @ColorInt startColor: Int,
        @ColorInt endColor: Int,
        radiusSize: Float,
        orientation: GradientDrawable.Orientation?
    ) {
        val drawable = GradientDrawable()
        drawable.shape = GradientDrawable.RECTANGLE
        drawable.cornerRadius = radiusSize
        drawable.gradientType = GradientDrawable.LINEAR_GRADIENT
        drawable.colors = arrayOf(startColor, endColor).toIntArray()
        drawable.orientation = orientation ?: GradientDrawable.Orientation.LEFT_RIGHT
        view.background = drawable
    }

    @BindingAdapter(value = ["fontFamily"], requireAll = false)
    @JvmStatic
    fun fontFamily(view: TextView, font: Int) {
        try {
            val typeface = ResourcesCompat.getFont(view.context, font) ?: Typeface.DEFAULT
            view.typeface = typeface
        } catch (e: Exception) {

        }
    }

    @BindingAdapter(value = ["textIsMedium"], requireAll = false)
    @JvmStatic
    fun textIsMedium(view: TextView, isMedium: Boolean) {
        try {
            view.typeface = if (isMedium) {
                ResourcesCompat.getFont(view.context, R.font.medium)
            } else {
                Typeface.DEFAULT
            }
        } catch (e: Exception) {

        }
    }

    @BindingAdapter(value = ["isMessageStatus", "messageCount"], requireAll = false)
    @JvmStatic
    fun messageCountView(view: MessageCountTextView, isMessageStatus: Boolean, messageCount: Int) {
        if (isMessageStatus) {
            view.setStatus(isMessageStatus)
        }
        view.setMessageCount(messageCount)
    }

    @BindingAdapter(value = ["layoutheight"])
    @JvmStatic
    fun layoutHeight(view: View, height: Int) {
        val lp = view.layoutParams
        lp.height = height
        view.layoutParams = lp
    }

    @BindingAdapter(value = ["backgroundColor"])
    @JvmStatic
    fun viewBgColor(view: View, @ColorInt bg: Int) {
        try {
            view.setBackgroundColor(bg)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    @BindingAdapter(
        value = ["layout_marginStart", "layout_marginTop", "layout_marginEnd", "layout_marginBottom"],
        requireAll = false
    )
    @JvmStatic
    fun layoutMargin(view: View, l: Float, t: Float, r: Float, b: Float) {
        try {
            val lp = view.layoutParams
            if (lp is ViewGroup.MarginLayoutParams) {
                if (lp.marginStart != l.toInt()) {
                    lp.marginStart = l.toInt()
                }
                if (lp.topMargin != t.toInt()) {
                    lp.topMargin = t.toInt()
                }
                if (lp.marginEnd != r.toInt()) {
                    lp.marginEnd = r.toInt()
                }
                if (lp.bottomMargin != b.toInt()) {
                    lp.bottomMargin = b.toInt()
                }
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    @BindingAdapter(value = ["src"])
    @JvmStatic
    fun imageSrc(view: ImageView, @DrawableRes bg: Int) {
        try {
            view.setImageResource(bg)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    @BindingAdapter(value = ["srcCompat"])
    @JvmStatic
    fun srcCompat(view: AppCompatImageView, @DrawableRes bg: Int) {
        try {
            val vectorDrawableCompat =
                VectorDrawableCompat.create(view.resources, bg, view.context.theme)
            view.setImageDrawable(vectorDrawableCompat)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    @BindingAdapter(value = ["srcCompat"])
    @JvmStatic
    fun srcCompat(view: AppCompatImageView, bg: Drawable?) {
        try {
            view.setImageDrawable(bg)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    @BindingAdapter(value = ["backgroundResource"])
    @JvmStatic
    fun viewBgResource(view: View, bg: Drawable?) {
        try {
            view.background = bg
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    @BindingAdapter(value = ["paddingStart", "paddingTop", "paddingEnd", "paddingBottom"])
    @JvmStatic
    fun padding(view: View, start: Float, top: Float, end: Float, bottom: Float) {
        try {
            view.setPadding(start.toInt(), top.toInt(), end.toInt(), bottom.toInt())
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    @BindingAdapter(value = ["bindStatusBar"])
    @JvmStatic
    fun bindStatusBar(view: View, isBind: Boolean) {
        if (isBind.not()) {
            return
        }
        try {
            when (view.context) {
                is Fragment -> {
                    ImmersionBar.setTitleBar(view.context as Fragment, view)
                }
                is Activity -> {
                    ImmersionBar.setTitleBar(view.context as Activity, view)
                }

            }

        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    @BindingAdapter(value = ["viewBindAction", "viewBindData"], requireAll = false)
    @JvmStatic
    fun bindAction(
        view: CommonHeaderView,
        listener: ICustomViewActionListener?,
        customViewModel: CommonHeaderModel?
    ) {
        view.mActionListener = listener
        customViewModel?.let {
            view.setData(it)
        }
    }

    /**
     * 给当前viewGroup 添加一个自定义navigationBar
     * */
    @BindingAdapter(
        value = ["bindDefaultNavigationBarAction", "bindDefaultNavigationBarModel"],
        requireAll = false
    )
    @JvmStatic
    fun attachDefaultNavigationBar(
        viewGroup: ViewGroup,
        listener: ICustomViewActionListener,
        data: CommonHeaderModel
    ) {
        DefaultNavigationBar.Builder(viewGroup.context, viewGroup).bindAction(listener).bindData(
            data
        ).create()
    }

    @BindingAdapter(value = ["resetAddView"])
    @JvmStatic
    fun resetAddView(viewGroup: ViewGroup, addView: View?) {
        addView?.let {
            try {
                //removeAllViews 不能放到外面 和createAddView方式创建View  有先后顺序冲突
                viewGroup.removeAllViews()
                viewGroup.addView(addView)
            } catch (e: Exception) {

            }
        }
    }

    @BindingAdapter(value = ["createAddView", "createAddViewModel"], requireAll = true)
    @JvmStatic
    fun createAddView(viewGroup: ViewGroup, addView: ICreateSubView?, vm: ViewModel) {
        addView?.createView(viewGroup, vm)?.let {
            viewGroup.removeAllViews()
            viewGroup.addView(it)
        }
    }

    interface ICreateSubView {
        fun createView(parent: ViewGroup, vm: ViewModel): View?
    }

    @BindingAdapter(
        value = ["drawableStart", "drawableEnd", "drawableTop", "drawableBottom"],
        requireAll = false
    )
    @JvmStatic
    fun textCompoundDrawable(
        view: TextView,
        drawableStart: Int = 0,
        drawableEnd: Int = 0,
        drawableTop: Int = 0,
        drawableBottom: Int = 0
    ) {
        try {
            val drawables = view.compoundDrawables
            if (drawableStart != 0) {
                drawables[0] = ContextCompat.getDrawable(view.context, drawableStart)
            } else {
                drawables[0] = null
            }
            if (drawableTop != 0) {
                drawables[1] = ContextCompat.getDrawable(view.context, drawableTop)
            } else {
                drawables[1] = null
            }
            if (drawableEnd != 0) {
                drawables[2] = ContextCompat.getDrawable(view.context, drawableEnd)
            } else {
                drawables[2] = null
            }
            if (drawableBottom != 0) {
                drawables[3] = ContextCompat.getDrawable(view.context, drawableBottom)
            } else {
                drawables[3] = null
            }
            view.setCompoundDrawablesWithIntrinsicBounds(
                drawables[0],
                drawables[1],
                drawables[2],
                drawables[3]
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @BindingAdapter(
        value = ["drawableStart", "drawableEnd", "drawableTop", "drawableBottom"],
        requireAll = false
    )
    @JvmStatic
    fun textCompoundDrawable(
        view: TextView,
        drawableStart: Drawable? = null,
        drawableEnd: Drawable? = null,
        drawableTop: Drawable? = null,
        drawableBottom: Drawable? = null,
    ) {
        try {

            val drawables = view.compoundDrawables
            drawables[0] = drawableStart
            drawables[1] = drawableTop
            drawables[2] = drawableEnd
            drawables[3] = drawableBottom
            view.setCompoundDrawablesWithIntrinsicBounds(
                drawables[0],
                drawables[1],
                drawables[2],
                drawables[3]
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @BindingAdapter(value = ["replaceFragment", "replaceFragmentManager"])
    @JvmStatic
    fun replaceFragment(view: View, replaceFragment: Fragment?, fragmentManager: FragmentManager?) {
        if (fragmentManager == null || replaceFragment == null) {
            return
        }
        val fragment = fragmentManager.findFragmentById(view.id)
        val transaction = fragmentManager.beginTransaction()
        if (fragment != null) {
            transaction.show(fragment)
        } else {
            transaction.replace(view.id, replaceFragment)
        }
        transaction.commit()
    }

    @BindingAdapter(
        value = ["addFragmentManager", "showFragment", "hideFragment"],
        requireAll = true
    )
    @JvmStatic
    fun addFragments(
        view: View,
        fragmentManager: FragmentManager?,
        showFragmentClass: Fragment?,
        hideFragmentClass: Fragment?
    ) {
        if (fragmentManager == null || showFragmentClass == null) {
            return
        }
        val transaction = fragmentManager.beginTransaction()
        val findFragment = fragmentManager.findFragmentByTag(showFragmentClass.javaClass.simpleName)
        if (findFragment == null) {
            transaction.add(view.id, showFragmentClass, showFragmentClass.javaClass.simpleName)
        }
        findFragment?.let {
            transaction.show(it)
        }
        hideFragmentClass?.let {
            val findHideFragment = fragmentManager.findFragmentByTag(it.javaClass.simpleName)
            findHideFragment?.let {
                transaction.hide(findHideFragment)
            }
        }
        transaction.commit()
    }

    @BindingAdapter(value = ["bankFormat"], requireAll = false)
    @JvmStatic
    fun bankFormat(textView: TextView, bankNumber: String?) {
        bankNumber?.let {
            try {
                textView.text = it.replace("(.{4})".toRegex(), "$1 ")
            } catch (e: java.lang.Exception) {
                textView.text = bankNumber
            }
        }
    }

    @BindingAdapter(value = ["isScrolled"], requireAll = false)
    @JvmStatic
    fun isScrolled(textView: TextView, isScrolled: Boolean) {
        if (isScrolled) {
            textView.movementMethod = ScrollingMovementMethod.getInstance()
        }
    }

    @BindingAdapter(value = ["invalidateText"], requireAll = false)
    @JvmStatic
    fun invalidateText(textView: TextView, textStr: String) {
        textView.setText(textStr, TextView.BufferType.EDITABLE)
    }


    @BindingAdapter(value = ["movementMethod"], requireAll = false)
    @JvmStatic
    fun movementMethod(view: TextView, number: Int) {
        view.movementMethod = LinkMovementMethod.getInstance()
    }


    @BindingAdapter(
        value = ["bgMaterialShapeDrawableTranslationZ", "bgMaterialShapeDrawableCornerSize", "bgMaterialShapeDrawableShadowColor", "bgMaterialShapeDrawableBgColor"],
        requireAll = false
    )
    @JvmStatic
    fun bgMaterialShapeDrawable(
        view: View,
        translationZ: Float,
        cornerSize: Float,
        @ColorInt shadowColor: Int,
        @ColorInt bgColor: Int,
    ) {
        val materialShapeDrawable = MaterialShapeDrawable.createWithElevationOverlay(view.context)
        if (shadowColor != 0) {
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    view.outlineSpotShadowColor = shadowColor
                }
            } catch (e: Error) {
                e.printStackTrace()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        if (bgColor != 0) {
            materialShapeDrawable.fillColor = ColorStateList.valueOf(bgColor)
        }
        materialShapeDrawable.translationZ = translationZ
        materialShapeDrawable.setCornerSize(cornerSize)
        ViewCompat.setTranslationZ(view, translationZ)
        ViewCompat.setBackground(view, materialShapeDrawable)
    }

    @BindingAdapter(value = ["convertVerifyCode"], requireAll = false)
    @JvmStatic
    fun convertVerifyCode(view: TextView, number: Int) {
        when (number) {
            -1 -> {
                view.setTextKeepState("发送验证码", TextView.BufferType.NORMAL)
            }
            0 -> {
                view.setTextKeepState("重新发送", TextView.BufferType.NORMAL)
            }
            else -> {
                view.setTextKeepState("${number}秒后重发", TextView.BufferType.NORMAL)
            }
        }
    }

    @BindingAdapter(value = ["layoutMarginStart"], requireAll = false)
    @JvmStatic
    fun layoutMarginStart(view: View, startMargin: Float) {
        val params = view.layoutParams
        if (params is ViewGroup.MarginLayoutParams) {
            params.marginStart = startMargin.toInt()
        }
    }

    @BindingAdapter(value = ["passListener"], requireAll = false)
    @JvmStatic
    fun listener(view: PasswordEditText, listener: PasswordEditText.PasswordFullListener) {
        view.mListener = listener
    }

    @BindingAdapter(value = ["requestFocused"], requireAll = false)
    @JvmStatic
    fun requestFocused(view: AppCompatEditText, request: Boolean) {
        if (request) {
//            view.requestFocus()  不要加这一行影响到了软键盘弹出
            view.postDelayed({
                KeyboardUtils.showSoftInput(view)
                view.setSelection(view.text?.length ?: 0)
            }, 300)
        } else {
            view.clearFocus()
            view.postDelayed({
                KeyboardUtils.hideSoftInput(view)
            }, 300)
        }
    }

    @BindingAdapter(
        value = ["clickSpanShowText", "clickSpanClickText", "clickSpanColor", "clickSpanListener"],
        requireAll = true
    )
    @JvmStatic
    fun clickSpan(
        textView: TextView,
        showText: CharSequence?,
        clickText: CharSequence?,
        @ColorInt clickColor: Int,
        listener: View.OnClickListener
    ) {
        val append = SpanUtils.with(textView)
        showText?.let {
            append.append(it)
        }
        if (clickText.isNullOrEmpty().not()) {
            clickText?.let {
                append
                    .append(it)
                    .setClickSpan(clickColor, false, listener)
            }
        }
        append.create()
    }

    @BindingAdapter(value = ["loadWebUrl"], requireAll = false)
    @JvmStatic
    fun loadWebUrl(web: WebView, url: String?) {
        url?.let {
            web.loadUrl(it)
        }
    }

    @BindingAdapter(value = ["layoutConstraintDimensionRatio"], requireAll = false)
    @JvmStatic
    fun layoutConstraintDimensionRatio(layout: View, ratio: String) {
        if (layout.layoutParams !is ConstraintLayout.LayoutParams) {
            return
        }
        val params = layout.layoutParams as ConstraintLayout.LayoutParams
        params.dimensionRatio = ratio
        layout.layoutParams = params
    }
}