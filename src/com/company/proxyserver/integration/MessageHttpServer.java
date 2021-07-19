package com.company.proxyserver.integration;

import com.company.proxyserver.Settings;
import com.sun.net.httpserver.HttpServer;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;


public class MessageHttpServer {
    private HttpServer server;

    public MessageHttpServer(int poolSize) throws Exception {
        try {
            server = HttpServer.create(new InetSocketAddress("localhost", Settings.PROXY_PORT), 0);
            server.createContext("/message", new MessageHttpHandler());
            server.setExecutor(Executors.newFixedThreadPool(poolSize));
        } catch (Exception ex) {
            System.out.println("Error creating MessageHttpServer");
            throw new Exception(ex);
        }
    }

    public void start() {
        if (server == null) {
            return;
        }
        server.start();
        System.out.println("MessageHttpServer started");
    }

    public void stop() {
        if (server == null) {
            return;
        }
        server.stop(0);
        System.out.println("MessageHttpServer stopped");
    }
}
