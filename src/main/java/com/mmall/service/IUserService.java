package com.mmall.service;

import com.mmall.commom.ServerResponse;
import com.mmall.pojo.User;

public interface IUserService {
    ServerResponse<User> login(String userName, String password);

    ServerResponse<String> register(User user);

    ServerResponse<String> checkValid(String account,String type);

    ServerResponse<String> selectQuestion(String userName);

    ServerResponse<String> checkAnswer(String username ,String question,String answer);

    ServerResponse<String> forgetRestPassword(String userName, String newPassword, String forgetToken);

    ServerResponse<String> resetPassword(User user,String oldPassword, String newPassword);

    ServerResponse<User> updateInformation(User user);

    ServerResponse<User> getInformation(Integer userId);

}
