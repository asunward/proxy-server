package com.company.proxyserver.services;

import com.company.proxyserver.models.Message;

import java.util.concurrent.ConcurrentLinkedQueue;

public class QueueService {
    private static final ConcurrentLinkedQueue<Message> MESSAGE_QUEUE = new ConcurrentLinkedQueue<>();

    public static void add(Message message) {
        MESSAGE_QUEUE.add(message);
    }

    public static Message poll() {
        return MESSAGE_QUEUE.poll();
    }
}
