package de.dplatz.jdbctrace.business.control;

import java.util.List;

import de.dplatz.jdbctrace.business.entity.JDBCDatasource;

public interface AppServerPlugin {

	boolean isEligible();

	List<JDBCDatasource> findAllDatasources();

	void updateDatasource(JDBCDatasource ds);

	void generateTestData();

	void enableLogging(boolean flag);

	void startup();

	void teardown();

}