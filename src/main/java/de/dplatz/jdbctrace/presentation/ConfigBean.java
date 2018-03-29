package de.dplatz.jdbctrace.presentation;

import java.io.Serializable;
import java.util.List;

import javax.enterprise.inject.Model;
import javax.inject.Inject;

import de.dplatz.jdbctrace.control.ApplicationInitializer;
import de.dplatz.jdbctrace.control.wildfly.WildflySupport;
import de.dplatz.jdbctrace.entity.JDBCDatasource;

@Model
public class ConfigBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	ApplicationInitializer initializer;

	@Inject
	WildflySupport serverSupport;
	
	
	List<JDBCDatasource> datasources;
	
	public List<JDBCDatasource> getDatasources() {
		if (datasources == null) {
			datasources = serverSupport.getDatasources();
		}
		return datasources;
	}
	
	public String save() {
		if (datasources == null) return null;
		datasources.forEach(ds -> serverSupport.update(ds));
		boolean anyActive = datasources.stream()
			.filter(JDBCDatasource::isSpyingEnabled)
			.findAny()
			.isPresent();

		serverSupport.enableLogging(anyActive);
		
		datasources = null;
		return null;
	}

	public ApplicationInitializer getInitializer() {
		return initializer;
	}
}
