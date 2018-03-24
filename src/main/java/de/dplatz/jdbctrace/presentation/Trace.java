package de.dplatz.jdbctrace.presentation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.event.Event;
import javax.enterprise.inject.Model;
import javax.inject.Inject;

import de.dplatz.jdbctrace.control.LogMonitorInitializer;
import de.dplatz.jdbctrace.control.LoggingJmxManager;
import de.dplatz.jdbctrace.entity.JDBCEvent;

@Model
public class Trace implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	LogMonitorInitializer logs;
	
	@Inject
    LoggingJmxManager logging;
    

    List<JDBCEvent> events = new ArrayList<>();

	public List<JDBCEvent> getEntries() {
		return events;
	}
    
    public String toggleTrace() {
        System.out.println("TOGGLE");
        try {
        logging.setTrace(!logging.isTrace());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Inject
    Event<JDBCEvent> event;

    public String generateEvent() {
        System.out.println("event");
       
        event.fire(new JDBCEvent("BLA"));

        return null;
    }
}
