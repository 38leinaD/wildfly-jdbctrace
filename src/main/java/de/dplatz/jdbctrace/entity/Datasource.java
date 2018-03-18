package de.dplatz.jdbctrace.entity;

import java.io.IOException;

import javax.management.AttributeNotFoundException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanException;
import javax.management.MBeanServerConnection;
import javax.management.ObjectInstance;
import javax.management.ReflectionException;

/**
 * Datasource
 */
public class Datasource {

    MBeanServerConnection connection;
    ObjectInstance object;

    public Datasource(ObjectInstance object, MBeanServerConnection connection) {
        this.object = object;
        this.connection = connection;
    }

    public String getName() {
        return object.getObjectName().getCanonicalName();
    }

    public boolean getSpy() {
        try {
            Object obj = connection.getAttribute(object.getObjectName(), "spy");
            System.out.println(obj);
            return false;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

    }
}