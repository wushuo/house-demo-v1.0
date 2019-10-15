package com.mooc.house.biz.service;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.mooc.house.biz.mapper.UserMapper;
import com.mooc.house.common.model.User;
import com.mooc.house.common.utils.BeanHelper;
import com.mooc.house.common.utils.HashUtils;

@Service
public class UserService {

  @Autowired(required = false)
  private UserMapper userMapper;
  @Autowired
  private FileService fileService;
  @Autowired
  private MailService mailService;

  @Value("${file.prefix}")
  private String imgPrefix;

  public List<User> getUsers() {
    return userMapper.selectUsers();
  }

  /**
   * 注册信息保存到数据库并发送邮件
   * @descripse
   * 1、设置激活状态
   * 2、密码加盐加密
   * 3、保存图像
   * 4、发送激活邮件
   * @param account 用户注册信息
   * @return
   */
  public boolean addAccount(User account) {
    //密码加密
    account.setPasswd(HashUtils.encryPassword(account.getPasswd()));
    List<String> paths = fileService.getImagePath(Lists.newArrayList(account.getAvatarFile()));
    if(CollectionUtils.isNotEmpty(paths)) {
      account.setAvatar(paths.get(0));
    }
    BeanHelper.setDefaultProp(account, User.class);
    BeanHelper.onInsert(account);
    account.setEnable(0);
    userMapper.insert(account);
    
    //发送邮件
    mailService.registerNotify(account.getEmail());
    return false;
  }

}
