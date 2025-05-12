package com.vtidc.mymail.config.soap;

import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.protocol.HttpContext;
import org.springframework.stereotype.Service;

@Service
public class SoapRemoveHeadersInterceptor implements HttpRequestInterceptor {
    @Override
    public void process(HttpRequest request, HttpContext context) {
        // Xóa Content-Length nếu có
        if (request.containsHeader("Content-Length")) {
            request.removeHeaders("Content-Length");
        }
        request.setHeader("Content-Type", "application/soap+xml");
    }


}

