package com.mmall.controller.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by guojingfeng on 2017/5/19.
 */
@Controller
@RequestMapping("/")
public class RouterController {

  //  首页
  @RequestMapping("index")
  public String indexView () {
    return "index";
  }

  //  登录
  @RequestMapping("login")
  public String loginView () {
    return "login";
  }

  //  注册
  @RequestMapping("register")
  public String registerView () {
    return "register";
  }

  //  关于
  @RequestMapping("about")
  public String aboutView () {
    return "about";
  }

  //  订单列表
  @RequestMapping("order-list")
  public String orderListView () {
    return "order-list";
  }

  //  订单详情
  @RequestMapping("order-detail")
  public String orderDetailView () {
    return "order-detail";
  }

  //  订单确认
  @RequestMapping("confirm")
  public String confirmView () {
    return "confirm";
  }

  //  商品列表
  @RequestMapping("list")
  public String listView () {
    return "list";
  }

  //  商品详情
  @RequestMapping("detail")
  public String detailView () {
    return "detail";
  }

  //  修改个人信息
  @RequestMapping("user-info-update")
  public String userInfoUpdateView () {
    return "user-info-update";
  }

  //  用户中心
  @RequestMapping("user-center")
  public String userCenterView () {
    return "user-center";
  }

  //  修改密码
  @RequestMapping("pass-update")
  public String passUpdateView () {
    return "pass-update";
  }

  //  支付
  @RequestMapping("payment")
  public String paymentView () {
    return "payment";
  }

  //  购物车
  @RequestMapping("cart")
  public String cartView () {
    return "cart";
  }

  //  操作结果
  @RequestMapping("result")
  public String resultView () {
    return "result";
  }
}
