package jack.project.mmall.service.impl;

import jack.project.mmall.common.ServerResponse;
import jack.project.mmall.dao.UserRepo;
import jack.project.mmall.entity.User;
import jack.project.mmall.service.IUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Theme:
 * <p>
 * Description:
 *
 * @author Zhengde ZHOU
 * Created on 2018-12-01
 */
@Service("iUserService")
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserRepo userRepo;

    @Override
    public ServerResponse<User> login(String username, String password) {
        int resultCount = userRepo.countByUsername(username);
        if (resultCount == 0) {
            return ServerResponse.createByErrorMsg("用户名不存在");
        }

        // todo MD5 密码登录

        User user = userRepo.findByUsernameAndPassword(username, password);
        if (user == null) {
            return ServerResponse.createByErrorMsg("密码错误");
        }
        user.setPassword(StringUtils.EMPTY);
        return ServerResponse.createBySuccess("登录成功", user);
    }

}
