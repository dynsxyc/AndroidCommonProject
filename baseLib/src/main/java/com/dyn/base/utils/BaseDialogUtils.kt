package com.dyn.base.utils

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.hardware.display.DisplayManager
import android.text.Html
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.blankj.utilcode.util.KeyboardUtils
import com.blankj.utilcode.util.ScreenUtils
import com.dyn.base.BR
import com.dyn.base.R
import com.dyn.base.customview.BaseCustomModel
import com.dyn.base.customview.ICustomViewActionListener
import com.dyn.base.databinding.DialogAgreementWebBinding
import com.dyn.base.dialog.AlertDialogManager
import com.jakewharton.rxbinding4.widget.afterTextChangeEvents
import io.reactivex.rxjava3.functions.Consumer

object BaseDialogUtils {
    private var mEditNumberDialog: Dialog? = null
    private var mTipsDialog: Dialog? = null

    fun  showNumberEditText(mActivity: Activity, initNumber: Int, onComplete: (Int) -> Unit) {
        val contentView = LayoutInflater.from(mActivity).inflate(R.layout.dialog_model, null)
        val editText = contentView.findViewById<EditText>(R.id.mDialogEdit)
        editText.inputType = EditorInfo.TYPE_CLASS_NUMBER
        editText.setText(initNumber.toString())
        var mNumber = initNumber
        val mMinNumber = 1
        val mMaxNumber = Int.MAX_VALUE-1
        val disposable = editText.afterTextChangeEvents().subscribe(Consumer {
            mNumber = try {
                val edit = it.editable.toString()
                if (edit.isBlank()){
                    mMinNumber
                }else{
                    edit.toInt()
                }
            } catch (e: Exception) {
                Int.MAX_VALUE
            }
            if (mNumber < mMinNumber) {
                mNumber = mMinNumber
            }
            if (mNumber > mMaxNumber) {
                mNumber = Int.MAX_VALUE
            }
        })
        mEditNumberDialog = AlertDialogManager.Builder(mActivity).setView(contentView).setVisibilityView(R.id.mDialogEdit).setGoneView(R.id.mDialogDesTv)
            .setOnclickListener(R.id.mDialogLeftTv, View.OnClickListener {
                disposable.dispose()
                if (KeyboardUtils.isSoftInputVisible(mActivity)) {
                    KeyboardUtils.toggleSoftInput()
                }
                mEditNumberDialog?.dismiss()
            }).setOnclickListener(R.id.mDialogRightTv, View.OnClickListener {
                onComplete(mNumber)
                disposable.dispose()
                if (KeyboardUtils.isSoftInputVisible(mActivity)) {
                    KeyboardUtils.toggleSoftInput()
                }
                mEditNumberDialog?.dismiss()
            }).setOnDismissListener(DialogInterface.OnDismissListener {
                mEditNumberDialog = null
            }).setText(R.id.mDialogTitleTipsTv, "????????????").setWidthAndHeight(ScreenUtils.getScreenWidth() * 3 / 4, ViewGroup.LayoutParams.WRAP_CONTENT).show().dialog
        KeyboardUtils.showSoftInput(editText)
    }

    fun showTipsDialog(mActivity: Context, showContent: String, titleTips: String) {
        showTipsDialog(mActivity = mActivity,contentIsHtml = true,cancelable = true,titleStrip = titleTips,showContent=showContent,leftStr = "",rightStr = "??????",onComplete = {
            it?.dismiss()
        })
    }
    fun showTipsDialog(mActivity: Context, showContent: String, titleTips: String,rightStr: String,onDismiss: () -> Unit) {
        showTipsDialog(mActivity = mActivity,contentIsHtml = true,cancelable = true,titleStrip = titleTips,showContent=showContent,leftStr = "",rightStr = rightStr,onComplete = {
            it?.dismiss()
        },onDismiss = onDismiss)
    }
    fun showTipsDialog(mActivity: Context, showContent: String = "",titleTips:String= "??????", leftStr: String = "??????", rightStr: String = "??????", onCancel: () -> Unit = {}, onComplete: (Dialog?) -> Unit, onDismiss:()->Unit = {},cancelable: Boolean = true) {
        showTipsDialog(mActivity = mActivity,
            contentIsHtml = false,
            cancelable = cancelable,
            showContent = showContent,
            titleStrip = titleTips,
            leftStr = leftStr,
            rightStr = rightStr,
            onCancel = onCancel,
            onComplete = onComplete,onDismiss = onDismiss)
    }
    private fun showTipsDialog(mActivity: Context,
                               contentIsHtml: Boolean = false,
                               cancelable : Boolean = true,
                               titleStrip: String = "??????",
                               showContent: String,
                               leftStr: String = "??????", rightStr: String = "??????", onCancel: () -> Unit = {}, onComplete: (Dialog?) -> Unit, onDismiss:()->Unit = {}) {
        if (mTipsDialog == null) {
            val view = LayoutInflater.from(mActivity).inflate(R.layout.dialog_model,null)
            if (contentIsHtml){
                val tv = view.findViewById<androidx.appcompat.widget.AppCompatTextView>(R.id.mDialogDesTv)
                tv.movementMethod = LinkMovementMethod.getInstance()
            }
            val builder = AlertDialogManager.Builder(mActivity).setView(view)
                .setGoneView(R.id.mDialogEdit)
                .setOnclickListener(R.id.mDialogLeftTv, View.OnClickListener {
                    onCancel()
                    mTipsDialog?.dismiss()
                }).setOnclickListener(R.id.mDialogRightTv, View.OnClickListener {
                    onComplete(mTipsDialog)
                }).setOnDismissListener(DialogInterface.OnDismissListener {
                    mTipsDialog = null
                    onDismiss()
                }).setText(R.id.mDialogDesTv, if (contentIsHtml) Html.fromHtml(showContent) else showContent)
                .setText(R.id.mDialogRightTv, rightStr)
                .setText(R.id.mDialogLeftTv, leftStr)
                .setText(R.id.mDialogTitleTipsTv, titleStrip)
                .setWidthAndHeight((ScreenUtils.getScreenWidth() * 0.81f).toInt(), ViewGroup.LayoutParams.WRAP_CONTENT)
            if (leftStr.isNullOrEmpty()){
                builder.setGoneView(R.id.mDialogLeftTv);
            }
            if (titleStrip.isNullOrEmpty()){
                builder.setGoneView(R.id.mDialogTitleTipsTv);
            }
            mTipsDialog = builder.show().dialog
            (mTipsDialog as AlertDialog).setCancelable(cancelable)

        }else if (mTipsDialog!!.isShowing.not()){
            mTipsDialog!!.show()
        }
    }
    private var mAgreementDialog:Dialog? = null
    data class AgreementDialogModel(val webUrl:String):BaseCustomModel
    /**
     * ??????  ??????????????????
     * */
    fun showAgreementDialog(context: Context, webUrl:String, onCancel: () -> Unit, onComplete: (Dialog?) -> Unit){
       if (mAgreementDialog == null) {
           val webBinding = DataBindingUtil.inflate<DialogAgreementWebBinding>(LayoutInflater.from(context),R.layout.dialog_agreement_web,null,false)
           webBinding.setVariable(BR.vm,AgreementDialogModel(webUrl))
           webBinding.setVariable(BR.action,object :ICustomViewActionListener{
               override fun onAction(view: View, action: String, viewModel: BaseCustomModel) {
                   when(action){
                       BaseActionConstant.ACTION_CANCEL->{
                           onCancel()
                       }
                       BaseActionConstant.ACTION_CONFIRM->{
                           onComplete(mAgreementDialog)
                       }
                   }
               }
           })
           webBinding.executePendingBindings()
           mAgreementDialog = AlertDialogManager.Builder(context).setViewBinding(webBinding)
               .setWidthAndHeight(
                   (ScreenUtils.getScreenWidth() * 0.81f).toInt(),
                   (ScreenUtils.getScreenHeight() * 0.7f).toInt()
               )
               .show().dialog
           if (mAgreementDialog is AlertDialog){
               (mAgreementDialog as AlertDialog).setCancelable(false)
           }
       }else if (mAgreementDialog!!.isShowing.not()){
           mAgreementDialog!!.show()
       }
    }

}