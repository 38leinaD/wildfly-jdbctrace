package de.dplatz.jdbctrace.business.control;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import de.dplatz.jdbctrace.business.entity.RestartRequired;

@Singleton
@Startup
public class JDBCTraceApplication {

    boolean restartRequired = false;

    @Inject
    Instance<AppServerPlugin> plugins;

    AppServerPlugin appServerPlugin;
    
    @PostConstruct
    public void init() {
    	if (plugins.isUnsatisfied()) throw new RuntimeException("No plugin found for this application-server.");
    	for (AppServerPlugin plugin : plugins) {
    		boolean eligeble = plugin.isEligible();
    		System.out.println("++ " + plugin.getClass().getName() + "? " + eligeble);
    		if (eligeble) {
    			appServerPlugin = plugin;
    			break;
    		}
    	}
    	appServerPlugin.startup();
    }

    @PreDestroy
    public void teardown() {
        System.out.println("++ Shutting down...");
        appServerPlugin.teardown();
    }

    public void onRestartRequiredEvent(@Observes RestartRequired restartRequired) {
        this.restartRequired = true;
    }

    public boolean isRestartRequired() {
        return restartRequired;
    }
    
    public AppServerPlugin getPlugin() {
    	return appServerPlugin;
    }

}