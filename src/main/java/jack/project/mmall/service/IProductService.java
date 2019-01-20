package jack.project.mmall.service;

import jack.project.mmall.common.ServerResponse;
import jack.project.mmall.entity.Product;

/**
 * Theme:
 * <p>
 * Description:
 *
 * @author Zhengde ZHOU
 * Created on 2019-01-19
 */
public interface IProductService {

    ServerResponse<Product> saveOrUpdateProduct(Product product, boolean updateAllFields);

    ServerResponse<Product> setProductStatus(Integer productId, Integer status);

}
