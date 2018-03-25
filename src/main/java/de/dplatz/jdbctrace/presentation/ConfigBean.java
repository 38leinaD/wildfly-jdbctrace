package de.dplatz.jdbctrace.presentation;

import java.io.Serializable;
import java.util.List;

import javax.enterprise.inject.Model;
import javax.inject.Inject;

import de.dplatz.jdbctrace.control.wildfly.WildflySupport;
import de.dplatz.jdbctrace.entity.JDBCDatasource;

@Model
public class ConfigBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	WildflySupport serverConfigService;
	
	
	List<JDBCDatasource> datasources;
	
	public List<JDBCDatasource> getDatasources() {
		if (datasources == null) {
			datasources = serverConfigService.getDatasources();
		}
		return datasources;
	}
	
	public String save() {
		if (datasources == null) return null;
		datasources.forEach(ds -> serverConfigService.update(ds));

		
		datasources = null;
		return null;
	}
}
