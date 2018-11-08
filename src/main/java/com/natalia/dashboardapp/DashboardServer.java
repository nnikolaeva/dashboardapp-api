package com.natalia.dashboardapp;

import org.eclipse.jetty.server.Server;

public class DashboardServer {

  public static void main(String[] args) throws Exception {
    Server server = new Server(8080);

    // start server
    server.start();
    server.join();
  }

}
