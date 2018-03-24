package de.dplatz.jdbctrace.boundary;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.dplatz.jdbctrace.control.EventQueue;
import de.dplatz.jdbctrace.entity.JDBCEvent;

/**
 * Events
 */
@WebServlet("/events")
public class Events extends HttpServlet {

    @Inject
    EventQueue queue;

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/event-stream");
        resp.setCharacterEncoding("UTF-8");

        int i=0;
        //while (true) {
            try (PrintWriter pw = resp.getWriter()){
                System.out.println("wait");
                JDBCEvent event = queue.getQueue().take();
                System.out.println("taken");
                pw.print("data: " + event.getQuery()+"\r\n");
                resp.flushBuffer();
                Thread.sleep(500);
            } catch (Exception e) {
                e.printStackTrace();
            }
        //}
    }
}