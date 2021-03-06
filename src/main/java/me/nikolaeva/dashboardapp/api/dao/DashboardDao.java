package me.nikolaeva.dashboardapp.api.dao;

import me.nikolaeva.dashboardapp.proto.Dashboard;
import me.nikolaeva.dashboardapp.proto.DashboardList;
import me.nikolaeva.dashboardapp.proto.Post;
import me.nikolaeva.dashboardapp.proto.PostList;
import me.nikolaeva.dashboardapp.proto.User;

public interface DashboardDao {

  PostList getPosts(String dashboardId);

  void addPost(Post post);

  void deletePost(String id);

  void updatePost(Post post);

  User getUserByUserToken(String userToken);

  User getUserByProfileId(String profileId);

  void createUser(User user);

  void addUserToken(String userId, String userToken);

  void deleteUserToken(String userId, String userToken);

  DashboardList getDashboards(String id);

  void addDashboard(Dashboard dashboard);

  void addDashboardPermission(User sharee);

  // get all dashboards which are shared with the user.
  DashboardList getDashboardsSharedWithUser(String id);


}
