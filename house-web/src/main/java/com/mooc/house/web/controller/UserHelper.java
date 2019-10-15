package com.mooc.house.web.controller;

import com.mooc.house.common.model.User;
import com.mooc.house.common.result.ResultMsg;
import org.apache.commons.lang3.StringUtils;

/**
 * 用户信息辅助类型
 */
public class UserHelper {

    /**
     * 用户注册信息验证
     * @return
     */
    public static ResultMsg validateUser(User userInfo) {

        //验证邮箱
        if(StringUtils.isBlank(userInfo.getEmail())) {
            return ResultMsg.errorMsg("电子邮箱地址不能为空！");
        }

        //验证两次输入的密码是否一致
        if(StringUtils.isBlank(userInfo.getPasswd()) || StringUtils.isBlank(userInfo.getConfirmPasswd())) {
            return ResultMsg.errorMsg("密码不能为空！");
        }

        //校验邮箱唯一


        if(!userInfo.getConfirmPasswd().equals(userInfo.getPasswd())) {
            return ResultMsg.errorMsg("两次输入的密码不一致！");
        }

        return ResultMsg.successMsg("验证通过");
    }
}
