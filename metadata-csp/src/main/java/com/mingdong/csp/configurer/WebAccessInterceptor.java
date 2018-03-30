package com.mingdong.csp.configurer;

import com.mingdong.core.annotation.LoginRequired;
import com.mingdong.csp.component.RedisDao;
import com.mingdong.csp.constant.PathPage;
import com.mingdong.csp.model.RequestThread;
import com.mingdong.csp.model.UserSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Configuration
public class WebAccessInterceptor extends HandlerInterceptorAdapter
{
    private static Logger logger = LoggerFactory.getLogger("ACCESS");
    @Resource
    private RedisDao redisDao;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception
    {
        String path = request.getRequestURI(); // same with request.getServletPath()
        logger.info("web page request [{}]: {}", request.getMethod(), path);
        if(handler.getClass().isAssignableFrom(HandlerMethod.class))
        {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            String sessionId = request.getSession().getId();
            UserSession us = redisDao.getUserSession(sessionId);
            if(us == null)
            {
                RequestThread.setIsLogin(false);
            }
            else
            {
                RequestThread.setIsLogin(true);
            }
            LoginRequired annotation = handlerMethod.getMethod().getAnnotation(LoginRequired.class);
            if(annotation != null)
            {
                if(us == null)
                {
                    response.sendRedirect("/login.html");
                    return false;
                }
                RequestThread.set(us.getClientId(), us.getUserId(), us.getUsername(), us.getPrimary());
                RequestThread.setCurrPage(PathPage.getPageByPath(path));
            }
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception
    {
        RequestThread.removeBoolean();
        RequestThread.cleanup();
        super.afterCompletion(request, response, handler, ex);
    }
}
