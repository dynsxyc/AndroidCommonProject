package com.dyn.net.api

interface INetworkRequestInfo {
    fun getRequestHeaderMap():MutableMap<String,String>
    fun isDebug():Boolean
}