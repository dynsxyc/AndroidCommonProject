package com.dyn.base.dialog

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.util.SparseArray
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.annotation.NonNull
import androidx.annotation.StyleRes
import androidx.appcompat.app.AlertDialog
import androidx.databinding.ViewDataBinding
import com.dyn.base.R

class AlertDialogManager(val dialog: Dialog,val viewDataBinding: ViewDataBinding? = null) {
    class Builder constructor(
        @NonNull private val context: Context,
        @StyleRes private val themeResId: Int
    ) {
        private var mView: View? = null
        private var viewDataBinding: ViewDataBinding? = null
        private var mLayoutResId: Int = 0

        // 存放字体的修改
        private var mTextArray = SparseArray<CharSequence>()

        // 存放图片的修改
        private var mImageArray = SparseArray<Int>()

        // 存放显示的修改
        private var mViewVisibilityArray = mutableListOf<Int>()
        // 存放inVisibility显示的修改
        private var mViewInVisibilityArray = mutableListOf<Int>()

        // 存放隐藏的修改
        private var mViewGoneArray = mutableListOf<Int>()

        // 存放点击事件
        private var mClickArray = SparseArray<View.OnClickListener>()
        private var mAnimations: Int = 0
        private var mGravity: Int = Gravity.CENTER
        private var mWidth: Int = ViewGroup.LayoutParams.WRAP_CONTENT
        private var mHeight: Int = ViewGroup.LayoutParams.WRAP_CONTENT
        private var mOnDismissListener: DialogInterface.OnDismissListener? = null
        private var mOnCancelListener: DialogInterface.OnCancelListener? = null
        private var mOnKeyListener: DialogInterface.OnKeyListener? = null

        @ColorInt
        private var mWindowBackgroundColor: Int = Color.parseColor("#A6000000")

        constructor(context: Context) : this(context, R.style.dialog)

        fun setView(view: View): Builder {
            mView = view
            return this
        }

        fun setViewBinding(binding: ViewDataBinding):Builder{
            this.viewDataBinding = binding
            mView = binding.root
            return this
        }

        fun setView(layoutResId: Int): Builder {
            mLayoutResId = layoutResId
            return this
        }

        fun setOnCancelListener(listener: DialogInterface.OnCancelListener): Builder {
            mOnCancelListener = listener
            return this
        }

        fun setOnDismissListener(listener: DialogInterface.OnDismissListener): Builder {
            mOnDismissListener = listener
            return this
        }

        fun setOnKeyListener(listener: DialogInterface.OnKeyListener): Builder {
            mOnKeyListener = listener
            return this
        }

        fun setOnclickListener(viewId: Int, listener: View.OnClickListener): Builder {
            mClickArray.put(viewId, listener)
            return this
        }

        fun setVisibilityView(viewId: Int): Builder {
            mViewVisibilityArray.add(viewId)
            return this
        }

        fun setInVisibilityView(viewId: Int): Builder {
            mViewInVisibilityArray.add(viewId)
            return this
        }

        fun setGoneView(viewId: Int): Builder {
            mViewGoneArray.add(viewId)
            return this
        }

        /**
         *
         * 给对应viewId 的textView 设置文字内容
         * */
        fun setText(viewId: Int, text: CharSequence): Builder {
            mTextArray.put(viewId, text)
            return this
        }

        /**
         *
         * 给对应viewId 的textView 设置文字内容
         * */
        fun setImageResource(viewId: Int, @DrawableRes resourceId: Int): Builder {
            mImageArray.put(viewId, resourceId)
            return this
        }

        /**
         * 添加默认动画
         * @return
         */
        fun addDefaultAnimation(): Builder {
            mAnimations = R.style.dialog_scale_anim
            return this
        }

        /**
         * 设置动画
         * @param styleAnimation
         * @return
         */
        fun setAnimations(styleAnimation: Int): Builder {
            mAnimations = styleAnimation
            return this
        }

        /**
         * 从底部弹出
         * 经测试这个formBottom 动画有问题 在华为手机上 是从上往下掉下来的效果
         * @param isAnimation 是否有动画
         * @return
         */
        fun formBottom(isAnimation: Boolean): Builder {
            if (isAnimation) {
                mAnimations = R.style.dialog_from_bottom_anim
            }
            mGravity = Gravity.BOTTOM
            return this
        }

        fun fullWidth(): Builder {
            mWidth = ViewGroup.LayoutParams.MATCH_PARENT
            return this
        }

        /**
         * 设置Dialog的宽高
         * @param width
         * @param height
         * @return
         */
        fun setWidthAndHeight(width: Int, height: Int): Builder {
            mWidth = width
            mHeight = height
            return this
        }
        /**
         * 设置Dialog的宽高
         * @param width
         * @param height
         * @return
         */
        fun setWidth(width: Int,): Builder {
            mWidth = width
            return this
        }

        fun setGravity(gravity: Int): Builder {
            mGravity = gravity
            return this
        }

        fun setBackGroundColor(@ColorInt color: Int): Builder {
            mWindowBackgroundColor = color
            return this
        }


        private fun create(): AlertDialogManager {
            var builder = AlertDialog.Builder(context, themeResId)
            var mDialogViewHelper: DialogViewHelper? = null
            if (mLayoutResId != 0) {
                mDialogViewHelper = DialogViewHelper(context, mLayoutResId)
            }
            mView?.let {
                mDialogViewHelper = DialogViewHelper()
                mDialogViewHelper?.contentView = mView
            }
            mDialogViewHelper?.let { helper ->
                mTextArray?.let {
                    for (i in 0 until it.size()) {
                        try {
                            helper.setText(it.keyAt(i), it.valueAt(i))
                        } catch (e: IllegalStateException) {
                            e.printStackTrace()
                        }
                    }
                }
                mImageArray?.let {
                    for (i in 0 until it.size()) {
                        try {
                            helper.setImageResource(it.keyAt(i), it.valueAt(i))
                        } catch (e: IllegalStateException) {
                            e.printStackTrace()
                        }
                    }
                }
                mViewVisibilityArray?.let {
                    it.forEach {
                        helper.setVisibilityView(it)
                    }
                }
                mViewInVisibilityArray?.let {
                    it.forEach {
                        helper.setInVisibilityView(it)
                    }
                }
                mViewGoneArray?.apply {
                    forEach {
                        helper.setGoneView(it)
                    }
                }
                mClickArray?.let {
                    for (i in 0 until it.size()) {
                        try {
                            helper.setOnclickListener(it.keyAt(i), it.valueAt(i))
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                }
                builder.setView(helper.contentView)
            }
            builder.setOnDismissListener(mOnDismissListener)
            builder.setOnCancelListener(mOnCancelListener)
            builder.setOnKeyListener(mOnKeyListener)
           val  dialog = builder.create()
            return AlertDialogManager(dialog,viewDataBinding)
        }

        fun show(): AlertDialogManager {
            val dialogManager = create()
            dialogManager.dialog.show()
            val window = dialogManager.dialog.window
            //设置位置
            window?.setGravity(mGravity)
            // 设置动画
            if (mAnimations != 0) {
                window?.setWindowAnimations(mAnimations)
            }
            // 设置宽高
            val params = window?.attributes
            params?.width = mWidth
            params?.height = mHeight
            window?.attributes = params
            window?.clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM)
//             window.setBackgroundDrawable(ColorDrawable(mWindowBackgroundColor))
            return dialogManager
        }
    }
}