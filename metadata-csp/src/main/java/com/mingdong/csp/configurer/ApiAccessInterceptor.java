package com.mingdong.csp.configurer;

import com.mingdong.csp.component.RedisDao;
import com.mingdong.csp.model.RequestThread;
import com.mingdong.csp.model.UserSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Configuration
public class ApiAccessInterceptor extends HandlerInterceptorAdapter
{
    private static Logger logger = LoggerFactory.getLogger(ApiAccessInterceptor.class);
    @Resource
    private RedisDao redisDao;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception
    {
        String path = request.getRequestURI(); // same with request.getServletPath()
        logger.info("Request to {}", path);
        HttpSession session = request.getSession();
        String sessionId = session.getId();
        UserSession us = redisDao.getUserSession(sessionId);

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception
    {
        RequestThread.cleanup();
        super.afterCompletion(request, response, handler, ex);
    }
}
