package com.dyn.base.customview

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.dyn.base.common.throttleFirstClick
import java.lang.ClassCastException

abstract class BaseCustomView<DB : ViewDataBinding, DATA : BaseCustomModel>(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : FrameLayout(context, attrs, defStyleAttr), ICustomView<DATA>,ICustomViewActionListener {

    private var mVariableData = lazy { mutableMapOf<Int,Any>()}.value

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    var mActionListener:ICustomViewActionListener? = null
    protected open lateinit var mDataBinding: DB
    lateinit var mViewModel: DATA

    init {
        if (getViewLayoutId() != 0) {
            val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            mDataBinding = DataBindingUtil.inflate(inflater,getViewLayoutId(),this,false)
            if (rootClickable()) {
                mDataBinding.root.throttleFirstClick {
                    onAction(this, ICustomViewActionListener.ACTION_ROOT_VIEW_CLICKED, mViewModel)
                }
            }
            addView(mDataBinding.root)
        }
    }

    fun setNothingData(nothing: Any){
        try {
            setData(nothing as DATA)
        }catch (e:ClassCastException){
            e.printStackTrace()
        }
    }

    override fun setData(data: DATA) {
        Log.i("dyn","setData-------------$this")
        mViewModel = data
        mDataBinding.setVariable(getDataVariableId(),data)
        mDataBinding.setVariable(getClickVariableId(),this)
        getVariableData()?.let { map ->
            map.forEach {
                mDataBinding.setVariable(it.key,it.value)
            }
        }
        mDataBinding.executePendingBindings()
    }

    fun invalidateAll(){
        mDataBinding?.invalidateAll()
    }

    override fun onAction(view:View,action: String, viewModel: BaseCustomModel) {
        mActionListener?.onAction(view,action,viewModel)
    }

//    override fun onDetachedFromWindow() {
//        Log.i("dyn","onDetachedFromWindow-------------$this")
//        if (isDetachedUnBind()){
//            mDataBinding.unbind()
//        }
//        super.onDetachedFromWindow()
//    }
//    protected open fun isDetachedUnBind():Boolean{
//        return true
//    }
//    override fun onAttachedToWindow() {
//        Log.e("dyn","onAttachedToWindow-------------$this")
//        mDataBinding.executePendingBindings()
//        super.onAttachedToWindow()
//    }
    @LayoutRes
    protected abstract fun getViewLayoutId(): Int

    protected abstract fun getDataVariableId():Int

    protected abstract fun getClickVariableId():Int

    protected open fun rootClickable():Boolean{
        return true
    }

    protected open fun getVariableData(): MutableMap<Int, Any>?{
        return null
    }


}