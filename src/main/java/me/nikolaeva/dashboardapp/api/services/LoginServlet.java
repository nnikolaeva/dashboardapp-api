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

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    HttpServletResponse httpResponse = (HttpServletResponse) resp;
    String url =
        "https://accounts.google.com/o/oauth2/v2/auth?scope="
            + System.getenv("SCOPE")
            + "&redirect_uri="
            + System.getenv("REDIRECT_URL")
            + "&client_id="
            + System.getenv("CLIENT_ID")
            + "&response_type="
            + System.getenv("RESPONSE_TYPE")
            + "&state="
            + System.getenv("STATE");
    httpResponse.sendRedirect(url);
  }
}
