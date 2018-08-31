package com.qysd.lawtree.lawtreeutils;

import java.util.List;

public class JsonToJsonUtil {
    public static String getStringJson(List<String> paramList, List<String> valueList) {
        String str = "{";
        for (int i = 0; i < paramList.size(); i++) {
            str = str + "\"" + paramList.get(i) + "\"" + ":" + "\"" + valueList.get(i) + "\"";
            if (i < paramList.size() - 1) {
                str = str + ",";
            }
        }
        str = str + "}";
        return str;
    }
}
