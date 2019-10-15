package com.mooc.house.web.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.mooc.house.biz.service.UserService;
import com.mooc.house.common.constants.CommonConstants;
import com.mooc.house.common.model.User;
import com.mooc.house.common.result.ResultMsg;
import com.mooc.house.common.utils.HashUtils;

@Controller
public class UserController {

  @Autowired
  private UserService userService;

  /**
   * 用户注册
   * 1、注册页面跳转
   * 2、用户信息验证
   * 3、用户信息入库并发送邮件
   * 4、验证失败重定向注册页面
   * @param account 用户信息
   * @param modelMap 视图信息对象
   * @return
   */
  @RequestMapping(value = "/accounts/register")
  public String registerAccount(User account, ModelMap modelMap) {

    if(account==null || StringUtils.isBlank(account.getName())) {
        return "user/accounts/register";
    }

    ResultMsg resultMsg = UserHelper.validateUser(account);
    if(!resultMsg.isSuccess() || !userService.addAccount(account)) {
      return "user/accounts/register";
    }

    return "user/accounts/registerSubmit";
  }

}
