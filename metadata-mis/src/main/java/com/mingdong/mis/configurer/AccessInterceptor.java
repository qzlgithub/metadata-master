package com.mingdong.mis.configurer;

import com.alibaba.fastjson.JSON;
import com.mingdong.common.constant.Charset;
import com.mingdong.common.util.StringUtils;
import com.mingdong.common.util.WebUtils;
import com.mingdong.core.annotation.AuthRequired;
import com.mingdong.mis.component.RedisDao;
import com.mingdong.mis.constant.APIProduct;
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
        String uri = request.getRequestURI();
        logger.info("rest api request: [{}], {}", request.getMethod(), uri);
        MDResp resp = MDResp.create();
        RequestThread.init();
        if(handler.getClass().isAssignableFrom(HandlerMethod.class))
        {
            AuthRequired annotation = ((HandlerMethod) handler).getMethod().getAnnotation(AuthRequired.class);
            if(annotation != null)
            {
                String token = request.getHeader(Field.HEADER_ACCESS_TOKEN);
                if(StringUtils.isNullBlank(token))
                {
                    resp.setResult(MDResult.ACCESS_DENIED);
                    response.getOutputStream().write(JSON.toJSONString(resp).getBytes(Charset.UTF_8));
                    return false;
                }
                UserAuth auth = redisDao.findAuth(token);
                if(auth == null)
                {
                    resp.setResult(MDResult.INVALID_ACCESS_TOKEN);
                    response.getOutputStream().write(JSON.toJSONString(resp).getBytes(Charset.UTF_8));
                    return false;
                }
                APIProduct product = APIProduct.targetOf(auth.getProduct());
                if(product == null || !uri.equals(product.getUri()))
                {
                    resp.setResult(MDResult.ACCESS_RESTRICTED);
                    response.getOutputStream().write(JSON.toJSONString(resp).getBytes(Charset.UTF_8));
                    return false;
                }
                String ip = WebUtils.getIp(request);
                if(!auth.getHost().equals(ip))
                {
                    resp.setResult(MDResult.INVALID_CLIENT_IP);
                    response.getOutputStream().write(JSON.toJSONString(resp).getBytes(Charset.UTF_8));
                    return false;
                }
                RequestThread.setClientId(auth.getClientId());
                RequestThread.setUserId(auth.getUserId());
                RequestThread.setProductId(auth.getProductId());
                RequestThread.setClientProductId(auth.getClientProductId());
                RequestThread.setProduct(product);
                RequestThread.setBillPlan(auth.getBillPlan());
                RequestThread.setStart(auth.getStart());
                RequestThread.setEnd(auth.getEnd());
                RequestThread.setHost(ip);
                RequestThread.setAppSecret(auth.getSecretKey());
            }
        }
        RequestThread.setResp(resp);
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
