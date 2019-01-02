package jack.project.mmall.controller.backend;

import jack.project.mmall.common.Constants;
import jack.project.mmall.common.ServerResponse;
import jack.project.mmall.entity.Category;
import jack.project.mmall.entity.User;
import jack.project.mmall.service.ICategoryService;
import jack.project.mmall.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

/**
 * Theme:
 * <p>
 * Description:
 *
 * @author Zhengde ZHOU
 * Created on 2018-12-27
 */
@RestController
@RequestMapping("/category")
public class CategoryAdminController {

    private ICategoryService categoryService;

    private IUserService userService;

    @Autowired
    public CategoryAdminController(ICategoryService categoryService, IUserService userService) {
        this.categoryService = categoryService;
        this.userService = userService;
    }

    @GetMapping("/add")
    public ServerResponse<Category> addCategory(@RequestParam String name, @RequestParam(required = false) int parentId, HttpSession session) {
        ServerResponse isAdmin = UserAdminController.checkAdminRole(session, userService);
        if (!isAdmin.isSuccessful()) {
            return isAdmin;
        }
        return categoryService.addCategory(name, parentId);
    }

    @GetMapping("/update")
    public ServerResponse<Category> updateCategoryName(HttpSession session, @RequestParam String oldName, @RequestParam String newName) {
        ServerResponse isAdmin = UserAdminController.checkAdminRole(session, userService);
        if (!isAdmin.isSuccessful()) {
            return isAdmin;
        }
        return null;
    }

}
