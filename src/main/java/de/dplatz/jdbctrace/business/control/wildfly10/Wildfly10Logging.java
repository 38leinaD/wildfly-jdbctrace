package de.dplatz.jdbctrace.business.control.wildfly10;

import java.util.Optional;
import java.util.Set;

import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.MBeanServerConnection;
import javax.management.ObjectInstance;
import javax.management.ObjectName;
import javax.management.openmbean.CompositeData;

import de.dplatz.jdbctrace.business.entity.RestartRequired;


@Stateless
class Wildfly10Logging {

    @Inject
    MBeanServerConnection connection;

    @Inject
    Event<RestartRequired> restartEvent;
    
    public Optional<ObjectInstance> findSpyLogger() throws Exception {
        Set<ObjectInstance> spyLogger = connection.queryMBeans(new ObjectName("jboss.as:subsystem=logging,logger=jboss.jdbc.spy"), null);

        return spyLogger.stream().findAny();

    }

    public void createSpyLogger() throws Exception {
        connection.invoke(new ObjectName("jboss.as:subsystem=logging"), "addLogger",
                new Object[] { "jboss.jdbc.spy", null, null, null, null, "TRACE", null },
                new String[] { String.class.getName(), String.class.getName(), CompositeData.class.getName(),
                        String.class.getName(), String[].class.getName(), String.class.getName(),
                        boolean.class.getName() });


    }

    void setTrace(boolean on) throws Exception {
        if (!findSpyLogger().isPresent()) {
            this.createSpyLogger();
            restartEvent.fire(new RestartRequired());
        }
        ObjectName spy = new ObjectName("jboss.as:subsystem=logging,logger=jboss.jdbc.spy");
        AttributeList attrs = new AttributeList();
        attrs.add(new Attribute("level", on ? "TRACE" : "OFF"));
        connection.setAttributes(spy, attrs);
    }


    public boolean isTrace() throws Exception {
        ObjectName spy = new ObjectName("jboss.as:subsystem=logging,logger=jboss.jdbc.spy");
        String level = (String) connection.getAttribute(spy, "level");
        return level.equals("TRACE");
    }
}
