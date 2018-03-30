package de.dplatz.jdbctrace.presentation;

import java.io.Serializable;
import java.util.List;

import javax.enterprise.inject.Model;
import javax.inject.Inject;

import de.dplatz.jdbctrace.business.control.AppServerPlugin;
import de.dplatz.jdbctrace.business.control.JDBCTraceApplication;
import de.dplatz.jdbctrace.business.entity.JDBCDatasource;

@Model
public class ConfigBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	JDBCTraceApplication initializer;

	@Inject
	AppServerPlugin serverSupport;
	
	List<JDBCDatasource> datasources;
	
	public List<JDBCDatasource> getDatasources() {
		if (datasources == null) {
			datasources = serverSupport.findAllDatasources();
		}
		return datasources;
	}
	
	public String save() {
		if (datasources == null) return null;
		datasources.forEach(ds -> serverSupport.updateDatasource(ds));
		boolean anyActive = datasources.stream()
			.filter(JDBCDatasource::isSpyingEnabled)
			.findAny()
			.isPresent();

		serverSupport.enableLogging(anyActive);
		
		datasources = null;
		return null;
	}

	public JDBCTraceApplication getInitializer() {
		return initializer;
	}
}
