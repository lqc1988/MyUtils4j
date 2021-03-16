package com.test.compare;

import lombok.Data;

/**
 * @ClassName UserInfo
 * @Description TODO
 * @Author liqinchao
 * @Date 2021/3/16 10:59
 * @Version 1.0
 **/
@Data
public class UserInfo {
    private String id;
    private String name;

    public static UserInfo valueOf(String id, String name) {
        UserInfo userInfo = new UserInfo();
        userInfo.id = id;
        userInfo.name = name;
        return userInfo;
    }
}
