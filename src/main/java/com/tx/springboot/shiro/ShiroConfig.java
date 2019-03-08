package com.tx.springboot.shiro;

import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.mapstruct.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * shiro配置类
 *
 * @author tx
 * @date 2018/12/20
 */
@Configuration
public class ShiroConfig {
    /**
     * 创建ShiroFilterFactoryBean
     */
    @Bean
    public ShiroFilterFactoryBean getShiroFilterFactoryBean(@org.springframework.beans.factory.annotation.Qualifier("securityManager")
                                                                    DefaultWebSecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();

        //设置安全管理器
        shiroFilterFactoryBean.setSecurityManager(securityManager);

        /**
         * 添加shiro内置过滤器 可以实现权限相关的拦截器
         * 常用的过滤器：
         * anon：无需认证（登录）可以访问
         * authc：必须认证才可以访问
         * user：如果使用rememberMe的功能可以直接访问
         * perms：该资源必须得到资源权限才可以访问
         * role：该资源必须拥有角色权限才可以访问
         */
        Map<String, String> filterMap = new LinkedHashMap<String, String>();
        //认证过滤器 添加待认证url路径
        filterMap.put("/hello/add", "authc");
        filterMap.put("/hello/update", "authc");
        //授权过滤器 添加待授权url路径 注意当授权拦截后，shiro会自动自动跳转到未授权页面
        //表中role为hhh的可以授权添加操作 表中role为lll的可以进行更新操作 其他的没有权限
        filterMap.put("/hello/add", "perms[hhh]");
        filterMap.put("/hello/update", "perms[lll]");

        //设置认证拦截后的登录页面
        shiroFilterFactoryBean.setLoginUrl("/hello/toLogin");
        //设置授权拦截后的页面
        shiroFilterFactoryBean.setUnauthorizedUrl("/hello/noAuth");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterMap);
        return shiroFilterFactoryBean;
    }

    /**
     * 创建DefaultWebSecurityManager 安全管理器
     */
    @Bean(name = "securityManager")
    public DefaultWebSecurityManager getDefaultWebSecurityManager(@org.springframework.beans.factory.annotation.Qualifier("userRealm")
                                                                          UserRealm userRealm) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();

        //关联realm
        securityManager.setRealm(userRealm);
        return securityManager;
    }

    /**
     * 创建Ream
     */
    @Bean(name = "userRealm")
    public UserRealm getRealm() {
        return new UserRealm();
    }

}
