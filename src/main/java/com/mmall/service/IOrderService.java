package com.mmall.service;

import com.mmall.commom.ServerResponse;

import java.util.Map;

public interface IOrderService {
    
    ServerResponse pay(Long orderNo, Integer userId, String path);

    ServerResponse aliCallback(Map<String, String> params);

    ServerResponse queryOrderPayStatus(Integer userId,Long orderNo);
}
