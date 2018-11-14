package me.nikolaeva.dashboardapp.api.dao.psql;

import com.google.inject.Inject;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import me.nikolaeva.dashboardapp.api.dao.DashboardDao;
import me.nikolaeva.dashboardapp.proto.Dashboard;
import me.nikolaeva.dashboardapp.proto.DashboardList;
import me.nikolaeva.dashboardapp.proto.Post;
import me.nikolaeva.dashboardapp.proto.PostList;
import me.nikolaeva.dashboardapp.proto.User;

public class DashboardDaoImpl implements DashboardDao {

  private final Connection connection;

  @Inject
  public DashboardDaoImpl(Connection connection) {
    this.connection = connection;
  }

  public PostList getPosts(String id, String dashboardId) {
    PostList.Builder posts = PostList.newBuilder();
    try {
      Statement statement = connection.createStatement();
      String query = "SELECT * FROM post WHERE user_id = \'" + id + "\' and dashboard_id = \' " + dashboardId + "\'";
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

  @Override
  public void addPost(Post post) {
    try {
      Statement statement = connection.createStatement();
      String query = "INSERT INTO post(id, user_id, content, dashboard_id) VALUES (\'"
          + post.getId()
          + "\', "
          + "\'"
          + post.getUserId()
          + "\', \'"
          + post.getDashboardId()
          + "\', \'"
          + post.getContent()
          + "\') ";
      statement.executeUpdate(query);
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void deletePost(String id) {
    try {
      Statement statement = connection.createStatement();

      ResultSet rs = statement.executeQuery("SELECT * FROM post WHERE id = \'" + id + "\'");
      if (!rs.next()) {
        throw new RuntimeException("record with id " + id + " is not found in database");
      }

      statement.executeUpdate("DELETE FROM post WHERE id = \'" + id + "\'");
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void updatePost(Post post) {
    try {
      Statement statement = connection.createStatement();

      ResultSet rs =
          statement.executeQuery("SELECT * FROM post WHERE id = \'" + post.getId() + "\'");
      if (!rs.next()) {
        throw new RuntimeException("record with id " + post.getId() + " is not found in database");
      }
      String sql =
          "UPDATE post SET content="
              + "\'"
              + post.getContent()
              + "\'"
              + " where id = '"
              + post.getId()
              + "'";
      statement.executeUpdate(sql);

    } catch (SQLException e) {
      throw new RuntimeException(e);
    }

  }

  public User getUserByUserToken(String userToken) {
    User user = null;
    try {
      Statement statement = connection.createStatement();
      ResultSet rs = statement
          .executeQuery("SELECT * from user_token where user_token = \'" + userToken + "\'");
      if (rs.next()) {
        ResultSet resultSet = statement
            .executeQuery("SELECT * FROM users WHERE id = \'" + rs.getString("user_id") + "\'");
        if (resultSet.next()) {
          user = User.newBuilder().setId(resultSet.getString("id"))
              .setName(resultSet.getString("name"))
              .setEmail(resultSet.getString("email"))
              .setUserToken(userToken)
              .build();
        }
      }

    } catch (SQLException e) {
      e.printStackTrace();
    }
    return user;
  }

  @Override
  public User getUserByProfileId(String email) {
    User user = null;
    try {
      Statement statement = connection.createStatement();
      String query = "SELECT * FROM users WHERE email = \'" + email + "\'";
      ResultSet resultSet =
          statement.executeQuery(query);
      if (resultSet.next()) {
        user = User.newBuilder().setId(resultSet.getString("id"))
            .setName(resultSet.getString("name"))
            .setEmail(resultSet.getString("email"))
            .build();
      }

    } catch (SQLException e) {
      e.printStackTrace();
    }
    return user;
  }

  @Override
  public void createUser(User user) {
    try {
      Statement statement = connection.createStatement();
      String query =
          "INSERT INTO users (id, name, email) values (\'"
              + user.getId()
              + "\', \'"
              + user.getName()
              + "\', \'"
              + user.getEmail()
              + "\')";
      statement.executeUpdate(query);

    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void addUserToken(String userId, String userToken) {
    try {
      Statement statement = connection.createStatement();
      String query = "INSERT INTO user_token (user_id, user_token) values(\'" + userId + "\', \'"
          + userToken + "\')";
      statement.executeUpdate(query);
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void deleteUserToken(String userId, String userToken) {
    try {
      Statement statement = connection.createStatement();
      String query =
          "DELETE FROM user_token WHERE user_id = \'" + userId + "\' AND " + "user_token = "
              + "\'" + userToken + "\'";
      statement.executeUpdate(query);
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  @Override
  public DashboardList getDashboards(String id) {
    DashboardList.Builder list = DashboardList.newBuilder();
    try {
      Statement statement = connection.createStatement();
      String query = "SELECT * FROM dashboard WHERE user_id = \'" + id + "\'";
      ResultSet resultSet = statement.executeQuery(query);
      while (resultSet.next()) {
        list.addDashboards(Dashboard.newBuilder()
            .setId(resultSet.getString("id"))
            .setUserId(resultSet.getString("user_id"))
            .setName(resultSet.getString("name"))
            .build());
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }

    return list.build();
  }

  @Override
  public void addDashboard(Dashboard dashboard) {
    try {
      Statement statement = connection.createStatement();
      String query = "INSERT INTO dashboard(id, user_id, name) VALUES (\'"
          + dashboard.getId()
          + "\', "
          + "\'"
          + dashboard.getUserId()
          + "\', \'"
          + dashboard.getName()
          + "\') ";
      statement.executeUpdate(query);
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }
}
