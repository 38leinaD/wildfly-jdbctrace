package de.dplatz.jdbctrace.control;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

import javax.ejb.Singleton;
import javax.enterprise.event.Observes;

import de.dplatz.jdbctrace.entity.JDBCEvent;

/**
 * EventQueue
 */
@Singleton
public class EventQueue {

    BlockingQueue<JDBCEvent> queue = new LinkedBlockingDeque<>(50);

    public void onJDBCEvent(@Observes JDBCEvent event) {
        queue.add(event);
        System.out.println("=======");
    }

    public BlockingQueue<JDBCEvent> getQueue() {
        return queue;
    }
}