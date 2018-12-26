package jack.project.mmall.service;

import jack.project.mmall.common.ServerResponse;
import jack.project.mmall.entity.User;
import jack.project.mmall.pojo.UserResponse;

/**
 * Theme:
 * <p>
 * Description:
 *
 * @author Zhengde ZHOU
 * Created on 2018-12-01
 */
public interface IUserService {

    ServerResponse<UserResponse> login(String username, String password);

    ServerResponse<Integer> register(User user);

    ServerResponse<Boolean> checkValid(String value, String type);

    ServerResponse<String> getQuestion(String username);

    ServerResponse<String> checkAnswer(String username, String question, String answer);

    ServerResponse<String> resetPasswordForget(String username, String newPassword, String token);

    ServerResponse<String> resetPassword(int userId, String oldPassword, String newPassword);

    ServerResponse<UserResponse> updateUser(int userId, User newUser);

    ServerResponse<UserResponse> getUserDetails(int userId);

    // backend service
    ServerResponse<UserResponse> loginBackend(String username, String password);
}
