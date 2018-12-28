package jack.project.mmall.controller.backend;

import jack.project.mmall.common.Constants;
import jack.project.mmall.common.ServerResponse;
import jack.project.mmall.entity.Category;
import jack.project.mmall.entity.User;
import jack.project.mmall.service.ICategoryService;
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

    @Autowired
    public CategoryAdminController(ICategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/add")
    public ServerResponse<Category> addCategory(@RequestParam String name, @RequestParam int parentId, HttpSession session) {
        User user = (User) session.getAttribute(Constants.CURRENT_USER);
        if (user != null) {

        }
        return categoryService.addCategory(name, parentId);
    }

}
