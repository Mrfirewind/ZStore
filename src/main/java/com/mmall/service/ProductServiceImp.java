package com.mmall.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.mmall.commom.Const;
import com.mmall.commom.ResponseCode;
import com.mmall.commom.ServerResponse;
import com.mmall.dao.CategoryMapper;
import com.mmall.dao.ProductMapper;
import com.mmall.pojo.Category;
import com.mmall.pojo.Product;
import com.mmall.utils.DateTimeUtil;
import com.mmall.utils.PropertiesUtil;
import com.mmall.vo.ProductDetailVo;
import com.mmall.vo.ProductListVo;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("productServiceImp")
public class ProductServiceImp implements IProductService {
    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private ICategoryService categoryService;

    @Override
    public ServerResponse saveOrUpdateProduct(Product product) {
        if (product == null) {
            return ServerResponse.createByErroMessage("保存或更新产品参数错误");
        }
        if (StringUtils.isNotBlank(product.getSubImages())) {
            String[] subImageArray = product.getSubImages().split(",");
            if (subImageArray.length > 0) {
                product.setMainImage(subImageArray[0]);
            }
        }
        if (product.getId() != null) {
            int rowCount = productMapper.updateByPrimaryKeySelective(product);
            if (rowCount > 0) {
                return ServerResponse.createBySuccessMessage("更新产品成功");
            }
            return ServerResponse.createByErroMessage("更新产品失败");
        } else {
            int rowCount = productMapper.insert(product);
            if (rowCount > 0) {
                return ServerResponse.createBySuccessMessage("新增产品成功");
            }
            return ServerResponse.createByErroMessage("新增产品失败");
        }
    }


    public ServerResponse<String> setSaleStatus(Integer productId, Integer status) {
        if (productId == null || status == null) {
            return ServerResponse.createByCodeErroMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), "参数异常");
        }
        Product product = new Product();
        product.setId(productId);
        product.setStatus(status);
        int rowCount = productMapper.updateByPrimaryKeySelective(product);
        if (rowCount > 0) {
            return ServerResponse.createBySuccessMessage("更新销售产品状态成功");
        }
        return ServerResponse.createByErroMessage("更新销售产品状态失败");
    }

    public ServerResponse<ProductDetailVo> managerProductDetail(Integer productId) {
        if (productId == null) {
            return ServerResponse.createByCodeErroMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), "参数异常");
        }
        Product product = productMapper.selectByPrimaryKey(productId);
        if (product == null) {
            return ServerResponse.createByErroMessage("产品已下架或者删除");
        }
        ProductDetailVo productDetailVo = assembleProductDetailVo(product);
        return ServerResponse.createBySuccess(productDetailVo);
    }

    public ServerResponse<PageInfo> getProductList(int pageNum, int pageSize) {
        //设置起始页
        PageHelper.startPage(pageNum, pageSize);
        List<Product> productList = productMapper.selectList();
        List<ProductListVo> productListVoList = new ArrayList<ProductListVo>();
        for (Product product : productList) {
            ProductListVo productListVo = assembleProductListVo(product);
            productListVoList.add(productListVo);
        }

        PageInfo pageInfo = new PageInfo(productList);
        pageInfo.setList(productListVoList);
        return ServerResponse.createBySuccess(pageInfo);
    }

    private ProductDetailVo assembleProductDetailVo(Product product) {
        ProductDetailVo productDetailVo = new ProductDetailVo();
        productDetailVo.setId(product.getId());
        productDetailVo.setSubTitle(product.getSubtitle());
        productDetailVo.setMainImage(product.getMainImage());
        productDetailVo.setSubImage(product.getSubImages());
        productDetailVo.setPrice(product.getPrice());
        productDetailVo.setCategoryId(product.getCategoryId());
        productDetailVo.setStock(product.getStock());
        productDetailVo.setDetail(product.getDetail());
        productDetailVo.setName(product.getName());
        productDetailVo.setStatus(product.getStatus());

        productDetailVo.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix", "http://img.happymmall.com/"));
        Category category = categoryMapper.selectByPrimaryKey(product.getCategoryId());
        if (category == null) {
            productDetailVo.setParenetCategoryId(0);
        } else {
            productDetailVo.setParenetCategoryId(category.getParentId());
        }
        productDetailVo.setCreateTime(DateTimeUtil.dateToStr(category.getCreateTime()));
        productDetailVo.setUpdateTime(DateTimeUtil.dateToStr(category.getUpdateTime()));
        return productDetailVo;
    }

    private ProductListVo assembleProductListVo(Product product) {
        ProductListVo productListVo = new ProductListVo();
        productListVo.setId(product.getId());
        productListVo.setSubTitle(product.getSubtitle());
        productListVo.setMainImage(product.getMainImage());
        productListVo.setPrice(product.getPrice());
        productListVo.setCategoryId(product.getCategoryId());
        productListVo.setName(product.getName());
        productListVo.setStatus(product.getStatus());
        productListVo.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix", "http://img.happymmall.com/"));
        return productListVo;
    }

    public ServerResponse<PageInfo> searchProduct(String productName, Integer prrductId, int pageNum, int pageSize) {
        //设置起始页
        PageHelper.startPage(pageNum, pageSize);
        if (StringUtils.isNotBlank(productName)) {
            productName = new StringBuilder().append("%").append(productName).append("%").toString();
        }

        List<Product> productList = productMapper.selectByNameAndProductId(productName, prrductId);
        List<ProductListVo> productListVoList = Lists.newArrayList();
        for (Product product : productList) {
            ProductListVo productListVo = assembleProductListVo(product);
            productListVoList.add(productListVo);
        }

        PageInfo pageInfo = new PageInfo(productList);
        pageInfo.setList(productListVoList);
        return ServerResponse.createBySuccess(pageInfo);
    }

    public ServerResponse<ProductDetailVo> getProductDetail(Integer productId) {
        if (productId == null) {
            return ServerResponse.createByCodeErroMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), "参数异常");
        }
        Product product = productMapper.selectByPrimaryKey(productId);
        if (product == null) {
            return ServerResponse.createByErroMessage("产品已下架或者删除");
        }
        if (product.getStatus() != Const.ProductStatusEnum.ON_SLAE.getCode()) {
            return ServerResponse.createByErroMessage("产品已下架或者删除");
        }
        ProductDetailVo productDetailVo = assembleProductDetailVo(product);
        return ServerResponse.createBySuccess(productDetailVo);
    }

    public ServerResponse<PageInfo> getProductByKeywordCategory(String keyword, Integer categoryId, int pageNum, int pageSize, String orderBy) {
        if (StringUtils.isBlank(keyword) && categoryId == null) {
            return ServerResponse.createByErroMessage("获取产品列表参数错误");
        }
        List<Integer> categoryIdList = Lists.newArrayList();
        if (categoryId != null) {
            Category category = categoryMapper.selectByPrimaryKey(categoryId);
            if (categoryId == null && StringUtils.isBlank(keyword)) {
                PageHelper.startPage(pageNum, pageSize);
                List<ProductListVo> productListVoList = Lists.newArrayList();
                PageInfo pageInfo = new PageInfo(productListVoList);
                return ServerResponse.createBySuccess(pageInfo);
            }
            categoryIdList = categoryService.selectChildrenCategoryById(category.getId()).getData();
        }
        if (StringUtils.isNotBlank(keyword)) {
            keyword = new StringBuilder().append("%").append(keyword).append("%").toString();
        }
        PageHelper.startPage(pageNum, pageSize);

        if (StringUtils.isNotBlank(orderBy)) {
            //set集合的时间复杂度为O(1)，而list为O（N）
            if (Const.ProductListOrderBy.PRICE_ASC_DESC.contains(orderBy)) {
                String[] orderByArr = orderBy.split("_");
                PageHelper.orderBy(orderByArr[0] + " " + orderByArr[1]);
            }
        }
        List<Product> productList = productMapper.selectByNameAndCategoryIds(StringUtils.isBlank(keyword) ? null : keyword,
                CollectionUtils.isNotEmpty(categoryIdList) ? categoryIdList : null);
        List<ProductListVo> productListVoList = Lists.newArrayList();
        for (Product product : productList) {
            ProductListVo productListVo = assembleProductListVo(product);
            productListVoList.add(productListVo);
        }
        PageInfo pageInfo = new PageInfo(productList);
        pageInfo.setList(productListVoList);
        return ServerResponse.createBySuccess(pageInfo);
    }
}
