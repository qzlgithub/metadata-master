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
import java.util.Date;

@Configuration
public class WebAccessInterceptor extends HandlerInterceptorAdapter
{
    private static Logger logger = LoggerFactory.getLogger(WebAccessInterceptor.class);
    @Resource
    private RedisDao redisDao;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception
    {
        RequestThread.setTimeLong(new Date().getTime());
        String path = request.getRequestURI(); // same with request.getServletPath()
        logger.info("Request to {}", path);
        logger.info("Request method {}", request.getMethod());
        if(handler.getClass().isAssignableFrom(HandlerMethod.class))
        {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            logger.info("HandlerMethod {}",
                    handlerMethod.getBeanType().getName() + ";method:" + handlerMethod.getMethod().getName());
            LoginRequired annotation = handlerMethod.getMethod().getAnnotation(LoginRequired.class);
            if(annotation != null)
            {
                String sessionId = request.getSession().getId();
                UserSession us = redisDao.getUserSession(sessionId);
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
        RequestThread.cleanup();
        super.afterCompletion(request, response, handler, ex);
        Long timeLong = RequestThread.getTimeLong();
        long time = new Date().getTime();
        Long dif = time - timeLong;
        Double second = dif / 1000.0;
        RequestThread.removeLong();
        logger.info("Time consuming {}", second + "s");
        System.out.println("");
    }
}
