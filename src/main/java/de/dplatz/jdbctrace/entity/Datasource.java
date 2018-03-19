package de.dplatz.jdbctrace.entity;

import javax.management.Attribute;
import javax.management.MBeanServerConnection;
import javax.management.ObjectInstance;

public class Datasource {

    ObjectInstance object;
    MBeanServerConnection connection;
    public Datasource(ObjectInstance object, MBeanServerConnection connection) {
        this.object = object;
        this.connection = connection;
    }

    public String getName() {
        return object.getObjectName().getCanonicalName();
    }
    
    public boolean isSpyingEnabled() {
    	try {
            Object obj = connection.getAttribute(object.getObjectName(), "spy");
            return (Boolean)obj;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
    }
    
    public void setSpyingEnabled(boolean value) {
    	try {
			connection.setAttribute(object.getObjectName(), new Attribute("spy", true));
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
    }
}