package com.xd.common.util;

import com.bootdo.common.service.DictService;
import com.bootdo.system.domain.UserDO;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.support.DefaultSubjectContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Map;

/**
 * @author
 * @ClassName:
 * @Description: (这里用一句话描述这个类的作用)
 * @date
 */
@Component
public class UserUtil {
    @Autowired
    private DictService dictService;
    private static UserUtil  userUtil ;
    @PostConstruct //通过@PostConstruct实现初始化bean之前进行的操作
    public void init() {
        userUtil = this;
        userUtil.dictService = this.dictService;
        // 初使化时将已静态化的testService实例化
    }
    /**
     * 获取当前登录用户
     * @return
     */
    public UserDO getUser(){
        Session session= SecurityUtils.getSubject().getSession();
        UserDO userDO = null;
        SimplePrincipalCollection principalCollection = new SimplePrincipalCollection();
        if (session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY) == null) {

        } else {
            principalCollection = (SimplePrincipalCollection) session
                    .getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY);
            userDO = (UserDO) principalCollection.getPrimaryPrincipal();
        }
        return userDO;
    }
    /**
     * 当前用户数据权限
     */
    public Map<String, Object> addParams(Map<String, Object> params){
        UserDO userDO = getUser();
        String district = userDO.getDistrict();
        String name = userUtil.dictService.getName("city",userDO.getDistrict());
        if(name!=null && !name.equals("") && name.indexOf("市")!=-1){
            district=district.substring(0,4);
        }
        params.put("district",district+"%");
        return params;
    }
}
