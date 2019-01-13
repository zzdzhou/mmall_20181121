package jack.project.mmall.service.impl;

import jack.project.mmall.common.ServerResponse;
import jack.project.mmall.dao.CategoryRepo;
import jack.project.mmall.entity.Category;
import jack.project.mmall.service.ICategoryService;
import javafx.scene.effect.SepiaTone;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static jack.project.mmall.common.ResponseCode.*;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Theme:
 * <p>
 * Description:
 *
 * @author Zhengde ZHOU
 * Created on 2018-12-28
 */
@Service
public class CategoryServiceImpl implements ICategoryService {

    private CategoryRepo categoryRepo;

    @Autowired
    public CategoryServiceImpl(CategoryRepo categoryRepo) {
        this.categoryRepo = categoryRepo;
    }

    public ServerResponse<Category> addCategory(String name, Integer parentId) {
        if (StringUtils.isBlank(name)) {
            return ServerResponse.createByErrorMsg("参数错误");
        }
        if (parentId != null) {
            Optional<Category> parentOpt = categoryRepo.getById(parentId);
            if (!parentOpt.isPresent()) {
                return ServerResponse.createByErrorMsg(String.format("parent category %d 不存在", parentId));
            }
        }
        Category category = new Category();
        category.setName(name);
        category.setParentId(parentId);
        category.setStatus(true);
        Category result = categoryRepo.save(category);
        if (result == null) {
            return ServerResponse.createByErrorMsg("添加失败");
        }
        return ServerResponse.createBySuccess(result);
    }

    public ServerResponse<Category> updateCategoryName(Integer categoryId, String newName) {
        if (categoryId == null || StringUtils.isBlank(newName)) {
            return ServerResponse.createByResponseCode(ILLEGAL_ARGUMENT);
        }
        Optional<Category> categoryOpt = categoryRepo.getById(categoryId);
        if (!categoryOpt.isPresent()) {
            return ServerResponse.createByErrorMsg(String.format("Category %d 不存在", categoryId));
        }
        Category category = categoryOpt.get();
        category.setName(newName);
        return ServerResponse.createBySuccess(categoryRepo.save(category));
    }

    public ServerResponse<List<Category>> getPrimaryChildCategories(Integer parentId) {
        List<Category> returnedList;
        if (parentId == null) {
            returnedList = categoryRepo.getAllByParentIdIsNull();
        } else {
            returnedList = categoryRepo.getAllByParentId(parentId);
        }
        return ServerResponse.createBySuccess(returnedList);
    }

    private Set<Category> getAllChildCategories(Integer parentId) {
        HashSet<Category> categories = new HashSet<>();
        Optional<Category> categoryOpt = categoryRepo.getById(parentId);
        if (categoryOpt.isPresent()) {
            categories.add(categoryOpt.get());
        }
        ServerResponse<List<Category>> primaryChildCategories = getPrimaryChildCategories(parentId);

    }

}
