package de.dplatz.jdbctrace.boundary;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

import javax.enterprise.event.Observes;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.dplatz.jdbctrace.entity.JDBCEvent;

/**
 * Events
 */
@WebServlet("/events")
public class Events extends HttpServlet {

    BlockingQueue<JDBCEvent> queue = new LinkedBlockingDeque<>(50);

    public void onJDBCEvent(@Observes JDBCEvent event) {
        queue.add(event);
    }

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/event-stream");
        resp.setCharacterEncoding("UTF-8");

        int i=0;
        //while (true) {
            try (PrintWriter pw = resp.getWriter()){
                JDBCEvent event = queue.take();
                pw.print("data: " + event.getQuery()+"\r\n");
                resp.flushBuffer();
                Thread.sleep(500);
            } catch (Exception e) {
                e.printStackTrace();
            }
        //}
    }
}