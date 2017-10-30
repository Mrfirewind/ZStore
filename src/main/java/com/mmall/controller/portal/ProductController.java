package com.mmall.controller.portal;

import com.github.pagehelper.PageInfo;
import com.mmall.commom.ServerResponse;
import com.mmall.service.IProductService;
import com.mmall.vo.ProductDetailVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/product/")
public class ProductController {

    @Autowired
    private IProductService productService;

    @RequestMapping(value = "detail.do", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<ProductDetailVo> detail(Integer productId) {
        return productService.getProductDetail(productId);
    }

    @RequestMapping(value = "list.do", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<PageInfo> list(@RequestParam(value = "keyword", required = false) String keyword, @RequestParam(value = "categoryId", required = false) Integer categoryId,
                                         @RequestParam(value = "pageNum", defaultValue = "1") int pageNum, @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                         @RequestParam(value = "orderBy", defaultValue = "price_desc") String orderBy) {
        return productService.getProductByKeywordCategory(keyword, categoryId, pageNum, pageSize, orderBy);
    }
}
