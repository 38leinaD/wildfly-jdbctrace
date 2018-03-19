package de.dplatz.jdbctrace.boundary;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

import de.dplatz.jdbctrace.control.LoggingJmxManager;

@Path("datasources")
public class DatasourcesResource {

	@Inject
	LoggingJmxManager logging;
	
	@GET
	public String getDS() throws Exception {
		//logging.findAll();
		// 2018-03-19 21:51:48,075 DEBUG [jboss.jdbc.spy] (Periodic Recovery) java:/PCE_REPORTING_TX_DATASOURCE [Connection] close()
		return System.currentTimeMillis() + "";
	}
}
