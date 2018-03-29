package de.dplatz.jdbctrace.presentation;

import java.io.Serializable;

import javax.enterprise.event.Event;
import javax.enterprise.inject.Model;
import javax.faces.component.html.HtmlSelectBooleanCheckbox;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Inject;

import de.dplatz.jdbctrace.control.ApplicationInitializer;
import de.dplatz.jdbctrace.control.SQLStatementRecorder;
import de.dplatz.jdbctrace.entity.JDBCStatement;

@Model
public class TracesBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	ApplicationInitializer logs;
    
	@Inject
	SQLStatementRecorder recorder;
   
    @Inject
    Event<JDBCStatement> event;

    public void updateFilterJunk(ValueChangeEvent event) {
        recorder.setFilterJunk((boolean) event.getNewValue());
    }

    public SQLStatementRecorder getRecorder() {
    	return recorder;
    }
}
