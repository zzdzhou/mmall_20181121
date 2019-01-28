package jack.project.mmall.service.impl;

import jack.project.mmall.common.ResponseCode;
import jack.project.mmall.common.ServerResponse;
import jack.project.mmall.dao.CategoryRepo;
import jack.project.mmall.dao.ProductRepo;
import jack.project.mmall.entity.Category;
import jack.project.mmall.entity.Product;
import jack.project.mmall.service.IProductService;
import jack.project.mmall.util.BeanUtils;
import jack.project.mmall.vo.ProductVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${ftp.server.ip}")
    private String imageHost;

    private CategoryRepo categoryRepo;

    private ProductRepo productRepo;

    @Autowired
    public ProductServiceImpl(CategoryRepo categoryRepo, ProductRepo productRepo) {
        this.categoryRepo = categoryRepo;
        this.productRepo = productRepo;
    }

    public ServerResponse<ProductVO> saveOrUpdateProduct(ProductVO productVO, boolean updateAllFields) {
        if (productVO != null) {
            if (StringUtils.isNotBlank(productVO.getSubImages())) {
                String[] split = productVO.getSubImages().split(",");
                if (split.length > 0) {
                    productVO.setMainImage(split[0]);
                }
            }
            Product product = new Product();
            if (productRepo.getById(productVO.getId()).isPresent() && !updateAllFields && productVO.getCategoryId() != null) {

            }

            // if !productRepo.getById(productVO.getId()).isPresent(), 需要验证 productVO.getCategoryId()
            // if productRepo.getById(productVO.getId()).isPresent() && updateAllFields, 需要验证 productVO.getCategoryId()
            // if productRepo.getById(productVO.getId()).isPresent() && !updateAllFields && productVO.getCategoryId() == null, 不需验证 productVO.getCategoryId()

            Optional<Category> categoryOpt = categoryRepo.getById(productVO.getCategoryId());
            if ( !(productRepo.getById(productVO.getId()).isPresent() && !updateAllFields && productVO.getCategoryId() == null) ) {
                if (!categoryOpt.isPresent()) {
                    return ServerResponse.createByErrorMsg("categoryId 不存在");
                }
            }
            product.setCategory(categoryOpt.get());

            String[] ignoredProperties = {"createTime", "updateTime"};
            if (productRepo.getById(productVO.getId()).isPresent()) {
                // update
                /*BeanUtils.copyPropertiesExceptNull(productVO, product, ignoredProperties);
                product.setUpdateTime(LocalDateTime.now());*/

                if (updateAllFields) {
                    org.springframework.beans.BeanUtils.copyProperties(productVO, product, ignoredProperties);
                } else {
                    BeanUtils.copyPropertiesExceptNull(productVO, product, ignoredProperties);
                }
                product.setUpdateTime(LocalDateTime.now());
            } else {
                /*org.springframework.beans.BeanUtils.copyProperties(productVO, product, ignoredProperties);
                if (productRepo.getById(productVO.getId()).isPresent()) {
                    product.setUpdateTime(LocalDateTime.now());
                } else {
                    product.setCreateTime(LocalDateTime.now());
                }*/

                // save
                org.springframework.beans.BeanUtils.copyProperties(productVO, product, ignoredProperties);
                product.setCreateTime(LocalDateTime.now());
            }
            Product saved = productRepo.save(product);
            if (saved != null) {
                return ServerResponse.createBySuccess(assembleProductVOFromProduct(saved));
            }
            return ServerResponse.createByErrorMsg("保存或更新失败");
        }
        return ServerResponse.createByErrorMsg("参数不能为空");
    }

    public ServerResponse<ProductVO> setProductStatus(Integer productId, Integer status) {
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
                    return ServerResponse.createBySuccess(assembleProductVOFromProduct(updated));
                }
                return ServerResponse.createByErrorMsg("更新失败");
            }
            return ServerResponse.createByError(ResponseCode.ILLEGAL_ARGUMENT.getCode(), "productId 不存在");
        }
        return ServerResponse.createByError(ResponseCode.ILLEGAL_ARGUMENT.getCode(), "参数 productId 不能为空");
    }

    public ServerResponse<ProductVO> getProductDetail(Integer productId) {
        Optional<Product> byId = productRepo.getById(productId);
        if (!byId.isPresent()) {
            return ServerResponse.createByError(ResponseCode.ILLEGAL_ARGUMENT.getCode(), "Product id 不存在");
        }
        ProductVO productVO = assembleProductVOFromProduct(byId.get());
        return ServerResponse.createBySuccess(productVO);
    }

    // --------------------- private -------------------------------

    private ProductVO assembleProductVOFromProduct(Product product) {
        ProductVO productVO = new ProductVO();
        org.springframework.beans.BeanUtils.copyProperties(product, productVO);
        productVO.setCategoryId(product.getCategory().getId());

        productVO.setImageHost(this.imageHost);
        productVO.setParentCategoryId(product.getCategory().getParentId());
        return productVO;
    }
}
