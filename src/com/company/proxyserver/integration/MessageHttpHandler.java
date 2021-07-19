package com.company.proxyserver.integration;

import com.company.proxyserver.models.Message;
import com.company.proxyserver.services.QueueService;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.company.proxyserver.utils.IOUtils;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;

public class MessageHttpHandler implements HttpHandler {
    private static final String SPLIT_VALUE = "text=";

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        try {
            String requestParamValue = null;
            if ("GET".equals(httpExchange.getRequestMethod())) {
                requestParamValue = handleGet(httpExchange);
            } else if ("POST".equals(httpExchange.getRequestMethod())) {
                requestParamValue = handlePost(httpExchange);
            }

            if (requestParamValue == null) {
                handleResponse(httpExchange, HttpURLConnection.HTTP_BAD_REQUEST, "Bad request");
            } else {
                handleResponse(httpExchange, HttpURLConnection.HTTP_OK, "success");
                QueueService.add(new Message(requestParamValue));
                System.out.println("Message received: " + requestParamValue);
            }

        } catch (Exception ex) {
            handleResponse(httpExchange, HttpURLConnection.HTTP_INTERNAL_ERROR, "Internal server error");
        }
    }

    private String handleGet(HttpExchange httpExchange) {
        String[] split = httpExchange.
                getRequestURI()
                .toString()
                .split(SPLIT_VALUE);
        return split.length > 0 ? split[1] : null;
    }

    private String handlePost(HttpExchange httpExchange) {
        String body = IOUtils.toString(httpExchange.getRequestBody());
        String[] split = body.split(SPLIT_VALUE);
        return split.length > 0 ? split[1] : null;
    }

    private void handleResponse(HttpExchange httpExchange, int statusCode, String htmlResponse) throws IOException {
        httpExchange.sendResponseHeaders(statusCode, htmlResponse.length());
        try (OutputStream out = httpExchange.getResponseBody()) {
            out.write(htmlResponse.getBytes());
            out.flush();
        }
    }
}
