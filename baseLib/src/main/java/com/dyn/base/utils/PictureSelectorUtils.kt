package com.dyn.base.utils

import android.app.Activity
import android.content.Context
import androidx.fragment.app.Fragment
import com.dyn.base.R
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.animators.AnimationType
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.config.PictureMimeType
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.language.LanguageConfig
import com.luck.picture.lib.listener.OnResultCallbackListener
import com.luck.picture.lib.style.PictureWindowAnimationStyle

object PictureSelectorUtils {

    fun with(context: Context): SelectorBuilder {
        return SelectorBuilder(context)
    }

    class SelectorBuilder {
        var isSingleMode: Boolean = false
        var maxNum: Int = 3
        var isEnableCrop: Boolean = false
        var isCircleCrop: Boolean = false
        var crop_ratio_x: Int = 0
        var crop_ratio_y: Int = 0
        var requestCode: Int = PictureConfig.CHOOSE_REQUEST
        var listener: OnResultCallbackListener<LocalMedia>? = null
        var mContext: Context? = null
        var isCamera:Boolean = false
        var isGalleryHasCamera:Boolean = true

        constructor(context: Context) {
            this.mContext = context
        }

        fun isSingleMode(isSingleMode: Boolean): SelectorBuilder {
            this.isSingleMode = isSingleMode
            return this
        }

        fun maxNum(maxNum: Int): SelectorBuilder {
            this.maxNum = maxNum
            return this
        }

        fun isEnableCrop(isEnableCrop: Boolean): SelectorBuilder {
            this.isEnableCrop = isEnableCrop
            return this
        }

        fun isCircleCrop(isCircleCrop: Boolean): SelectorBuilder {
            this.isCircleCrop = isCircleCrop
            return this
        }

        fun crop_ratio_x(crop_ratio_x: Int): SelectorBuilder {
            this.crop_ratio_x = crop_ratio_x
            return this
        }

        fun crop_ratio_y(crop_ratio_y: Int): SelectorBuilder {
            this.crop_ratio_y = crop_ratio_y
            return this
        }

        fun requestCode(requestCode: Int): SelectorBuilder {
            this.requestCode = requestCode
            return this
        }

        fun isCamera(isCamera: Boolean): SelectorBuilder {
            this.isCamera = isCamera
            return this
        }
        fun isGalleryHasCamera(isGalleryHasCamera: Boolean): SelectorBuilder {
            this.isGalleryHasCamera = isGalleryHasCamera
            return this
        }

        fun listener(listener: OnResultCallbackListener<LocalMedia>?): SelectorBuilder {
            this.listener = listener
            return this
        }


        fun open() {
            if (mContext !is Fragment && mContext !is Activity) {
                return
            }
            val pictureSelector = if (mContext is Fragment) {
                PictureSelector.create(mContext as Fragment)
            } else {
                PictureSelector.create(mContext as Activity)
            }
            val anim = PictureWindowAnimationStyle(R.anim.in_from_right, R.anim.out_to_right, R.anim.in_from_left, R.anim.out_to_left)
            anim.ofAllAnimation(R.anim.in_from_right, R.anim.out_to_right)

            val pictureSelectionModel = if (isCamera){
                pictureSelector
                    .openCamera(PictureMimeType.ofImage())//相册 媒体类型 PictureMimeType.ofAll()、ofImage()、ofVideo()、ofAudio()
            }else{
                pictureSelector
                    .openGallery(PictureMimeType.ofImage())
            }
           pictureSelectionModel
                    .theme(R.style.picture_WeChat_style)// xml样式配制 R.style.picture_default_style、picture_WeChat_style or 更多参考Demo
//                .loadImageEngine()// 图片加载引擎 需要 implements ImageEngine接口
                    .imageEngine(GlideEngine.instance)
                    .selectionMode(if (isSingleMode) PictureConfig.SINGLE else PictureConfig.MULTIPLE)//单选or多选 PictureConfig.SINGLE PictureConfig.MULTIPLE
//                .isPageStrategy()//开启分页模式，默认开启另提供两个参数；pageSize每页总数；isFilterInvalidFile是否过滤损坏图片
                    .isSingleDirectReturn(false)//PictureConfig.SINGLE模式下是否直接返回
                    .isWeChatStyle(true)//开启R.style.picture_WeChat_style样式
//                .setPictureStyle()//动态自定义相册主题
//                .setPictureCropStyle()//动态自定义裁剪主题
                    .setPictureWindowAnimationStyle(anim)//相册启动退出动画
                    .isCamera(isGalleryHasCamera)//列表是否显示拍照按钮
                    .isZoomAnim(true)//图片选择缩放效果
//                .imageFormat(PictureMimeType.jp)//拍照图片格式后缀,默认jpeg, PictureMimeType.PNG，Android Q使用PictureMimeType.PNG_Q
                    .maxSelectNum(maxNum)//最大选择数量,默认9张
                    .minSelectNum(1)// 最小选择数量
//                .maxVideoSelectNum()//视频最大选择数量
//                .minVideoSelectNum()//视频最小选择数量
//                .videoMaxSecond()// 查询多少秒以内的视频
//                .videoMinSecond()// 查询多少秒以内的视频
                    .imageSpanCount(5)//列表每行显示个数
                    .isOpenClickSound(true)//是否开启点击声音
//                .selectionData()//是否传入已选图片
//                .recordVideoSecond()//录制视频秒数 默认60s
                    .isPreviewEggs(true)//预览图片时是否增强左右滑动图片体验
//                .cutOutQuality()// 注：已废弃 改用cutOutQuality()
                    .isGif(false)//是否显示gif
                    .isPreviewImage(true)//是否预览图片
                    .isPreviewVideo(false)//是否预览视频
                    .enablePreviewAudio(false)//是否预览音频
                    .isEnableCrop(isEnableCrop)//是否开启裁剪
//                .cropImageWideHigh()// 裁剪宽高比，设置如果大于图片本身宽高则无效
                    .withAspectRatio(crop_ratio_x, crop_ratio_y)//裁剪比例
//                .cutOutQuality()// 裁剪输出质量 默认100
//                .freeStyleCropEnabled()//裁剪框是否可拖拽
                    .circleDimmedLayer(isCircleCrop)// 是否开启圆形裁剪
//                .setCircleDimmedColor()//设置圆形裁剪背景色值
//                .setCircleDimmedBorderColor()//设置圆形裁剪边框色值
//                .setCircleStrokeWidth()//设置圆形裁剪边框粗细
                    .showCropFrame(!isCircleCrop)// 是否显示裁剪矩形边框 圆形裁剪时建议设为false
                    .showCropGrid(!isCircleCrop)//是否显示裁剪矩形网格 圆形裁剪时建议设为false
                    .rotateEnabled(true)//裁剪是否可旋转图片
                    .scaleEnabled(true)//裁剪是否可放大缩小图片
//                .isDragFrame()//是否可拖动裁剪框(固定)
//                .hideBottomControls()//显示底部uCrop工具栏
//                .basicUCropConfig()//对外提供ucrop所有的配制项
                    .isCompress(true)//是否压缩
//                .compressFocusAlpha()//压缩后是否保持图片的透明通道
                    .minimumCompressSize(300)// 小于多少kb的图片不压缩
//                .videoQuality()//视频录制质量 0 or 1
//                .compressQuality()//图片压缩后输出质量
//                .synOrAsy()//开启同步or异步压缩
//                .queryMaxFileSize()//查询指定大小内的图片、视频、音频大小，单位M
//                .compressSavePath()//自定义压缩图片保存地址，注意Q版本下的适配
//                .sizeMultiplier()//glide加载大小，已废弃
//                .glideOverride()//glide加载宽高，已废弃
                    .isMultipleSkipCrop(false)//多图裁剪是否支持跳过
                    .isMultipleRecyclerAnimation(true)// 多图裁剪底部列表显示动画效果
//                .querySpecifiedFormatSuffix()//只查询指定后缀的资源，PictureMimeType.ofJPEG() ...
                    .isReturnEmpty(false)//未选择数据时按确定是否可以退出
//                .isAndroidQTransform()//Android Q版本下是否需要拷贝文件至应用沙盒内
//                .setRequestedOrientation()//屏幕旋转方向 ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED ...
                    .isOriginalImageControl(false)//开启原图选项
//                .bindCustomPlayVideoCallback()//自定义视频播放拦截
//                .bindCustomCameraInterfaceListener()//自定义拍照回调接口
//                .bindCustomPreviewCallback()// 自定义图片预览回调接口
//                .cameraFileName()//自定义拍照文件名，如果是相册内拍照则内部会自动拼上当前时间戳防止重复
//                .renameCompressFile()//自定义压缩文件名，多张压缩情况下内部会自动拼上当前时间戳防止重复
//                .renameCropFileName()//自定义裁剪文件名，多张裁剪情况下内部会自动拼上当前时间戳防止重复
                    .setRecyclerAnimationMode(AnimationType.ALPHA_IN_ANIMATION)//列表动画效果,AnimationType.ALPHA_IN_ANIMATION、SLIDE_IN_BOTTOM_ANIMATION
                    .isUseCustomCamera(false)// 开启自定义相机
//                .setButtonFeatures()// 自定义相机按钮状态,CustomCameraView.BUTTON_STATE_BOTH
                    .setLanguage(LanguageConfig.CHINESE)//国际化语言 LanguageConfig.CHINESE、ENGLISH、JAPAN等
//                .isWithVideoImage()//图片和视频是否可以同选,只在ofAll模式下有效
                    .isMaxSelectEnabledMask(false)//选择条件达到阀时列表是否启用蒙层效果
                    .isAutomaticTitleRecyclerTop(true)//图片列表超过一屏连续点击顶部标题栏快速回滚至顶部
//                .loadCacheResourcesCallback()//获取ImageEngine缓存图片，参考Demo
//                .setOutputCameraPath()// 自定义相机输出目录只针对Android Q以下版本，具体参考Demo
                    .forResult(listener)//结果回调分两种方式onActivityResult()和OnResultCallbackListener方式
//        }
        }
    }

    /**
     * 打开图片预览功能
     * */
    fun openImgPre(context:Context, position: Int, list: MutableList<String>) {
        if (context !is Fragment && context !is Activity) {
            return
        }
        val pictureSelector = if (context is Fragment) {
            PictureSelector.create(context as Fragment)
        } else {
            PictureSelector.create(context as Activity)
        }
        val preData = list.map {
            val local = LocalMedia()
            local.path = it
            local
        }
        pictureSelector.themeStyle(R.style.picture_default_style).imageEngine(GlideEngine.instance)// 外部传入图片加载引擎，必传项   参考Demo MainActivity中代码
        pictureSelector.externalPicturePreview(position, preData, 0)
    }
}