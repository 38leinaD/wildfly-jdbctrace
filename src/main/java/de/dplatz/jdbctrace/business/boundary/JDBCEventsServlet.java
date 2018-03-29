package de.dplatz.jdbctrace.business.boundary;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.dplatz.jdbctrace.business.control.JDBCStatementRecorder;
import de.dplatz.jdbctrace.business.entity.JDBCStatement;

@WebServlet("/events")
public class JDBCEventsServlet extends HttpServlet {

	private static final long serialVersionUID = 3480370204795216989L;

	@Inject
	JDBCStatementRecorder recorder;

	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("text/event-stream");
		resp.setCharacterEncoding("UTF-8");

		try (PrintWriter pw = resp.getWriter()) {
			while (true) {
				JDBCStatement event = recorder.getQueue().poll(5, TimeUnit.SECONDS);
				if (event == null)
					return;

				JsonObject json = Json.createObjectBuilder()
					.add("thread", event.getThread())
					.add("sql", event.resolvedStatement())
					.build();


				pw.print("data: " + json.toString() + "\n\n");
				resp.flushBuffer();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}