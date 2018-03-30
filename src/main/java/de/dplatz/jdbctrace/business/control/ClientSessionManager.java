package de.dplatz.jdbctrace.business.control;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.enterprise.event.Observes;

import de.dplatz.jdbctrace.business.entity.ClientSession;
import de.dplatz.jdbctrace.business.entity.JDBCStatement;

@Singleton
@ConcurrencyManagement(ConcurrencyManagementType.BEAN)
public class ClientSessionManager {

    Map<String, ClientSession> sessions = new ConcurrentHashMap<>();
    
    private boolean record = false;

    public void on(@Observes JDBCStatement event) {
        if (!record) return;
        System.out.println("++" + event.resolvedStatement());

        sessions.values()
            .stream()
            .map(c -> c.getQueue())
            .map(q -> q.offer(event))
            .filter(accepted -> !accepted)
            .forEach(b -> System.out.println("++ Queue of client is full. Event was dropped on floor."));
    }


    @Schedule(hour = "*", minute = "*", second = "*/10")
    public void cleanup() {
        sessions.entrySet()
            .stream()
            .filter(e -> !e.getValue().isAlive())
            .map(e -> e.getKey())
            .forEach(id -> {
                sessions.remove(id);
            });
    }

    public ClientSession findOrCreateClientSession(String id) {
        ClientSession session = sessions.computeIfAbsent(id, ClientSession::new);
        session.refreshLifetime();
        return session;
    }
    
    public void record() {
    	this.record  = true;
    }
    
    public void stop() {
    	this.record = false;
    }

    public boolean isRecording() {
    	return this.record;
    }
}