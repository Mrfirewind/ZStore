package com.mmall.controller.backend;

import com.mmall.commom.ResponseCode;
import com.mmall.commom.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.service.ICategoryService;
import com.mmall.service.IUserService;
import com.mmall.utils.CookieUtil;
import com.mmall.utils.JsonUtil;
import com.mmall.utils.RedisPoolUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/manage/category")
public class CategoryManageController {

    @Autowired
    private IUserService userService;
    @Autowired
    private ICategoryService categoryService;

    @RequestMapping(value = "/add_category.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse addCategory(HttpServletRequest httpServletRequest, String categoryName, @RequestParam(value = "parentId", defaultValue = "0") int parentId) {
        String loginToken = CookieUtil.readLoginToken(httpServletRequest);
        if (StringUtils.isEmpty(loginToken)) {
            return ServerResponse.createByErroMessage("用户未登陆，无法获取到用户的个人信息");
        }

        String userStr = RedisPoolUtil.get(loginToken);
        User user = JsonUtil.string2Obj(userStr, User.class);
        if (user == null) {
            return ServerResponse.createByCodeErroMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录");
        }
        ServerResponse checkResult = userService.checkAdmin(user);
        if (!checkResult.isSuccess()) {
            return ServerResponse.createByErroMessage("当前用户不是管理员,无权进行此操作");
        }
        return categoryService.addCategory(categoryName, parentId);
    }

    @RequestMapping(value = "/set_category_name.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse setCategoryName(HttpServletRequest httpServletRequest, String categoryName, int categoryId) {
        String loginToken = CookieUtil.readLoginToken(httpServletRequest);
        if (StringUtils.isEmpty(loginToken)) {
            return ServerResponse.createByErroMessage("用户未登陆，无法获取到用户的个人信息");
        }

        String userStr = RedisPoolUtil.get(loginToken);
        User user = JsonUtil.string2Obj(userStr, User.class);
        if (user == null) {
            return ServerResponse.createByCodeErroMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录");
        }
        ServerResponse checkResult = userService.checkAdmin(user);
        if (!checkResult.isSuccess()) {
            return ServerResponse.createByErroMessage("当前用户不是管理员,无权进行此操作");
        }
        return categoryService.setCategoryName(categoryName, categoryId);
    }

    @RequestMapping(value = "/get_category.do", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse getChildrenParallelCategory(HttpServletRequest httpServletRequest, @RequestParam(value = "categoryId", defaultValue = "0") int categoryId) {
        String loginToken = CookieUtil.readLoginToken(httpServletRequest);
        if (StringUtils.isEmpty(loginToken)) {
            return ServerResponse.createByErroMessage("用户未登陆，无法获取到用户的个人信息");
        }

        String userStr = RedisPoolUtil.get(loginToken);
        User user = JsonUtil.string2Obj(userStr, User.class);
        if (user == null) {
            return ServerResponse.createByCodeErroMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录");
        }
        ServerResponse checkResult = userService.checkAdmin(user);
        if (!checkResult.isSuccess()) {
            return ServerResponse.createByErroMessage("当前用户不是管理员,无权进行此操作");
        }
        return categoryService.selectChildrenParallelCategoryByParentId(categoryId);
    }

    @RequestMapping(value = "/get_children_category.do", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse getChildrenCategory(HttpServletRequest httpServletRequest, @RequestParam(value = "categoryId", defaultValue = "0") int categoryId) {
        String loginToken = CookieUtil.readLoginToken(httpServletRequest);
        if (StringUtils.isEmpty(loginToken)) {
            return ServerResponse.createByErroMessage("用户未登陆，无法获取到用户的个人信息");
        }

        String userStr = RedisPoolUtil.get(loginToken);
        User user = JsonUtil.string2Obj(userStr, User.class);
        if (user == null) {
            return ServerResponse.createByCodeErroMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录");
        }
        ServerResponse checkResult = userService.checkAdmin(user);
        if (!checkResult.isSuccess()) {
            return ServerResponse.createByErroMessage("当前用户不是管理员,无权进行此操作");
        }
        return categoryService.selectChildrenCategoryById(categoryId);
    }
}
