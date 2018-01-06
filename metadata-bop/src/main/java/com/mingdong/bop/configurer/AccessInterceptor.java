package com.mingdong.bop.configurer;

import com.mingdong.bop.component.RedisDao;
import com.mingdong.core.model.RequestThread;
import com.movek.mis.constant.PathModule;
import com.movek.mis.model.ManagerSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Configuration
public class AccessInterceptor extends HandlerInterceptorAdapter
{
    private static Logger logger = LoggerFactory.getLogger(AccessInterceptor.class);
    @Resource
    private RedisDao redisDao;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception
    {
        String path = request.getRequestURI(); // same with request.getServletPath()
        logger.info("Request to {}", path);
        HttpSession session = request.getSession();
        String sessionId = session.getId();
        ManagerSession ms = redisDao.getManagerSession(sessionId);
        if(ms == null)
        {
            response.sendRedirect("/index.html");
            return false;
        }
        RequestThread.set(ms.getManagerId(), ms.getName(), ms.getPrivileges());
        String module = PathModule.getByPath(path);
        RequestThread.setModule(module != null ? module : "");
        return true;
    }
}
