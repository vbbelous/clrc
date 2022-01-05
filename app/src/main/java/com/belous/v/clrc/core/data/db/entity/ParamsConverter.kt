package com.belous.v.clrc.core.data.db.entity

import androidx.room.TypeConverter

class ParamsConverter {
    @TypeConverter
    fun fromParams(params: Map<String, String>): String {
        val stringBuilder = StringBuilder()
        for ((key, value) in params) {
            stringBuilder.append(key)
            stringBuilder.append(":")
            stringBuilder.append(value)
            stringBuilder.append(";")
        }
        return stringBuilder.toString()
    }

    @TypeConverter
    fun toParams(data: String): Map<String, String> {
        val params = HashMap<String, String>()
        for (str in data.split(";".toRegex()).toTypedArray()) {
            val param = str.split(":".toRegex()).toTypedArray()
            if (param.size > 1) {
                params[param[0]] = param[1]
            } else {
                params[param[0]] = ""
            }
        }
        return params
    }
}