package me.nikolaeva.dashboardapp.api.dao.psql;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.inject.Named;
import me.nikolaeva.dashboardapp.api.dao.DashboardDao;
import me.nikolaeva.dashboardapp.proto.AppConfig;

public class PsqlDaoModule extends AbstractModule {

  @Override
  protected void configure() {
    bind(DashboardDao.class).to(DashboardDaoImpl.class);
  }

  @Singleton
  @Provides
  Connection provideConnection(@Named("appConfig") AppConfig config) throws SQLException {
    try {
      Class.forName("org.postgresql.Driver");
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    }

    return DriverManager.getConnection(config.getPostgresqlConfig().getJdbcUrl());
  }

}
