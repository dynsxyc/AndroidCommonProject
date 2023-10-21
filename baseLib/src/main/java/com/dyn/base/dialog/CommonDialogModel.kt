package com.dyn.base.dialog

import android.view.Gravity
import android.view.inputmethod.EditorInfo
import androidx.databinding.ObservableField
import com.dyn.base.customview.BaseCustomModel

data class CommonDialogModel(
    val tipsStr: String? = null,
    val title: String? = "温馨提示",
    val hint: String? = null,
    val etGravity: Int = Gravity.START,
    val desGravity: Int = Gravity.CENTER,
    val etContent: ObservableField<String> = ObservableField(),
    val subEtContent: ObservableField<String> = ObservableField(),
    val desTitle: String? = null,
    var completeStr: String? = null,
    val cancelStr: String? = null,
    val desHint: String? = null,
    val cancelable: Boolean = true,
    val desIsHtml: Boolean = false,
    val hasCompleteBt: Boolean = true,
    val hasCancelBt: Boolean = true,
    val desContentStr: ObservableField<String> = ObservableField(),
    val requestFocused: ObservableField<Boolean> = ObservableField(true),
    val desInputType: Int = EditorInfo.TYPE_CLASS_TEXT,
    val etInputType: Int = EditorInfo.TYPE_CLASS_TEXT
) : BaseCustomModel