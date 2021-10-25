package com.dyn.base.utils

data class ServerException(val code: Int, override var message: String = "") : RuntimeException()
