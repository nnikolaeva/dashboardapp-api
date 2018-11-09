package me.nikolaeva.dashboardapp.api.services;

import com.google.inject.Inject;
import com.google.inject.Key;
import com.google.inject.Singleton;
import com.google.inject.name.Names;
import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import me.nikolaeva.dashboardapp.api.dao.DashboardDao;
import me.nikolaeva.dashboardapp.proto.User;

@Singleton
public class SeedLoggedUserFilter implements Filter {

  private final DashboardDao dao;

  @Inject
  public SeedLoggedUserFilter(DashboardDao dao) {
    this.dao = dao;
  }


  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {
    HttpServletRequest httpRequest = (HttpServletRequest) request;
    User user = User.getDefaultInstance();

    String userTokenFromCookies = getUserToken(httpRequest.getCookies());
    if (userTokenFromCookies != null) {
      User loggedUser = dao.getUserByUserToken(userTokenFromCookies);
      if (loggedUser != null) {
        user = loggedUser;
      }
    }

    httpRequest.setAttribute(Key.get(User.class, Names.named("user")).toString(), user);

    chain.doFilter(request, response);
  }

  private String getUserToken(Cookie[] cookies) {
    if (cookies == null) {
      return null;
    }

    for (Cookie c : cookies) {
      if (c.getName().equals("userToken")) {
        return c.getValue();
      }
    }
    return null;
  }

  public void init(FilterConfig filterConfig) throws ServletException {
    // nothing here
  }

  public void destroy() {
    // nothing here
  }
}
