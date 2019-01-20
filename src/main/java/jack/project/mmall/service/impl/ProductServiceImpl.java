package jack.project.mmall.service.impl;

import com.mysql.fabric.Server;
import jack.project.mmall.common.ResponseCode;
import jack.project.mmall.common.ServerResponse;
import jack.project.mmall.dao.ProductRepo;
import jack.project.mmall.entity.Product;
import jack.project.mmall.service.IProductService;
import jack.project.mmall.util.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.procedure.ProcedureOutputs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Theme:
 * <p>
 * Description:
 *
 * @author Zhengde ZHOU
 * Created on 2019-01-19
 */
@Service
public class ProductServiceImpl implements IProductService {

    private ProductRepo productRepo;

    @Autowired
    public ProductServiceImpl(ProductRepo productRepo) {
        this.productRepo = productRepo;
    }

    public ServerResponse<Product> saveOrUpdateProduct(Product product, boolean updateAllFields) {
        if (product != null) {
            if (StringUtils.isNotBlank(product.getSubImages())) {
                String[] split = product.getSubImages().split(",");
                if (split.length > 0) {
                    product.setMainImage(split[0]);
                }
            }
            Product save = new Product();
            if (productRepo.getById(product.getId()).isPresent()) {
                // update
                if (updateAllFields) {
                    save = product;
                } else {
                    BeanUtils.copyPropertiesExceptNull(product, save, new String[]{"createTime", "updateTime"});
                }
                save.setUpdateTime(LocalDateTime.now());
            } else {
                // save
                save = product;
                save.setCreateTime(LocalDateTime.now());
            }
            return ServerResponse.createBySuccess(productRepo.save(product));
        }
        return ServerResponse.createByErrorMsg("参数不能为空");
    }

    public ServerResponse<Product> setProductStatus(Integer productId, Integer status) {
        if (productId != null) {
            Optional<Product> productOpt = productRepo.getById(productId);
            if (productOpt.isPresent()) {
                if (Product.Status.resolve(status) == null) {
                    return ServerResponse.createByError(ResponseCode.ILLEGAL_ARGUMENT.getCode(), "参数 status 不合法");
                }
                Product product = productOpt.get();
                product.setStatus(status);
                Product updated = productRepo.save(product);
                if (updated != null) {
                    return ServerResponse.createBySuccess(updated);
                }
                return ServerResponse.createByErrorMsg("更新失败");
            }
            return ServerResponse.createByError(ResponseCode.ILLEGAL_ARGUMENT.getCode(), "productId 不存在");
        }
        return ServerResponse.createByError(ResponseCode.ILLEGAL_ARGUMENT.getCode(), "参数 productId 不能为空");
    }
}
