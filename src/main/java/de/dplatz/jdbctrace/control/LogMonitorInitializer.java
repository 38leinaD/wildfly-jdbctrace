package de.dplatz.jdbctrace.control;

import java.io.File;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.enterprise.concurrent.ManagedExecutorService;
import javax.inject.Inject;

import org.apache.commons.io.input.Tailer;
import org.apache.commons.io.input.TailerListenerAdapter;

/**
 * LogParser
 */
@Singleton
@Startup
public class LogMonitorInitializer {

    @Inject
    LogExtractor extractor;

    @Resource
    ManagedExecutorService executor;

    File serverLog;
    Tailer tail;

    @PostConstruct
    public void init() {
        String logPath = System.getProperty("jboss.server.log.dir") + "/server.log";
        serverLog = new File(logPath);
        System.out.println("++ Reading " + logPath);

        tail = new Tailer(serverLog, new TailerListenerAdapter() {
            @Override
            public void handle(String line) {
                extractor.handleLine(line);
            }
        }, 100, true);

        executor.execute(tail);
    }

    @PreDestroy
    public void teardown() {
        System.out.println("++ Shutting down...");
        tail.stop();
    }


}