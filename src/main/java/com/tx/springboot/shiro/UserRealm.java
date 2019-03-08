package com.tx.springboot.shiro;

import com.tx.springboot.pojo.User;
import com.tx.springboot.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 自定义realm
 *
 * @author tx
 * @date 2018/12/20
 */
public class UserRealm extends AuthorizingRealm {
    @Autowired
    UserService userService;

    /**
     * 执行授权逻辑
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        System.out.println("执行授权逻辑");

        //给资源进行授权
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();

        //添加资源的授权字符串
        //info.addStringPermission("user:add");

        //到数据库查询当前登录用户的授权字符串
        Subject subject = SecurityUtils.getSubject();
        //获取当前登录用户 从认证的Principal（第一个参数 user）获取
        User user = (User) subject.getPrincipal();
        User dbUser = userService.findById(user.getId());

        info.addStringPermission(dbUser.getRole());
        return info;
    }

    /**
     * 执行认证逻辑
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        System.out.println("执行认证逻辑");

        //编写shiro判断逻辑 判断用户名与密码
        //1、判断用户名
        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
        //连接数据库
        User user = userService.findByName(token.getUsername());
        if (user == null) {
            //用户名与数据库不同 用户名不存在 shiro底层会抛出UnknownAccountException异常
            return null;
        }
        //2.判断密码 返回AuthenticationInfo的子类SimpleAuthenticationInfo
        // 其中第一个参数是Principal认识后传给授权 第二个参数是数据密码
        return new SimpleAuthenticationInfo(user, user.getPassworld(), "");
    }

//        //假设数据库的用户名与密码
//        String name = "tx";
//        String password = "123456";
//
//        //编写shiro判断逻辑 判断用户名与密码
//        //1、判断用户名
//        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
//
//        if (!token.getUsername().equals(name)) {
//            //用户名与数据库不同 用户名不存在 shiro底层会抛出UnknownAccountException异常
//            return null;
//        }
//        //2.判断密码 返回AuthenticationInfo的子类SimpleAuthenticationInfo 第二个参数是数据密码
//        return new SimpleAuthenticationInfo("", password, "");
//    }

}
