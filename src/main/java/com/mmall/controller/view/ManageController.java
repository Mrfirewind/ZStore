package com.mmall.controller.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by guojingfeng on 2017/5/21.
 */
@Controller
@RequestMapping("/manage")
public class ManageController {

  @RequestMapping("home")
  public String homeView () {
    return "/manage/home";
  }
}
