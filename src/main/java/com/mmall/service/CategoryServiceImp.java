package com.mmall.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.mmall.commom.ServerResponse;
import com.mmall.dao.CategoryMapper;
import com.mmall.pojo.Category;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service("categoryService")
public class CategoryServiceImp implements ICategoryService {
    @Autowired
    private CategoryMapper categoryMapper;

    private static final Logger log = LoggerFactory.getLogger(CategoryServiceImp.class);

    /**
     * 添加商品品类
     *
     * @param categoryName
     * @param parentId
     * @return
     */

    @Override
    public ServerResponse addCategory(String categoryName, Integer parentId) {
        if (parentId == null || StringUtils.isBlank(categoryName)) {
            return ServerResponse.createByErroMessage("添加品类参数错误");
        }
        Category category = new Category();
        category.setName(categoryName);
        category.setParentId(parentId);
        category.setStatus(true);
        int rowCount = categoryMapper.insert(category);
        if (rowCount > 0) {
            return ServerResponse.createBySuccess("添加品类成功");
        }
        return ServerResponse.createByErroMessage("添加品类失败");
    }

    @Override
    public ServerResponse setCategoryName(String categoryName, Integer categoryId) {
        if (categoryId == null || StringUtils.isBlank(categoryName)) {
            return ServerResponse.createByErroMessage("修改品类名称错误");
        }
        Category category = new Category();
        category.setName(categoryName);
        category.setId(categoryId);
        int rowCount = categoryMapper.updateByPrimaryKeySelective(category);
        if (rowCount > 0) {
            return ServerResponse.createBySuccess("修改品类名称成功");
        }
        return ServerResponse.createByErroMessage("添加品类名称失败");
    }

    public ServerResponse<List<Category>> selectChildrenParallelCategoryByParentId(Integer categoryId) {
        List<Category> categories = categoryMapper.selectChildrenParallelCategoryByParentId(categoryId);
        if (CollectionUtils.isEmpty(categories)) {
            log.info("未找到当前分类的子分类");
        }
        return ServerResponse.createBySuccess(categories);
    }

    /**
     * 查询所有子节点id
     * @param categoryId
     * @return
     */
    @Override
    public ServerResponse<List<Integer>> selectChildrenCategoryById(Integer categoryId) {
        Set<Category> categorySet = Sets.newHashSet();
        findChildrenCategory(categorySet, categoryId);
        List<Integer> categoryIdList = Lists.newArrayList();
        if (categoryId != null) {
            for (Category categoryItem : categorySet) {
                categoryIdList.add(categoryItem.getId());
            }
        }
        return ServerResponse.createBySuccess(categoryIdList);
    }

    private Set<Category> findChildrenCategory(Set<Category> categorySet, Integer categoryId) {
        Category category = categoryMapper.selectByPrimaryKey(categoryId);
        if (category != null) {
            categorySet.add(category);
        }
        List<Category> categoryList = categoryMapper.selectChildrenParallelCategoryByParentId(categoryId);
        for (Category categoryItem : categoryList) {
            findChildrenCategory(categorySet, categoryItem.getId());
        }
        return categorySet;
    }
}
