package com.mmall.controller.portal;

import com.mmall.commom.ResponseCode;
import com.mmall.commom.ServerResponse;
import com.mmall.pojo.Shipping;
import com.mmall.pojo.User;
import com.mmall.service.IShippingService;
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
@RequestMapping("/shipping/")
public class ShippingController {
    @Autowired
    private IShippingService shippingServiceImp;

    @RequestMapping(value = "add.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse add(HttpServletRequest httpServletRequest, Shipping shipping) {
        String loginToken = CookieUtil.readLoginToken(httpServletRequest);
        if (StringUtils.isEmpty(loginToken)) {
            return ServerResponse.createByErroMessage("用户未登陆，无法获取到用户的个人信息");
        }

        String userStr = RedisPoolUtil.get(loginToken);
        User user = JsonUtil.string2Obj(userStr, User.class);
        if (user == null) {
            return ServerResponse.createByCodeErroMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        return shippingServiceImp.add(user.getId(), shipping);
    }

    @RequestMapping(value = "delete.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse delete(HttpServletRequest httpServletRequest, Integer shippingId) {
        String loginToken = CookieUtil.readLoginToken(httpServletRequest);
        if (StringUtils.isEmpty(loginToken)) {
            return ServerResponse.createByErroMessage("用户未登陆，无法获取到用户的个人信息");
        }

        String userStr = RedisPoolUtil.get(loginToken);
        User user = JsonUtil.string2Obj(userStr, User.class);
        if (user == null) {
            return ServerResponse.createByCodeErroMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        return shippingServiceImp.delete(user.getId(), shippingId);
    }

    @RequestMapping(value = "update.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse update(HttpServletRequest httpServletRequest, Shipping shipping) {
        String loginToken = CookieUtil.readLoginToken(httpServletRequest);
        if (StringUtils.isEmpty(loginToken)) {
            return ServerResponse.createByErroMessage("用户未登陆，无法获取到用户的个人信息");
        }

        String userStr = RedisPoolUtil.get(loginToken);
        User user = JsonUtil.string2Obj(userStr, User.class);
        if (user == null) {
            return ServerResponse.createByCodeErroMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        return shippingServiceImp.update(user.getId(), shipping);
    }

    @RequestMapping(value = "select.do", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse select(HttpServletRequest httpServletRequest, Integer shippingId) {
        String loginToken = CookieUtil.readLoginToken(httpServletRequest);
        if (StringUtils.isEmpty(loginToken)) {
            return ServerResponse.createByErroMessage("用户未登陆，无法获取到用户的个人信息");
        }

        String userStr = RedisPoolUtil.get(loginToken);
        User user = JsonUtil.string2Obj(userStr, User.class);
        if (user == null) {
            return ServerResponse.createByCodeErroMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        return shippingServiceImp.select(user.getId(), shippingId);
    }

    @RequestMapping(value = "list.do", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse list(HttpServletRequest httpServletRequest, @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                               @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        String loginToken = CookieUtil.readLoginToken(httpServletRequest);
        if (StringUtils.isEmpty(loginToken)) {
            return ServerResponse.createByErroMessage("用户未登陆，无法获取到用户的个人信息");
        }

        String userStr = RedisPoolUtil.get(loginToken);
        User user = JsonUtil.string2Obj(userStr, User.class);
        if (user == null) {
            return ServerResponse.createByCodeErroMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        return shippingServiceImp.list(user.getId(), pageNum, pageSize);
    }

}
