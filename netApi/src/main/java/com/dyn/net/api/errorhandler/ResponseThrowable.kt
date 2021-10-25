package com.dyn.net.api.errorhandler

data class ResponseThrowable(val throwable: Throwable, val code: Int, override var message: String = "") : Exception(throwable)
