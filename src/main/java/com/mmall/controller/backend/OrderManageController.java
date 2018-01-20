package com.mmall.controller.backend;

import com.github.pagehelper.PageInfo;
import com.mmall.commom.ResponseCode;
import com.mmall.commom.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.service.IOrderService;
import com.mmall.service.IUserService;
import com.mmall.utils.CookieUtil;
import com.mmall.utils.JsonUtil;
import com.mmall.utils.RedisPoolUtil;
import com.mmall.vo.OrderVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/manage/order/")
public class OrderManageController {

    @Autowired
    private IUserService userService;
    @Autowired
    private IOrderService orderService;

    @RequestMapping(value = "list.do", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<PageInfo> orderList(HttpServletRequest httpServletRequest, @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                              @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
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
        return orderService.getManageOrderList(user.getId(), pageNum, pageSize);
    }

    @RequestMapping(value = "detail.do", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<OrderVo> orderDetail(HttpServletRequest httpServletRequest, Long orderNo) {
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
        return orderService.getManagerOrderDetail(orderNo);
    }

    @RequestMapping(value = "search.do", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<PageInfo> orderSearch(HttpServletRequest httpServletRequest, Long orderNo, @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                                @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
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
        return orderService.searchOrderDetail(orderNo, pageNum, pageSize);
    }

    @RequestMapping(value = "send_goods.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> sendGoods(HttpServletRequest httpServletRequest, Long orderNo) {
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
        return orderService.sendGoods(orderNo);
    }
}
