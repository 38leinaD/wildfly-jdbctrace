package de.dplatz.jdbctrace.presentation;

import java.io.Serializable;

import javax.enterprise.inject.Model;
import javax.inject.Inject;

import de.dplatz.jdbctrace.business.control.ClientSessionManager;
import de.dplatz.jdbctrace.business.control.JDBCTraceApplication;

@Model
public class TracesBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	JDBCTraceApplication logs;
    
	@Inject
	ClientSessionManager clientSessionManager;

    public ClientSessionManager getClientSessionManager() {
    	return clientSessionManager;
    }
}
