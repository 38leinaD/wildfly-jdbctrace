package de.dplatz.jdbctrace.control.wildfly;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.enterprise.event.Event;
import javax.enterprise.util.TypeLiteral;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.hamcrest.CoreMatchers.any;
import static org.hamcrest.CoreMatchers.anything;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

import static org.junit.Assert.assertThat;

import static org.mockito.Mockito.mock;

import de.dplatz.jdbctrace.entity.JDBCStatement;

public class WildflySupportIT {
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
	JDBCStatement capturedEvent;

	@Test(timeout = 5000)
	public void test() throws InterruptedException {
		new Thread(new Runnable() {

			@Override
			public void run() {
				System.out.println("++++");
				try (BufferedWriter writer = new BufferedWriter(new FileWriter(logFile))) {
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

		WildflySupport support = new WildflySupport();
		WildflyLogParser logParser = new WildflyLogParser();
		Event<JDBCStatement> event = new Event<JDBCStatement>() {

			@Override
			public void fire(JDBCStatement event) {
				capturedEvent = event;
				System.out.println("HELLO");
				latch.countDown();
			}

			@Override
			public Event<JDBCStatement> select(Annotation... qualifiers) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public <U extends JDBCStatement> Event<U> select(Class<U> subtype, Annotation... qualifiers) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public <U extends JDBCStatement> Event<U> select(TypeLiteral<U> subtype, Annotation... qualifiers) {
				// TODO Auto-generated method stub
				return null;
			}
			
		};

		logParser.event = event;
		support.logParser = logParser;
		support.executor = null;
		support.serverLog = logFile;
		support.executor = Executors.newFixedThreadPool(1);
		support.init();
		System.out.println("BEFORE");
		latch.await();
		System.out.println("AFTER");
		// verify(event).fire(any(JDBCStatement.class));
		assertThat(capturedEvent, is(notNullValue()));
		// assertTrue(statement.getStatement().startsWith("UPDATE LEASE SET VALIDTO"));
	}

}
