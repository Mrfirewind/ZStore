package com.mmall.service;

import com.mmall.commom.ServerResponse;
import com.mmall.pojo.Category;

import java.util.List;

public interface ICategoryService {
    ServerResponse addCategory(String categoryName, Integer parentId);

    ServerResponse setCategoryName(String categoryName, Integer categoryId);

    ServerResponse<List<Category>> selectChildrenParallelCategoryByParentId(Integer categoryId);

    ServerResponse<List<Integer>> selectChildrenCategoryById(Integer categoryId);
}
