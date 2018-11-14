package me.nikolaeva.dashboardapp.api.services;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.google.protobuf.util.JsonFormat;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.inject.Named;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import me.nikolaeva.dashboardapp.api.dao.DashboardDao;
import me.nikolaeva.dashboardapp.proto.Post;
import me.nikolaeva.dashboardapp.proto.Post.Builder;
import me.nikolaeva.dashboardapp.proto.PostList;
import me.nikolaeva.dashboardapp.proto.User;

@Singleton
public class PostServlet extends HttpServlet {

  private final DashboardDao dao;
  private final Provider<User> userProvider;

  @Inject
  public PostServlet(DashboardDao dao, @Named("user") Provider<User> userProvider) {
    this.dao = dao;
    this.userProvider = userProvider;
  }


  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    String dashboardId = req.getParameter("dashboard_id");
    User loggedUser = userProvider.get();
    resp.getWriter().write(renderJson(dao.getPosts(loggedUser.getId(), dashboardId)));
  }

  private String renderJson(PostList posts) {
    try {
      return JsonFormat.printer().print(posts);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    String data = "";
    BufferedReader in = new BufferedReader(new InputStreamReader(req.getInputStream()));
    data = in.lines().collect(Collectors.joining(System.lineSeparator()));
    Builder post = Post.newBuilder();
    JsonFormat.parser().merge(data, post);

    User currentUser = userProvider.get();
    if (post.getId().length() == 0) {
      post.setId(UUID.randomUUID().toString());
      post.setUserId(currentUser.getId());
      dao.addPost(post.build());
    } else {
      dao.updatePost(post.build());
    }
  }

  @Override
  protected void doDelete(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    String id = req.getParameter("id");
    dao.deletePost(id);
  }
}
