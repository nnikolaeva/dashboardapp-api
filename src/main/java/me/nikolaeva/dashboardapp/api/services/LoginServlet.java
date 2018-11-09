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

  private final AppConfig appConfig;

  @Inject
  public LoginServlet(@Named("appConfig") AppConfig appConfig) {
    this.appConfig = appConfig;
  }

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    HttpServletResponse httpResponse = (HttpServletResponse) resp;
    String url =
        "https://accounts.google.com/o/oauth2/v2/auth?scope="
            + appConfig.getGoogleOauthConfig().getScope()
            + "&redirect_uri="
            + appConfig.getGoogleOauthConfig().getRedirectUrl()
            + "&client_id="
            + appConfig.getGoogleOauthConfig().getClientId()
            + "&response_type="
            + appConfig.getGoogleOauthConfig().getResponseType()
            + "&state="
            + appConfig.getGoogleOauthConfig().getState();
    httpResponse.sendRedirect(url);
  }
}
