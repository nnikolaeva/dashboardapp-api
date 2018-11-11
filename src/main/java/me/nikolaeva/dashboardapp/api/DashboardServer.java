package me.nikolaeva.dashboardapp.api;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Provider;
import com.google.inject.name.Names;
import com.google.inject.servlet.GuiceFilter;
import com.google.inject.servlet.GuiceServletContextListener;
import com.google.inject.servlet.RequestScoped;
import com.google.inject.servlet.ServletModule;
import java.util.EnumSet;
import javax.servlet.DispatcherType;
import me.nikolaeva.dashboardapp.api.dao.appconfig.AppConfigModule;
import me.nikolaeva.dashboardapp.api.dao.psql.PsqlDaoModule;
import me.nikolaeva.dashboardapp.api.services.AuthWithGoogleServlet;
import me.nikolaeva.dashboardapp.api.services.LoginServlet;
import me.nikolaeva.dashboardapp.api.services.PostServlet;
import me.nikolaeva.dashboardapp.api.services.SeedLoggedUserFilter;
import me.nikolaeva.dashboardapp.api.services.UserLoggedInRequiredFilter;
import me.nikolaeva.dashboardapp.proto.User;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlets.CrossOriginFilter;

public class DashboardServer {

  public static void main(String[] args) throws Exception {
    Server server = new Server(Integer.valueOf(System.getenv("PORT")));

    ServletContextHandler servletContextHandler =
        new ServletContextHandler();
    servletContextHandler.setContextPath("/");

    servletContextHandler.addEventListener(
        new GuiceServletContextListener() {
          @Override
          protected Injector getInjector() {
            return Guice.createInjector(
                new PsqlDaoModule(),
                new ServletModule() {
                  @Override
                  protected void configureServlets() {
                    serve("/login").with(LoginServlet.class);
                    serve("/post").with(PostServlet.class);
                    serve("/authwithcode").with(AuthWithGoogleServlet.class);
                    filter("/*").through(SeedLoggedUserFilter.class);
                    filter("/*").through(UserLoggedInRequiredFilter.class);
                    bind(User.class).annotatedWith(Names.named("user")).toProvider(
                        new Provider<User>() {
                          public User get() {
                            throw new RuntimeException("User should not be null");
                          }
                        }).in(RequestScoped.class);
                  }
                });
          }
        });

    servletContextHandler.addFilter(createHolder(), "/*", EnumSet.of(DispatcherType.REQUEST));

    servletContextHandler.addFilter(GuiceFilter.class, "/*", EnumSet.of(DispatcherType.REQUEST));

    HandlerList handlers = new HandlerList();
    handlers.setHandlers(new Handler[]{servletContextHandler});
    server.setHandler(handlers);

    // start server
    server.start();
    server.join();
  }

  private static FilterHolder createHolder() {
    FilterHolder holder = new FilterHolder(new CrossOriginFilter());
    holder.setInitParameter(CrossOriginFilter.ALLOWED_ORIGINS_PARAM, "*");
    holder.setInitParameter(CrossOriginFilter.ACCESS_CONTROL_ALLOW_ORIGIN_HEADER, System.getenv("UI_URL"));
    holder.setInitParameter(CrossOriginFilter.ACCESS_CONTROL_ALLOW_CREDENTIALS_HEADER, "true");

    holder.setInitParameter(
        CrossOriginFilter.ALLOWED_METHODS_PARAM, "GET,POST,HEAD,OPTIONS,DELETE");
    return holder;
  }

}
