package jack.project.mmall.controller.portal;

import jack.project.mmall.common.Constants;
import jack.project.mmall.common.ServerResponse;
import jack.project.mmall.entity.User;
import jack.project.mmall.pojo.UserResponse;
import jack.project.mmall.service.IUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
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

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private IUserService userService;

    @Autowired
    public UserController(IUserService userService) {
        this.userService = userService;
    }

    @PostMapping(value = "/login", consumes = "application/json", produces = "application/json")
    public ServerResponse<UserResponse> login(@RequestBody Map<String, String> model, HttpSession httpSession) {
        ServerResponse<UserResponse> res = userService.login(model.get("username"), model.get("password"));
        if (res.isSuccessful()) {
            httpSession.setAttribute(Constants.CURRENT_USER, res.getData());
//            httpSession.setMaxInactiveInterval(5);
            logger.info("http session timeout: {}", httpSession.getMaxInactiveInterval());
        }
        return res;
    }

    @GetMapping(value = "/logout", consumes = "application/json", produces = "application/json")
    public ServerResponse<String> logout(HttpServletRequest request) {
        //httpSession.removeAttribute(Constants.CURRENT_USER);
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return ServerResponse.createBySuccess("退出登录成功");

    }

    @PostMapping(value = "/register", consumes = "application/json", produces = "application/json")
    public ServerResponse<Integer> register(@RequestBody User user) {
        return userService.register(user);
    }

    @PostMapping(value = "/isValid", consumes = "application/json", produces = "application/json")
    public ServerResponse<Boolean> checkValid(@RequestBody Map<String, String> model) {
        return userService.checkValid(model.get("value"), model.get("type"));
    }

    @GetMapping(value = "/getUserInfo", consumes = "application/json", produces = "application/json")
    public ServerResponse<UserResponse> getUserInfo(HttpSession httpSession) {
        UserResponse userResponse = (UserResponse) httpSession.getAttribute(Constants.CURRENT_USER);
        if (userResponse == null) {
            return ServerResponse.createByErrorMsg("用户未登录，无法获取当前用户信息！");
        }
        return ServerResponse.createBySuccess(userResponse);
    }

    @GetMapping(value = "/getUserDetails", consumes = "application/json", produces = "application/json")
    public ServerResponse<UserResponse> getUserDetails(HttpSession httpSession) {
        UserResponse userResponse = (UserResponse) httpSession.getAttribute(Constants.CURRENT_USER);
        if (userResponse == null) {
            return ServerResponse.createByErrorMsg("用户未登录，无法获取当前用户信息！");
        }
        return userService.getUserDetails(userResponse.getId());
    }

    @PostMapping(value = "/forgetPassword/question", consumes = "application/json", produces = "application/json")
    public ServerResponse<String> getQuestion(@RequestBody Map<String, String> model) {
        return userService.getQuestion(model.get("username"));
    }

    @PostMapping(value = "/forgetPassword/answer", consumes = "application/json", produces = "application/json")
    public ServerResponse<String> checkAnswer(@RequestBody Map<String, String> model) {
        return userService.checkAnswer(model.get("username"), model.get("question"), model.get("answer"));
    }

    @PostMapping(value = "/forgetPassword/reset", consumes = "application/json", produces = "application/json")
    public ServerResponse<String> resetPasswordForget(@RequestBody Map<String, String> model) {
        return userService.resetPasswordForget(model.get("username"), model.get("newPassword"), model.get("token"));
    }

    @PostMapping(value = "/resetPassword", consumes = "application/json", produces = "application/json")
    public ServerResponse<String> resetPassword(HttpSession httpSession, @RequestBody Map<String, String> model) {
        UserResponse userResponse = (UserResponse) httpSession.getAttribute(Constants.CURRENT_USER);
        if (userResponse == null) {
            return ServerResponse.createByErrorMsg("用户未登录");
        }
        return userService.resetPassword(userResponse.getId(), model.get("oldPassword"), model.get("newPassword"));
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
    public ServerResponse<UserResponse> updateUser(HttpSession httpSession, @RequestBody User user) {
        UserResponse currentUser = (UserResponse) httpSession.getAttribute(Constants.CURRENT_USER);
        // 必须先登录，才能修改用户信息
        // Q: 如何保证修改的是当前登录的用户的信息，而非其他用户？
        // A: userService.updateUser(currentUser, user) --> userRepo.save(currentUser); currentUser.id 始终没有改变
        if (currentUser == null) {
            return ServerResponse.createByErrorMsg("用户未登录");
        }
        ServerResponse<UserResponse> response = userService.updateUser(currentUser.getId(), user);
        if (response.isSuccessful()) {
            httpSession.setAttribute(Constants.CURRENT_USER, response.getData());
        }
        return response;
    }

}
