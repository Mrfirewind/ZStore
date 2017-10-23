package com.mmall.controller.backend;

import com.mmall.commom.Const;
import com.mmall.commom.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/manager/user")
public class UserManagerController {
    @Autowired
    IUserService userService;

    @RequestMapping(value = "/login.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> login(String userName, String password, HttpSession session) {
        ServerResponse<User> response = userService.login(userName, password);
        if (response.isSuccess()) {
            User user = response.getData();
            if (user.getRole() == (Const.Role.ROLE_ADMIN)) {
                session.setAttribute(Const.CURRENT_USER, response.getData());
                return response;
            }
            return ServerResponse.createByErroMessage("不是管理员无法登陆");
        }
        return response;
    }
}
