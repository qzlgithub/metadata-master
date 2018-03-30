package com.mingdong.bop.configurer;

import com.mingdong.bop.component.RedisDao;
import com.mingdong.bop.constant.ModulePath;
import com.mingdong.bop.model.ManagerSession;
import com.mingdong.bop.model.RequestThread;
import com.mingdong.bop.service.SystemService;
import com.mingdong.common.constant.Charset;
import com.mingdong.core.annotation.LoginRequired;
import com.mingdong.core.constant.RestResult;
import com.mingdong.core.model.RestResp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;

@Configuration
public class RestInterceptor extends HandlerInterceptorAdapter
{
    private static Logger logger = LoggerFactory.getLogger("ACCESS");
    @Resource
    private RedisDao redisDao;
    @Resource
    private SystemService systemService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception
    {
        String path = request.getRequestURI(); // same with request.getServletPath()
        logger.info("rest api request [{}]: {}", request.getMethod(), path);
        if(handler.getClass().isAssignableFrom(HandlerMethod.class))
        {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            HttpSession session = request.getSession();
            String sessionId = session.getId();
            ManagerSession ms = redisDao.getManagerSession(sessionId);
            if(ms != null)
            {
                RequestThread.set(ms.getManagerId(), ms.getName(), ms.getRoleType(), ms.getPrivileges());
                String module = ModulePath.getByPath(path);
                RequestThread.setModule(module != null ? module : "");
                Map<String, String> system = redisDao.getSystemModule();
                if(system == null)
                {
                    system = systemService.cacheSystemModule();
                }
                RequestThread.setSystem(system);
            }
            LoginRequired annotation = handlerMethod.getMethod().getAnnotation(LoginRequired.class);
            if(annotation != null && ms == null)
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
