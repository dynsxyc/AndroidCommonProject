package com.dyn.base.utils
//
import com.luck.picture.lib.entity.LocalMedia

object LocalMediaUtils {
    fun LocalMedia.filter(): String {
        return when {
            isCut && cutPath.isNullOrEmpty().not() -> {
                cutPath
            }
            isCompressed && compressPath.isNullOrEmpty().not() -> {
                compressPath
            }
            androidQToPath.isNullOrEmpty().not() -> {
                androidQToPath
            }
            else -> {
                path
            }
        }
    }
}