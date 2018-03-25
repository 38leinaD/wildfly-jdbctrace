package de.dplatz.jdbctrace.entity;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * JDBCEvent
 */
public class JDBCStatement {

	String timestamp;
	String thread;
	String datasource;
    String statement;

    Map<Integer, String> params = new HashMap<>();
    
    public JDBCStatement() {

	}
    
	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public String getThread() {
		return thread;
	}

	public void setThread(String thread) {
		this.thread = thread;
	}

	public String getDatasource() {
		return datasource;
	}

	public void setDatasource(String datasource) {
		this.datasource = datasource;
	}

	public String getStatement() {
		return statement;
	}

	public void setStatement(String statement) {
		this.statement = statement;
	}
	
	public void setParam(int index, String param) {
		params.put(index, param);
	}
	
	public Map<Integer, String> getParams() {
		return params;
	}
	
	public String resolvedStatement() {
		String resolvedStatement = statement;
		for (Entry<Integer, String> entry : params.entrySet()) {
			resolvedStatement = resolveParam(resolvedStatement, entry.getKey(), entry.getValue());
		}
				
		return resolvedStatement;
	}
	
	String resolveParam(String statement, int index, String param) {
		int numIndices = 0;
		for (int i=0; i<statement.length(); i++) {
			char c = statement.charAt(i);
			if (c == '?') {
				numIndices++;
				
				if (numIndices == index) {
					return statement.substring(0, i) + "'" + param + "'" + statement.substring(i+1, statement.length());
				}
			}
		}
		
		throw new IllegalArgumentException(String.format("Index %s does not exist in '%s'", index, statement));
	}
	
	public String toString() {
		return statement;
	}

}