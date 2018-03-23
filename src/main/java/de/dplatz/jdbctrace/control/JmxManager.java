package de.dplatz.jdbctrace.control;

import java.lang.management.ManagementFactory;

import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.Singleton;
import javax.enterprise.inject.Produces;
import javax.management.MBeanServerConnection;


@Singleton
@ConcurrencyManagement(ConcurrencyManagementType.CONTAINER)
public class JmxManager {

    boolean restartRequired = false;
    
    public JmxManager() {
    }

    public void connect() throws Exception {
        /*String urlString = System.getProperty("jmx.service.url", "service:jmx:remote+http://localhost:9990");
        JMXServiceURL serviceURL = new JMXServiceURL(urlString);
        JMXConnector jmxConnector = JMXConnectorFactory.connect(serviceURL, null);
        connection = jmxConnector.getMBeanServerConnection();*/
    	
    	//connection = ManagementFactory.getPlatformMBeanServer();
    }


    
    public boolean isRestartRequired() {
    	return this.restartRequired;
    }
    
    public void setRestartRequired() {
    	this.restartRequired = true;
    }
    
    @Produces
    public MBeanServerConnection getConnection() {
    	return ManagementFactory.getPlatformMBeanServer();
    }
       
/*
    public void traceJDBC(boolean enabled) throws Exception {
        ObjectName mBeanName = new ObjectName("jboss.as:subsystem=logging,logger=jboss.jdbc.spy");
        Object attrVal = connection.getAttribute(mBeanName, "level");
        System.out.println("Value via JMX: " + attrVal);
        connection.setAttribute(mBeanName, new Attribute("level", "OFF"));
    }*/
}