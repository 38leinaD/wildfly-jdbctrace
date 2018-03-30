package de.dplatz.jdbctrace.business.boundary;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObject;
import javax.servlet.AsyncContext;
import javax.servlet.ServletException;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.dplatz.jdbctrace.business.control.ClientSessionManager;
import de.dplatz.jdbctrace.business.entity.ClientSession;
import de.dplatz.jdbctrace.business.entity.JDBCStatement;

@WebServlet(value = "/events", asyncSupported = true)
public class JDBCEventsServlet extends HttpServlet {

	private static final long serialVersionUID = 3480370204795216989L;

	@Inject
	ClientSessionManager clientSessionManager;

	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		AsyncContext context = req.startAsync();
		ClientSession clientSession = clientSessionManager.findOrCreateClientSession(req.getQueryString());
		drainQueue(clientSession, context);
	}

	void drainQueue(ClientSession clientSession, AsyncContext context) {
		ServletResponse response = context.getResponse();
		response.setContentType("text/event-stream");
		response.setCharacterEncoding("UTF-8");

		try (PrintWriter pw = response.getWriter()) {
			
			BlockingQueue<JDBCStatement> queue = clientSession.getQueue();
			while (true) {

				JDBCStatement event = queue.poll(5, TimeUnit.SECONDS);
				if (event == null)
					return;

				JsonObject json = Json.createObjectBuilder()
					.add("thread", event.getThread())
					.add("sql", event.resolvedStatement())
					.build();


				pw.print("data: " + json.toString() + "\n\n");
				response.flushBuffer();
				clientSession.refreshLifetime();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}