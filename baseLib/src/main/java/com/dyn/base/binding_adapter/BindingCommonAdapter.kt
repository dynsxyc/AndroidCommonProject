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
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import android.graphics.drawable.ScaleDrawable
import android.graphics.drawable.ShapeDrawable
import android.os.Build
import android.text.Html
import android.text.InputFilter
import android.text.InputFilter.LengthFilter
import android.text.InputType
import android.text.method.HideReturnsTransformationMethod
import android.text.method.LinkMovementMethod
import android.text.method.PasswordTransformationMethod
import android.text.method.ScrollingMovementMethod
import android.text.util.Linkify
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import android.view.inputmethod.EditorInfo
import android.webkit.WebView
import android.widget.CompoundButton
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.annotation.IdRes
import androidx.appcompat.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.ViewCompat
import androidx.core.widget.ImageViewCompat
import androidx.core.widget.TextViewCompat
import androidx.databinding.BindingAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.blankj.utilcode.util.KeyboardUtils
import com.blankj.utilcode.util.SpanUtils
import com.bumptech.glide.request.RequestListener
import com.dyn.base.R
import com.dyn.base.common.findViewByParent
import com.dyn.base.customview.BaseCustomModel
import com.dyn.base.customview.BaseCustomView
import com.dyn.base.customview.ICustomViewActionListener
import com.dyn.base.ui.HIndicator
import com.dyn.base.ui.base.recycler.BasePager2Adapter
import com.dyn.base.ui.base.recycler.BaseRecyclerAdapter
import com.dyn.base.ui.magicindicator.IMagicItem
import com.dyn.base.ui.navigationbar.DefaultNavigationBar
import com.dyn.base.ui.weight.ClearEditText
import com.dyn.base.ui.weight.ExpandableTextView
import com.dyn.base.ui.weight.MessageCountTextView
import com.dyn.base.ui.weight.header.CommonHeaderModel
import com.dyn.base.ui.weight.header.CommonHeaderView
import com.dyn.base.ui.weight.passedit.PasswordEditText
import com.dyn.base.utils.ImageLoaderManager
import com.dyn.base.utils.LinkSpanUtils
import com.dyn.base.utils.OnOpenWebUrlListener
import com.dyn.base.utils.StarTransformationMethod
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.gyf.immersionbar.ImmersionBar
import com.jakewharton.rxbinding4.view.clicks
import com.jakewharton.rxbinding4.view.scrollChangeEvents
import com.jakewharton.rxbinding4.widget.editorActions
import java.util.concurrent.TimeUnit


/**
 */
object BindingCommonAdapter {
    @BindingAdapter(
        value = ["imageUrl", "placeHolder", "isGif", "imageRequestListener"],
        requireAll = false
    )
    @JvmStatic
    fun loadUrl(
        view: ImageView,
        url: Any?,
        placeHolder: Drawable?,
        isGif: Boolean? = false,
        requestListener: RequestListener<Drawable>? = null
    ) {
        url?.let {
            ImageLoaderManager.displayImage(view, it, isGif, placeHolder, requestListener)
        }
    }
    @BindingAdapter(
        value = ["imageNoneCacheUrl", "noneCachePlaceHolder"],
        requireAll = false
    )
    @JvmStatic
    fun loadNoneCacheUrl(
        view: ImageView,
        url: Any?,
        placeHolder: Drawable?
    ) {
        url?.let {
            ImageLoaderManager.displayImageNoCache(view, it, placeHolder)
        }
    }


    @BindingAdapter(value = ["imageBitmap"], requireAll = false)
    @JvmStatic
    fun imageBitmap(view: ImageView, bitmap: Bitmap?) {
        bitmap?.let {
            view.setImageBitmap(it)
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
        val animator = ObjectAnimator.ofFloat(view,"rotation",view.rotation,rotation)
        animator.duration = 300
        animator.interpolator = LinearInterpolator()
        animator.start()
    }

    @BindingAdapter(value = ["rotationAnimation"], requireAll = false)
    @JvmStatic
    fun rotationAnimation(view: View, rotation: Float) {
        if (view.rotation != rotation) {
            val animator = ObjectAnimator.ofFloat(view, "rotation", view.rotation, rotation)
            animator.duration = 300
            animator.interpolator = LinearInterpolator()
            animator.start()
        }
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

    @BindingAdapter(value = ["textHtmlContent"], requireAll = false)
    @JvmStatic
    fun setTextHtmlContent(view: TextView, content: String?) {
        content?.let {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                view.text = Html.fromHtml(it, Html.FROM_HTML_MODE_COMPACT)
            } else {
                view.text = Html.fromHtml(it)

            }
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
        imageSrc(imageView, imageRes)
    }

    @BindingAdapter(value = ["changedDrawableStart", "normalDrawableStart"], requireAll = true)
    @JvmStatic
    fun changedButton(view: TextView, changedDrawableStart: Int, normalDrawableStart: Int) {
        view.setOnClickListener {
            val tag = it.tag.toString().toInt()
            if (tag == 0) {
                view.tag = 1
                view.setCompoundDrawablesRelativeWithIntrinsicBounds(changedDrawableStart, 0, 0, 0)
            } else {
                view.tag = 0
                view.setCompoundDrawablesRelativeWithIntrinsicBounds(normalDrawableStart, 0, 0, 0)
            }
        }
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

    @BindingAdapter(
        value = ["onClickWithDebouncingListener", "onClickWithDebouncingById"],
        requireAll = true
    )
    @JvmStatic
    fun onClickWithDebouncingById(view: View, clickListener: View.OnClickListener?, id: Int) {
        val v = view.findViewByParent<View>(id)
        view.clicks().throttleFirst(800, TimeUnit.MILLISECONDS).subscribe {
            try {
                clickListener?.onClick(v)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    interface IActionSearchListener {
        fun onSearch(str: String)
    }

    interface IActionListener<T> {
        fun onSearch(data: T)
    }

    @BindingAdapter(value = ["editorSearchAction"], requireAll = false)
    @JvmStatic
    fun onTextEditorActions(editText: AppCompatEditText, searchAction: IActionSearchListener?) {
        editText.editorActions {
            when (it) {
                EditorInfo.IME_ACTION_SEARCH -> {
                    if (editText.text.isNullOrEmpty().not()) {
                        searchAction?.onSearch(editText.text.toString())
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

    @BindingAdapter(value = ["editorSendActionClick"], requireAll = false)
    @JvmStatic
    fun editorSendActionClick(editText: AppCompatEditText, clickListener: View.OnClickListener) {
        editText.editorActions {
            when (it) {
                EditorInfo.IME_ACTION_SEND -> {
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

    @BindingAdapter(
        value = [
            "tabLayoutBindViewPager2Id",
            "tabLayoutBindViewPager2Adapter",
            "tabLayoutBindData",
            "tabLayoutBindAutoRefresh",
            "tabLayoutBindSmoothScroll",
        ],
        requireAll = false
    )
    @JvmStatic
    fun initTabLayout(
        view: TabLayout,
        viewPagerId: Int,
        pagerAdapter: RecyclerView.Adapter<*>?,
        list: MutableList<out IMagicItem>,
        autoRefresh: Boolean? = true,
        smoothScroll: Boolean? = false,
    ) {
        val viewPager = view.findViewByParent<ViewPager2>(viewPagerId)
        view.isLongClickable = false
        viewPager?.let { pager ->
            pagerAdapter?.let { adapter ->
                if (pager.adapter != adapter) {
                    pager.adapter = adapter
                }

                pager.adapter?.let { adapter ->
                    if (adapter is BasePager2Adapter<*>) {
                        list?.let {
                            adapter.setList(it as MutableList<Nothing>)
                        }
                    }
                    if (adapter is BaseRecyclerAdapter<*, *>) {
                        list?.let {
                            adapter.submitList(it as MutableList<Nothing>)
                        }
                    }
                }

                val tag = view.getTag(R.id.tabLayoutBindViewPager2Id)
                if (tag == null) {
                    val mediator = TabLayoutMediator(
                        view, pager, autoRefresh ?: true, smoothScroll ?: false
                    ) { _, _ -> }
                    mediator.attach()
                    view.setTag(R.id.tabLayoutBindViewPager2Id, mediator)
                }

                if (view.tabCount == list.size) {
                    for (i in 0..view.tabCount) {
                        val tab = view.getTabAt(i)
                        list.getOrNull(i)?.getShowTabIconRes()?.let {
                            if (it > 0) {
                                try {
                                    view.getTabAt(i)?.setIcon(it)
                                } catch (e: Exception) {

                                }
                            }
                        }
                        tab?.let {
                            it.text = list.getOrNull(i)?.getShowTabName()
                            it.view.isLongClickable =false
                            if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.O){
                                it.view.tooltipText = null
                            }
                        }
                    }
                }
            }
        }
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
    fun drawableTint(textView: TextView, @ColorInt color: Int?) {
        color?.let {
            if (it == -2) {// white 是-1 透明 是0
                TextViewCompat.setCompoundDrawableTintList(textView, null)
                return
            }
            TextViewCompat.setCompoundDrawableTintList(textView, ColorStateList.valueOf(it))
        }
    }

    @BindingAdapter(value = ["backgroundTint"], requireAll = false)
    @JvmStatic
    fun backgroundTint(textView: View, @ColorInt color: Int?) {
        color?.let {
            textView.backgroundTintList =
                androidx.databinding.adapters.Converters.convertColorToColorStateList(it)
        }
    }

    @BindingAdapter(value = ["drawableTint"], requireAll = false)
    @JvmStatic
    fun drawableTint(textView: ImageView, @ColorInt color: Int?) {
        color?.let {
            if (it == -2) {// white 是-1 透明 是0
                ImageViewCompat.setImageTintList(textView, null)
                return
            }
            ImageViewCompat.setImageTintList(textView, ColorStateList.valueOf(it))
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

    @BindingAdapter(value = ["textIsBold"], requireAll = false)
    @JvmStatic
    fun textIsBold(view: TextView, isMedium: Boolean) {
        try {
            view.typeface = if (isMedium) {
                Typeface.DEFAULT_BOLD
            } else {
                Typeface.DEFAULT
            }
        } catch (e: Exception) {

        }
    }

    @BindingAdapter(value = ["checkListener"], requireAll = false)
    @JvmStatic
    fun checkListener(view: CompoundButton, listener: CompoundButton.OnCheckedChangeListener?) {
        try {
            listener?.let {
                view.setOnCheckedChangeListener(it)
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

    @BindingAdapter(value = ["customViewBindAction", "customViewBindData"], requireAll = false)
    @JvmStatic
    fun customBindAction(
        view: BaseCustomView<*, out BaseCustomModel>,
        listener: ICustomViewActionListener?,
        customViewModel: BaseCustomModel?
    ) {
        view.mActionListener = listener
        customViewModel?.let {
            view.setNothingData(it)
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
    fun createAddView(viewGroup: ViewGroup, addView: ICreateSubView?, vm: ViewModel?) {
        addView?.createView(viewGroup, vm)?.let {
            viewGroup.removeAllViews()
            viewGroup.addView(it)
        }
    }

    interface ICreateSubView {
        fun createView(parent: ViewGroup, vm: ViewModel?): View?
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

    @BindingAdapter(
        value = ["drawablePadding"],
        requireAll = false
    )
    @JvmStatic
    fun textDrawablePadding(
        view: TextView,
        drawablePadding: Int? = 0,
    ) {
        try {
            view.compoundDrawablePadding = drawablePadding ?: 0
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
        try {
            transaction.commit()
        } catch (e: Exception) {
            e.printStackTrace()
        }
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

    @BindingAdapter(
        value = ["layoutMarginStart", "layoutMarginTop", "layoutMarginEnd", "layoutMarginBottom"],
        requireAll = false
    )
    @JvmStatic
    fun layoutMarginStart(
        view: View,
        startMargin: Float,
        topMargin: Float,
        endMargin: Float,
        bottomMargin: Float
    ) {
        val params = view.layoutParams
        if (params is ViewGroup.MarginLayoutParams) {
            params.marginStart = startMargin.toInt()
            params.topMargin = topMargin.toInt()
            params.marginEnd = endMargin.toInt()
            params.bottomMargin = bottomMargin.toInt()
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
    fun layoutConstraintDimensionRatio(layout: View, ratio: String?) {
        if (layout.layoutParams !is ConstraintLayout.LayoutParams) {
            return
        }
        val params = layout.layoutParams as ConstraintLayout.LayoutParams
        params.dimensionRatio = ratio
        layout.layoutParams = params
    }


    @BindingAdapter(value = ["setViewHeight"], requireAll = false)
    @JvmStatic
    fun setViewHeight(view: View, height: Int) {
        val params = view.layoutParams
        params?.let {
            it.height = height
            view.layoutParams = it
        }
    }

    @BindingAdapter(value = ["setViewWidth"], requireAll = false)
    @JvmStatic
    fun setViewSize(view: View, width: Int) {
        val params = view.layoutParams
        params?.let {
            it.width = width
            view.layoutParams = it
        }
    }

    @BindingAdapter(value = ["setViewHeight"], requireAll = false)
    @JvmStatic
    fun setViewHeight(view: View, height: Float) {
        val params = view.layoutParams
        params?.let {
            it.height = height.toInt()
            view.layoutParams = it
        }
    }

    @BindingAdapter(value = ["setViewWidth"], requireAll = false)
    @JvmStatic
    fun setViewSize(view: View, width: Float) {
        val params = view.layoutParams
        params?.let {
            it.width = width.toInt()
            view.layoutParams = it
        }
    }

    @BindingAdapter(value = ["expandableTextContent"], requireAll = false)
    @JvmStatic
    fun setExpandableTextContent(view: ExpandableTextView, content: String?) {
        content?.let {
            view.text = content
        }
    }

    @BindingAdapter(value = ["layoutConstraintHeightMax"], requireAll = false)
    @JvmStatic
    fun layoutConstraintHeightMax(view: View, height: Int) {
        val params = view.layoutParams
        if (params is ConstraintLayout.LayoutParams) {
            params.matchConstraintMaxHeight = height
            view.layoutParams = params
        }
    }

    @BindingAdapter(value = ["layoutConstraintWidthPercent"], requireAll = false)
    @JvmStatic
    fun layoutConstraintWidthPercent(view: View, percent: Float) {
        val params = view.layoutParams
        if (params is ConstraintLayout.LayoutParams) {
            params.matchConstraintPercentWidth = percent
            view.layoutParams = params
        }
    }

    @BindingAdapter(value = ["layoutConstraintHorizontalWeight"], requireAll = false)
    @JvmStatic
    fun layoutConstraintHorizontalWeight(view: View, height: Float) {
        val params = view.layoutParams
        if (params is ConstraintLayout.LayoutParams) {
            params.horizontalWeight = height
            view.layoutParams = params
        }
    }

    @BindingAdapter(value = ["viewTranslationY"], requireAll = false)
    @JvmStatic
    fun setViewTranslationY(view: View, translationY: Float) {
        view.translationY = translationY
    }

    @BindingAdapter(value = ["viewTag"], requireAll = false)
    @JvmStatic
    fun setViewTag(view: View, tag: Any?) {
        view.tag = tag
    }


    @BindingAdapter(value = ["indicatorBindRecyclerView"], requireAll = false)
    @JvmStatic
    fun indicatorBindRecyclerView(view: HIndicator, @IdRes viewId: Int) {
        val baseIndicator = view.findViewByParent<RecyclerView>(viewId)
        baseIndicator?.let {
            view.bindRecyclerView(it)
        }
    }

    @BindingAdapter(
        value = ["progressBgColor", "progressStartColor", "progressEndColor"],
        requireAll = false
    )
    @JvmStatic
    fun progressColor(view: ProgressBar, bgColor: Int?, startColor: Int?, endColor: Int?) {
        val bgDrawable = view.progressDrawable
        if (bgDrawable is LayerDrawable) {
            val bg = bgDrawable.findDrawableByLayerId(android.R.id.background)
            if (bg is GradientDrawable && bgColor != null) {
                bg.setColor(bgColor)
            }
            val progressDrawable = bgDrawable.findDrawableByLayerId(android.R.id.progress)
            if (progressDrawable is ScaleDrawable) {
                val pd = progressDrawable.drawable
                if (pd is GradientDrawable) {
                    if (startColor != null && endColor != null) {
                        pd.colors = intArrayOf(startColor, endColor)
                    }
                }
            }
            view.background = bgDrawable
        }
    }

    @BindingAdapter(
        value = ["textContentIsHtml", "textContentHtmlStr", "textOpenWebListener"],
        requireAll = false
    )
    @JvmStatic
    fun textContentHtml(
        textView: TextView,
        isHtml: Boolean? = false,
        contentStr: String? = null,
        openWebListener: OnOpenWebUrlListener? = null
    ) {
        if (isHtml == true) {
            if (openWebListener != null) {
                textView.autoLinkMask = Linkify.WEB_URLS
                textView.movementMethod = LinkMovementMethod.getInstance()
                textView.text = LinkSpanUtils.getClickableHtml(
                    Html.fromHtml(
                        contentStr,
                        Html.FROM_HTML_MODE_LEGACY
                    ), openWebListener
                )
            } else {
                textView.text = Html.fromHtml(contentStr, Html.FROM_HTML_MODE_LEGACY)
                textView.movementMethod = LinkMovementMethod.getInstance()
            }
        } else {
            textView.text = contentStr
        }
    }
}