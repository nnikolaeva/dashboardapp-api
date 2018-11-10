package me.nikolaeva.dashboardapp.api.services;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import javax.inject.Named;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import me.nikolaeva.dashboardapp.api.dao.DashboardDao;
import me.nikolaeva.dashboardapp.proto.AppConfig;
import me.nikolaeva.dashboardapp.proto.User;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

@Singleton
public class AuthWithGoogleServlet extends HttpServlet {

  private final AppConfig appConfig;
  private final DashboardDao dao;

  @Inject
  public AuthWithGoogleServlet(@Named("appConfig") AppConfig appConfig, DashboardDao dao) {
    this.appConfig = appConfig;
    this.dao = dao;
  }

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {

    // exchange the authorization code for an access token.
    String code = req.getParameter("code");

    if (code != null) {
      HttpPost method = new HttpPost("https://www.googleapis.com/oauth2/v4/token");

      List<NameValuePair> arguments = new ArrayList<>(3);
      arguments.add(new BasicNameValuePair("code", code));
      arguments
          .add(new BasicNameValuePair("client_id", appConfig.getGoogleOauthConfig().getClientId()));
      arguments.add(new BasicNameValuePair("client_secret",
          appConfig.getGoogleOauthConfig().getClientSecret()));
      arguments.add(
          new BasicNameValuePair(
              "redirect_uri", appConfig.getGoogleOauthConfig().getRedirectUrl()));
      arguments.add(
          new BasicNameValuePair("grant_type", appConfig.getGoogleOauthConfig().getGrantType()));

      method.setEntity(new UrlEncodedFormEntity(arguments));
      HttpClient client = HttpClientBuilder.create().build();
      HttpResponse res = client.execute(method);

      InputStream in = res.getEntity().getContent();
      JSONParser jsonParser = new JSONParser();
      String token = null;
      try {
        JSONObject jsonObject = (JSONObject) jsonParser.parse(new InputStreamReader(in, "UTF-8"));
        token = jsonObject.get("id_token").toString();

      } catch (ParseException e) {
        e.printStackTrace();
      }

      Payload payload = getPayload(token);
      User user = dao.getUserByProfileId(payload.getEmail());
      if (user == null) {
        String id = UUID.randomUUID().toString();
        user = createUserFromPayload(payload, id);
        dao.createUser(user);
      }

      String userToken = UUID.randomUUID().toString();
      User.Builder userToUpdate = user.toBuilder();
      userToUpdate.setUserToken(userToken);
      user = userToUpdate.build();
      dao.addUserToken(user.getId(), userToken);

      Cookie cookie = new Cookie("userToken", user.getUserToken());
      resp.addCookie(cookie);
      resp.sendRedirect("http://localhost:8080/post");
    }
  }

  private Payload getPayload(String token) throws IOException {
    Payload payload = null;
    try {
      HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
      JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();

      GoogleIdTokenVerifier verifier =
          new GoogleIdTokenVerifier.Builder(httpTransport, jsonFactory)
              .setAudience(
                  Collections.singletonList(appConfig.getGoogleOauthConfig().getClientId()))
              .build();

      GoogleIdToken idToken = verifier.verify(token);
      if (idToken != null) {
        payload = idToken.getPayload();

      } else {
        System.out.println("Invalid ID token.");
      }

    } catch (GeneralSecurityException e) {
      e.printStackTrace();
    }
    return payload;
  }

  private User createUserFromPayload(Payload payload, String id) {
    String name = (String) payload.get("name");
    return User.newBuilder()
        .setEmail(payload.getEmail())
        .setName(name)
        .setId(id)
        .build();
  }
}
