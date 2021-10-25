package com.dyn.base.ui.weight

import android.content.Context
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import com.flyco.roundview.RoundViewDelegate

class RoundConstraintLayout constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : ConstraintLayout(context, attrs, defStyleAttr){
    constructor(context: Context):this(context, null)
    constructor(context: Context, attrs: AttributeSet?):this(context, attrs, 0)

    private var delegate: RoundViewDelegate = RoundViewDelegate(this, context, attrs)


    /** use delegate to set attr  */
    fun getDelegate(): RoundViewDelegate {
        return delegate
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        if (delegate.isWidthHeightEqual && width > 0 && height > 0) {
            val max = width.coerceAtLeast(height)
            val measureSpec = MeasureSpec.makeMeasureSpec(max, MeasureSpec.EXACTLY)
            super.onMeasure(measureSpec, measureSpec)
            return
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        if (delegate.isRadiusHalfHeight) {
            delegate.cornerRadius = height / 2
        } else {
            delegate.setBgSelector()
        }
    }
}