package com.dyn.base.ui.databinding

import android.util.SparseArray
import androidx.annotation.LayoutRes
import androidx.lifecycle.ViewModel

data class DataBindingConfig(val vmVariableId: Int, val stateViewModel: ViewModel) {
    val bindingParams  by lazy {
        SparseArray<Any?>()
    }
    fun addBindingParam(variableId: Int, data: Any?): DataBindingConfig {
        if (bindingParams.get(variableId) == null) {
            bindingParams.put(variableId, data)
        }
        return this
    }


}
