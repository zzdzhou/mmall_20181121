package jack.project.mmall.dao;

import jack.project.mmall.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.QueryByExampleExecutor;

import java.util.List;
import java.util.Optional;

/**
 * Theme:
 * <p>
 * Description:
 *
 * @author Zhengde ZHOU
 * Created on 2019-01-19
 */
public interface ProductRepo extends Repository<Product, Integer>, QueryByExampleExecutor<Product> {

    Optional<Product> getById(Integer id);

    Product save(Product product);

    Page<Product> findAll(Pageable pageable);

}

