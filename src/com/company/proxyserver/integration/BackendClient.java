package com.company.proxyserver.integration;

import com.company.proxyserver.Settings;
import com.company.proxyserver.models.Message;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.nio.charset.StandardCharsets;


public class BackendClient {
    private final String address;

    public BackendClient(String address) {
        this.address = address;
    }

    private static byte[] getMessageData(Message message) {
        //здесь должна быть сереализация, но для упрощения будем отправлять просто 1 атрибут
        String serMessage = String.format("text=%s", message.getText());
        return serMessage.getBytes(StandardCharsets.UTF_8);
    }

    private static HttpURLConnection getURLConnection(String address) throws IOException {
        URL url = new URL(address);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setDoOutput(true);
        con.setUseCaches(false);
        con.setConnectTimeout(Settings.BACKEND_SEND_TIMEOUT_MS);
        con.setReadTimeout(Settings.BACKEND_SEND_TIMEOUT_MS);
        con.setRequestProperty("Connection", "keep-alive");
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json");
        con.setDoOutput(true);
        return con;
    }

    public boolean send(Message message) throws Exception {
        try {
            HttpURLConnection con = getURLConnection(address);
            try (OutputStream out = con.getOutputStream()) {
                out.write(getMessageData(message));
                out.flush();
            }
            return con.getResponseCode() == HttpURLConnection.HTTP_OK;
        } catch (ConnectException| SocketTimeoutException ignored) {
            //проглатываем ошибки связанные с недоступностью бэкенда
        }
        return false;
    }
}
