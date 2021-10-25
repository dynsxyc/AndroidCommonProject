package com.dyn.base.utils

import java.util.*

object ServiceLoaderUtils {
    fun <T> loadFirstService(clazz: Class<T>):T?{
        val iterator = ServiceLoader.load(clazz).iterator()
        return try {
            iterator.next()
        }catch (e : Exception){
            null
        }
    }
    fun <T> loadServices(clazz: Class<T>):MutableIterator<T>{
        return ServiceLoader.load(clazz).iterator()
    }
}