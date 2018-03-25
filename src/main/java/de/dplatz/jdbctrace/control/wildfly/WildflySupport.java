package de.dplatz.jdbctrace.control.wildfly;

import java.io.File;
import java.lang.management.ManagementFactory;
import java.util.List;
import java.util.concurrent.ExecutorService;

import javax.annotation.Resource;
import javax.ejb.Singleton;
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
	
    @Produces
    public MBeanServerConnection getConnection() {
    	return ManagementFactory.getPlatformMBeanServer();
    }
    
    //--
    
    @Inject
    WildflyLogParser logParser;

    @Resource
    ExecutorService executor;

    File serverLog;
    Tailer tail;

    public void init() {
        String logPath = System.getProperty("jboss.server.log.dir") + "/server.log";
        serverLog = new File(logPath);
        System.out.println("++ Reading " + logPath);

        tail = new Tailer(serverLog, new TailerListenerAdapter() {
            @Override
            public void handle(String line) {
            	System.out.println("HE");
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
