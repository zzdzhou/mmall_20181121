package jack.project.mmall.service.impl;

import jack.project.mmall.common.ServerResponse;
import jack.project.mmall.dao.CategoryRepo;
import jack.project.mmall.entity.Category;
import jack.project.mmall.service.ICategoryService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

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

    public ServerResponse<Category> updateCategoryName(String oldName, String newName) {
//        categoryRepo.getById()
        return null;
    }

}
