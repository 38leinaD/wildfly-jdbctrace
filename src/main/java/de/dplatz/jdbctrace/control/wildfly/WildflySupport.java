package de.dplatz.jdbctrace.control.wildfly;

import java.io.File;
import java.lang.management.ManagementFactory;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.Singleton;
import javax.enterprise.concurrent.ManagedExecutorService;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.management.MBeanServerConnection;

import org.apache.commons.io.input.Tailer;
import org.apache.commons.io.input.TailerListenerAdapter;

import de.dplatz.jdbctrace.entity.JDBCDatasource;

@Singleton
public class WildflySupport {
	
	@Inject
	WildflyDatasourcesConfig datasources;
	
	public List<JDBCDatasource> getDatasources() {
		return datasources.findAll();
	}
	
	public void update(JDBCDatasource ds) {
		datasources.persist(ds);
	}
    
    @Inject
    WildflyLoggingConfig logging;

    public void enableLogging(boolean flag) {
        try {
			logging.setTrace(flag);
		} catch (Exception e) {
			e.printStackTrace();
		}
    }

    //--
    
    @Inject
    WildflyLogParser logParser;

    @Resource
    ManagedExecutorService executor;

    File serverLog = new File(System.getProperty("jboss.server.log.dir") + "/server.log");
    Tailer tail;

    public void init() {
        System.out.println("++ Reading " + serverLog.getAbsolutePath());
        tail = new Tailer(serverLog, new TailerListenerAdapter() {
            @Override
            public void handle(String line) {
                logParser.handleLine(line);
            }
        }, 100, true);

        executor.execute(tail);
    }

    public void teardown() {
        System.out.println("++ Shutting down...");
        tail.stop();
    }
}
