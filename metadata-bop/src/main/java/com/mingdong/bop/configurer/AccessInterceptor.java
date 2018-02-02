package com.mingdong.bop.configurer;

import com.mingdong.bop.component.RedisDao;
import com.mingdong.bop.constant.PathModule;
import com.mingdong.bop.model.ManagerSession;
import com.mingdong.bop.model.RequestThread;
import com.mingdong.bop.service.SystemService;
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
import java.util.Map;

@Configuration
public class AccessInterceptor extends HandlerInterceptorAdapter
{
    private static Logger logger = LoggerFactory.getLogger(AccessInterceptor.class);
    @Resource
    private RedisDao redisDao;
    @Resource
    private SystemService systemService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception
    {
        RequestThread.setTimeLong(new Date().getTime());
        String path = request.getRequestURI(); // same with request.getServletPath()
        logger.info("Request path {}", path);
        logger.info("Request method {}", request.getMethod());
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        if(handler.getClass().isAssignableFrom(HandlerMethod.class))
        {
            logger.info("HandlerMethod {}",
                    handlerMethod.getBeanType().getName() + ";method:" + handlerMethod.getMethod().getName());
        }
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
        Map<String, String> system = redisDao.getSystemModule();
        if(system == null)
        {
            system = systemService.cacheSystemModule();
        }
        RequestThread.setSystem(system);
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
        logger.info("Time consuming {}", second + "s");
        RequestThread.removeLong();
        System.out.println("");
    }
}
