package de.dplatz.jdbctrace.business.control;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.enterprise.event.Observes;

import de.dplatz.jdbctrace.business.entity.JDBCStatement;

@Singleton
@ConcurrencyManagement(ConcurrencyManagementType.BEAN)
public class JDBCStatementRecorder {

    List<JDBCStatement> recordings = new LinkedList<>();
    BlockingQueue<JDBCStatement> queue = new LinkedBlockingDeque<>(50);
    private boolean record = false;
    private boolean filterJunk = true;

    public void on(@Observes JDBCStatement event) {
        if (!record) return;
//     	// REPROCESS CACHEREFRESHCOUNT LEASE TASKRUN
        System.out.println("+" + event.resolvedStatement());
        queue.add(event);
    }
    
    public BlockingQueue<JDBCStatement> getQueue() {
    	return queue;
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