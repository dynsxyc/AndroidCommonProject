package com.dyn.base.utils

import android.graphics.Rect
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import android.text.*
import android.text.method.TransformationMethod
import android.text.style.UpdateLayout
import android.view.View
import java.lang.ref.WeakReference

class StarTransformationMethod : TransformationMethod {

    companion object {
        private const val DOT = '\u002a'
        private val ACTIVE = NoCopySpan.Concrete()
        @Volatile
        private var sInstance: StarTransformationMethod? = null
        fun getInstance() =
            sInstance ?: synchronized(this) {
                sInstance ?: StarTransformationMethod().also { sInstance = it }
            }
    }

    override fun getTransformation(source: CharSequence, view: View?): CharSequence? {
        if (source is Spannable) {
            val sp = source

            /*
             * Remove any references to other views that may still be
             * attached.  This will happen when you flip the screen
             * while a password field is showing; there will still
             * be references to the old EditText in the text.
             */
            val vr: Array<ViewReference> = sp.getSpans(
                0, sp.length,
                ViewReference::class.java
            )
            for (i in vr.indices) {
                sp.removeSpan(vr[i])
            }
            removeVisibleSpans(sp)
            sp.setSpan(
                ViewReference(view), 0, 0,
                Spannable.SPAN_POINT_POINT
            )
        }
        return PasswordCharSequence(source)
    }


    override fun onFocusChanged(
        view: View?, sourceText: CharSequence?,
        focused: Boolean, direction: Int,
        previouslyFocusedRect: Rect?
    ) {
        if (!focused) {
            if (sourceText is Spannable) {
                removeVisibleSpans(sourceText)
            }
        }
    }

    private fun removeVisibleSpans(sp: Spannable) {
        val old: Array<Visible> = sp.getSpans(0, sp.length, Visible::class.java)
        for (i in old.indices) {
            sp.removeSpan(old[i])
        }
    }

    class PasswordCharSequence(private val mSource: CharSequence) : CharSequence, GetChars {
        override val length: Int = mSource.length

        override fun get(index: Int): Char {

            if (mSource is Spanned) {
                val sp = mSource
                var st = sp.getSpanStart(ACTIVE)
                var en = sp.getSpanEnd(ACTIVE)
                if (index in st until en) {
                    return mSource[index]
                }
                val visible: Array<Visible> = sp.getSpans(0, sp.length, Visible::class.java)
                for (a in visible.indices) {
                    if (sp.getSpanStart(visible[a].mTransformer) >= 0) {
                        st = sp.getSpanStart(visible[a])
                        en = sp.getSpanEnd(visible[a])
                        if (index in st until en) {
                            return mSource[index]
                        }
                    }
                }
            }
            return DOT
        }

        override fun subSequence(startIndex: Int, endIndex: Int): CharSequence {
            val buf = CharArray(endIndex - startIndex)
            getChars(startIndex, endIndex, buf, 0)
            return String(buf)
        }

        override fun toString(): String {
            return subSequence(0, length).toString()
        }

        override fun getChars(start: Int, end: Int, dest: CharArray, off: Int) {
            TextUtils.getChars(mSource, start, end, dest, off)
            var st = -1
            var en = -1
            var nvisible = 0
            var starts: IntArray? = null
            var ends: IntArray? = null
            if (mSource is Spanned) {
                val sp = mSource
                st = sp.getSpanStart(ACTIVE)
                en = sp.getSpanEnd(ACTIVE)
                val visible: Array<Visible> = sp.getSpans(0, sp.length, Visible::class.java)
                nvisible = visible.size
                starts = IntArray(nvisible)
                ends = IntArray(nvisible)
                for (i in 0 until nvisible) {
                    if (sp.getSpanStart(visible[i].mTransformer) >= 0) {
                        starts[i] = sp.getSpanStart(visible[i])
                        ends[i] = sp.getSpanEnd(visible[i])
                    }
                }
            }
            for (i in start until end) {
                if (!(i in st until en)) {
                    var visible = false
                    for (a in 0 until nvisible) {
                        if (i >= starts!![a] && i < ends!![a]) {
                            visible = true
                            break
                        }
                    }
                    if (!visible) {
                        dest[i - start + off] = DOT
                    }
                }
            }
        }
    }

    class Visible(
        private val mText: Spannable,
        val mTransformer: StarTransformationMethod
    ) :
        Handler(Looper.myLooper()!!), UpdateLayout, Runnable {
        override fun run() {
            mText.removeSpan(this)
        }

        init {
            postAtTime(this, SystemClock.uptimeMillis() + 1500)
        }
    }

    /**
     * Used to stash a reference back to the View in the Editable so we
     * can use it to check the settings.
     */
    class ViewReference(v: View?) : WeakReference<View?>(v), NoCopySpan

}
