package com.mmall.controller.backend;

import com.google.common.collect.Maps;
import com.mmall.commom.Const;
import com.mmall.commom.ResponseCode;
import com.mmall.commom.ServerResponse;
import com.mmall.pojo.Product;
import com.mmall.pojo.User;
import com.mmall.service.IFileService;
import com.mmall.service.IProductService;
import com.mmall.service.IUserService;
import com.mmall.utils.PropertiesUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/manage/product")
public class ProductManageController {
    @Autowired
    private IUserService userService;

    @Autowired
    private IProductService productService;

    @Autowired
    private IFileService fileService;

    @RequestMapping(value = "/save.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse saveProduct(HttpSession session, Product product) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByCodeErroMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登陆，需要登陆");
        }
        ServerResponse checkResult = userService.checkAdmin(user);
        if (!checkResult.isSuccess()) {
            return ServerResponse.createByErroMessage("当前用户非管理员，无权限进行操作");
        }
        return productService.saveOrUpdateProduct(product);
    }

    @RequestMapping(value = "/set_sale_status.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse setSaleStatus(HttpSession session, Integer productId, Integer status) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByCodeErroMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登陆，需要登陆");
        }
        ServerResponse checkResult = userService.checkAdmin(user);
        if (!checkResult.isSuccess()) {
            return ServerResponse.createByErroMessage("当前用户非管理员，无权限进行操作");
        }
        return productService.setSaleStatus(productId, status);
    }

    @RequestMapping(value = "/detail.do", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse getDetail(HttpSession session, Integer productId) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByCodeErroMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登陆，需要登陆");
        }
        ServerResponse checkResult = userService.checkAdmin(user);
        if (!checkResult.isSuccess()) {
            return ServerResponse.createByErroMessage("当前用户非管理员，无权限进行操作");
        }
        return productService.managerProductDetail(productId);
    }

    @RequestMapping(value = "/list.do", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse getList(HttpSession session, @RequestParam(value = "pageNum", defaultValue = "1") int pageNum, @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByCodeErroMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登陆，需要登陆");
        }
        ServerResponse checkResult = userService.checkAdmin(user);
        if (!checkResult.isSuccess()) {
            return ServerResponse.createByErroMessage("当前用户非管理员，无权限进行操作");
        }
        return productService.getProductList(pageNum, pageSize);
    }

    @RequestMapping(value = "/search.do", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse searchProduct(HttpSession session, String productName, Integer productId, @RequestParam(value = "pageNum", defaultValue = "1") int pageNum, @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByCodeErroMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登陆，需要登陆");
        }
        ServerResponse checkResult = userService.checkAdmin(user);
        if (!checkResult.isSuccess()) {
            return ServerResponse.createByErroMessage("当前用户非管理员，无权限进行操作");
        }
        return productService.searchProduct(productName, productId, pageNum, pageSize);
    }

    @RequestMapping(value = "/upload.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse uploadProduct(HttpSession session, @RequestParam(value = "upload_file", required = false) MultipartFile file, HttpServletRequest request) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByCodeErroMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登陆，需要登陆");
        }
        ServerResponse checkResult = userService.checkAdmin(user);
        if (!checkResult.isSuccess()) {
            return ServerResponse.createByErroMessage("当前用户非管理员，无权限进行操作");
        }
        String path = session.getServletContext().getRealPath("upload");
        String targetFile = fileService.upload(file, path);
        String url = PropertiesUtil.getProperty("ftp.server.http.prefix") + targetFile;
        Map<String, String> fileMap = new HashMap<String, String>();
        fileMap.put("uri", targetFile);
        fileMap.put("url", url);
        return ServerResponse.createBySuccess(fileMap);
    }

    @RequestMapping(value = "/upload_rich_text.do", method = RequestMethod.POST)
    @ResponseBody
    public Map uploadRichTextImage(HttpSession session, @RequestParam(value = "upload_file", required = false) MultipartFile file, HttpServletRequest request, HttpServletResponse response) {
        //富文本对返回值有自己的有要求，该项目使用的是simditor，按照其要求进行返回
        Map resultMap = Maps.newHashMap();
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            resultMap.put("success", false);
            resultMap.put("msg", "用户未登陆，需要登陆");
            return resultMap;
        }
        ServerResponse checkResult = userService.checkAdmin(user);
        if (!checkResult.isSuccess()) {
            resultMap.put("success", false);
            resultMap.put("msg", "当前用户非管理员，无权限进行操作");
            return resultMap;
        }
        String path = session.getServletContext().getRealPath("upload");
        String targetFile = fileService.upload(file, path);
        if (StringUtils.isBlank(targetFile)) {
            resultMap.put("success", false);
            resultMap.put("msg", "上传富文本文件失败");
            return resultMap;
        }

        String url = PropertiesUtil.getProperty("ftp.server.http.prefix") + targetFile;
        resultMap.put("success", true);
        resultMap.put("msg", "上传富文本文件成功");
        resultMap.put("file_path", url);
        response.setHeader("Access-Control-Allow-Headers", "X-File-Name");
        return resultMap;
    }
}
