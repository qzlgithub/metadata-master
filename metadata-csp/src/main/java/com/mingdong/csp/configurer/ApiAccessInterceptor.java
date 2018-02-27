package com.mingdong.csp.configurer;

import com.mingdong.core.annotation.LoginRequired;
import com.mingdong.core.constant.RestResult;
import com.mingdong.core.model.RestResp;
import com.mingdong.csp.component.RedisDao;
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
import javax.servlet.http.HttpSession;
import java.util.Date;

@Configuration
public class ApiAccessInterceptor extends HandlerInterceptorAdapter
{
    private static Logger logger = LoggerFactory.getLogger(ApiAccessInterceptor.class);
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
                HttpSession session = request.getSession();
                String sessionId = session.getId();
                UserSession us = redisDao.getUserSession(sessionId);
                if(us == null)
                {
                    String resp = RestResp.getErrorResp(RestResult.ACCESS_LIMITED);
                    response.getOutputStream().write(resp.getBytes("UTF-8"));
                    return false;
                }
                RequestThread.set(us.getClientId(), us.getUserId(), us.getUsername(), us.getPrimary());
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
        long time = new Date().getTime();
        Long timeLong = RequestThread.getTimeLong();
        Long dif = time - timeLong;
        Double second = dif / 1000.0;
        RequestThread.removeLong();
        logger.info("Time consuming {}", second + "s");
        System.out.println("");
    }
}
