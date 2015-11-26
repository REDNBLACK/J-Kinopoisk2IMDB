package org.f0w.k2i.core.utils;

import com.google.common.base.Joiner;
import java.util.Map;

public class StringHelper {
    public static String buildHttpQuery(final String url, Map<String, String> queryData) {
        return  url + Joiner.on("&").withKeyValueSeparator("=").join(queryData);
    }
}
