package com.dyn.base.ui.bannerindicator

import com.dyn.base.utils.ImageLoaderManager
import com.youth.banner.adapter.BannerImageAdapter
import com.youth.banner.holder.BannerImageHolder

class ImageBannerAdapter: BannerImageAdapter<String>(null){
    override fun onBindView(holder: BannerImageHolder?, data: String?, position: Int, size: Int) {
        holder?.let { h->
            data?.let {
                ImageLoaderManager.displayImage(h.imageView, it)
            }
        }
    }

}
