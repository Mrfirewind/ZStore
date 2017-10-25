package com.mmall.controller.backend;

import com.mmall.commom.Const;
import com.mmall.commom.ResponseCode;
import com.mmall.commom.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.service.ICategoryService;
import com.mmall.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/manage/category")
public class CategoryManageController {

    @Autowired
    private IUserService userService;
    @Autowired
    private ICategoryService categoryService;

    @RequestMapping(value = "/add_category.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse addCategory(HttpSession session, String categoryName, @RequestParam(value = "parentId", defaultValue = "0") int parentId) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
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
    public ServerResponse setCategoryName(HttpSession session, String categoryName, int categoryId) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
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
    public ServerResponse getChildrenParallelCategory(HttpSession session, @RequestParam(value = "categoryId", defaultValue = "0") int categoryId) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
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
    public ServerResponse getChildrenCategory(HttpSession session, @RequestParam(value = "categoryId", defaultValue = "0") int categoryId) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
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
