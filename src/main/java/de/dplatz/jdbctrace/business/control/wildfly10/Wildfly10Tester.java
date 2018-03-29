package de.dplatz.jdbctrace.business.control.wildfly10;

import java.util.Arrays;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class Wildfly10Tester {
	
	public static String[] SAMPLE_TRACE_DATA = {
			"java:/MY_DS [Connection] prepareStatement(UPDATE MYTABLE SET VALIDTO = ?, OWNER = ?, PREVIOUSDATA = DATA, DATA = ? WHERE PROCESSINGCENTER = ? AND CATEGORY = ? AND ID = ? AND (VALIDTO < ? AND VALIDTO < ? OR OWNER = ?))",
			"java:/MY_DS [PreparedStatement] setTimestamp(1, 2018-03-22 18:44:19.692, java.util.GregorianCalendar[time=1521740059694,areFieldsSet=true,areAllFieldsSet=true,lenient=true,zone=sun.util.calendar.ZoneInfo[id=\"UTC\",offset=0,dstSavings=0,useDaylight=false,transitions=0,lastRule=null],firstDayOfWeek=1,minimalDaysInFirstWeek=1,ERA=1,YEAR=2018,MONTH=2,WEEK_OF_YEAR=12,WEEK_OF_MONTH=4,DAY_OF_MONTH=22,DAY_OF_YEAR=81,DAY_OF_WEEK=5,DAY_OF_WEEK_IN_MONTH=4,AM_PM=1,HOUR=5,HOUR_OF_DAY=17,MINUTE=34,SECOND=19,MILLISECOND=694,ZONE_OFFSET=0,DST_OFFSET=0])",
			"java:/MY_DS [PreparedStatement] setObject(2, BLA, 12)",
			"java:/MY_DS [PreparedStatement] setObject(3, {\"cat\":\"Default\",\"#\":\"0\",\"msec\":\"1\"}, 12)",
			"java:/MY_DS [PreparedStatement] setObject(4, ZHETA, 12)",
			"java:/MY_DS [PreparedStatement] setObject(5, Chunk, 12)",
			"java:/MY_DS [PreparedStatement] setObject(6, Default/14, 12)",
			"java:/MY_DS [PreparedStatement] setTimestamp(7, 2018-03-22 18:44:19.692, java.util.GregorianCalendar[time=1521740059694,areFieldsSet=true,areAllFieldsSet=true,lenient=true,zone=sun.util.calendar.ZoneInfo[id=\"UTC\",offset=0,dstSavings=0,useDaylight=false,transitions=0,lastRule=null],firstDayOfWeek=1,minimalDaysInFirstWeek=1,ERA=1,YEAR=2018,MONTH=2,WEEK_OF_YEAR=12,WEEK_OF_MONTH=4,DAY_OF_MONTH=22,DAY_OF_YEAR=81,DAY_OF_WEEK=5,DAY_OF_WEEK_IN_MONTH=4,AM_PM=1,HOUR=5,HOUR_OF_DAY=17,MINUTE=34,SECOND=19,MILLISECOND=694,ZONE_OFFSET=0,DST_OFFSET=0])",
			"java:/MY_DS [PreparedStatement] setTimestamp(8, 2018-03-22 18:44:19.692, java.util.GregorianCalendar[time=1521740059694,areFieldsSet=true,areAllFieldsSet=true,lenient=true,zone=sun.util.calendar.ZoneInfo[id=\"UTC\",offset=0,dstSavings=0,useDaylight=false,transitions=0,lastRule=null],firstDayOfWeek=1,minimalDaysInFirstWeek=1,ERA=1,YEAR=2018,MONTH=2,WEEK_OF_YEAR=12,WEEK_OF_MONTH=4,DAY_OF_MONTH=22,DAY_OF_YEAR=81,DAY_OF_WEEK=5,DAY_OF_WEEK_IN_MONTH=4,AM_PM=1,HOUR=5,HOUR_OF_DAY=17,MINUTE=34,SECOND=19,MILLISECOND=694,ZONE_OFFSET=0,DST_OFFSET=0])",
			"java:/MY_DS [PreparedStatement] setObject(9, ABC9, 12)",
			"java:/MY_DS [PreparedStatement] setQueryTimeout(120)",
			"java:/MY_DS [PreparedStatement] executeUpdate()",
			"java:/MY_DS [PreparedStatement] close()"
	    };
	
	public void generateTestData() {
		Arrays.asList(SAMPLE_TRACE_DATA).stream().forEach(System.out::println);
	}
}
