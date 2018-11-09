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

public class AppConfigModule extends AbstractModule {

  @Override
  protected void configure() {

  }

  @Singleton
  @Provides
  @Named("appConfig")
  AppConfig provideAppConfig() throws ParseException {
    String text = null;
    try {
      text = Files.asCharSource(new File("config.textproto"), Charsets.UTF_8).read();
    } catch (IOException e) {
      e.printStackTrace();
    }
    AppConfig.Builder config = AppConfig.newBuilder();
    TextFormat.getParser().merge(text, config);

    return config.build();
  }


}
