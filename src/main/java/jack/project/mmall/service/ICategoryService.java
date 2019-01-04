package jack.project.mmall.service;

import jack.project.mmall.common.ServerResponse;
import jack.project.mmall.entity.Category;

import java.util.List;

public interface ICategoryService {

    ServerResponse<Category> addCategory(String name, Integer parentId);

    ServerResponse<Category> updateCategoryName(Integer categoryId, String newName);

    ServerResponse<List<Category>> getChildrenCategories(Integer parentId);
}
