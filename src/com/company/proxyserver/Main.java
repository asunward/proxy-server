package com.company.proxyserver;

import com.company.proxyserver.integration.MessageHttpServer;
import com.company.proxyserver.listeners.MessageListener;

public class Main {

    public static void main(String[] args) {
        MessageHttpServer messageHttpServer = null;
        try {
            messageHttpServer = new MessageHttpServer(Settings.HTTP_SERVER_POOL_SIZE);
            messageHttpServer.start();
            try (MessageListener messageListener = new MessageListener(Settings.MESSAGE_LISTENER_POOL_SIZE)) {
                while (messageListener.isRunning()) {
                    Thread.sleep(10000);
                }
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        } finally {
            if (messageHttpServer != null) {
                messageHttpServer.stop();
            }
        }
    }
}
