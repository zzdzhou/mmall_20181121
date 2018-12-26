package jack.project.mmall.controller.backend;

import jack.project.mmall.common.Constants;
import jack.project.mmall.common.ServerResponse;
import jack.project.mmall.entity.User;
import jack.project.mmall.pojo.UserResponse;
import jack.project.mmall.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * Theme:
 * <p>
 * Description:
 *
 * @author Zhengde ZHOU
 * Created on 2018-12-19
 */
@ResponseBody
@RequestMapping("/user/admin")
public class UserAdminController {

    private IUserService userService;

    @Autowired
    public UserAdminController(IUserService userService) {
        this.userService = userService;
    }

    @PostMapping("/loginAdmin")
    public ServerResponse<UserResponse> loginAdmin(HttpSession httpSession, String username, String password) {
        ServerResponse<UserResponse> response = userService.loginBackend(username, password);
        if (response.isSuccessful()) {
            httpSession.setAttribute(Constants.CURRENT_USER, response.getData());
        }
        return response;
    }
}
