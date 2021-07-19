package com.company.proxyserver;

public class Settings {
    public static final int PROXY_PORT = 8085;
    public static final String BACKEND_URL = "http://localhost:8086/message";

    public static final int HTTP_SERVER_POOL_SIZE = 5;
    public static final int MESSAGE_LISTENER_POOL_SIZE = 5;
    public static final int BACKEND_SEND_TIMEOUT_MS = 3000;
}
