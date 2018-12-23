package jack.project.mmall.controller.portal;

import jack.project.mmall.common.Constants;
import jack.project.mmall.common.ServerResponse;
import jack.project.mmall.entity.User;
import jack.project.mmall.service.IUserService;
import org.apache.tomcat.util.bcel.Const;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * Theme:
 * <p>
 * Description:
 *
 * @author Zhengde ZHOU
 * Created on 2018-11-23
 */
@RestController
@RequestMapping("/user")
public class UserController {

    private IUserService userService;

    @Autowired
    public UserController(IUserService userService) {
        this.userService = userService;
    }

    @PostMapping(value = "/login", consumes = "application/json", produces = "application/json")
    public ServerResponse<User> login(String username, String password, HttpSession httpSession) {
        ServerResponse<User> res = userService.login(username, password);
        if (res.isSuccessful()) {
            httpSession.setAttribute(Constants.CURRENT_USER, res.getData());
        }
        return res;
    }

    @GetMapping(value = "/logout", consumes = "application/json", produces = "application/json")
    public ServerResponse<String> logout(HttpSession httpSession) {
        httpSession.removeAttribute(Constants.CURRENT_USER);
        return ServerResponse.createBySuccess();
    }

    @PostMapping(value = "/register", consumes = "application/json", produces = "application/json")
    public ServerResponse<Integer> register(@RequestBody User user) {
        return userService.register(user);
    }

    @PostMapping(value = "/isValid", consumes = "application/json", produces = "application/json")
    public ServerResponse<Boolean> checkValid(@RequestBody Map<String, String> model) {
        String value = model.get("value");
        String type = model.get("type");
        return userService.checkValid(value, type);
    }

    @GetMapping(value = "/getUserInfo", consumes = "application/json", produces = "application/json")
    public ServerResponse<User> getUserInfo(HttpSession httpSession) {
        User user = (User) httpSession.getAttribute(Constants.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorMsg("用户未登录，无法获取当前用户信息！");
        }
        return ServerResponse.createBySuccess(user);
    }

    @GetMapping(value = "/getUserDetails", consumes = "application/json", produces = "application/json")
    public ServerResponse<User> getUserDetails(HttpSession httpSession) {
        User user = (User) httpSession.getAttribute(Constants.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorMsg("用户未登录，无法获取当前用户信息！");
        }
        return userService.getUserDetails(user.getId());
    }

    @PostMapping(value = "/forgetPassword/question", consumes = "application/json", produces = "application/json")
    public ServerResponse<String> getQuestion(@RequestBody Map<String, String> model) {
        return userService.getQuestion(model.get("username"));
    }

    @PostMapping(value = "/forgetPassword/answer", consumes = "application/json", produces = "application/json")
    public ServerResponse<String> checkAnswer(String username, String question, String answer) {
        return userService.checkAnswer(username, question, answer);
    }

    @PostMapping(value = "/forgetPassword/reset", consumes = "application/json", produces = "application/json")
    public ServerResponse<String> resetPasswordForget(String username, String newPassword, String token) {
        return userService.resetPasswordForget(username, newPassword, token);
    }

    @PostMapping(value = "/resetPassword", consumes = "application/json", produces = "application/json")
    public ServerResponse<String> resetPassword(HttpSession httpSession, String username, String oldPassword, String newPassword) {
        User user = (User) httpSession.getAttribute(Constants.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorMsg("用户未登录");
        }
        return userService.resetPassword(user, oldPassword, newPassword);
    }

    /**
     * 1. 只能更新 参数 user 中不为 null 的 email， phone, question, answer 字段
     * 2. 验证 email 是否已存在
     * 3. 注意更新 updateTime
     * 4. 必须先登录，才能更新用户信息
     * 5. 保证修改的是当前登录的用户的信息，而非其他用户
     * @param httpSession
     * @param user
     * @return
     */
    @PostMapping(value = "/updateUser", consumes = "application/json", produces = "application/json")
    public ServerResponse<User> updateUser(HttpSession httpSession, User user) {
        User currentUser = (User) httpSession.getAttribute(Constants.CURRENT_USER);
        // 必须先登录，才能修改用户信息
        // Q: 如何保证修改的是当前登录的用户的信息，而非其他用户？
        // A: userService.updateUser(currentUser, user) --> userRepo.save(currentUser); currentUser.id 始终没有改变
        if (currentUser == null) {
            return ServerResponse.createByErrorMsg("用户未登录");
        }
        ServerResponse<User> response = userService.updateUser(currentUser, user);
        if (response.isSuccessful()) {
            httpSession.setAttribute(Constants.CURRENT_USER, response.getData());
        }
        return response;
    }


}
