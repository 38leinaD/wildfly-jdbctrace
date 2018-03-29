package de.dplatz.jdbctrace.business.entity;

import javax.management.ObjectInstance;

public class JDBCDatasource {

    ObjectInstance object;
    boolean spyingEnabled;
    boolean valid;

    public JDBCDatasource(ObjectInstance object, boolean spyingEnabled) {
        this.object = object;
        this.spyingEnabled = spyingEnabled;
        this.valid = true;
    }

    public String getName() {
        return object.getObjectName().getCanonicalName();
    }

	public boolean isSpyingEnabled() {
		return spyingEnabled;
	}

	public void setSpyingEnabled(boolean spyingEnabled) {
		this.spyingEnabled = spyingEnabled;
		this.valid = false;
	}

	public ObjectInstance getObjectInstance() {
		return object;
	}

	public boolean isValid() {
		return valid;
	}
}