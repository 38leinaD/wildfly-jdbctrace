package de.dplatz.jdbctrace.entity;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;

import static org.junit.Assert.assertThat;

public class JDBCStatementTest {

	@Test
	public void resolveObjectParameter() {
		JDBCStatement stmt = new JDBCStatement();
		stmt.setStatement("UPDATE LEASE SET VALIDTO = ?, OWNER = ?, PREVIOUSDATA = DATA, DATA = ? WHERE PROCESSINGCENTER = ? AND CATEGORY = ? AND ID = ? AND (VALIDTO < ? AND VALIDTO < ? OR OWNER = ?)");
		stmt.setParam(2, "Alpha.Cluster.dev-vm");
		
		assertThat(stmt.resolvedStatement(), is("UPDATE LEASE SET VALIDTO = ?, OWNER = 'Alpha.Cluster.dev-vm', PREVIOUSDATA = DATA, DATA = ? WHERE PROCESSINGCENTER = ? AND CATEGORY = ? AND ID = ? AND (VALIDTO < ? AND VALIDTO < ? OR OWNER = ?)"));
	}

}
