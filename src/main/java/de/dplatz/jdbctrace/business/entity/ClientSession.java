package de.dplatz.jdbctrace.business.entity;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ClientSession {
    private static final int MAX_BUFFERED_MESSAGES = 100;
    private static final int MAX_IDLE_CONNECTION_LIFETIME_SECONDS = 10;

    String id;
	long lastSignOfLife;
    BlockingQueue<JDBCStatement> queue = new LinkedBlockingQueue<>(MAX_BUFFERED_MESSAGES);

    public ClientSession(String id) {
        this.id = id;
    }

    public BlockingQueue<JDBCStatement> getQueue() {
        return queue;
    }

    public void refreshLifetime() {
        lastSignOfLife = System.nanoTime();
    }

    public void markDead() {
        lastSignOfLife = 0;
    }

    public boolean isAlive() {
        return (System.nanoTime() - lastSignOfLife) < MAX_IDLE_CONNECTION_LIFETIME_SECONDS * 1e9;
    }
}