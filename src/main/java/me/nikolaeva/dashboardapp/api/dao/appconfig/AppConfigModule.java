package me.nikolaeva.dashboardapp.api.dao.appconfig;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.protobuf.TextFormat;
import com.google.protobuf.TextFormat.ParseException;
import java.io.File;
import java.io.IOException;
import javax.inject.Named;
import me.nikolaeva.dashboardapp.proto.AppConfig;
import me.nikolaeva.dashboardapp.proto.GoogleOAuthConfig;
import me.nikolaeva.dashboardapp.proto.PostgresqlConfig;
import me.nikolaeva.dashboardapp.proto.RunConfig;

public class AppConfigModule extends AbstractModule {

  @Override
  protected void configure() {

  }

  @Singleton
  @Provides
  @Named("appConfig")
  AppConfig provideAppConfig() throws ParseException {
    AppConfig config = AppConfig.newBuilder()
        .setPostgresqlConfig(
            PostgresqlConfig.newBuilder().setJdbcUrl(System.getenv("JDBC_DATABASE_URL")).build())
        .setGoogleOauthConfig(GoogleOAuthConfig.newBuilder()
            .setClientId(System.getenv("CLIENT_ID"))
            .setClientSecret(System.getenv("CLIENT_SECRET"))
            .setGrantType(System.getenv("GRANT_TYPE"))
            .setRedirectUrl(System.getenv("REDIRECT_URL"))
            .setResponseType(System.getenv("RESPONSE_TYPE"))
            .setScope(System.getenv("SCOPE"))
            .setState(System.getenv("STATE"))
            .build())
        .setRunConfig(RunConfig.newBuilder()
            .setPort(System.getenv("PORT"))
            .setBaseUrl(System.getenv("BASE_URL"))
            .setUiUrl(System.getenv("UI_URL"))
            .build())
        .build();

    return config;
  }


}
