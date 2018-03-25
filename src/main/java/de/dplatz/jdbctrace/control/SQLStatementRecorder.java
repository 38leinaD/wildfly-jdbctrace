package de.dplatz.jdbctrace.control;

import java.util.LinkedList;
import java.util.List;

import javax.ejb.Singleton;
import javax.enterprise.event.Observes;

import de.dplatz.jdbctrace.entity.JDBCStatement;

@Singleton
public class SQLStatementRecorder {

    List<JDBCStatement> recordings = new LinkedList<>();
	private boolean record = false;

    public void on(@Observes JDBCStatement event) {
    	if (record) {
    		System.out.println(".");
    		recordings.add(event);
    	}
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
    
    public boolean isRecording() {
    	return this.record;
    }
}