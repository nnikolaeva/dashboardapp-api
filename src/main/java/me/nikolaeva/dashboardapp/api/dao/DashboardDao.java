package me.nikolaeva.dashboardapp.api.dao;

import me.nikolaeva.dashboardapp.proto.PostList;
import me.nikolaeva.dashboardapp.proto.User;

public interface DashboardDao {
  PostList getPosts();

  User getUserByUserToken(String userToken);


}
