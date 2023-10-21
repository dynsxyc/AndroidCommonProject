package com.dyn.base.ui.addnumber

import android.app.Activity
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.databinding.Observable
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.dyn.base.BR
import com.dyn.base.R
import com.dyn.base.common.throttleFirstClick
import com.dyn.base.customview.BaseCustomModel
import com.dyn.base.customview.BaseCustomView
import com.dyn.base.customview.ICustomViewActionListener
import com.dyn.base.databinding.LayoutAddNumberBinding
import com.dyn.base.utils.BaseActionConstant
import com.dyn.base.utils.BaseDialogUtils

class AddNumberView(context: Context, attrs: AttributeSet?, defStyleAttr: Int) :
    BaseCustomView<ViewDataBinding, AddNumberModel>(context, attrs, defStyleAttr) {
    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    private var mOnNumberChangeListener: IOnNumberChangeListener? = null
    fun setOnPropertyChangedCallback(listener: IOnNumberChangeListener) {
        mOnNumberChangeListener = listener
    }

    private var stepNumber = 1

    init {
        val a = context.obtainStyledAttributes(attrs, R.styleable.AddNumberView)
        val layoutId = a.getResourceId(R.styleable.AddNumberView_layoutId, R.layout.layout_add_number)
        stepNumber = a.getInt(R.styleable.AddNumberView_step_number, 1)
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        mDataBinding = DataBindingUtil.inflate(inflater, layoutId, this, false)
        if (rootClickable()) {
            mDataBinding.root.throttleFirstClick {
                onAction(
                    this,
                    ICustomViewActionListener.ACTION_ROOT_VIEW_CLICKED,
                    mViewModel as BaseCustomModel
                )
            }
        }
        addView(mDataBinding.root)

        setData(AddNumberModel())
        val isEditAble =
            a.getBoolean(R.styleable.AddNumberView_isEditable, mViewModel?.isEditEnable?.get()!!)
        val min = a.getInt(R.styleable.AddNumberView_min_number, mViewModel?.minCount ?.get()?: 1)
        val max = a.getInt(R.styleable.AddNumberView_max_number, mViewModel?.maxCount?.get() ?: 1)
        mViewModel?.minCount?.set(min)
        mViewModel?.maxCount?.set(max)
        mViewModel?.count?.set(1 * stepNumber)
        mViewModel?.isEditEnable?.set(isEditAble)
        mViewModel?.count?.addOnPropertyChangedCallback(object :
            Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                mOnNumberChangeListener?.onChange()
            }
        })
    }

    override fun getViewLayoutId(): Int {
        return 0
    }

    override fun getDataVariableId(): Int {
        return BR.vm
    }

    override fun getClickVariableId(): Int {
        return BR.action
    }

    override fun onAction(view: View, action: String, viewModel: BaseCustomModel) {
        when (action) {
            BaseActionConstant.ACTION_ADD_NUMBER_SUB -> {
                if ((mViewModel?.count?.get()
                        ?: 0) - (1 * stepNumber) >= mViewModel?.minCount?.get()?: 0
                ) {
                    mViewModel?.count?.set((mViewModel?.count?.get() ?: 1) - (1 * stepNumber))
                }
            }

            BaseActionConstant.ACTION_ADD_NUMBER_PLUS -> {
                if (mViewModel?.count?.get() ?: 0 >= mViewModel?.maxCount?.get() ?: 0) {
                    return
                }
                if ((mViewModel?.count?.get()
                        ?: 0) + (1 * stepNumber) <= mViewModel?.maxCount?.get()!!
                ) {
                    mViewModel?.count?.set((mViewModel?.count?.get() ?: 1) + (1 * stepNumber))
                }
            }

            BaseActionConstant.ACTION_ADD_NUMBER_CONTENT -> {
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
                if (activity is Activity) {
                    BaseDialogUtils.showNumberEditText(activity, mViewModel?.count?.get() ?: 1) {
                        val maxCount = mViewModel?.maxCount?.get() ?: 0
                        if (it > maxCount) {
                            mViewModel?.count?.set(maxCount)
                        } else {
                            mViewModel?.count?.set(it)
                        }
                    }
                }
            }
        }

    }
}