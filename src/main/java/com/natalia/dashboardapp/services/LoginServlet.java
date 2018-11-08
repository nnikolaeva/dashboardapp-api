package com.natalia.dashboardapp.services;

import com.google.inject.Singleton;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Singleton
public class LoginServlet extends HttpServlet {

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    resp.setContentType("text/html; charset=utf-8");
    resp.setStatus(HttpServletResponse.SC_OK);
    PrintWriter out = resp.getWriter();
    out.println("<h1>response from login servlet</h1>");
  }
}
