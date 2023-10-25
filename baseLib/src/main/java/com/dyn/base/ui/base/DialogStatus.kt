package com.dyn.base.ui.base

import android.widget.Toast
import androidx.annotation.IntDef
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy

class DialogStatus {

    companion object{
        const val LOADING_DIALOG_STATUS_SHOW_CANCEL = 0
        const val LOADING_DIALOG_STATUS_SHOW_UNCANCEL = 1
        const val LOADING_DIALOG_STATUS_SHOW_UNCANCEL_STR = 3
        const val LOADING_DIALOG_STATUS_HIDE = 2
    }

    @Target(AnnotationTarget.TYPE)
    @IntDef(value = [LOADING_DIALOG_STATUS_SHOW_CANCEL, LOADING_DIALOG_STATUS_SHOW_UNCANCEL,LOADING_DIALOG_STATUS_SHOW_UNCANCEL_STR, LOADING_DIALOG_STATUS_HIDE])
    @Retention(RetentionPolicy.SOURCE)
    annotation class EnumDialogStatus
}