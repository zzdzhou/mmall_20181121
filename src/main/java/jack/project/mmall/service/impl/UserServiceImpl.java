package jack.project.mmall.service.impl;

import jack.project.mmall.common.Constants;
import jack.project.mmall.common.ServerResponse;
import jack.project.mmall.dao.UserRepo;
import jack.project.mmall.entity.User;
import jack.project.mmall.service.IUserService;
import jack.project.mmall.util.MD5Util;
import javafx.print.PaperSource;
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

    private UserRepo userRepo;

    @Autowired
    public UserServiceImpl(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public ServerResponse<User> login(String username, String password) {
        int resultCount = userRepo.countByUsername(username);
        if (resultCount == 0) {
            return ServerResponse.createByErrorMsg("用户名不存在");
        }
        password = MD5Util.MD5EncodeUtf(password);
        User user = userRepo.findByUsernameAndPassword(username, password);
        if (user == null) {
            return ServerResponse.createByErrorMsg("密码错误");
        }
        user.setPassword(StringUtils.EMPTY);
        return ServerResponse.createBySuccess("登录成功", user);
    }

    @Override
    public ServerResponse<Integer> register(User user) {
        int count = userRepo.countByUsername(user.getUsername());
        if (count > 0) {
            return ServerResponse.createByErrorMsg("用户名已存在");
        }
        count = userRepo.countByEmail(user.getEmail());
        if (count > 0) {
            return ServerResponse.createByErrorMsg("邮箱已存在");
        }
        user.setRole(Constants.role.ROLE_CUSTOMMER);
        user.setPassword(MD5Util.MD5EncodeUtf(user.getPassword()));
        userRepo.save(user);
        return null;
    }

}
