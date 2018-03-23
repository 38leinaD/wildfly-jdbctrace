package de.dplatz.jdbctrace.entity;

/**
 * JDBCEvent
 */
public class JDBCEvent {

    String query;

    public JDBCEvent(String query) {
        this.query = query;
    }

    public String getQuery() {
        return query;
    }
}