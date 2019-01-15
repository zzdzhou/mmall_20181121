package jack.project.mmall.controller.backend;

import jack.project.mmall.common.Constants;
import jack.project.mmall.common.ResponseCode;
import jack.project.mmall.common.ServerResponse;
import jack.project.mmall.entity.User;
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
    public ServerResponse<User> loginAdmin(HttpSession httpSession, String username, String password) {
        ServerResponse<User> response = userService.loginBackend(username, password);
        if (response.isSuccessful()) {
            httpSession.setAttribute(Constants.CURRENT_USER, response.getData());
        }
        return response;
    }


    static ServerResponse checkAdminRole(HttpSession session, IUserService userService) {
        User user = (User) session.getAttribute(Constants.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByError(ResponseCode.NEED_LOGIN.getCode(), "用户未登陆");
        } else if (!userService.isAdminRole(user.getId())) {
            return ServerResponse.createByErrorMsg("需要管理员权限");
        }
        return ServerResponse.createBySuccess();
    }
}
