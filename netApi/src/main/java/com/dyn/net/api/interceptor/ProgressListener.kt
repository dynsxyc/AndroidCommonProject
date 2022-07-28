package com.dyn.net.api.interceptor

interface ProgressListener {
    fun onProgress(progress: Long, total: Long, speed: Long, done: Boolean)
}