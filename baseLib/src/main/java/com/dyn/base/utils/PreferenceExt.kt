package com.dyn.base.utils

import android.content.Context
import android.util.Log
import com.blankj.utilcode.util.GsonUtils
import java.lang.Exception
import java.lang.reflect.Type
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty
import kotlin.reflect.typeOf

/**
 * @author dyn
 * @date on 2019/10/24  18:00
 * @packagename com.zhenpinji.lib.base.utils
 * @fileName PreferenceExt
 * @describe 添加描述
 * @email 583454199@qq.com
 * default 和 clazz配合使用 default为null clazz 必须制定类型
 **/
class PreferenceExt<T>(
    val context: Context,
    val name: String,
    private val default: T?,
    private val clazz: Class<T>? = null,
    private val prefName: String = "initDefault"
) : ReadWriteProperty<Any?, T?> {
    private val prefs by lazy {
        context.getSharedPreferences(prefName, Context.MODE_PRIVATE)
    }

    override fun getValue(thisRef: Any?, property: KProperty<*>): T? {
        return findPreference(name)
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T?) {
        putPreference(name, value)
    }

    private fun findPreference(key: String): T? {
        return when (default) {
            is Long -> prefs.getLong(key, default)
            is String -> prefs.getString(key, default)
            is Boolean -> prefs.getBoolean(key, default)
            is Int -> prefs.getInt(key, default)
            is Float -> prefs.getFloat(key, default)
            else -> {
                try {
                    val jsonStr = prefs.getString(key, "")
                    if (jsonStr.isNullOrEmpty().not() && clazz != null) {
                        GsonFactory.gson.fromJson(jsonStr, clazz)
                    } else {
                        default
                    }
                } catch (e: Exception) {
                    default
                }
            }
        }?.let {
            it as T
        }
    }

    private fun putPreference(key: String, value: T?) {
        with(prefs.edit()) {
            when (value) {
                is Long -> putLong(key, value)
                is String -> putString(key, value)
                is Boolean -> putBoolean(key, value)
                is Int -> putInt(key, value)
                is Float -> putFloat(key, value)
                else -> putString(key, GsonFactory.gson.toJson(value))
            }
            this.commit()
        }

    }
}