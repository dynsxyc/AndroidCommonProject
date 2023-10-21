package com.dyn.base.utils.switchover

import android.app.Dialog
import android.content.Context
import android.text.TextUtils
import android.view.View
import android.view.ViewGroup
import androidx.databinding.Observable
import com.blankj.utilcode.util.ScreenUtils
import com.dyn.base.customview.BaseCustomModel
import com.dyn.base.customview.ICustomViewActionListener
import com.dyn.base.dialog.AlertDialogManager

object SwitchoverUtils {
    private var currentContext: Context? = null
    private var dialog: Dialog? = null
    fun showSwitchoverDialog(
        context: Context,
        currentHost: String? = null,
        btStr:String? = null,
        hosts: MutableList<HostBean>,
        onSubmit: (String?) -> Unit,
        onBtClick: () -> Unit ={},
        isSandBox:Boolean = false,
        onSandBoxPay:(Boolean)->Unit ={},
    ) {
        if (dialog != null && currentContext == context) {
            dialog?.show()
            return
        }
        val view = SwitchoverHostView(context)
        val viewModel = SwitchoverHostModel().also { mod ->
            mod.currentHost.set(currentHost)
            mod.editHost.set(currentHost)
            hosts.forEach {
                it.isChecked.set(it.hostUrl == currentHost)
            }
            mod.hosts.set(hosts)
        }
        viewModel.btStr.set(btStr)
        viewModel.isSandBoxPay.set(isSandBox)
        viewModel.isSandBoxPay.addOnPropertyChangedCallback(object :Observable.OnPropertyChangedCallback(){
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                    onSandBoxPay(viewModel.isSandBoxPay.get()?:false)
            }
        })
        view.setData(viewModel)
        view.mActionListener = object : ICustomViewActionListener {
            override fun onAction(view: View, action: String, model: BaseCustomModel) {
                when (action) {
                    ChoiceAction.ACTION_BT->{
                        onBtClick()
                    }
                    ChoiceAction.ACTION_CANCEL -> {
                        dialog?.dismiss()
                    }
                    ChoiceAction.ACTION_ITEM_CONTENT -> {
                        viewModel.hosts.get()?.let { list ->
                            list.forEach {
                                it.isChecked.set(it == model)
                            }
                        }
                        if (model is HostBean) {
                            viewModel.editHost.set(model.hostUrl)
                        }
                    }
                    ChoiceAction.ACTION_SUBMIT -> {
                        val currentHost = viewModel.currentHost.get()
                        val editHost = viewModel.editHost.get()
                        val result : String? = if (TextUtils.equals(currentHost, editHost)) {
                            null
                        } else if (editHost.isNullOrBlank()) {
                            val filterHost = viewModel.hosts.get()?.filter {
                                it.isChecked.get() == true
                            }?.first()
                            if (filterHost != null && TextUtils.equals(
                                    currentHost,
                                    filterHost.hostUrl
                                ).not()
                            ) {
                                filterHost.hostUrl
                            } else {
                                null
                            }
                        } else {
                            editHost.toString()
                        }
                        onSubmit(result)
                        dialog?.dismiss()

                    }
                }
            }
        }
        dialog = AlertDialogManager.Builder(context).setView(view)
            .setWidthAndHeight((ScreenUtils.getScreenWidth() * 0.81f).toInt(), ViewGroup.LayoutParams.WRAP_CONTENT)
            .show().dialog
    }
}