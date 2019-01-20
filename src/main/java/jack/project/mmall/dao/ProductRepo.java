package jack.project.mmall.dao;

import jack.project.mmall.entity.Product;
import org.springframework.data.repository.Repository;

import java.util.Optional;

/**
 * Theme:
 * <p>
 * Description:
 *
 * @author Zhengde ZHOU
 * Created on 2019-01-19
 */
public interface ProductRepo extends Repository<Product, Integer> {

    Optional<Product> getById(Integer id);

    Product save(Product product);

}
