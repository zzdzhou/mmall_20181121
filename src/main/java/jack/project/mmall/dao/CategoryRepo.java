package jack.project.mmall.dao;

import jack.project.mmall.entity.Category;
import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepo extends Repository<Category, Integer> {

    Optional<Category> getById(Integer id);

    Category save(Category category);

    List<Category> getAllByParentIdIsNull();

    List<Category> getAllByParentId(Integer parentId);

}
