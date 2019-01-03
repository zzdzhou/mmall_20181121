package jack.project.mmall.dao;

import jack.project.mmall.entity.Category;
import org.springframework.data.repository.Repository;

import java.util.Optional;

public interface CategoryRepo extends Repository<Category, Integer> {

    Optional<Category> getById(int id);

    Category save(Category category);

    Category getByName(String name);

}
