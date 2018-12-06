package jack.project.mmall.service.impl;

import jack.project.mmall.common.Constants;
import jack.project.mmall.common.ServerResponse;
import jack.project.mmall.dao.UserRepo;
import jack.project.mmall.entity.User;
import jack.project.mmall.service.IUserService;
import jack.project.mmall.util.MD5Util;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

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
        Optional<User> optUser = userRepo.getByUsername(username);
        if (!optUser.isPresent()) {
            return ServerResponse.createByErrorMsg("用户名不存在");
        }
        password = MD5Util.MD5EncodeUtf(password);
        optUser = userRepo.findByUsernameAndPassword(username, password);
        if (!optUser.isPresent()) {
            return ServerResponse.createByErrorMsg("密码错误");
        }
        User user = optUser.get();
        user.setPassword(StringUtils.EMPTY);
        return ServerResponse.createBySuccess("登录成功", user);
    }

    @Override
    public ServerResponse<Integer> register(User user) {
        Optional<User> optUser = userRepo.getByUsername(user.getUsername());
        if (optUser.isPresent()) {
            return ServerResponse.createByErrorMsg("用户名已存在");
        }
        optUser = userRepo.getByEmail(user.getEmail());
        if (optUser.isPresent()) {
            return ServerResponse.createByErrorMsg("邮箱已存在");
        }
        user.setRole(Constants.role.ROLE_CUSTOMMER);
        user.setPassword(MD5Util.MD5EncodeUtf(user.getPassword()));
        int customerNumber = userRepo.save(user);
        return ServerResponse.createBySuccess(customerNumber);
    }

    @Override
    public ServerResponse<Boolean> checkValid(String value, String type) {
        Optional<User> optUser = null;
        if (Constants.USERNAME.equals(type)) {
            optUser = userRepo.getByUsername(value);
            if (!optUser.isPresent()) {
                return ServerResponse.createBySuccess("用户名不存在", false);
            }
        } else if (Constants.EMAIL.equals(type)) {
            optUser = userRepo.getByEmail(value);
            if (!optUser.isPresent()) {
                return ServerResponse.createBySuccess("邮箱不存在", false);
            }
        } else {
            return ServerResponse.createByErrorMsg("type参数错误");
        }
        return ServerResponse.createBySuccess("用户存在", true);
    }

    public ServerResponse<String> getQuestion(String username) {
        ServerResponse<Boolean> result = checkValid(username, Constants.USERNAME);
        if (result.getData()) {
            String question = userRepo.getQuestionByUsername(username);
            if (StringUtils.isNotEmpty(question)) {
                return ServerResponse.createBySuccess(question);
            }
            return ServerResponse.createByErrorMsg("该用户没有设置忘记密码问题");
        }
        return ServerResponse.createByErrorMsg("用户名不存在");
    }

    public ServerResponse<String> checkAnswer(String username, String question, String answer) {
        Optional<User> optUser = userRepo.getByUsername(username);
        if (!optUser.isPresent()) {
            return ServerResponse.createByErrorMsg("用户名不存在");
        }
        User user = optUser.get();
        if (StringUtils.isEmpty(user.getQuestion())) {
            return ServerResponse.createByErrorMsg("该用户没有设置忘记密码的问题");
        } else if (!user.getQuestion().equals(question)) {
            return ServerResponse.createByErrorMsg("问题不正确");
        }
        if (user.getAnswer() == null || !user.getAnswer().equals(answer)) {
            return ServerResponse.createByErrorMsg("答案不正确");
        }
        String token = UUID.randomUUID().toString();
        return ServerResponse.createBySuccess("答案正确");
    }

    public static void main(String[] args) {
        System.out.println(UUID.randomUUID().toString());
    }
}
