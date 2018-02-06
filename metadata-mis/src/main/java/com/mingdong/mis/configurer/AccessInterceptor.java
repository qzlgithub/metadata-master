package com.mingdong.mis.configurer;

import com.alibaba.fastjson.JSON;
import com.mingdong.common.constant.Charset;
import com.mingdong.common.util.StringUtils;
import com.mingdong.common.util.WebUtils;
import com.mingdong.core.annotation.AuthRequired;
import com.mingdong.mis.component.RedisDao;
import com.mingdong.mis.constant.APIProduct;
import com.mingdong.mis.constant.Field;
import com.mingdong.mis.constant.MetadataResult;
import com.mingdong.mis.model.MetadataRes;
import com.mingdong.mis.model.RequestThread;
import com.mingdong.mis.model.UserAuth;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Configuration
public class AccessInterceptor extends HandlerInterceptorAdapter
{
    @Resource
    private RedisDao redisDao;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception
    {
        MetadataRes res = MetadataRes.create();
        String uri = request.getRequestURI();
        RequestThread.init();
        if(handler.getClass().isAssignableFrom(HandlerMethod.class))
        {
            AuthRequired annotation = ((HandlerMethod) handler).getMethod().getAnnotation(AuthRequired.class);
            if(annotation != null)
            {
                String token = request.getHeader(Field.HEADER_ACCESS_TOKEN);
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
                APIProduct product = APIProduct.valueOf(auth.getProduct());
                if(!product.getUri().equals(uri))
                {
                    res.setResult(MetadataResult.RC_13);
                    response.getOutputStream().write(JSON.toJSONString(res).getBytes(Charset.UTF_8));
                    return false;
                }
                String ip = WebUtils.getIp(request);
                if(!auth.getValidIP().equals(ip))
                {
                    res.setResult(MetadataResult.RC_3);
                    response.getOutputStream().write(JSON.toJSONString(res).getBytes(Charset.UTF_8));
                    return false;
                }
                RequestThread.setIp(ip);
                RequestThread.setAccountId(auth.getAccountId());
                RequestThread.setClientId(auth.getClientId());
                RequestThread.setUserId(auth.getUserId());
                RequestThread.setAppSecret(auth.getAppSecret());
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
