package de.dplatz.jdbctrace.business.control.wildfly10;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import javax.enterprise.event.Event;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.mockito.Mockito;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

import static org.junit.Assert.assertThat;

import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;

import de.dplatz.jdbctrace.business.entity.JDBCStatement;

public class Wildfly10PluginIT {

	final static String[] JDBC_TRACE_LINES = {
			"2018-03-22 18:34:19,694 DEBUG [jboss.jdbc.spy] (MyThread) java:/MYDS [Connection] prepareStatement(UPDATE MYTABLE SET VALIDTO = ?, OWNER = ?, PREVIOUSDATA = DATA, DATA = ? WHERE PROCESSINGCENTER = ? AND CATEGORY = ? AND ID = ? AND (VALIDTO < ? AND VALIDTO < ? OR OWNER = ?))",
			"2018-03-22 18:34:19,694 DEBUG [jboss.jdbc.spy] (MyThread) java:/MYDS [PreparedStatement] setTimestamp(1, 2018-03-22 18:44:19.692, java.util.GregorianCalendar[time=1521740059694,areFieldsSet=true,areAllFieldsSet=true,lenient=true,zone=sun.util.calendar.ZoneInfo[id=\"UTC\",offset=0,dstSavings=0,useDaylight=false,transitions=0,lastRule=null],firstDayOfWeek=1,minimalDaysInFirstWeek=1,ERA=1,YEAR=2018,MONTH=2,WEEK_OF_YEAR=12,WEEK_OF_MONTH=4,DAY_OF_MONTH=22,DAY_OF_YEAR=81,DAY_OF_WEEK=5,DAY_OF_WEEK_IN_MONTH=4,AM_PM=1,HOUR=5,HOUR_OF_DAY=17,MINUTE=34,SECOND=19,MILLISECOND=694,ZONE_OFFSET=0,DST_OFFSET=0])",
			"2018-03-22 18:34:19,694 DEBUG [jboss.jdbc.spy] (MyThread) java:/MYDS [PreparedStatement] setObject(2, Alpha, 12)",
			"2018-03-22 18:34:19,694 DEBUG [jboss.jdbc.spy] (MyThread) java:/MYDS [PreparedStatement] setObject(3, {\"cat\":\"Default\",\"#\":\"0\",\"msec\":\"1\"}, 12)",
			"2018-03-22 18:34:19,694 DEBUG [jboss.jdbc.spy] (MyThread) java:/MYDS [PreparedStatement] setObject(4, ALPHA, 12)",
			"2018-03-22 18:34:19,694 DEBUG [jboss.jdbc.spy] (MyThread) java:/MYDS [PreparedStatement] setObject(5, Chunk, 12)",
			"2018-03-22 18:34:19,694 DEBUG [jboss.jdbc.spy] (MyThread) java:/MYDS [PreparedStatement] setObject(6, Default/14, 12)",
			"2018-03-22 18:34:19,694 DEBUG [jboss.jdbc.spy] (MyThread) java:/MYDS [PreparedStatement] setTimestamp(7, 2018-03-22 18:44:19.692, java.util.GregorianCalendar[time=1521740059694,areFieldsSet=true,areAllFieldsSet=true,lenient=true,zone=sun.util.calendar.ZoneInfo[id=\"UTC\",offset=0,dstSavings=0,useDaylight=false,transitions=0,lastRule=null],firstDayOfWeek=1,minimalDaysInFirstWeek=1,ERA=1,YEAR=2018,MONTH=2,WEEK_OF_YEAR=12,WEEK_OF_MONTH=4,DAY_OF_MONTH=22,DAY_OF_YEAR=81,DAY_OF_WEEK=5,DAY_OF_WEEK_IN_MONTH=4,AM_PM=1,HOUR=5,HOUR_OF_DAY=17,MINUTE=34,SECOND=19,MILLISECOND=694,ZONE_OFFSET=0,DST_OFFSET=0])",
			"2018-03-22 18:34:19,694 DEBUG [jboss.jdbc.spy] (MyThread) java:/MYDS [PreparedStatement] setTimestamp(8, 2018-03-22 18:44:19.692, java.util.GregorianCalendar[time=1521740059694,areFieldsSet=true,areAllFieldsSet=true,lenient=true,zone=sun.util.calendar.ZoneInfo[id=\"UTC\",offset=0,dstSavings=0,useDaylight=false,transitions=0,lastRule=null],firstDayOfWeek=1,minimalDaysInFirstWeek=1,ERA=1,YEAR=2018,MONTH=2,WEEK_OF_YEAR=12,WEEK_OF_MONTH=4,DAY_OF_MONTH=22,DAY_OF_YEAR=81,DAY_OF_WEEK=5,DAY_OF_WEEK_IN_MONTH=4,AM_PM=1,HOUR=5,HOUR_OF_DAY=17,MINUTE=34,SECOND=19,MILLISECOND=694,ZONE_OFFSET=0,DST_OFFSET=0])",
			"2018-03-22 18:34:19,694 DEBUG [jboss.jdbc.spy] (MyThread) java:/MYDS [PreparedStatement] setObject(9, Alpha, 12)",
			"2018-03-22 18:34:19,694 DEBUG [jboss.jdbc.spy] (MyThread) java:/MYDS [PreparedStatement] setQueryTimeout(120)",
			"2018-03-22 18:34:19,694 DEBUG [jboss.jdbc.spy] (MyThread) java:/MYDS [PreparedStatement] executeUpdate()",
			"2018-03-22 18:34:19,695 DEBUG [jboss.jdbc.spy] (MyThread) java:/MYDS [PreparedStatement] close()"
	};
	
	@Rule
	public TemporaryFolder tmpFolder = new TemporaryFolder();
	File logFile;

	@Before
	public void init() throws IOException {
		logFile = tmpFolder.newFile("server.log");
	}

	CountDownLatch latch = new CountDownLatch(2);
	JDBCStatement capturedEvent;

	@SuppressWarnings("unchecked")
	@Test(timeout = 5000)
	public void test() throws InterruptedException {
		
		Wildfly10Plugin support = new Wildfly10Plugin();
		Wildfly10LogParser logParser = new Wildfly10LogParser();
				
		logParser.event = mock(Event.class);
		doAnswer(invocation -> {
			capturedEvent = invocation.getArgument(0);
			latch.countDown();
			return null;
		}).when(logParser.event).fire(Mockito.any(JDBCStatement.class));
		
		support.logParser = logParser;
		support.serverLog = logFile;
		support.executor = ManagedExecutorServiceFake.create();
		support.startup();

		new Thread(new Runnable() {

			@Override
			public void run() {
				try (BufferedWriter writer = new BufferedWriter(new FileWriter(logFile))) {
					for (int i = 0; i < JDBC_TRACE_LINES.length; i++) {
						writer.append(JDBC_TRACE_LINES[i] + "\n");
						writer.flush();
						TimeUnit.MILLISECONDS.sleep(100);

					}
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					latch.countDown();
				}
			}
		}).start();

		latch.await();
		assertThat(capturedEvent, is(notNullValue()));
		//System.out.println(capturedEvent);
	}

}
