package com.dyn.net.api.interceptor

import okhttp3.MediaType
import okhttp3.ResponseBody
import okio.*
import java.io.IOException


class ProgressResponseBody(
    private val responseBody: ResponseBody,
    val progressListener: ProgressListener? = null
) : ResponseBody() {
    private var bufferedSource: BufferedSource? = null
    override fun contentType(): MediaType? {
        return responseBody.contentType()
    }

    override fun contentLength(): Long {
        return responseBody.contentLength()
    }

    override fun source(): BufferedSource {
        if (bufferedSource == null) {
            bufferedSource = source(responseBody.source()).buffer()
        }
        return bufferedSource as BufferedSource
    }

    private fun source(source: Source): Source {
        return object : ForwardingSource(source) {
            var totalBytesRead = 0L
            @Throws(IOException::class)
            override fun read(sink: Buffer, byteCount: Long): Long {
                //当前读取字节数
                val bytesRead = super.read(sink, byteCount)
                //增加当前读取的字节数，如果读取完成了bytesRead会返回-1
                totalBytesRead += if (bytesRead != -1L) bytesRead else 0
                //回调，如果contentLength()不知道长度，会返回-1
                progressListener?.onProgress(
                    totalBytesRead,
                    responseBody.contentLength(),
                    bytesRead,
                    bytesRead == -1L
                )
                return bytesRead
            }
        }
    }

}