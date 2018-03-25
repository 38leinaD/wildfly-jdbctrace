package de.dplatz.jdbctrace.presentation;

import java.io.Serializable;

import javax.enterprise.event.Event;
import javax.enterprise.inject.Model;
import javax.inject.Inject;

import de.dplatz.jdbctrace.control.ApplicationInitializer;
import de.dplatz.jdbctrace.control.SQLStatementRecorder;
import de.dplatz.jdbctrace.entity.JDBCStatement;

@Model
public class TracesBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	ApplicationInitializer logs;
    
	@Inject
	SQLStatementRecorder recorder;

   
    @Inject
    Event<JDBCStatement> event;

    public String generateEvent() {
        System.out.println("event");
       
        JDBCStatement stmt = new JDBCStatement();
        stmt.setStatement("Hello World!");
        event.fire(stmt);

        return null;
    }
    
    public SQLStatementRecorder getRecorder() {
    	return recorder;
    }
}
