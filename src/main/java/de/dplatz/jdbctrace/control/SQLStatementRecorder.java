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
    private boolean filterJunk = true;

    public void on(@Observes JDBCStatement event) {
        if (!record) return;
        if (filtered(event)) return;

    	recordings.add(event);
    }

    private boolean filtered(JDBCStatement stmt) {
        if (stmt.getStatement().startsWith("select REPROCESSID, PROCESS")) return true;
        if (stmt.getStatement().startsWith("select count(*) from REPROCESS")) return true;
        if (stmt.getStatement().startsWith("select PROCESSINGCENTERID, VERSION from CACHEREFRESHCOUNT")) return true;
        if (stmt.getStatement().startsWith("UPDATE LEASE SET VALIDTO")) return true;
        if (stmt.getStatement().startsWith("update TASKRUN set SERVER")) return true;
        if (stmt.getStatement().startsWith("insert into TASKRUN")) return true;
        
		return false;
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