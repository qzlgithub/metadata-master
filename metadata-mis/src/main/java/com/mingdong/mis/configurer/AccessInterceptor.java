package com.mingdong.mis.configurer;

import com.alibaba.fastjson.JSON;
import com.mingdong.common.constant.Charset;
import com.mingdong.common.util.StringUtils;
import com.mingdong.common.util.WebUtils;
import com.mingdong.core.annotation.AuthRequired;
import com.mingdong.mis.component.RedisDao;
import com.mingdong.mis.constant.Field;
import com.mingdong.mis.constant.MDResult;
import com.mingdong.mis.model.MDResp;
import com.mingdong.mis.model.RequestThread;
import com.mingdong.mis.model.UserAuth;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Configuration
public class AccessInterceptor extends HandlerInterceptorAdapter
{
    private static Logger logger = LoggerFactory.getLogger("ACCESS");
    @Resource
    private RedisDao redisDao;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception
    {
        RequestThread.init();
        String uri = request.getRequestURI();
        String ip = WebUtils.getIp(request);
        // logger.info("rest api request from {}: [{}], {}", ip, request.getMethod(), uri);
        MDResp resp = MDResp.create();
        if(handler.getClass().isAssignableFrom(HandlerMethod.class))
        {
            AuthRequired annotation = ((HandlerMethod) handler).getMethod().getAnnotation(AuthRequired.class);
            if(annotation != null)
            {
                String token = request.getHeader(Field.HEADER_ACCESS_TOKEN);
                // 验证接入规范
                if(StringUtils.isNullBlank(token))
                {
                    resp.response(MDResult.ACCESS_DENIED);
                    response.getOutputStream().write(JSON.toJSONString(resp).getBytes(Charset.UTF_8));
                    return false;
                }
                UserAuth auth = redisDao.findAuth(token);
                // 验证请求凭证
                if(auth == null)
                {
                    resp.response(MDResult.INVALID_ACCESS_TOKEN);
                    response.getOutputStream().write(JSON.toJSONString(resp).getBytes(Charset.UTF_8));
                    return false;
                }
                // 验证ip白名单
                if(!auth.getHost().equals(ip))
                {
                    resp.response(MDResult.INVALID_CLIENT_IP);
                    response.getOutputStream().write(JSON.toJSONString(resp).getBytes(Charset.UTF_8));
                    return false;
                }
                RequestThread.setAccessInfo(auth);
                // 验证产品权限
                if(RequestThread.getProduct() == null || !uri.equals(RequestThread.getProduct().getUri()))
                {
                    resp.response(MDResult.ACCESS_RESTRICTED);
                    response.getOutputStream().write(JSON.toJSONString(resp).getBytes(Charset.UTF_8));
                    return false;
                }
            }
        }
        RequestThread.setResp(resp);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception
    {
        logger.info("request elapsed time: {}", System.currentTimeMillis() - RequestThread.getTimestamp());
        RequestThread.cleanup();
        super.afterCompletion(request, response, handler, ex);
    }
}
