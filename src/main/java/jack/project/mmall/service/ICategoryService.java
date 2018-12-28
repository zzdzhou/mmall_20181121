package jack.project.mmall.service;

import jack.project.mmall.common.ServerResponse;
import jack.project.mmall.entity.Category;

public interface ICategoryService {

    ServerResponse<Category> addCategory(String name, Integer parentId);

}
