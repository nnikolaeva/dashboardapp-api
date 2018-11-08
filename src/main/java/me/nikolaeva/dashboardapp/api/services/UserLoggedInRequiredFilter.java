package me.nikolaeva.dashboardapp.api.services;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import java.io.IOException;
import javax.inject.Named;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import me.nikolaeva.dashboardapp.proto.User;

@Singleton
public class UserLoggedInRequiredFilter implements Filter {

  private final Provider<User> userProvider;

  @Inject
  public UserLoggedInRequiredFilter(@Named("user") Provider<User> userProvider) {
    this.userProvider = userProvider;
  }

  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {
    HttpServletResponse httpResponse = (HttpServletResponse) response;
    
    if (!isUserLoggedIn()) {
      httpResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
    } else {
      chain.doFilter(request, response);
    }

  }

  private boolean isUserLoggedIn() {
    User currentUser = userProvider.get();
    if (!currentUser.getId().equals("")) {
      return true;
    }
    return false;
  }


  public void init(FilterConfig filterConfig) throws ServletException {
    // do nothing here
  }

  public void destroy() {
    // do nothing here
  }
}
