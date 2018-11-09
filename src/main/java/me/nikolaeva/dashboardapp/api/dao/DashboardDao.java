package me.nikolaeva.dashboardapp.api.dao;

import me.nikolaeva.dashboardapp.proto.PostList;
import me.nikolaeva.dashboardapp.proto.User;

public interface DashboardDao {

  PostList getPosts();

  User getUserByUserToken(String userToken);

  User getUserByProfileId(String profileId);

  void createUser(User user);

  void addUserToken(String userId, String userToken);


}
