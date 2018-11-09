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

  @Override
  public void addPost(Post post) {
    try {
      Statement statement = connection.createStatement();
      String query = "INSERT INTO post(id, user_id, content) VALUES (\'"
          + post.getId()
          + "\', "
          + "\'"
          + post.getUserId()
          + "\', \'"
          + post.getContent()
          + "\') ";
      System.out.println(query);
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
      System.out.println(sql);
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
              .setGoogleProfileId(resultSet.getString("google_profile_id"))
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
  public User getUserByProfileId(String profileId) {
    User user = null;
    try {
      Statement statement = connection.createStatement();
      String query = "SELECT * FROM users WHERE google_profile_id = \'" + profileId + "\'";
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
          "INSERT INTO users (id, name, email, google_profile_id) values (\'"
              + user.getId()
              + "\', \'"
              + user.getName()
              + "\', \'"
              + user.getEmail()
              + "\', \'"
              + user.getGoogleProfileId()
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
}
