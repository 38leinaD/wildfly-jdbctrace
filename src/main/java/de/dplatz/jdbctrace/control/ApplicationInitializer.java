package de.dplatz.jdbctrace.control;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import de.dplatz.jdbctrace.control.wildfly.WildflySupport;
import de.dplatz.jdbctrace.entity.RestartRequired;

@Singleton
@Startup
public class ApplicationInitializer {

    boolean restartRequired = false;

    @Inject
    Instance<WildflySupport> support;

    @PostConstruct
    public void init() {
    	support.get().init();
    }

    @PreDestroy
    public void teardown() {
        System.out.println("++ Shutting down...");
        support.get().teardown();
    }

    public void onRestartRequiredEvent(@Observes RestartRequired restartRequired) {
        this.restartRequired = true;
    }

    public boolean isRestartRequired() {
        return restartRequired;
    }

}