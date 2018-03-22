package de.dplatz.jdbctrace.presentation;

import java.io.Serializable;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Model;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import de.dplatz.jdbctrace.control.DatasourcesJmxManager;
import de.dplatz.jdbctrace.control.LoggingJmxManager;
import de.dplatz.jdbctrace.control.JmxManager;
import de.dplatz.jdbctrace.entity.Datasource;

@Model
public class Index implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	DatasourcesJmxManager datasourceDao;
	
	@Inject
	JmxManager connector;
	
	@Inject
	LoggingJmxManager logging;
	
	List<Datasource> datasources;
	
	public List<Datasource> getDatasources() {
		if (datasources == null) {
			datasources = datasourceDao.findAll();
		}
		return datasources;
	}
	
	public JmxManager getConnector() {
		return connector;
	}
	
	public String save() {
		if (datasources == null) return null;
		datasources.forEach(ds -> datasourceDao.persist(ds));

		try {
		if (datasources.stream()
			.filter(ds -> ds.isSpyingEnabled())
			.findAny()
			.isPresent()) {
			logging.setTrace(true);
		}
		else {
			logging.setTrace(false);
		}
		} catch(Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Error while enabling tracing", e.getLocalizedMessage()));
		}
		
		datasources = null;
		return null;
	}
}
