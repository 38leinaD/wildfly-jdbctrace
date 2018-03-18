package de.dplatz.jdbctrace.control;

import java.util.List;

import org.junit.Test;

import de.dplatz.jdbctrace.entity.Datasource;

public class ServerConnectorTest {

    @Test
    public void queryDatasources() throws Exception {
        System.out.println("Starting lookup ...");

        ServerConnector connector = new ServerConnector();
        connector.connect();

        List<Datasource> datasources = connector.getDatasources();

        datasources.forEach(ds -> {
            System.out.println(ds.getName() + ": " + ds.getSpy());
        });
    }

}