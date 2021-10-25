package com.dyn.base.ui.addnumber

import android.app.Activity
import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.databinding.Observable
import androidx.fragment.app.Fragment
import com.dyn.base.BR
import com.dyn.base.R
import com.dyn.base.customview.BaseCustomModel
import com.dyn.base.customview.BaseCustomView
import com.dyn.base.databinding.LayoutAddNumberBinding
import com.dyn.base.utils.BaseActionConstant
import com.dyn.base.utils.BaseDialogUtils

class AddNumberView(context: Context, attrs: AttributeSet?,defStyleAttr:Int)  :BaseCustomView<LayoutAddNumberBinding,AddNumberModel>(context, attrs, defStyleAttr) {
    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs,0)
    private var mOnNumberChangeListener:IOnNumberChangeListener? = null
    fun setOnPropertyChangedCallback(listener: IOnNumberChangeListener){
       mOnNumberChangeListener = listener
    }
    init {
        setData(AddNumberModel())
        val a = context.obtainStyledAttributes(attrs,R.styleable.AddNumberView)
        val isEditAble = a.getBoolean(R.styleable.AddNumberView_isEditable,mViewModel.isEditEnable.get()!!)
        mViewModel.isEditEnable.set(isEditAble)
        mViewModel.count.addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback(){
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                mOnNumberChangeListener?.onChange()
            }
        })
    }
    override fun getViewLayoutId(): Int {
        return R.layout.layout_add_number
    }

    override fun getDataVariableId(): Int {
        return BR.vm
    }

    override fun getClickVariableId(): Int {
        return BR.action
    }

    override fun onAction(view: View, action: String, viewModel: BaseCustomModel) {
        when(action){
            BaseActionConstant.ACTION_ADD_NUMBER_SUB->{
                    if ((mViewModel.count.get()?:0) -1 >= mViewModel.minCount){
                        mViewModel.count.set((mViewModel.count.get()?:1) -1)
                    }
            }
            BaseActionConstant.ACTION_ADD_NUMBER_PLUS->{
                if (mViewModel.count.get() == mViewModel.maxCount.get()){
                    return
                }
                if ((mViewModel.count.get()?:0) +1 <= mViewModel.maxCount.get()!!){
                    mViewModel.count.set((mViewModel.count.get()?:1) +1)
                }
            }
            BaseActionConstant.ACTION_ADD_NUMBER_CONTENT->{
                val activity = when (context) {
                    is Activity -> {
                        context
                    }
                    is Fragment -> {
                        (context as Fragment).requireActivity()
                    }
                    else -> {
                        null
                    }
                }
                if (activity is Activity){
                    BaseDialogUtils.showNumberEditText(activity,mViewModel.count.get()?:1){
                        mViewModel.count.set(it)
                    }
                }
            }
        }

    }
}