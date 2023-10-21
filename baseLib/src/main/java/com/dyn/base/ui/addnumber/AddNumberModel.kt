package com.dyn.base.ui.addnumber

import androidx.databinding.ObservableField
import com.dyn.base.customview.BaseCustomModel

data class AddNumberModel(val count:ObservableField<Int> = ObservableField(1),var minCount:ObservableField<Int> = ObservableField(1),var maxCount:ObservableField<Int> = ObservableField(Int.MAX_VALUE),val isEditEnable:ObservableField<Boolean> = ObservableField(false)):BaseCustomModel
