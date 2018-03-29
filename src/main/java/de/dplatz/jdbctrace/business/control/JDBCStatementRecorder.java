package de.dplatz.jdbctrace.business.control;

import java.util.LinkedList;
import java.util.List;

import javax.ejb.Singleton;
import javax.enterprise.event.Observes;

import de.dplatz.jdbctrace.business.entity.JDBCStatement;

@Singleton
public class JDBCStatementRecorder {

    List<JDBCStatement> recordings = new LinkedList<>();
    private boolean record = false;
    private boolean filterJunk = true;

    public void on(@Observes JDBCStatement event) {
    	System.out.println("+" + event.getStatement());
        if (!record) return;
//     	// REPROCESS CACHEREFRESHCOUNT LEASE TASKRUN

    	recordings.add(event);
    }

	public List<JDBCStatement> getRecordedStatements() {
        return recordings;
    }
    
    public void record() {
    	this.record  = true;
    }
    
    public void stop() {
    	this.record = false;
    }
    
    public void clear() {
        this.recordings.clear();
    }

    public boolean isRecording() {
    	return this.record;
    }

    public boolean isFilterJunk() {
    	return this.filterJunk;
    }

    public void setFilterJunk(boolean filter) {
    	this.filterJunk = filter;
    }
}