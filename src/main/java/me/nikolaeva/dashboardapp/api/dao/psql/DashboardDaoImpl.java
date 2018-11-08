package me.nikolaeva.dashboardapp.api.dao.psql;

import com.google.inject.Inject;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import me.nikolaeva.dashboardapp.api.dao.DashboardDao;
import me.nikolaeva.dashboardapp.proto.Post;
import me.nikolaeva.dashboardapp.proto.PostList;
import me.nikolaeva.dashboardapp.proto.User;

public class DashboardDaoImpl implements DashboardDao {

  private final Connection connection;

  @Inject
  public DashboardDaoImpl(Connection connection) {
    this.connection = connection;
  }

  public PostList getPosts() {
    PostList.Builder posts = PostList.newBuilder();
    try {
      Statement statement = connection.createStatement();
      String query = "SELECT * FROM post";
      ResultSet resultSet = statement.executeQuery(query);
      while (resultSet.next()) {
        posts.addPosts(Post.newBuilder().setId(resultSet.getString("id"))
            .setUserId(resultSet.getString("user_id"))
            .setContent(resultSet.getString("content"))
            .build());

      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return posts.build();
  }

  public User getUserByUserToken(String userToken) {
    return null;
  }
}
