package de.dplatz.jdbctrace.control;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.management.Attribute;
import javax.management.MBeanServerConnection;
import javax.management.ObjectInstance;
import javax.management.ObjectName;

import de.dplatz.jdbctrace.entity.Datasource;

@Stateless
public class DatasourcesJmxManager {
	
	@Inject
	MBeanServerConnection connection;
		
    @Inject
    JmxManager connector;
	
    public List<Datasource> findAll() {
        //ObjectName mBeanName = new ObjectName("jboss.as:subsystem=logging,logger=jboss.jdbc.spy");
        try {
            ObjectName datasourceMBean = new ObjectName("jboss.as:subsystem=datasources,xa-data-source=*");
            Set<ObjectInstance> ds = connection.queryMBeans(datasourceMBean, null);

            List<Datasource> datasources = new ArrayList<>();

            ds.forEach(o -> datasources.add(new Datasource(o, (Boolean)getAttribute(o, "spy"))));

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
    
    public void persist(Datasource ds) {
    	if (!ds.isValid()) {
    		setAttribute(ds.getObject(), "spy", ds.isSpyingEnabled());
    		connector.setRestartRequired();
    	}
    }

    
}
