package com.dyn.net.api.interceptor

import com.blankj.utilcode.util.NetworkUtils
import com.dyn.net.api.INetworkRequestInfo
import com.dyn.net.api.errorhandler.ExceptionHandle
import okhttp3.Interceptor
import okhttp3.Response
/**
 * 请求拦截处理
 * @param mRequestInfo 请求的一些配置参数 请求头/是否是debug
 * @param mRequestHandler 做请求之前和请求之后的拦截处理
 * */
class RequestInterceptor(val mRequestInfo: INetworkRequestInfo, private val mRequestHandler: HttpRequestHandler?) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val builder = chain.request().newBuilder()
            val headers = mRequestInfo.getRequestHeaderMap()
            if (headers.isNotEmpty()){
                headers.forEach {
                    builder.addHeader(it.key,it.value)
                }
            }
        var request = builder.build()
        mRequestHandler?.let {
            request = it.onHttpRequestBefore(chain,request)
        }
        val response = chain.proceed(request)
        val result = response.body?.let {
            it.toString()
        }?:""
        return mRequestHandler?.onHttpResultResponse(result,chain,response) ?: response
    }
}