package com.dyn.net.api.interceptor

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

interface HttpRequestHandler {
    /**
     * 接口调用请求之后
     * */
    fun onHttpResultResponse(httpResult: String, chain: Interceptor.Chain, response: Response): Response
    /**
     * 接口调用请求之前
     * */
    fun onHttpRequestBefore(chain: Interceptor.Chain, request: Request): Request
}