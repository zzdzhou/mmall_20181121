package jack.project.mmall.service.impl;

import jack.project.mmall.common.ServerResponse;
import jack.project.mmall.dao.CategoryRepo;
import jack.project.mmall.entity.Category;
import jack.project.mmall.service.ICategoryService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public ServerResponse<String> addCategory(String name, Integer parentId) {
        if (StringUtils.isBlank(name) || parentId == null) {
            return ServerResponse.createByErrorMsg("参数错误");
        }
        Category category = new Category();
        category.setName(name);
        category.setParentId(parentId);


        return null;
    }

}
