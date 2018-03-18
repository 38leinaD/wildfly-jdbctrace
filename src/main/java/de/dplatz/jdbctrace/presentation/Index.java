package de.dplatz.jdbctrace.presentation;

import java.io.Serializable;
import java.util.List;

import javax.enterprise.inject.Model;
import javax.inject.Inject;

import de.dplatz.jdbctrace.control.ServerConnector;
import de.dplatz.jdbctrace.entity.Datasource;

@Model
public class Index implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	ServerConnector connector;
	
	public Object clicked() {
		
		System.out.println("HELLO!!!");
		return null;
	}
	
	public List<Datasource> getDatasources() {
		List<Datasource> datasources = connector.getDatasources();
		
		return datasources;
	}
}
