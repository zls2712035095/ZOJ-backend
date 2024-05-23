package com.zack.ZOJ.service;

import javax.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * 用户服务测试
 */
@SpringBootTest
public class UserServiceTest {

    @Resource
    private UserService userService;

    @Test
    void userRegister() {
        String userAccount = "zack";
        String userPassword = "";
        String checkPassword = "123456";
        String userName = "zhang";
        try {
            long result = userService.userRegister(userAccount, userPassword, checkPassword, userName);
            Assertions.assertEquals(-1, result);
            userAccount = "yu";
            result = userService.userRegister(userAccount, userPassword, checkPassword, userName);
            Assertions.assertEquals(-1, result);
        } catch (Exception e) {

        }
    }
}
