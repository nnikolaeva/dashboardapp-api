package me.nikolaeva.dashboardapp.api.services;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.google.protobuf.util.JsonFormat;
import java.io.IOException;
import javax.inject.Named;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import me.nikolaeva.dashboardapp.api.dao.DashboardDao;
import me.nikolaeva.dashboardapp.proto.DashboardList;
import me.nikolaeva.dashboardapp.proto.User;

@Singleton
public class DashboardServlet extends HttpServlet {

  private final DashboardDao dao;
  private final Provider<User> userProvider;

  @Inject
  public DashboardServlet(DashboardDao dao, @Named("user") Provider<User> userProvider) {
    this.dao = dao;
    this.userProvider = userProvider;
  }

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    User loggedUser = userProvider.get();
    resp.getWriter().write(renderJson(dao.getDashboards(loggedUser.getId())));
  }


  private String renderJson(DashboardList dashboards) {
    try {
      return JsonFormat.printer().print(dashboards);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
