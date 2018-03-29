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
    
    /* (non-Javadoc)
	 * @see de.dplatz.jdbctrace.business.control.wildfly10.AppServerPlugin#isEligible()
	 */
    @Override
	public boolean isEligible() {
    	String logPath = System.getProperty("jboss.server.log.dir");
    	return logPath != null;
    }
    
	/* (non-Javadoc)
	 * @see de.dplatz.jdbctrace.business.control.wildfly10.AppServerPlugin#findAllDatasources()
	 */
	@Override
	public List<JDBCDatasource> findAllDatasources() {
		return datasources.findAll();
	}
	
	/* (non-Javadoc)
	 * @see de.dplatz.jdbctrace.business.control.wildfly10.AppServerPlugin#updateDatasource(de.dplatz.jdbctrace.business.entity.JDBCDatasource)
	 */
	@Override
	public void updateDatasource(JDBCDatasource ds) {
		datasources.persist(ds);
	}
    
    /* (non-Javadoc)
	 * @see de.dplatz.jdbctrace.business.control.wildfly10.AppServerPlugin#generateTestData()
	 */
    @Override
	public void generateTestData() {
    	tester.generateTestData();
    }
    
    /* (non-Javadoc)
	 * @see de.dplatz.jdbctrace.business.control.wildfly10.AppServerPlugin#enableLogging(boolean)
	 */
    @Override
	public void enableLogging(boolean flag) {
        try {
			logging.setTrace(flag);
		} catch (Exception e) {
			e.printStackTrace();
		}
    }

    /* (non-Javadoc)
	 * @see de.dplatz.jdbctrace.business.control.wildfly10.AppServerPlugin#startup()
	 */
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

    /* (non-Javadoc)
	 * @see de.dplatz.jdbctrace.business.control.wildfly10.AppServerPlugin#teardown()
	 */
    @Override
	public void teardown() {
        System.out.println("++ Shutting down...");
        tail.stop();
    }
}
