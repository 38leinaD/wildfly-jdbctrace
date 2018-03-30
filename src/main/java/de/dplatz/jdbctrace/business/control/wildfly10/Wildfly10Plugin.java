package de.dplatz.jdbctrace.business.control.wildfly10;

import java.io.File;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.Singleton;
import javax.enterprise.concurrent.ManagedExecutorService;
import javax.inject.Inject;

import org.apache.commons.io.input.Tailer;
import org.apache.commons.io.input.TailerListenerAdapter;

import de.dplatz.jdbctrace.business.control.AppServerPlugin;
import de.dplatz.jdbctrace.business.entity.JDBCDatasource;

@Singleton
public class Wildfly10Plugin implements AppServerPlugin {
	
	@Inject
	Wildfly10Datasources datasources;
	
    @Inject
    Wildfly10Logging logging;

    @Inject
    Wildfly10Tester tester;
    
    @Inject
    Wildfly10LogParser logParser;

    @Resource
    ManagedExecutorService executor;

    File serverLog = new File(System.getProperty("jboss.server.log.dir") + "/server.log");
    
    Tailer tail;
    
    @Override
	public boolean isEligible() {
    	String logPath = System.getProperty("jboss.server.log.dir");
    	return logPath != null;
    }
    
	@Override
	public List<JDBCDatasource> findAllDatasources() {
		return datasources.findAll();
	}
	
	@Override
	public void updateDatasource(JDBCDatasource ds) {
		datasources.persist(ds);
	}
    
    @Override
	public void generateTestData() {
    	tester.generateTestData();
    }
    
    @Override
	public void enableLogging(boolean flag) {
        try {
			logging.setTrace(flag);
		} catch (Exception e) {
			e.printStackTrace();
		}
    }

    @Override
	public void startup() {
        System.out.println("++ Reading " + serverLog.getAbsolutePath());
        tail = new Tailer(serverLog, new TailerListenerAdapter() {
            @Override
            public void handle(String line) {
                logParser.handleLine(line);
            }
        }, 100, true);

        executor.execute(tail);
    }

    @Override
	public void teardown() {
        System.out.println("++ Shutting down...");
        tail.stop();
    }
}
