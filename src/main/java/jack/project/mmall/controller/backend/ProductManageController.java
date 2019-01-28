package jack.project.mmall.controller.backend;

import jack.project.mmall.common.ServerResponse;
import jack.project.mmall.entity.Product;
import jack.project.mmall.service.IProductService;
import jack.project.mmall.service.IUserService;
import jack.project.mmall.vo.ProductVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

/**
 * Theme:
 * <p>
 * Description:
 *
 * @author Zhengde ZHOU
 * Created on 2019-01-19
 */
@RestController
@RequestMapping("/admin/product")
public class ProductManageController {

    private IUserService userService;

    private IProductService productService;

    @Autowired
    public ProductManageController(IUserService userService, IProductService productService) {
        this.userService = userService;
        this.productService = productService;
    }

    @PostMapping("/save_update")
    public ServerResponse<ProductVO> saveOrUpdateProduct(HttpSession session, @RequestBody ProductVO productVo,
                                                       @RequestParam(required = false, defaultValue = "0") Boolean updateAllFields) {
        ServerResponse isAdmin = UserAdminController.checkAdminRole(session, userService);
        if (!isAdmin.isSuccessful()) {
            return ServerResponse.createByError(isAdmin.getCode(), isAdmin.getMsg());
        }
        return productService.saveOrUpdateProduct(productVo, updateAllFields);
    }

    @GetMapping("/status")
    public ServerResponse<ProductVO> setProductStatus(HttpSession session, @RequestParam Integer productId, @RequestParam Integer status) {
        ServerResponse isAdmin = UserAdminController.checkAdminRole(session, userService);
        if (!isAdmin.isSuccessful()) {
            return ServerResponse.createByError(isAdmin.getCode(), isAdmin.getMsg());
        }
        return productService.setProductStatus(productId, status);
    }

    @GetMapping("/product_details")
    public ServerResponse<ProductVO> getProductDetatil(@RequestParam Integer productId) {
        return productService.getProductDetail(productId);
    }



}
