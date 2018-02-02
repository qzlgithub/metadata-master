package com.mingdong.mis.configurer;

import com.alibaba.fastjson.JSON;
import com.mingdong.common.constant.Charset;
import com.mingdong.common.util.StringUtils;
import com.mingdong.common.util.WebUtils;
import com.mingdong.core.annotation.AuthRequired;
import com.mingdong.mis.component.RedisDao;
import com.mingdong.mis.constant.Field;
import com.mingdong.mis.constant.MetadataResult;
import com.mingdong.mis.model.MetadataRes;
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
    private static Logger logger = LoggerFactory.getLogger(AccessInterceptor.class);
    @Resource
    private RedisDao redisDao;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception
    {
        MetadataRes res = MetadataRes.create();
        RequestThread.init();
        if(handler.getClass().isAssignableFrom(HandlerMethod.class))
        {
            AuthRequired annotation = ((HandlerMethod) handler).getMethod().getAnnotation(AuthRequired.class);
            if(annotation != null)
            {
                String token = request.getHeader(Field.HEADER_METADATA_TOKEN);
                if(StringUtils.isNullBlank(token))
                {
                    res.setResult(MetadataResult.RC_1);
                    response.getOutputStream().write(JSON.toJSONString(res).getBytes(Charset.UTF_8));
                    return false;
                }
                UserAuth auth = redisDao.findAuth(token);
                if(auth == null || !auth.auth())
                {
                    res.setResult(MetadataResult.RC_2);
                    response.getOutputStream().write(JSON.toJSONString(res).getBytes(Charset.UTF_8));
                    return false;
                }
                String ip = WebUtils.getIp(request);
                if(!auth.getHost().equals(ip))
                {
                    res.setResult(MetadataResult.RC_3);
                    response.getOutputStream().write(JSON.toJSONString(res).getBytes(Charset.UTF_8));
                    return false;
                }
                RequestThread.setIp(ip);
                RequestThread.setProductId(auth.getProductId());
                RequestThread.setClientId(auth.getClientId());
                RequestThread.setUserId(auth.getUserId());
            }
        }
        RequestThread.setResult(res);
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
