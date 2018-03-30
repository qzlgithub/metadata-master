package com.mingdong.csp.configurer;

import com.mingdong.common.constant.Charset;
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

@Configuration
public class ApiAccessInterceptor extends HandlerInterceptorAdapter
{
    private static Logger logger = LoggerFactory.getLogger("ACCESS");
    @Resource
    private RedisDao redisDao;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception
    {
        logger.info("rest api request [{}]: {}", request.getMethod(), request.getRequestURI());
        if(handler.getClass().isAssignableFrom(HandlerMethod.class))
        {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            String sessionId = request.getSession().getId();
            UserSession us = redisDao.getUserSession(sessionId);
            if(us != null)
            {
                RequestThread.set(us.getClientId(), us.getUserId(), us.getUsername(), us.getPrimary());
            }
            LoginRequired annotation = handlerMethod.getMethod().getAnnotation(LoginRequired.class);
            if(annotation != null && us == null)
            {
                String resp = RestResp.getErrorResp(RestResult.ACCESS_LIMITED);
                response.getOutputStream().write(resp.getBytes(Charset.UTF_8));
                return false;
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
    }
}
