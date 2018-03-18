package de.dplatz.jdbctrace.control;

import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.Singleton;
import javax.ejb.Stateless;
import javax.management.Attribute;
import javax.management.MBeanServerConnection;
import javax.management.ObjectInstance;
import javax.management.ObjectName;

import de.dplatz.jdbctrace.entity.Datasource;


@Singleton
@ConcurrencyManagement(ConcurrencyManagementType.CONTAINER)
public class ServerConnector {

    MBeanServerConnection connection;

    public ServerConnector() {
    	connection = ManagementFactory.getPlatformMBeanServer();
    }

    public void connect() throws Exception {
        /*String urlString = System.getProperty("jmx.service.url", "service:jmx:remote+http://localhost:9990");
        JMXServiceURL serviceURL = new JMXServiceURL(urlString);
        JMXConnector jmxConnector = JMXConnectorFactory.connect(serviceURL, null);
        connection = jmxConnector.getMBeanServerConnection();*/
    	
    	//connection = ManagementFactory.getPlatformMBeanServer();
    }

    public List<Datasource> getDatasources() {
        if (connection == null) throw new IllegalStateException("No open MBeanServerConnection.");
        //ObjectName mBeanName = new ObjectName("jboss.as:subsystem=logging,logger=jboss.jdbc.spy");
        try {
            ObjectName datasourceMBean = new ObjectName("jboss.as:subsystem=datasources,xa-data-source=*");
            Set<ObjectInstance> ds = connection.queryMBeans(datasourceMBean, null);

            List<Datasource> datasources = new ArrayList<>();

            ds.forEach(o -> datasources.add(new Datasource(o, connection)));

            return datasources;
            
        } catch (Exception e) {
			throw new RuntimeException(e);
		}
    }

    public void traceJDBC(boolean enabled) throws Exception {
        ObjectName mBeanName = new ObjectName("jboss.as:subsystem=logging,logger=jboss.jdbc.spy");
        Object attrVal = connection.getAttribute(mBeanName, "level");
        System.out.println("Value via JMX: " + attrVal);
        connection.setAttribute(mBeanName, new Attribute("level", "OFF"));
    }
}