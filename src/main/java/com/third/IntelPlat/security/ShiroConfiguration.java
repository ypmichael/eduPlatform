package com.third.IntelPlat.security;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.Filter;

import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ShiroConfiguration {    

    @Bean(name = "securityManager")
    public DefaultWebSecurityManager getDefaultWebSecurityManager(AuthorizingRealm realm) {
        DefaultWebSecurityManager dwsm = new DefaultWebSecurityManager();
        dwsm.setRealm(realm);
        return dwsm;
    }

    @Bean(name = "realm")
    public AuthorizingRealm getShiroRealm() {
        ShiroDbRealm shiroDbRealm = new ShiroDbRealm();
        return shiroDbRealm;
    }
    
    @Bean(name = "lifecycleBeanPostProcessor")
    public LifecycleBeanPostProcessor getLifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }    

    @Bean(name = "shiroFilter")
    public ShiroFilterFactoryBean getShiroFilterFactoryBean(DefaultWebSecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean
                .setSecurityManager(securityManager);
        shiroFilterFactoryBean.setLoginUrl("/rest/user/login");
        
	    Map<String, Filter> filters = new HashMap<String, Filter>();
	    filters.put("bearerAuthc", new BearerTokenAuthenticatingFilter());
	    shiroFilterFactoryBean.setFilters(filters);
        
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<String, String>();
        
        // 不需要拦截的服务
        filterChainDefinitionMap.put("/rest/user/validToken", "anon");
        filterChainDefinitionMap.put("/rest/user/register", "anon");
        
        // 关闭是否登录的检查
        filterChainDefinitionMap.put("/rest/**", "noSessionCreation, bearerAuthc");

        
        shiroFilterFactoryBean
                .setFilterChainDefinitionMap(filterChainDefinitionMap);
        return shiroFilterFactoryBean;
    }


    @Bean
    public DefaultAdvisorAutoProxyCreator getDefaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator daap = new DefaultAdvisorAutoProxyCreator();
        daap.setProxyTargetClass(true);
        return daap;
    }

    @Bean
    public AuthorizationAttributeSourceAdvisor getAuthorizationAttributeSourceAdvisor(DefaultWebSecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor aasa = new AuthorizationAttributeSourceAdvisor();
        aasa.setSecurityManager(securityManager);
        return new AuthorizationAttributeSourceAdvisor();
    }


}
