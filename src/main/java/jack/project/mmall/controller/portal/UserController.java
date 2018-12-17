package jack.project.mmall.controller.portal;

import jack.project.mmall.common.Constants;
import jack.project.mmall.common.ServerResponse;
import jack.project.mmall.entity.User;
import jack.project.mmall.service.IUserService;
import org.apache.tomcat.util.bcel.Const;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

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

    @PostMapping("/login")
    public ServerResponse<User> login(String username, String password, HttpSession httpSession) {
        ServerResponse<User> res = userService.login(username, password);
        if (res.isSuccessful()) {
            httpSession.setAttribute(Constants.CURRENT_USER, res.getData());
        }
        return res;
    }

    @GetMapping("/logout")
    public ServerResponse<String> logout(HttpSession httpSession) {
        httpSession.removeAttribute(Constants.CURRENT_USER);
        return ServerResponse.createBySuccess();
    }

    @PostMapping("/register")
    public ServerResponse<Integer> register(User user) {
        return userService.register(user);
    }

    @PostMapping("/isValid")
    public ServerResponse<Boolean> checkValid(String value, String type) {
        return userService.checkValid(value, type);
    }

    @GetMapping("/userInfo")
    public ServerResponse<User> getUserInfo(HttpSession httpSession) {
        User user = (User) httpSession.getAttribute(Constants.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorMsg("用户未登录，无法获取当前用户信息！");
        }
        return ServerResponse.createBySuccess(user);
    }

    @PostMapping("/resetpassword/question")
    public ServerResponse<String> getQuestion(String username) {
        return userService.getQuestion(username);
    }

    @PostMapping("/resetpassword/answer")
    public ServerResponse<String> checkAnswer(String username, String question, String answer) {
        return userService.checkAnswer(username, question, answer);
    }

    @PostMapping("/resetpassword/forget")
    public ServerResponse<String> resetPasswordForget(String username, String newPassword, String token) {
        return userService.resetPasswordForget(username, newPassword, token);
    }

}
