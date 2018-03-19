package de.dplatz.jdbctrace.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;
import javax.management.MBeanServerConnection;
import javax.management.ObjectInstance;
import javax.management.ObjectName;

import de.dplatz.jdbctrace.control.ServerConnector;

public class Datasources {
	
	@Inject
	MBeanServerConnection connection;
		
    @Inject
    ServerConnector connector;
	
    public List<Datasource> getAll() {
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
    

    
}
