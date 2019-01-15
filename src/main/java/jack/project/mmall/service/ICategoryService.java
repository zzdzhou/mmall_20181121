package jack.project.mmall.service;

import jack.project.mmall.common.ServerResponse;
import jack.project.mmall.entity.Category;

import java.util.List;
import java.util.Set;

public interface ICategoryService {

    ServerResponse<Category> addCategory(String name, Integer parentId);

    ServerResponse<Category> updateCategoryName(Integer categoryId, String newName);

    ServerResponse<List<Category>> getChildCategories(Integer parentId);

    ServerResponse<Set<Integer>> getCategoryIdAndAllChildCategoryIds(Integer parentId);

}
