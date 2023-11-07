package com.dyn.base.dialog.pop

import android.content.Context
import android.view.View
import android.view.ViewGroup.LayoutParams
import com.blankj.utilcode.util.ConvertUtils
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.core.BasePopupView
import com.lxj.xpopup.enums.PopupPosition
import com.lxj.xpopup.interfaces.XPopupCallback

object PopupManager {

    /**
     *  menu
     * */
    fun showCommonAttachViewModeDialog(
        context: Context,
        atView: View,
        contentView: View,
        position: PopupPosition = PopupPosition.Bottom,
        xOffset: Int = ConvertUtils.dp2px(0f),
        yOffset: Int = ConvertUtils.dp2px(0f),
        isViewMode:Boolean = true,
        hasShadowBg:Boolean = true,
        params:LayoutParams = LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT),
        callBack: XPopupCallback? = null,
    ): BasePopupView {
        return XPopup.Builder(context)
            .atView(atView)
            .hasShadowBg(hasShadowBg)
            .isViewMode(isViewMode)
            .popupPosition(position)
            .setPopupCallback(callBack)
            .offsetX(xOffset)
            .offsetY(yOffset)
            .asCustom(
                CommonPartShadowPopupView(context, contentView,params)
            )
            .show()
    }
}