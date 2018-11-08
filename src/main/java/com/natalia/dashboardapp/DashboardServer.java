package com.natalia.dashboardapp;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceFilter;
import com.google.inject.servlet.GuiceServletContextListener;
import com.google.inject.servlet.ServletModule;
import com.natalia.dashboardapp.services.LoginServlet;
import java.util.EnumSet;
import javax.servlet.DispatcherType;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHandler;

public class DashboardServer {

  public static void main(String[] args) throws Exception {
    Server server = new Server(8080);

    ServletContextHandler servletContextHandler =
        new ServletContextHandler();
    servletContextHandler.setContextPath("/");

    servletContextHandler.addEventListener(
        new GuiceServletContextListener() {
          @Override
          protected Injector getInjector() {
            return Guice.createInjector(
                new ServletModule() {
                  @Override
                  protected void configureServlets() {
                    serve("/login").with(LoginServlet.class);
                  }
                });
          }
        });

    servletContextHandler.addFilter(GuiceFilter.class, "/*", EnumSet.of(DispatcherType.REQUEST));

    HandlerList handlers = new HandlerList();
    handlers.setHandlers(new Handler[]{servletContextHandler});
    server.setHandler(handlers);

    // start server
    server.start();
    server.join();
  }

}
