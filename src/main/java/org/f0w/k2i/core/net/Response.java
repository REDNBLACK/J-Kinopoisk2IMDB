package org.f0w.k2i.core.net;

import java.util.Map;

public interface Response {
    Response getResponse();

    int getStatusCode();

    Map<String, String> getInfo();
}
