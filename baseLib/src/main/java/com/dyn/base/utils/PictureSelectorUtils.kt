package com.dyn.base.utils

import android.app.Activity
import android.content.Context
import androidx.fragment.app.Fragment
import com.luck.picture.lib.basic.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.config.SelectMimeType
import com.luck.picture.lib.config.SelectMimeType.ofImage
import com.luck.picture.lib.config.SelectModeConfig
import com.luck.picture.lib.engine.CompressFileEngine
import com.luck.picture.lib.engine.UriToFileTransformEngine
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.interfaces.OnKeyValueResultCallbackListener
import com.luck.picture.lib.interfaces.OnResultCallbackListener
import com.luck.picture.lib.utils.SandboxTransformUtils
import top.zibin.luban.Luban
import top.zibin.luban.OnNewCompressListener
import java.io.File


object PictureSelectorUtils {

    fun with(context: Activity): SelectorBuilder {
        return SelectorBuilder(context)
    }

    class SelectorBuilder {
        var isSingleMode: Boolean = false
        var maxNum: Int = 3
        var isEnableCrop: Boolean = false
        var isCircleCrop: Boolean = false
        var crop_ratio_x: Int = 0
        var crop_ratio_y: Int = 0
        var imposeCompressSize: Int = -1 //限制图片压缩大小 不起作用，luban压缩不支持
        var ignoreCompressSize: Int = 100 //忽略图片压缩大小
        var requestCode: Int = PictureConfig.CHOOSE_REQUEST
        var listener: OnResultCallbackListener<LocalMedia>? = null
        var mContext: Activity? = null
        var isCamera: Boolean = false
        var isGalleryHasCamera: Boolean = true

        constructor(context: Activity) {
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

        fun setImposeCompressSize(imposeCompressSize: Int): SelectorBuilder {
            this.imposeCompressSize = imposeCompressSize
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
            val pictureSelector = PictureSelector.create(mContext as Activity)

            val compress = createLbCompress(imposeCompressSize)
            if (isCamera) {
                pictureSelector
                    .openCamera(ofImage())//相册 媒体类型 PictureMimeType.ofAll()、ofImage()、ofVideo()、ofAudio()
                    .setOutputCameraDir(mContext!!.cacheDir.absolutePath)
                    .setSandboxFileEngine(MeSandboxFileEngine())
                    .setCompressEngine(compress)
                    .forResultActivity(listener)
            } else {
                pictureSelector
                    .openGallery(ofImage())
                    .setCompressEngine(compress)
                    .setSelectionMode(if (maxNum == 1 || isSingleMode) SelectModeConfig.SINGLE else SelectModeConfig.MULTIPLE)
                    .setMaxSelectNum(maxNum)
                    .setSandboxFileEngine(MeSandboxFileEngine())
                    .isDirectReturnSingle(false)
                    .setImageEngine(GlideEngine.createGlideEngine())
                    .forResult(listener)
            }
        }
    }

    /**
     * 自定义沙盒文件处理
     */
    private class MeSandboxFileEngine : UriToFileTransformEngine {
        override fun onUriToFileAsyncTransform(
            context: Context,
            srcPath: String,
            mineType: String,
            call: OnKeyValueResultCallbackListener
        ) {
            if (call != null) {
                call.onCallback(
                    srcPath,
                    SandboxTransformUtils.copyPathToSandbox(context, srcPath, mineType)
                )
            }
        }
    }

    fun openImgPre(context: Context, position: Int, sourceUrl: String) {
        openImgPre(context, position, mutableListOf(sourceUrl))
    }

    fun openVideoPre(context: Context, position: Int, sourceData: LocalMedia) {
        openPre(context, position, mutableListOf(sourceData))
    }

    /**
     * 打开图片预览功能
     * */
    fun openImgPre(context: Context, position: Int, list: MutableList<String>) {
        val preData = list.map {
            val local = LocalMedia()
            local.path = it
            local
        }.toMutableList()
        if (preData.isNullOrEmpty().not()) {
            openPre(context, position, preData)
        }
    }

    private fun openPre(context: Context, position: Int, preData: MutableList<LocalMedia>) {
        PictureSelector.create(context).openPreview()
            .setImageEngine(GlideEngine.createGlideEngine())
            .isAutoVideoPlay(true)
            .isLoopAutoVideoPlay(true)
            .startActivityPreview(position, false, arrayListOf(*preData.toTypedArray()))
    }

    fun choiceVideo(context: Context, listener: OnResultCallbackListener<LocalMedia>? = null) {
        PictureSelector.create(context)
            .openGallery(SelectMimeType.ofVideo())
            .isDisplayCamera(false)
            .setFilterVideoMaxSecond(60)
            .setImageEngine(GlideEngine.createGlideEngine())
            .setMaxSelectNum(1).forResult(listener)
    }


    private fun createLbCompress(imposeCompressSize: Int): CompressFileEngine {
        return CompressFileEngine { context, source, call ->
            Luban.with(context).load(source).ignoreBy(100)
                .setCompressListener(object : OnNewCompressListener {
                    override fun onStart() {}
                    override fun onSuccess(source: String?, compressFile: File) {
                        call?.onCallback(source, compressFile.absolutePath)
                    }

                    override fun onError(source: String, e: Throwable) {
                        call?.onCallback(source, null)
                    }
                }).launch()
        }
    }

//    private fun imposeCompress(
//        context: Context,
//        imposeCompressSize: Int,
//        source: String?,
//        file: File,
//        call: OnKeyValueResultCallbackListener?
//    ) {
//        val fileSize = (FileUtils.getLength(file).toDouble()) / MemoryConstants.KB
//        if (fileSize > imposeCompressSize) {
//            Luban.with(context).load(file).ignoreBy(100)
//                .setCompressListener(object : OnNewCompressListener {
//                    override fun onStart() {}
//                    override fun onSuccess(source: String?, compressFile: File) {
//                        file.delete()
//                        imposeCompress(context, imposeCompressSize, source, compressFile, call)
//                    }
//
//                    override fun onError(source: String, e: Throwable) {
//                        call?.onCallback(source, null)
//                    }
//                }).launch()
//        } else {
//            call?.onCallback(source, file.absolutePath)
//        }
//    }
}