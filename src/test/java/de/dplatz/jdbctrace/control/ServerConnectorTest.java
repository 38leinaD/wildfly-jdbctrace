package de.dplatz.jdbctrace.control;

import org.junit.Test;

public class ServerConnectorTest {

    @Test
    public void queryDatasources() throws Exception {
        System.out.println("Starting lookup ...");

        JmxManager connector = new JmxManager();
        connector.connect();

        /*List<Datasource> datasources = connector.ge;

        datasources.forEach(ds -> {
            System.out.println(ds.getName() + ": " + ds.getName());
        });*/
    }

}