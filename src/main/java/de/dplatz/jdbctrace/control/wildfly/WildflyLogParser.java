package de.dplatz.jdbctrace.control.wildfly;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.enterprise.event.Event;
import javax.inject.Inject;

import de.dplatz.jdbctrace.entity.JDBCStatement;

/**
 * LogExtractor
 */
public class WildflyLogParser {

    Map<String, JDBCStatement> statementsPerThread = new HashMap<>();

    Pattern pattern = Pattern.compile("^(.*)\\sDEBUG\\s\\[(.+)\\]\\s\\((.+)\\)\\s(.+)\\s\\[.+\\]\\s(.+)$");
    
    @Inject
    Event<JDBCStatement> event;
    
    public void handleLine(String line) {
    	parseLine(line);
    }
    
    void parseLine(String line) {
        if (!line.contains("[jboss.jdbc.spy]")) return;
        
        Matcher matcher = pattern.matcher(line);
        if (!matcher.find()) return;
        
        String timestamp = matcher.group(1);
    	String thread = matcher.group(3);
    	String datasource = matcher.group(4);
    	String command = matcher.group(5);
        	
    	JDBCStatement stmt = statementsPerThread.get(thread);
    	
    	if (isStatementStart(command)) {
    		event.fire(stmt);
    		
    		stmt = new JDBCStatement();
    		stmt.setTimestamp(timestamp);
    		stmt.setThread(thread);
    		stmt.setDatasource(datasource);
    		
    		extractStatement(stmt, command);
    		
    		statementsPerThread.put(thread, stmt);
    	}
    	else if (isStatementEnd(command)) {
    		event.fire(stmt);
    		statementsPerThread.remove(thread);
    	}
    	else if (isStatementObjectParameter(command)) {
    		extractObjectSetter(stmt, command);
		}
    }
    
    void extractStatement(JDBCStatement statement, String command) {
    	statement.setStatement(command.substring("prepareStatement(".length(), command.length() - 1));
    }
    
    void extractObjectSetter(JDBCStatement statement, String command) {
    	// "setObject(2, Alpha.Cluster.dev-vm, 12)"
    	int bracket = command.indexOf("(");
    	int start = command.indexOf(",");
    	int end = command.lastIndexOf(",");
    	statement.setParam(Integer.parseInt(command.substring(bracket+1, start)), command.substring(start+2, end));
    }
    
    boolean isStatementStart(String command) {
    	return command.startsWith("prepareStatement(");
    }
    
    boolean isStatementEnd(String command) {
    	return command.startsWith("executeUpdate(");
    }
    
    boolean isStatementObjectParameter(String command) {
    	return command.startsWith("setObject(")
    			|| command.startsWith("setTimestamp(");
    }
}