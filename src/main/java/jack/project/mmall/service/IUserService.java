package jack.project.mmall.service;

import jack.project.mmall.common.ServerResponse;
import jack.project.mmall.entity.User;

/**
 * Theme:
 * <p>
 * Description:
 *
 * @author Zhengde ZHOU
 * Created on 2018-12-01
 */
public interface IUserService {

    ServerResponse<User> login(String username, String password);

    ServerResponse<User> register(User user);
}
