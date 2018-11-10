package me.nikolaeva.dashboardapp.api.dao;

import me.nikolaeva.dashboardapp.proto.Post;
import me.nikolaeva.dashboardapp.proto.PostList;
import me.nikolaeva.dashboardapp.proto.User;

public interface DashboardDao {

  PostList getPosts(String id);

  void addPost(Post post);

  void deletePost(String id);

  void updatePost(Post post);

  User getUserByUserToken(String userToken);

  User getUserByProfileId(String profileId);

  void createUser(User user);

  void addUserToken(String userId, String userToken);


}
