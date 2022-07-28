package com.dyn.net.api

data class ServerException(val code: Int, override var message: String = "") : RuntimeException()
