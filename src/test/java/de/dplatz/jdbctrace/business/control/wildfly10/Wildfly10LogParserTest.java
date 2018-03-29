package de.dplatz.jdbctrace.business.control.wildfly10;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import static org.hamcrest.CoreMatchers.is;

import static org.junit.Assert.assertThat;

import javax.enterprise.event.Event;

import de.dplatz.jdbctrace.business.control.wildfly10.Wildfly10LogParser;
import de.dplatz.jdbctrace.business.entity.JDBCStatement;

public class Wildfly10LogParserTest {
	Wildfly10LogParser cut;
    
    @Before
    public void init() {
    	cut = new Wildfly10LogParser();
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

    @Test
    public void extractTimestampSetter() {
    	JDBCStatement stmt = new JDBCStatement();
    	cut.extractTimestampSetter(stmt, "setTimestamp(7, 2018-03-22 18:44:19.692, java.util.GregorianCalendar[time=1521740059694,areFieldsSet=true,areAllFieldsSet=true,lenient=true,zone=sun.util.calendar.ZoneInfo[id=\"UTC\",offset=0,dstSavings=0,useDaylight=false,transitions=0,lastRule=null],firstDayOfWeek=1,minimalDaysInFirstWeek=1,ERA=1,YEAR=2018,MONTH=2,WEEK_OF_YEAR=12,WEEK_OF_MONTH=4,DAY_OF_MONTH=22,DAY_OF_YEAR=81,DAY_OF_WEEK=5,DAY_OF_WEEK_IN_MONTH=4,AM_PM=1,HOUR=5,HOUR_OF_DAY=17,MINUTE=34,SECOND=19,MILLISECOND=694,ZONE_OFFSET=0,DST_OFFSET=0])");
		assertThat(stmt.getParams().get(7), is("2018-03-22 18:44:19.692"));
    }

    @Test
    public void extractStringSetter() {
    	JDBCStatement stmt = new JDBCStatement();
    	cut.extractStringSetter(stmt, "setString(1, com.myproclassic.server.internodecomm.entities.PCEInterNodeListener)");
		assertThat(stmt.getParams().get(1), is("com.myproclassic.server.internodecomm.entities.PCEInterNodeListener"));
    }

    @SuppressWarnings("unchecked")
	@Test
    public void extractFullEvent() {

        String[] jdbcTraceData = {
			"2018-03-22 18:34:19,694 DEBUG [jboss.jdbc.spy] (MyThread) java:/MY_DS [Connection] prepareStatement(UPDATE MYTABLE SET VALIDTO = ?, OWNER = ?, PREVIOUSDATA = DATA, DATA = ? WHERE PROCESSINGCENTER = ? AND CATEGORY = ? AND ID = ? AND (VALIDTO < ? AND VALIDTO < ? OR OWNER = ?))",
			"2018-03-22 18:34:19,694 DEBUG [jboss.jdbc.spy] (MyThread) java:/MY_DS [PreparedStatement] setTimestamp(1, 2018-03-22 18:44:19.692, java.util.GregorianCalendar[time=1521740059694,areFieldsSet=true,areAllFieldsSet=true,lenient=true,zone=sun.util.calendar.ZoneInfo[id=\"UTC\",offset=0,dstSavings=0,useDaylight=false,transitions=0,lastRule=null],firstDayOfWeek=1,minimalDaysInFirstWeek=1,ERA=1,YEAR=2018,MONTH=2,WEEK_OF_YEAR=12,WEEK_OF_MONTH=4,DAY_OF_MONTH=22,DAY_OF_YEAR=81,DAY_OF_WEEK=5,DAY_OF_WEEK_IN_MONTH=4,AM_PM=1,HOUR=5,HOUR_OF_DAY=17,MINUTE=34,SECOND=19,MILLISECOND=694,ZONE_OFFSET=0,DST_OFFSET=0])",
			"2018-03-22 18:34:19,694 DEBUG [jboss.jdbc.spy] (MyThread) java:/MY_DS [PreparedStatement] setObject(2, Alpha, 12)",
			"2018-03-22 18:34:19,694 DEBUG [jboss.jdbc.spy] (MyThread) java:/MY_DS [PreparedStatement] setObject(3, {\"cat\":\"Default\",\"#\":\"0\",\"msec\":\"1\"}, 12)",
			"2018-03-22 18:34:19,694 DEBUG [jboss.jdbc.spy] (MyThread) java:/MY_DS [PreparedStatement] setObject(4, ALPHA, 12)",
			"2018-03-22 18:34:19,694 DEBUG [jboss.jdbc.spy] (MyThread) java:/MY_DS [PreparedStatement] setObject(5, Chunk, 12)",
			"2018-03-22 18:34:19,694 DEBUG [jboss.jdbc.spy] (MyThread) java:/MY_DS [PreparedStatement] setObject(6, Default, 12)",
			"2018-03-22 18:34:19,694 DEBUG [jboss.jdbc.spy] (MyThread) java:/MY_DS [PreparedStatement] setTimestamp(7, 2018-03-22 18:44:19.692, java.util.GregorianCalendar[time=1521740059694,areFieldsSet=true,areAllFieldsSet=true,lenient=true,zone=sun.util.calendar.ZoneInfo[id=\"UTC\",offset=0,dstSavings=0,useDaylight=false,transitions=0,lastRule=null],firstDayOfWeek=1,minimalDaysInFirstWeek=1,ERA=1,YEAR=2018,MONTH=2,WEEK_OF_YEAR=12,WEEK_OF_MONTH=4,DAY_OF_MONTH=22,DAY_OF_YEAR=81,DAY_OF_WEEK=5,DAY_OF_WEEK_IN_MONTH=4,AM_PM=1,HOUR=5,HOUR_OF_DAY=17,MINUTE=34,SECOND=19,MILLISECOND=694,ZONE_OFFSET=0,DST_OFFSET=0])",
			"2018-03-22 18:34:19,694 DEBUG [jboss.jdbc.spy] (MyThread) java:/MY_DS [PreparedStatement] setTimestamp(8, 2018-03-22 18:44:19.692, java.util.GregorianCalendar[time=1521740059694,areFieldsSet=true,areAllFieldsSet=true,lenient=true,zone=sun.util.calendar.ZoneInfo[id=\"UTC\",offset=0,dstSavings=0,useDaylight=false,transitions=0,lastRule=null],firstDayOfWeek=1,minimalDaysInFirstWeek=1,ERA=1,YEAR=2018,MONTH=2,WEEK_OF_YEAR=12,WEEK_OF_MONTH=4,DAY_OF_MONTH=22,DAY_OF_YEAR=81,DAY_OF_WEEK=5,DAY_OF_WEEK_IN_MONTH=4,AM_PM=1,HOUR=5,HOUR_OF_DAY=17,MINUTE=34,SECOND=19,MILLISECOND=694,ZONE_OFFSET=0,DST_OFFSET=0])",
			"2018-03-22 18:34:19,694 DEBUG [jboss.jdbc.spy] (MyThread) java:/MY_DS [PreparedStatement] setObject(9, Zheta, 12)",
			"2018-03-22 18:34:19,694 DEBUG [jboss.jdbc.spy] (MyThread) java:/MY_DS [PreparedStatement] setQueryTimeout(120)",
			"2018-03-22 18:34:19,694 DEBUG [jboss.jdbc.spy] (MyThread) java:/MY_DS [PreparedStatement] executeUpdate()",
			"2018-03-22 18:34:19,695 DEBUG [jboss.jdbc.spy] (MyThread) java:/MY_DS [PreparedStatement] close()"
	    };

        cut.event = Mockito.mock(Event.class);

        ArgumentCaptor<JDBCStatement> capturedStatement = ArgumentCaptor.forClass(JDBCStatement.class);

        for (int i=0; i<jdbcTraceData.length; i++) {
            cut.handleLine(jdbcTraceData[i]);
        }

        Mockito.verify(cut.event).fire(capturedStatement.capture());
        JDBCStatement stmt = capturedStatement.getValue();
        assertThat(stmt.resolvedStatement(), is("UPDATE MYTABLE SET VALIDTO = '2018-03-22 18:44:19.692', OWNER = 'Alpha', PREVIOUSDATA = DATA, DATA = '{\"cat\":\"Default\",\"#\":\"0\",\"msec\":\"1\"}' WHERE PROCESSINGCENTER = 'ALPHA' AND CATEGORY = 'Chunk' AND ID = 'Default' AND (VALIDTO < '2018-03-22 18:44:19.692' AND VALIDTO < '2018-03-22 18:44:19.692' OR OWNER = 'Zheta')"));
    }
}