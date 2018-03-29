package de.dplatz.jdbctrace.control.wildfly;

import java.lang.management.ManagementFactory;

import javax.ejb.Singleton;
import javax.enterprise.inject.Produces;
import javax.management.MBeanServerConnection;

@Singleton
public class WildflyJmxExposer {

    @Produces
    public MBeanServerConnection getConnection() {
    	return ManagementFactory.getPlatformMBeanServer();
    }
}