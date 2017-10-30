package com.mmall.service;

import com.github.pagehelper.PageInfo;
import com.mmall.commom.ServerResponse;
import com.mmall.pojo.Product;
import com.mmall.vo.ProductDetailVo;

public interface IProductService {

    public ServerResponse saveOrUpdateProduct(Product product);

    public ServerResponse<String> setSaleStatus(Integer productId, Integer status);

    public ServerResponse<ProductDetailVo> managerProductDetail(Integer productId);

    public ServerResponse<PageInfo> getProductList(int pageNum, int pageSize);

    public ServerResponse<PageInfo> searchProduct(String productName,Integer prrductId,int pageNum, int pageSize);

    public ServerResponse<ProductDetailVo> getProductDetail(Integer productId);

    public ServerResponse<PageInfo> getProductByKeywordCategory(String keyword, Integer categoryId, int pageNum, int pageSize, String orderBy);
}
