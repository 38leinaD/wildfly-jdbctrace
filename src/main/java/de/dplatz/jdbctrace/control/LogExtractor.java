package de.dplatz.jdbctrace.control;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.enterprise.event.Event;
import javax.inject.Inject;

import de.dplatz.jdbctrace.entity.JDBCEvent;
import de.dplatz.jdbctrace.entity.JDBCTraceItem;

/**
 * LogExtractor
 */
public class LogExtractor {

    Map<String, List<JDBCTraceItem>> threadBuffer = new HashMap<>();

    @Inject
    Event<JDBCEvent> event;
    
    public void handleLine(String line) {
        Optional<JDBCTraceItem> optionalTraceLine = parseLine(line);
            if (optionalTraceLine.isPresent()) {
                JDBCTraceItem traceLine = optionalTraceLine.get();
                threadBuffer.putIfAbsent(traceLine.getThread(), new LinkedList<>());
                threadBuffer.get(traceLine.getThread()).add(traceLine);


                if (traceLine.getCommand().startsWith("execute")) {
                    List<JDBCTraceItem> thread = threadBuffer.get(traceLine.getThread());
                    String command = null;
                    for (JDBCTraceItem item : thread) {
                        if (item.getCommand().startsWith("prepareStatement(")) {
                            command = item.getCommand().substring(17, item.getCommand().length() - 1);
                            //listener.accept(new JDBCEvent(command));
                            event.fire(new JDBCEvent(command));
                        }
/*
                        if (command != null && item.getCommand().startsWith("setObject")) {
                            int index = item.getCommand().substring(beginIndex, endIndex)
                        }*/
                    }
                }
            }
    }

    Optional<JDBCTraceItem> parseLine(String line) {
        if (!line.contains("[jboss.jdbc.spy]")) return Optional.empty();
        
        Pattern pattern = Pattern.compile("^(.*)\\sDEBUG\\s\\[(.+)\\]\\s\\((.+)\\)\\s(.+)\\s\\[.+\\]\\s(.+)$");
        Matcher matcher = pattern.matcher(line);
        if (matcher.find()) {
            JDBCTraceItem item = new JDBCTraceItem(matcher.group(1), matcher.group(3), matcher.group(4), matcher.group(5));
            return Optional.of(item);
        }
        
        return Optional.empty();
    }
}