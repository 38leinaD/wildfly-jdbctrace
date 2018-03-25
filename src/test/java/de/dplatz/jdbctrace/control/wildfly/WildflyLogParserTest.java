package de.dplatz.jdbctrace.control.wildfly;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;

import static org.junit.Assert.assertThat;

import de.dplatz.jdbctrace.entity.JDBCStatement;

public class WildflyLogParserTest {
	WildflyLogParser cut;
    
    @Before
    public void init() {
    	cut = new WildflyLogParser();
    }
    
    @Test
    public void extractStatement() {
    	JDBCStatement stmt = new JDBCStatement();
        cut.extractStatement(stmt, "prepareStatement(select REPROCESSID, PROCESSINGCENTER, LEASEID, CATEGORY, ACTION, NODEID, TRANSACTIONTYPE, TRANSACTIONDATE, TRANSACTIONNUMBER, SESSIONID, CLIENTID, THREADID, STATE, TRYCOUNTER, VERSION, SEQ, MODULE, REFERENCEID, REASON, MODIFICATIONDATE, SCHEDULEDATE, DATA, NODEIDHIERARCHYDEPTH1, NODEIDHIERARCHYDEPTH2, NODEIDHIERARCHYDEPTH3, ACQUIRERID from REPROCESS where state = 'active' and scheduledate <= ? and processingcenter = ? and leaseid = ?)");
        
        assertThat(stmt.getStatement(), is("select REPROCESSID, PROCESSINGCENTER, LEASEID, CATEGORY, ACTION, NODEID, TRANSACTIONTYPE, TRANSACTIONDATE, TRANSACTIONNUMBER, SESSIONID, CLIENTID, THREADID, STATE, TRYCOUNTER, VERSION, SEQ, MODULE, REFERENCEID, REASON, MODIFICATIONDATE, SCHEDULEDATE, DATA, NODEIDHIERARCHYDEPTH1, NODEIDHIERARCHYDEPTH2, NODEIDHIERARCHYDEPTH3, ACQUIRERID from REPROCESS where state = 'active' and scheduledate <= ? and processingcenter = ? and leaseid = ?"));
    }
    
    @Test
    public void extractObjectSetter() {
    	JDBCStatement stmt = new JDBCStatement();
    	cut.extractObjectSetter(stmt, "setObject(2, Alpha.Cluster.dev-vm, 12)");
		assertThat(stmt.getParams().get(2), is("Alpha.Cluster.dev-vm"));
    }
}