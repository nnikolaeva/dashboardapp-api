package me.nikolaeva.dashboardapp.api.services;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import java.io.IOException;
import javax.inject.Named;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import me.nikolaeva.dashboardapp.proto.AppConfig;

@Singleton
public class LoginServlet extends HttpServlet {

  private final AppConfig config;

  @Inject
  public LoginServlet(@Named("appConfig") AppConfig config) {
    this.config = config;
  }

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    HttpServletResponse httpResponse = (HttpServletResponse) resp;
    String url =
        "https://accounts.google.com/o/oauth2/v2/auth?scope="
            + config.getGoogleOauthConfig().getScope()
            + "&redirect_uri="
            + config.getGoogleOauthConfig().getRedirectUrl()
            + "&client_id="
            + config.getGoogleOauthConfig().getClientId()
            + "&response_type="
            + config.getGoogleOauthConfig().getResponseType()
            + "&state="
            + config.getGoogleOauthConfig().getState();
    httpResponse.sendRedirect(url);
  }
}
