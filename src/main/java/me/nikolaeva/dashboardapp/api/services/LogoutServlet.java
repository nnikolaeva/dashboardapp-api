package me.nikolaeva.dashboardapp.api.services;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import java.io.IOException;
import javax.inject.Named;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import me.nikolaeva.dashboardapp.api.dao.DashboardDao;
import me.nikolaeva.dashboardapp.proto.AppConfig;
import me.nikolaeva.dashboardapp.proto.User;

@Singleton
public class LogoutServlet extends HttpServlet {

  private final AppConfig config;
  private final Provider<User> userProvider;
  private final DashboardDao dao;

  @Inject
  public LogoutServlet(@Named("appConfig") AppConfig config,
      @Named("user") Provider<User> userProvider, DashboardDao dao) {
    this.config = config;
    this.userProvider = userProvider;
    this.dao = dao;
  }

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    User loggedUser = userProvider.get();
    dao.deleteUserToken(loggedUser.getId(), loggedUser.getUserToken());

    Cookie cookie = new Cookie("userToken", null);
    cookie.setMaxAge(0);
    resp.addCookie(cookie);
    resp.sendRedirect(config.getRunConfig().getUiUrl()+ "/login");
  }
}
