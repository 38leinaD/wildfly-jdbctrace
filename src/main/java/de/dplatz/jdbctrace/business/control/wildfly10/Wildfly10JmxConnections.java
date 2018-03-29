package de.dplatz.jdbctrace.business.control.wildfly10;

import java.lang.management.ManagementFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.management.MBeanServerConnection;

@ApplicationScoped
public class Wildfly10JmxConnections {

    @Produces
    public MBeanServerConnection getConnection() {
    	return ManagementFactory.getPlatformMBeanServer();
    }
}