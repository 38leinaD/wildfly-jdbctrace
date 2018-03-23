package de.dplatz.jdbctrace.control;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.enterprise.event.Event;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.mockito.Mockito;

import de.dplatz.jdbctrace.entity.JDBCEvent;
import de.dplatz.jdbctrace.entity.JDBCTraceItem;

/**
 * LogParserTest
 */
public class LogParserTest {

    @Rule
    public TemporaryFolder tmpFolder = new TemporaryFolder();
    File logFile;

    @Before
    public void init() throws IOException {
        logFile = tmpFolder.newFile("server.log");
    }

    String[] jdbcTraceData = {
        "2018-03-22 18:34:19,694 DEBUG [jboss.jdbc.spy] (ReprocessControl) java:/PCE_JDBC_TX_DATASOURCE [Connection] prepareStatement(UPDATE LEASE SET VALIDTO = ?, OWNER = ?, PREVIOUSDATA = DATA, DATA = ? WHERE PROCESSINGCENTER = ? AND CATEGORY = ? AND ID = ? AND (VALIDTO < ? AND VALIDTO < ? OR OWNER = ?))", 
        "2018-03-22 18:34:19,694 DEBUG [jboss.jdbc.spy] (ReprocessControl) java:/PCE_JDBC_TX_DATASOURCE [PreparedStatement] setTimestamp(1, 2018-03-22 18:44:19.692, java.util.GregorianCalendar[time=1521740059694,areFieldsSet=true,areAllFieldsSet=true,lenient=true,zone=sun.util.calendar.ZoneInfo[id=\"UTC\",offset=0,dstSavings=0,useDaylight=false,transitions=0,lastRule=null],firstDayOfWeek=1,minimalDaysInFirstWeek=1,ERA=1,YEAR=2018,MONTH=2,WEEK_OF_YEAR=12,WEEK_OF_MONTH=4,DAY_OF_MONTH=22,DAY_OF_YEAR=81,DAY_OF_WEEK=5,DAY_OF_WEEK_IN_MONTH=4,AM_PM=1,HOUR=5,HOUR_OF_DAY=17,MINUTE=34,SECOND=19,MILLISECOND=694,ZONE_OFFSET=0,DST_OFFSET=0])", 
        "2018-03-22 18:34:19,694 DEBUG [jboss.jdbc.spy] (ReprocessControl) java:/PCE_JDBC_TX_DATASOURCE [PreparedStatement] setObject(2, Alpha.Cluster.dev-vm, 12)", 
        "2018-03-22 18:34:19,694 DEBUG [jboss.jdbc.spy] (ReprocessControl) java:/PCE_JDBC_TX_DATASOURCE [PreparedStatement] setObject(3, {\"cat\":\"Default\",\"#\":\"0\",\"msec\":\"1\"}, 12)", 
        "2018-03-22 18:34:19,694 DEBUG [jboss.jdbc.spy] (ReprocessControl) java:/PCE_JDBC_TX_DATASOURCE [PreparedStatement] setObject(4, ALPHA, 12)", 
        "2018-03-22 18:34:19,694 DEBUG [jboss.jdbc.spy] (ReprocessControl) java:/PCE_JDBC_TX_DATASOURCE [PreparedStatement] setObject(5, Reprocess-Chunk, 12)", 
        "2018-03-22 18:34:19,694 DEBUG [jboss.jdbc.spy] (ReprocessControl) java:/PCE_JDBC_TX_DATASOURCE [PreparedStatement] setObject(6, Default/14, 12)", 
        "2018-03-22 18:34:19,694 DEBUG [jboss.jdbc.spy] (ReprocessControl) java:/PCE_JDBC_TX_DATASOURCE [PreparedStatement] setTimestamp(7, 2018-03-22 18:44:19.692, java.util.GregorianCalendar[time=1521740059694,areFieldsSet=true,areAllFieldsSet=true,lenient=true,zone=sun.util.calendar.ZoneInfo[id=\"UTC\",offset=0,dstSavings=0,useDaylight=false,transitions=0,lastRule=null],firstDayOfWeek=1,minimalDaysInFirstWeek=1,ERA=1,YEAR=2018,MONTH=2,WEEK_OF_YEAR=12,WEEK_OF_MONTH=4,DAY_OF_MONTH=22,DAY_OF_YEAR=81,DAY_OF_WEEK=5,DAY_OF_WEEK_IN_MONTH=4,AM_PM=1,HOUR=5,HOUR_OF_DAY=17,MINUTE=34,SECOND=19,MILLISECOND=694,ZONE_OFFSET=0,DST_OFFSET=0])", 
        "2018-03-22 18:34:19,694 DEBUG [jboss.jdbc.spy] (ReprocessControl) java:/PCE_JDBC_TX_DATASOURCE [PreparedStatement] setTimestamp(8, 2018-03-22 18:44:19.692, java.util.GregorianCalendar[time=1521740059694,areFieldsSet=true,areAllFieldsSet=true,lenient=true,zone=sun.util.calendar.ZoneInfo[id=\"UTC\",offset=0,dstSavings=0,useDaylight=false,transitions=0,lastRule=null],firstDayOfWeek=1,minimalDaysInFirstWeek=1,ERA=1,YEAR=2018,MONTH=2,WEEK_OF_YEAR=12,WEEK_OF_MONTH=4,DAY_OF_MONTH=22,DAY_OF_YEAR=81,DAY_OF_WEEK=5,DAY_OF_WEEK_IN_MONTH=4,AM_PM=1,HOUR=5,HOUR_OF_DAY=17,MINUTE=34,SECOND=19,MILLISECOND=694,ZONE_OFFSET=0,DST_OFFSET=0])", 
        "2018-03-22 18:34:19,694 DEBUG [jboss.jdbc.spy] (ReprocessControl) java:/PCE_JDBC_TX_DATASOURCE [PreparedStatement] setObject(9, Alpha.Cluster.dev-vm, 12)", 
        "2018-03-22 18:34:19,694 DEBUG [jboss.jdbc.spy] (ReprocessControl) java:/PCE_JDBC_TX_DATASOURCE [PreparedStatement] setQueryTimeout(120)", 
        "2018-03-22 18:34:19,694 DEBUG [jboss.jdbc.spy] (ReprocessControl) java:/PCE_JDBC_TX_DATASOURCE [PreparedStatement] executeUpdate()", 
        "2018-03-22 18:34:19,695 DEBUG [jboss.jdbc.spy] (ReprocessControl) java:/PCE_JDBC_TX_DATASOURCE [PreparedStatement] close()"
    };

    CountDownLatch latch = new CountDownLatch(2);
    JDBCEvent event;

    @Test(timeout=5000)
    @Ignore
    public void testParser() throws InterruptedException {
        new Thread(new Runnable() {

            @Override
            public void run() {
                System.out.println("++++");
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(logFile))){
                    for (int i = 0; i < jdbcTraceData.length; i++) {
                        writer.append(jdbcTraceData[i] + "\n");
                        writer.flush();
                        TimeUnit.MILLISECONDS.sleep(50);

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    System.out.println("DDDDDONE");
                    latch.countDown();
                }
            }
        }).start();

        LogMonitorInitializer parser = new LogMonitorInitializer();
        LogExtractor extractor = new LogExtractor();
        extractor.event = Mockito.mock(Event.class);
        parser.extractor = extractor;
        parser.executor = null;
        parser.serverLog = logFile;
        parser.init();
System.out.println("BEFORE");
        latch.await();
System.out.println("AFTER");
        assertThat(event, is(notNullValue()));
        assertTrue(event.getQuery().startsWith("UPDATE LEASE SET VALIDTO"));
    }

    private void handleEvent(JDBCEvent e) {
        System.out.println("******************"+ e.getQuery());
        if (this.event == null) {
            this.event = e;
            latch.countDown();
        }
        
    }

    @Test
    public void parseLine() {
        String traceLine = "2018-03-22 17:41:06,989 DEBUG [jboss.jdbc.spy] (hystrix-Reprocess.Reader.Default-1) java:/PCE_JDBC_TX_DATASOURCE [Connection] prepareStatement(select REPROCESSID, PROCESSINGCENTER, LEASEID, CATEGORY, ACTION, NODEID, TRANSACTIONTYPE, TRANSACTIONDATE, TRANSACTIONNUMBER, SESSIONID, CLIENTID, THREADID, STATE, TRYCOUNTER, VERSION, SEQ, MODULE, REFERENCEID, REASON, MODIFICATIONDATE, SCHEDULEDATE, DATA, NODEIDHIERARCHYDEPTH1, NODEIDHIERARCHYDEPTH2, NODEIDHIERARCHYDEPTH3, ACQUIRERID from REPROCESS where state = 'active' and scheduledate <= ? and processingcenter = ? and leaseid = ?)";

        LogExtractor extractor = new LogExtractor();
        Optional<JDBCTraceItem> possibleItem = extractor.parseLine(traceLine);
        assertTrue(possibleItem.isPresent());
        JDBCTraceItem item = possibleItem.get();
        assertThat(item.getTimestamp(), is("2018-03-22 17:41:06,989"));
        assertThat(item.getThread(), is("hystrix-Reprocess.Reader.Default-1"));
    }
}