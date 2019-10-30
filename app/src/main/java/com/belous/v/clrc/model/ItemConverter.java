package com.belous.v.clrc.model;

import androidx.room.TypeConverter;

import java.util.HashMap;
import java.util.Map;

public class ItemConverter {

    @TypeConverter
    public String fromParams(Map<String, String> params) {
        StringBuilder stringBuilder = new StringBuilder();
        for (Map.Entry entry : params.entrySet()) {
            stringBuilder.append(entry.getKey());
            stringBuilder.append(":");
            stringBuilder.append(entry.getValue());
            stringBuilder.append(";");
        }
        return stringBuilder.toString();
    }

    @TypeConverter
    public Map<String, String> toParams(String data) {
        Map<String, String> params = new HashMap<>();
        for (String str : data.split(";")) {
            String[] param = str.split(":");
            if (param.length > 1) {
                params.put(param[0], param[1]);
            } else {
                params.put(param[0], "");
            }
        }
        return params;
    }
}
