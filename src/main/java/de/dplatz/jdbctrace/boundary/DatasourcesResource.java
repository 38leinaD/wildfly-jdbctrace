package de.dplatz.jdbctrace.boundary;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

import de.dplatz.jdbctrace.control.ServerConnector;
import de.dplatz.jdbctrace.entity.Datasource;

@Path("datasources")
public class DatasourcesResource {

	
	@GET
	public String getDS() throws Exception {
		ServerConnector s = new ServerConnector();
		s.connect();
		List<Datasource> datasources = s.getDatasources();
		
		return datasources.get(0).getName().toString() + System.currentTimeMillis();
	}
}
