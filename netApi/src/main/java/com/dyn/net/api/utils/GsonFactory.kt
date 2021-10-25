package com.dyn.net.api.utils

import com.google.gson.*
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter
import java.io.IOException
import java.lang.reflect.Type

object GsonFactory {
    val gson: Gson by lazy {
        GsonBuilder().registerTypeAdapter(String::class.java, StringNullAdapter())
                .registerTypeAdapter(Long::class.java, LongDefaultAdapter())
                .registerTypeAdapter(Int::class.java, IntDefaultAdapter())
                .create()
    }
}

class StringNullAdapter : TypeAdapter<String>() {
    @Throws(IOException::class)
    override fun read(reader: JsonReader?): String? {
        val peek = reader?.peek()
        if (peek == JsonToken.NULL) {
            reader.nextNull()
            return ""
        }
        /* coerce booleans to strings for backwards compatibility */
        val nexStr = reader?.nextString()
        return if (nexStr == null || "null".equals(nexStr, true)) {
            ""
        } else nexStr
    }

    @Throws(IOException::class)
    override fun write(out: JsonWriter?, value: String?) {
        if (value == null) {
            out?.value("")
            return
        }
        out?.value(value)
    }
}


class LongDefaultAdapter: JsonSerializer<Long>, JsonDeserializer<Long> {
    override fun serialize(src: Long?, typeOfSrc: Type?, context: JsonSerializationContext?): JsonElement {
        return JsonPrimitive(src?:0)
    }

    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): Long {
        try {
            if (json?.asString == "" || json?.asString == "null") {//定义为long类型,如果后台返回""或者null,则返回0
                return 0
            }
        } catch (ignore: Exception) {
            return 0
        }

        return try {
            json?.asLong ?: 0
        } catch (e: NumberFormatException) {
            0
        }

    }
}

class IntDefaultAdapter : JsonSerializer<Int>, JsonDeserializer<Int> {
    override fun serialize(src: Int?, typeOfSrc: Type?, context: JsonSerializationContext?): JsonElement {
        return JsonPrimitive(src?:0)
    }

    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): Int {
        try {
            if (json?.asString == "" || json?.asString == "null") {//定义为long类型,如果后台返回""或者null,则返回0
                return 0
            }
        } catch (ignore: Exception) {
            return 0
        }

        return try {
            json?.asInt ?: 0
        } catch (e: NumberFormatException) {
            0
        }

    }
}