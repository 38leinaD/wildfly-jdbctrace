package de.dplatz.jdbctrace.control;

import java.util.Optional;
import java.util.Set;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.MBeanServerConnection;
import javax.management.ObjectInstance;
import javax.management.ObjectName;

@Stateless
public class LoggingJmxManager {

	@Inject
	MBeanServerConnection connection;
		
    @Inject
    JmxManager connector;
    
    public Optional<ObjectInstance> findSpyLogger() {
        //ObjectName mBeanName = new ObjectName("jboss.as:subsystem=logging,logger=jboss.jdbc.spy");
        try {
            ObjectName datasourceMBean = new ObjectName("jboss.as:subsystem=logging,logger=*");
            Set<ObjectInstance> ds = connection.queryMBeans(datasourceMBean, null);

            return ds.stream()
            	.filter(o -> o.getObjectName().getCanonicalName().endsWith("jboss.jdbc.spy"))
            	.findFirst();
            
        } catch (Exception e) {
			throw new RuntimeException(e);	
		}
    }
    
    public void createLogger() {
    	
    }
    
    public void setTrace(boolean on) {
    	try {
    		//findSpyLogger().orElse(connection.setAttributes(name, attributes)))
    		ObjectName spy = new ObjectName("jboss.as:subsystem=logging,logger=jboss.jdbc.spy");
    		AttributeList attrs = new AttributeList();
    		attrs.add(new Attribute("level", on ? "TRACE" : "OFF"));
    		connection.setAttributes(spy, attrs);
    	} catch (Exception e) {
			throw new RuntimeException(e);	
		}
    }
    		
}
