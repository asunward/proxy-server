package com.company.proxyserver.listeners;

import com.company.proxyserver.Settings;
import com.company.proxyserver.models.Message;
import com.company.proxyserver.integration.BackendClient;
import com.company.proxyserver.services.QueueService;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

public class MessageListener implements AutoCloseable {
    private ExecutorService executor;
    private AtomicBoolean running = new AtomicBoolean(true);

    public MessageListener(int poolSize) {
        executor = Executors.newFixedThreadPool(poolSize);
        for (int i = 0; i < poolSize; i++) {
            executor.execute(new MessageRunner());
        }
    }

    public void close() {
        running.set(false);
        executor.shutdownNow();
    }

    public boolean isRunning() {
        return running.get();
    }


    private class MessageRunner implements Runnable {
        public void run() {
            while (running.get()) {
                Message message = QueueService.poll();
                if (message == null) {
                    continue;
                }
                try {
                    BackendClient backendClient = new BackendClient(Settings.BACKEND_URL);
                    boolean sendResult = backendClient.send(message);
                    if (!sendResult) {
                        System.out.println(message.getText() + " failed to send");
                        QueueService.add(message);
                    } else {
                        System.out.println(message.getText() + " sent");
                    }
                } catch (Exception ex) {
                    QueueService.add(message);
                    System.out.println("MessageRunner error " + ex.getMessage());
                    running.set(false);
                }
            }
        }
    }
}
