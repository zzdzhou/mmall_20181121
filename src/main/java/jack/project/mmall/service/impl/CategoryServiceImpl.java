package jack.project.mmall.service.impl;

import jack.project.mmall.common.ServerResponse;
import jack.project.mmall.dao.CategoryRepo;
import jack.project.mmall.entity.Category;
import jack.project.mmall.service.ICategoryService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static jack.project.mmall.common.ResponseCode.ILLEGAL_ARGUMENT;

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
//        if (parentId != null) {
        Optional<Category> parentOpt = categoryRepo.getById(parentId);
        if (!parentOpt.isPresent()) {
            return ServerResponse.createByErrorMsg(String.format("parent category %d 不存在", parentId));
        }
//        }
        Category category = new Category();
        category.setName(name);
        category.setParentId(parentOpt.get().getId());
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

    /**
     * get primary child categories for a parentId
     * if parentId is null, return all root categories
     * @param parentId
     * @return
     */
    public ServerResponse<List<Category>> getChildCategories(Integer parentId) {
        List<Category> returnedList;
        if (parentId == null) {
            returnedList = categoryRepo.getAllByParentIdIsNull();
        } else {
            returnedList = categoryRepo.getAllByParentId(parentId);
        }
        return ServerResponse.createBySuccess(returnedList);
    }

    public ServerResponse<Set<Integer>> getCategoryIdAndAllChildCategoryIds(Integer parentId) {
        HashSet<Integer> categoryIds = new HashSet<>();
        Set<Category> categories;
        try {
            categories = getCategoryAndAllChildCategories(parentId);
        } catch (Exception e) {
            return ServerResponse.createByErrorMsg(e.getMessage());
        }
        for (Category item : categories) {
            categoryIds.add(item.getId());
        }
        return ServerResponse.createBySuccess(categoryIds);
    }


    //  ======================== private ========================
    /**
     * get all child categories for a parentId by recursion algorithm
     * if parentId is null, throws a {@link Exception}
     * @param parentId
     * @return
     * @throws Exception
     */
    private Set<Category> getCategoryAndAllChildCategories(Integer parentId) throws Exception {
        if (parentId == null) {
            throw new Exception("parentId 不能为空");
        }
        HashSet<Category> categories = new HashSet<>();
        Optional<Category> categoryOpt = categoryRepo.getById(parentId);
        if (!categoryOpt.isPresent()) {
            return categories;
        }
        categories.add(categoryOpt.get());
        // 递归算法一定要有一个退出的条件. 查找自己点，若没有自己点，就退出
        List<Category> allByParentId = categoryRepo.getAllByParentId(parentId);
        for (Category item : allByParentId) {
            categories.addAll(getCategoryAndAllChildCategories(item.getId()));
        }
        return categories;
    }



}
