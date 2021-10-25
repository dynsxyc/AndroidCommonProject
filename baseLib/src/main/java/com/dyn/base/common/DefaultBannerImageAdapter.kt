package com.dyn.base.common

import com.dyn.base.utils.ImageLoaderManager
import com.youth.banner.adapter.BannerImageAdapter
import com.youth.banner.holder.BannerImageHolder

class DefaultBannerImageAdapter : BannerImageAdapter<Any?>(null) {
    override fun onBindView(holder: BannerImageHolder?, data: Any?, position: Int, size: Int) {
        holder?.let { h->
            data?.let {
                ImageLoaderManager.displayImage(h.imageView,it)
            }
        }
    }
}