package de.dplatz.jdbctrace.business.control.wildfly10;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.management.Attribute;
import javax.management.MBeanServerConnection;
import javax.management.ObjectInstance;
import javax.management.ObjectName;

import de.dplatz.jdbctrace.business.entity.JDBCDatasource;
import de.dplatz.jdbctrace.business.entity.RestartRequired;

@ApplicationScoped
class Wildfly10Datasources {
	
	@Inject
	MBeanServerConnection connection;
		
    @Inject
    Event<RestartRequired> restartEvent;
    
    public List<JDBCDatasource> findAll() {
        try {
            ObjectName datasourceMBean = new ObjectName("jboss.as:subsystem=datasources,xa-data-source=*");
            Set<ObjectInstance> ds = connection.queryMBeans(datasourceMBean, null);

            List<JDBCDatasource> datasources = new ArrayList<>();

            ds.forEach(o -> datasources.add(new JDBCDatasource(o, (Boolean)getAttribute(o, "spy"))));

            return datasources;
            
        } catch (Exception e) {
			throw new RuntimeException(e);	
		}
    }
    
    Object getAttribute(ObjectInstance object, String attr) {
    	try {
            Object obj = connection.getAttribute(object.getObjectName(), "spy");
            return (Boolean)obj;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
    }
    
    void setAttribute(ObjectInstance object, String attr, Object value) {
    	try {
			connection.setAttribute(object.getObjectName(), new Attribute(attr, value));
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
    }
    
    public void persist(JDBCDatasource ds) {
    	if (!ds.isValid()) {
    		setAttribute(ds.getObjectInstance(), "spy", ds.isSpyingEnabled());
    	    restartEvent.fire(new RestartRequired());
    	}
    }
}
