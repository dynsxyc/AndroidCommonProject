package com.dyn.base.ui.weight.header

import android.graphics.Color
import android.graphics.drawable.Drawable
import androidx.databinding.ObservableField
import com.blankj.utilcode.util.ColorUtils
import com.blankj.utilcode.util.ResourceUtils
import com.blankj.utilcode.util.SizeUtils.dp2px
import com.blankj.utilcode.util.SizeUtils.sp2px
import com.dyn.base.R
import com.dyn.base.customview.BaseCustomModel

data class CommonHeaderModel(
        var bg : ObservableField<Int> = ObservableField<Int>(ColorUtils.getColor(R.color.common_header_bg)),
        var title: ObservableField<String> =  ObservableField<String>(""),
        var titleColor:ObservableField<Int> = ObservableField(ColorUtils.getColor(R.color.common_header_title)),
        var titleAlpha:ObservableField<Float> = ObservableField(1.0f),
        var titleSize:ObservableField<Int> = ObservableField(sp2px(18f)),
        var bindStatusBar:ObservableField<Boolean> = ObservableField(true),
        var hasBack:ObservableField<Boolean> = ObservableField(true),
        var hasFinish:ObservableField<Boolean> = ObservableField(false),
        var hasRight:ObservableField<Boolean> = ObservableField(false),
        var hasRightLast:ObservableField<Boolean> = ObservableField(false),
        var backStyle:CommonHeaderButton = CommonHeaderButton(drawableStart = ObservableField(ResourceUtils.getDrawable(R.drawable.ic_nav_back))),
        var finishStyle:CommonHeaderButton = CommonHeaderButton(),
        var rightStyle:CommonHeaderButton = CommonHeaderButton(),
        var rightLastStyle:CommonHeaderButton = CommonHeaderButton(),
        var hasBottomLine:ObservableField<Boolean> = ObservableField(false),
) : BaseCustomModel
data class CommonHeaderButton(
        var drawableStart:ObservableField<Drawable> = ObservableField(),
        var drawableEnd:ObservableField<Drawable> = ObservableField(),
        var drawableTop:ObservableField<Drawable> = ObservableField(),
        var drawableBottom:ObservableField<Drawable> = ObservableField(),
        var drawableTint:ObservableField<Int?> = ObservableField(ColorUtils.getColor(R.color.common_header_title)),
        var drawablePadding:ObservableField<Int?> = ObservableField(0),
        var text: ObservableField<String> =  ObservableField(""),
        var textSize:ObservableField<Int> = ObservableField(sp2px(0f)),
        var textColor :ObservableField<Int> = ObservableField(ColorUtils.getColor(R.color.common_header_title)),
        var messageCount:ObservableField<Int> = ObservableField(0),
        var isMessageStatus:ObservableField<Boolean> = ObservableField(false),
        var isBold:ObservableField<Boolean> = ObservableField(false),
)