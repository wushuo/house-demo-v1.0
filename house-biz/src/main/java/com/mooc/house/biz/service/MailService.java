package com.mooc.house.biz.service;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalNotification;
import com.mooc.house.biz.mapper.UserMapper;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class MailService {

    @Autowired(required = false)
    private UserMapper userMapper;

    //创建本地缓存对象
    private Cache<String, String> localCache = CacheBuilder.newBuilder().maximumSize(100)
            .expireAfterAccess(15, TimeUnit.MINUTES)
            .removalListener(new RemovalListener<String, String>() {

                //过期删除本地缓存
                @Override
                public void onRemoval(RemovalNotification<String, String> removalNotification) {
                    userMapper.delete(removalNotification.getValue());
                }

            }).build();

    @Autowired
    private JavaMailSender mailSender;

    @Value("${domain.name}")
    private String httpUrl;

    @Value("${spring.mail.username}")
    private String from;

    /**
     * 邮件发送方法
     * @param title 主题
     * @param url 激活链接地址
     * @param email 收件人邮箱地址
     */
    public void senMail(String title, String url, String email) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom(from);
        mailMessage.setTo(email);
        mailMessage.setSubject(title);
        mailMessage.setText(url);
        mailSender.send(mailMessage);
    }

    /**
     * 1、缓存随意生成的key
     * 2、异步发送邮件
     * @param email 邮箱地址
     */
    @Async
    public void registerNotify(String email) {
        //生成10位数的随机值
        String randomKey = RandomStringUtils.randomAlphabetic(10);
        localCache.put(randomKey, email);
        //激活地址
        String url = "http://"+httpUrl+"/accounts/verify?key="+randomKey;
        //发送邮件
        senMail("房产销售平台激活邮件", url, email);
    }

}
