package de.dplatz.jdbctrace.entity;

import javax.management.ObjectInstance;

public class Datasource {

    ObjectInstance object;
    boolean spyingEnabled;
    boolean valid;

    public Datasource(ObjectInstance object, boolean spyingEnabled) {
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
		System.out.println("XXXX");
		this.spyingEnabled = spyingEnabled;
		this.valid = false;
	}

	public ObjectInstance getObject() {
		return object;
	}

	public boolean isValid() {
		return valid;
	}
}