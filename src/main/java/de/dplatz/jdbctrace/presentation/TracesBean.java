package de.dplatz.jdbctrace.presentation;

import java.io.Serializable;

import javax.enterprise.event.Event;
import javax.enterprise.inject.Model;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Inject;

import de.dplatz.jdbctrace.business.control.JDBCStatementRecorder;
import de.dplatz.jdbctrace.business.control.JDBCTraceApplication;
import de.dplatz.jdbctrace.business.entity.JDBCStatement;

@Model
public class TracesBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	JDBCTraceApplication logs;
    
	@Inject
	JDBCStatementRecorder recorder;
   
    @Inject
    Event<JDBCStatement> event;

    public void updateFilterJunk(ValueChangeEvent event) {
        recorder.setFilterJunk((boolean) event.getNewValue());
    }

    public JDBCStatementRecorder getRecorder() {
    	return recorder;
    }
}
