package jack.project.mmall.controller.backend;

import jack.project.mmall.common.ServerResponse;
import jack.project.mmall.entity.Category;
import jack.project.mmall.service.ICategoryService;
import jack.project.mmall.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Set;

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
    public ServerResponse<Category> addCategory(
            @RequestParam String name, @RequestParam(required = false) Integer parentId, HttpSession session) {
        ServerResponse isAdmin = UserAdminController.checkAdminRole(session, userService);
        if (!isAdmin.isSuccessful()) {
            return ServerResponse.createByError(isAdmin.getCode(), isAdmin.getMsg());
        }
        return categoryService.addCategory(name, parentId);
    }

    @GetMapping("/update")
    public ServerResponse<Category> updateCategoryName(HttpSession session, @RequestParam Integer categoryId, @RequestParam String newName) {
        ServerResponse isAdmin = UserAdminController.checkAdminRole(session, userService);
        if (!isAdmin.isSuccessful()) {
            return ServerResponse.createByError(isAdmin.getCode(), isAdmin.getMsg());
        }
        return categoryService.updateCategoryName(categoryId, newName);
    }

    @GetMapping("/get_child_category")
    public ServerResponse<List<Category>> getChildrenCategories(HttpSession session, Integer parentId) {
        ServerResponse isAdmin = UserAdminController.checkAdminRole(session, userService);
        if (!isAdmin.isSuccessful()) {
            return ServerResponse.createByError(isAdmin.getCode(), isAdmin.getMsg());
        }
        return categoryService.getChildCategories(parentId);
    }

    @PostMapping("/get_all_child_categories")
    public ServerResponse<Set<Integer>> getCategoryIdAndAllChildCategoryIds(HttpSession session, @RequestParam Integer parentId) {
        ServerResponse isAdmin = UserAdminController.checkAdminRole(session, userService);
        if (!isAdmin.isSuccessful()) {
            return ServerResponse.createByError(isAdmin.getCode(), isAdmin.getMsg());
        }
        return categoryService.getCategoryIdAndAllChildCategoryIds(parentId);
    }

}
