package jack.project.mmall.service.impl;

import jack.project.mmall.common.Constants;
import jack.project.mmall.common.ServerResponse;
import jack.project.mmall.common.TokenCache;
import jack.project.mmall.dao.UserRepo;
import jack.project.mmall.entity.User;
import jack.project.mmall.service.IUserService;
import jack.project.mmall.util.MD5Util;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            return ServerResponse.createByErrorMsg("参数错误");
        }
        Optional<User> optUser = userRepo.getByUsername(username);
        if (!optUser.isPresent()) {
            return ServerResponse.createByErrorMsg("用户名不存在");
        }
        password = MD5Util.encodeUTF8(password);
        optUser = userRepo.findByUsernameAndPassword(username, password);
        if (!optUser.isPresent()) {
            return ServerResponse.createByErrorMsg("密码错误");
        }
        User user = optUser.get();
        user.setPassword(null);
        user.setAnswer(null);
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
        user.setPassword(MD5Util.encodeUTF8(user.getPassword()));
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(user.getCreateTime());
        int customerNumber = userRepo.save(user).getId();
        return ServerResponse.createBySuccess(customerNumber);
    }

    @Override
    public ServerResponse<Boolean> checkValid(String value, String type) {
        Optional<User> optUser;
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
        TokenCache.add(TokenCache.TOKEN_PREFIX + username, token);
        return ServerResponse.createBySuccess(token);
    }

    public static void main(String[] args) {
        System.out.println(UUID.randomUUID().toString());
    }

    public ServerResponse<String> resetPasswordForget(String username, String newPassword, String token) {
        // check if username exists or not
        Optional<User> userOpt = userRepo.getByUsername(username);
        if (!userOpt.isPresent()) {
            return ServerResponse.createByErrorMsg("User %s doesn't exist");
        }
        // check token
        // reset password if token is valid
        String cachedToken = TokenCache.get(TokenCache.TOKEN_PREFIX + username);
        if (token != null && !StringUtils.EMPTY.equals(cachedToken) && StringUtils.equals(token, cachedToken)) {
            User user = userOpt.get();
            user.setPassword(MD5Util.encodeUTF8(newPassword));
            userRepo.save(user);
            TokenCache.remove(TokenCache.TOKEN_PREFIX + username);
            return ServerResponse.createBySuccessMsg("密码重置成功");
        }
        return ServerResponse.createByErrorMsg("token 无效或过期");
    }

    public ServerResponse<String> resetPassword(User user, String oldPassword, String newPassword) {
        Optional<User> userOpt = userRepo.getByIdAndPassword(user.getId(), MD5Util.encodeUTF8(oldPassword));
        if (userOpt.isPresent()) {
            user.setPassword(MD5Util.encodeUTF8(newPassword));
            userRepo.save(user);
            return ServerResponse.createBySuccessMsg("密码修改成功");
        }
        return ServerResponse.createByErrorMsg("旧密码错误");
    }

    public ServerResponse<User> updateUser(User currentUser, User newUser) {
        Optional<User> userOpt = userRepo.getById(currentUser.getId());
        if (!userOpt.isPresent()) {
            ServerResponse.createByErrorMsg("当前 User 不存在: " + currentUser.getId());
        }
        currentUser = userOpt.get();
        if (newUser.getUsername() != null) {
            return ServerResponse.createByErrorMsg("username 无法更新");
        }
        String email = newUser.getEmail();
        String phone = newUser.getPhone();
        String question = newUser.getQuestion();
        String answer = newUser.getAnswer();

        if (email != null && !StringUtils.equals(currentUser.getEmail(), email)) {
            if (checkValid(email, Constants.EMAIL).getData()) {
                return ServerResponse.createByErrorMsg(String.format("邮箱 %s 已存在", email));
            } else {
                currentUser.setEmail(email);
            }
        }
        if (phone != null) {
            currentUser.setPhone(phone);
        }
        if (question != null) {
            currentUser.setQuestion(question);
        }
        if (answer != null) {
            currentUser.setAnswer(answer);
        }
        currentUser.setUpdateTime(LocalDateTime.now());
        currentUser = userRepo.save(currentUser);
        currentUser.setPassword(null);
        currentUser.setAnswer(null);
        if (currentUser != null) {
            return ServerResponse.createBySuccess(currentUser);
        }
        return ServerResponse.createByErrorMsg("更新失败");

    }

    public ServerResponse<User> getUserDetails(int userId) {
        Optional<User> optUser = userRepo.getById(userId);
        if (optUser.isPresent()) {
            User user = optUser.get();
            user.setPassword(null);
            return ServerResponse.createBySuccess(user);
        }
        return ServerResponse.createByErrorMsg("用户不存在");
    }

    public ServerResponse<User> loginBackend(String username, String password) {
        ServerResponse<User> response = this.login(username, password);
        if (response.isSuccessful() && Constants.role.ROLE_ADMIN != response.getData().getRole()) {
            return ServerResponse.createByErrorMsg("不是管理员，无法登陆");
        }
        return response;
    }

}
