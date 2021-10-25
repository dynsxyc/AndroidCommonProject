package com.dyn.net.api.errorhandler

import android.net.ParseException
import com.dyn.base.utils.ServerException
import com.google.gson.JsonParseException
import org.apache.http.conn.ConnectTimeoutException
import org.json.JSONException
import retrofit2.HttpException
import java.net.ConnectException

object ExceptionHandle {

    private const val UNAUTHORIZED = 401
    private const val FORBIDDEN = 403
    private const val NOT_FOUND = 404
    private const val REQUEST_TIMEOUT = 408
    private const val INTERNAL_SERVER_ERROR = 500
    private const val BAD_GATEWAY = 502
    private const val SERVICE_UNAVAILABLE = 503
    private const val GATEWAY_TIMEOUT = 504

    fun handleException(e: Throwable): Throwable {
        e.printStackTrace()
        return if (e is HttpException) {
            val ex = ResponseThrowable(e, ERROR.HTTP_ERROR);
            when (e.code()) {
                UNAUTHORIZED,
                FORBIDDEN,
                NOT_FOUND,
                REQUEST_TIMEOUT,
                GATEWAY_TIMEOUT,
                INTERNAL_SERVER_ERROR,
                BAD_GATEWAY,
                SERVICE_UNAVAILABLE->{
                    ex.message = e.code().toString()
                }
                else ->
                    ex.message = "网络错误";
            }
            ex
        } else if (e is ServerException) {
            e
        }  else if (e is JsonParseException
                || e is JSONException
                || e is ParseException) {
            ResponseThrowable(e, ERROR.PARSE_ERROR, "Json 解析错误");
        } else if (e is ConnectException) {
            ResponseThrowable(e, ERROR.NETWORD_ERROR, "连接失败")
        } else if (e is javax.net.ssl.SSLHandshakeException) {
            ResponseThrowable(e, ERROR.SSL_ERROR, "证书验证失败");
        } else if (e is ConnectTimeoutException) {
            ResponseThrowable(e, ERROR.TIMEOUT_ERROR, "连接超时");
        } else if (e is java.net.SocketTimeoutException) {
            ResponseThrowable(e, ERROR.TIMEOUT_ERROR, "连接超时");
        } else {
            ResponseThrowable(e, ERROR.UNKNOWN, "未知错误");
        }
    }




    /**
     * 约定异常
     */
    object ERROR {
        /**
         * 未知错误
         */
        const val UNKNOWN = 1000

        /**
         * 解析错误
         */
        const val PARSE_ERROR = 1001

        /**
         * 网络错误
         */
        const val NETWORD_ERROR = 1002

        /**
         * 协议出错
         */
        const val HTTP_ERROR = 1003

        /**
         * 证书出错
         */
        const val SSL_ERROR = 1005

        /**
         * 连接超时
         */
        const val TIMEOUT_ERROR = 1006
    }
}