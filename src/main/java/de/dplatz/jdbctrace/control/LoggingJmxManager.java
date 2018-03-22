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
import javax.management.openmbean.CompositeData;

@Stateless
public class LoggingJmxManager {

    @Inject
    MBeanServerConnection connection;

    @Inject
    JmxManager connector;

    public Optional<ObjectInstance> findSpyLogger() throws Exception {
        //ObjectName mBeanName = new ObjectName("jboss.as:subsystem=logging,logger=jboss.jdbc.spy");

        Set<ObjectInstance> spyLogger = connection.queryMBeans(new ObjectName("jboss.as:subsystem=logging,logger=jboss.jdbc.spy"), null);

        return spyLogger.stream().findAny();

    }

    public void createSpyLogger() throws Exception {
        //ObjectName mBeanName = new ObjectName("jboss.as:subsystem=logging,logger=jboss.jdbc.spy");


        connection.invoke(new ObjectName("jboss.as:subsystem=logging"), "addLogger",
                new Object[] { "jboss.jdbc.spy", null, null, null, null, "TRACE", null },
                new String[] { String.class.getName(), String.class.getName(), CompositeData.class.getName(),
                        String.class.getName(), String[].class.getName(), String.class.getName(),
                        boolean.class.getName() });


    }

    public void setTrace(boolean on) throws Exception {
        //findSpyLogger().orElse(this::createSpyLogger)
        if (!findSpyLogger().isPresent()) {
            System.out.println("CREATE");
            this.createSpyLogger();
        }
        ObjectName spy = new ObjectName("jboss.as:subsystem=logging,logger=jboss.jdbc.spy");
        AttributeList attrs = new AttributeList();
        attrs.add(new Attribute("level", on ? "TRACE" : "OFF"));
        connection.setAttributes(spy, attrs);
    }

}
