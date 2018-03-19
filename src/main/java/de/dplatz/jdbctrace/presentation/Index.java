package de.dplatz.jdbctrace.presentation;

import java.io.Serializable;
import java.util.List;

import javax.enterprise.inject.Model;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Inject;

import de.dplatz.jdbctrace.control.ServerConnector;
import de.dplatz.jdbctrace.entity.Datasource;
import de.dplatz.jdbctrace.entity.Datasources;

@Model
public class Index implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	Datasources datasources;
	
	@Inject
	ServerConnector connector;
	
	public Object clicked() {
		
		System.out.println("HELLO!!!");
		return null;
	}
	
	public List<Datasource> getDatasources() {
		return datasources.getAll();
	}
	
	public ServerConnector getConnector() {
		return connector;
	}
	
	public void spyingEnabledChanged(ValueChangeEvent event) {
		//connector.setRestartRequired();
		System.out.println("XXX" + event.getNewValue());
		connector.setRestartRequired();
	}
}
