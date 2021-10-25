package com.dyn.aliosslib

import android.app.Application
import com.alibaba.sdk.android.oss.OSSClient
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider
import com.dyn.base.common.handlerThread
import com.dyn.base.ui.base.BaseApplication
import io.reactivex.rxjava3.core.Observable


class OssServiceManager(private val endPoint:String, provider: OSSCredentialProvider) {

    private lateinit var mOssClient :OSSClient

    init{
        Observable.just(provider).flatMap {
            Observable.just(OSSClient(BaseApplication.AppContext,endPoint,it))
        }.handlerThread().subscribe {
            mOssClient = it
        }
    }
}