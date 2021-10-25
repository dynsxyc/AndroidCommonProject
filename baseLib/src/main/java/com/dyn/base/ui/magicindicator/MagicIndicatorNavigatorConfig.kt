package com.dyn.base.ui.magicindicator

data class MagicIndicatorNavigatorConfig(
    var mAdjustMode: Boolean = false,         // 自适应模式，适用于数目固定的、少量的title
    var mEnablePivotScroll: Boolean = false, // 启动中心点滚动
    var mScrollPivotX: Float = 0.5f, // 滚动中心点 0.0f - 1.0f
    var mSmoothScroll: Boolean = true, // 是否平滑滚动，适用于 !mAdjustMode && !mFollowTouch
    var mFollowTouch: Boolean = true, // 是否手指跟随滚动
    var mRightPadding: Int = 0,
    var mLeftPadding: Int = 0,
    var mIndicatorOnTop: Boolean = false,// 指示器是否在title上层，默认为下层
    var mSkimOver: Boolean = false,// 跨多页切换时，中间页是否显示 "掠过" 效果
    var mReselectWhenLayout: Boolean = true // PositionData准备好时，是否重新选中当前页，为true可保证在极端情况下指示器状态正确
)
