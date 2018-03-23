package de.dplatz.jdbctrace.entity;

/**
 * JDBCTraceItem
 */
public class JDBCTraceItem {

    String timestamp;
    String thread;
    String datasource;
    String command;

    public JDBCTraceItem(String timestamp, String thread, String datasource, String command) {
        this.timestamp = timestamp;
        this.thread = thread;
        this.datasource = datasource;
        this.command = command;
    }

    public String getTimestamp() {
        return this.timestamp;
    }
    
    public String getThread() {
        return this.thread;
    }

    public String getDatasource() {
        return this.datasource;
    }

    public String getCommand() {
        return this.command;
    }
}