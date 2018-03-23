package de.dplatz.jdbctrace.presentation;

import java.io.Serializable;
import java.util.List;

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
    
	public List<JDBCEvent> getEntries() {
		return null;
	}
    
    public Object toggleTrace() {
        System.out.println("TOGGLE");
        try {
        logging.setTrace(!logging.isTrace());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
