package jack.project.mmall.controller.portal;

import jack.project.mmall.common.Constants;
import jack.project.mmall.common.ServerResponse;
import jack.project.mmall.entity.User;
import jack.project.mmall.service.IUserService;
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
    public ServerResponse<String> register(User user) {
        return null;
    }

}
