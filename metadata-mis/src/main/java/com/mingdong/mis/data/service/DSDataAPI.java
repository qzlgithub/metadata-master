package com.mingdong.mis.data.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mingdong.common.constant.Charset;
import com.mingdong.common.util.MapUtils;
import com.mingdong.common.util.StringUtils;
import com.mingdong.core.constant.TrueOrFalse;
import com.mingdong.core.exception.MetadataAPIException;
import com.mingdong.core.exception.MetadataCoreException;
import com.mingdong.mis.component.BusinessAbstract;
import com.mingdong.mis.component.RedisDao;
import com.mingdong.mis.model.DSAuthToken;
import com.mingdong.mis.model.metadata.Blacklist;
import com.mingdong.mis.model.metadata.MultipleApp;
import com.mingdong.mis.model.vo.BlacklistVO;
import com.mingdong.mis.model.vo.MultipleAppVO;
import com.mingdong.mis.util.HttpUtils;
import com.mingdong.mis.util.SignUtils;
import org.apache.http.HttpEntity;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;

@Component
public class DSDataAPI
{
    // 获取接入凭证
    private static final String AUTH_TOKEN_API = "https://api.dsdatas.com/credit/api/token";
    // API - 泛黑名单
    private static final String GENERALIZE_BLACKLIST_API = "https://api.dsdatas.com/blackData/fireGeneralizeBlack";
    // API - 常贷客
    private static final String MULTIPLE_APP_API = "https://api.dsdatas.com/credit/api/v1/query";
    private static final String MULTIPLE_APP_API_2 =
            "https://api.dsdatas.com/notify/fire/multipleApplications/report/v1";

    private static final String APP_KEY = "e7e552ec9d83114ee17b81fa04230200";
    private static final String SECURITY_KEY = "cb17fe4f427c8eaf232f37ab3cfa2ab6c204f670";
    private static Logger logger = LoggerFactory.getLogger(DSDataAPI.class);

    @Resource
    private RedisDao redisDao;

    private static Map<String, String> getHeader()
    {
        Map<String, String> header = new HashMap<>();
        header.put("Content-Type", "application/json;charset=UTF-8");
        return header;
    }

    private DSAuthToken obtainAuthToken() throws MetadataAPIException
    {
        Map<String, Object> body = new HashMap<>();
        body.put("apiKey", APP_KEY);
        body.put("securityKey", SECURITY_KEY);
        body.put("refresh", 1);
        try
        {
            HttpEntity entity = HttpUtils.postData(AUTH_TOKEN_API, getHeader(), JSON.toJSONString(body));
            String resp = EntityUtils.toString(entity, Charset.UTF_8);
            logger.info("DS API response: {}", resp);
            JSONObject res = JSON.parseObject(resp);
            if(res.getIntValue("code") == 200)
            {
                JSONObject data = res.getJSONObject("data");
                return JSON.parseObject(data.toJSONString(), DSAuthToken.class);
            }
        }
        catch(Exception e)
        {
            logger.error("Failed to obtain DaSheng auth token: {}", e.getMessage());
        }
        throw new MetadataAPIException("error to revoke DaSheng data api");
    }

    public Blacklist callBlacklist(BlacklistVO parameterVO) throws MetadataAPIException
    {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", System.currentTimeMillis());
        body.put("name", parameterVO.getName());
        body.put("idCard", parameterVO.getIdNo());
        body.put("phone", parameterVO.getPhone());
        Map<String, String> header = getHeader();
        header.put("auth_token", getAuthToken());
        return getDataForPost(GENERALIZE_BLACKLIST_API, "blacklist", header, body, new BusinessAbstract<Blacklist>()
        {
            @Override
            public Blacklist getSuccessData(JSONObject res)
            {
                String orderNo = res.getString("orderNo");
                JSONObject data = res.getJSONObject("res");
                Integer stat = data.getInteger("stat");
                Blacklist blacklist = new Blacklist();
                blacklist.setOrderNo(orderNo);
                blacklist.setHit(TrueOrFalse.TRUE.equals(stat) ? TrueOrFalse.TRUE : TrueOrFalse.FALSE);
                return blacklist;
            }
        });
    }

    public MultipleApp callMultipleApp(MultipleAppVO parameterVO) throws MetadataAPIException, MetadataCoreException
    {
        String orderNo;
        Map<String, String> header = getHeader();
        header.put("auth_token", getAuthToken());
        if(StringUtils.isNullBlank(parameterVO.getOrderNo()))
        {
            Map<String, Object> body = new HashMap<>();
            body.put("apiKey", APP_KEY);
            body.put("channelNo", "CH0426533894");
            body.put("interfaceName", "multipleApplications");
            body.put("timestamp", System.currentTimeMillis());
            Map<String, Object> payload = new HashMap<>();
            payload.put("idCard", parameterVO.getIdNo());
            payload.put("name", parameterVO.getName());
            payload.put("phone", parameterVO.getPhone());
            Map<String, Object> signMap = new HashMap<>();
            signMap.putAll(body);
            signMap.putAll(payload);
            SortedMap sortedMap = MapUtils.sortKey(signMap);
            String orgStr = JSON.toJSONString(sortedMap);
            String sign = SignUtils.sign(orgStr, SECURITY_KEY);
            body.put("payload", payload);
            body.put("sign", sign);
            orderNo = getDataForPost(MULTIPLE_APP_API, "multipleApp", header, body, new BusinessAbstract<String>()
            {
                @Override
                public String getSuccessData(JSONObject res)
                {
                    return res.getString("orderNo");
                }
            });
        }
        else
        {
            orderNo = parameterVO.getOrderNo();
        }
        Map<String, Object> data = new HashMap<>();
        data.put("orderNo", orderNo);
        return getDataForGet(MULTIPLE_APP_API_2, "multipleApp2", header, data, new BusinessAbstract<MultipleApp>()
        {
            @Override
            public MultipleApp getSuccessData(JSONObject res)
            {
                String orderNo = res.getString("orderNo");
                JSONObject data = res.getJSONObject("res");
                Integer status = data.getInteger("status");
                String msg = data.getString("msg");
                MultipleApp multipleApp = new MultipleApp();
                multipleApp.setOrderNo(orderNo);
                if(StringUtils.isNullBlank(parameterVO.getOrderNo()))
                {
                    multipleApp.setHit(TrueOrFalse.FALSE.equals(status) ? TrueOrFalse.FALSE : TrueOrFalse.TRUE);
                }
                else
                {
                    multipleApp.setHit(-1);
                    multipleApp.setStatus(status);
                    multipleApp.setMsg(msg);
                    if(TrueOrFalse.TRUE.equals(status))
                    {
                        multipleApp.setDataSize(res.getInteger("dataSize"));//模糊 range 范围，表示后面带次数的字段模糊区间。 如： 3
                        multipleApp.setIdCardMpTimesType(res.getInteger("idCardMpTimesType"));//证件查询次数级别，0,1,2,3……
                        multipleApp.setIdCardMpTimesRange(res.getString("idCardMpTimesRange"));//证件查询平台数，0， [1,3],[4,6]……以下 type 以此类推
                        multipleApp.setIdCardFirstTime(res.getString("idCardFirstTime"));//证件最早出现日期
                        multipleApp.setIdCardEndTime(res.getString("idCardEndTime"));//证件最近出现日期
                        multipleApp.setPhoneMpTimesType(res.getInteger("phoneMpTimesType"));//手机查询次数级别
                        multipleApp.setPhoneMpTimesRange(res.getString("phoneMpTimesRange"));//手机查询平台数
                        multipleApp.setPhoneFirstTime(res.getString("phoneFirstTime"));//手机最早出现日期
                        multipleApp.setPhoneEndTime(res.getString("phoneEndTime"));//手机最近出现日期
                        multipleApp.setTodayIdCardApplicationTimeType(
                                res.getInteger("todayIdCardApplicationTimeType"));//证件当日查询次数级别
                        multipleApp.setTodayIdCardApplicationTimeRange(
                                res.getString("todayIdCardApplicationTimeRange"));//证件当日查询平台数
                        multipleApp.setLastThreeDayIdCardApplicationTimeType(
                                res.getInteger("lastThreeDayIdCardApplicationTimeType"));//证件3天内查询次数级别
                        multipleApp.setLastThreeDayIdCardApplicationTimeRange(
                                res.getString("lastThreeDayIdCardApplicationTimeRange"));//证件3天内查询平台数
                        multipleApp.setLastSevenDayIdCardApplicationTimeType(
                                res.getInteger("lastSevenDayIdCardApplicationTimeType"));//证件7天内查询次数级别
                        multipleApp.setLastSevenDayIdCardApplicationTimeRange(
                                res.getString("lastSevenDayIdCardApplicationTimeRange"));//证件7天内查询平台数
                        multipleApp.setLastFifteenDayIdCardApplicationTimeType(
                                res.getInteger("lastFifteenDayIdCardApplicationTimeType"));//证件15天内查询次数级别
                        multipleApp.setLastFifteenDayIdCardApplicationTimeRange(
                                res.getString("lastFifteenDayIdCardApplicationTimeRange"));//证件15天内查询平台数
                        multipleApp.setLastAMonthIdCardApplicationTimeType(
                                res.getInteger("lastAMonthIdCardApplicationTimeType"));//证件1个月内查询次数级别
                        multipleApp.setLastAMonthIdCardApplicationTimeRange(
                                res.getString("lastAMonthIdCardApplicationTimeRange"));//证件1个月内查询平台数
                        multipleApp.setLastTwoMonthIdCardApplicationTimeType(
                                res.getInteger("lastTwoMonthIdCardApplicationTimeType"));//证件2个月内查询次数级别
                        multipleApp.setLastTwoMonthIdCardApplicationTimeRange(
                                res.getString("lastTwoMonthIdCardApplicationTimeRange"));//证件2个月内查询平台数
                        multipleApp.setLastThreeMonthIdCardApplicationTimeType(
                                res.getInteger("lastThreeMonthIdCardApplicationTimeType"));//证件3个月内查询次数级别
                        multipleApp.setLastThreeMonthIdCardApplicationTimeRange(
                                res.getString("lastThreeMonthIdCardApplicationTimeRange"));//证件3个月内查询平台数
                        multipleApp.setTodayPhoneApplicationTimeType(
                                res.getInteger("todayPhoneApplicationTimeType"));//手机当日查询次数级别
                        multipleApp.setTodayPhoneApplicationTimeRange(
                                res.getString("todayPhoneApplicationTimeRange"));//手机当日查询平台数
                        multipleApp.setLastThreeDayPhoneApplicationTimeType(
                                res.getInteger("lastThreeDayPhoneApplicationTimeType"));//手机3天内查询次数级别
                        multipleApp.setLastThreeDayPhoneApplicationTimeRange(
                                res.getString("lastThreeDayPhoneApplicationTimeRange"));//手机3天内查询平台数
                        multipleApp.setLastSevenDayPhoneApplicationTimeType(
                                res.getInteger("lastSevenDayPhoneApplicationTimeType"));//手机7天内查询次数级别
                        multipleApp.setLastSevenDayPhoneApplicationTimeRange(
                                res.getString("lastSevenDayPhoneApplicationTimeRange"));//手机7天内查询平台数
                        multipleApp.setLastFifteenDayPhoneApplicationTimeType(
                                res.getInteger("lastFifteenDayPhoneApplicationTimeType"));//手机15天内查询次数级别
                        multipleApp.setLastFifteenDayPhoneApplicationTimeRange(
                                res.getString("lastFifteenDayPhoneApplicationTimeRange"));//手机15天内查询平台数
                        multipleApp.setLastAMonthPhoneApplicationTimeType(
                                res.getInteger("lastAMonthPhoneApplicationTimeType"));//手机1个月内查询次数级别
                        multipleApp.setLastAMonthPhoneApplicationTimeRange(
                                res.getString("lastAMonthPhoneApplicationTimeRange"));//手机1个月内查询平台数
                        multipleApp.setLastTwoMonthPhoneApplicationTimeType(
                                res.getInteger("lastTwoMonthPhoneApplicationTimeType"));//手机2个月内查询次数级别
                        multipleApp.setLastTwoMonthPhoneApplicationTimeRange(
                                res.getString("lastTwoMonthPhoneApplicationTimeRange"));//手机2个月内查询平台数
                        multipleApp.setLastThreeMonthPhoneApplicationTimeType(
                                res.getInteger("lastThreeMonthPhoneApplicationTimeType"));//手机3个月内查询次数级别
                        multipleApp.setLastThreeMonthPhoneApplicationTimeRange(
                                res.getString("lastThreeMonthPhoneApplicationTimeRange"));//手机3个月内查询平台数
                        JSONArray jsonArray = (JSONArray) res.get("platformDetails");
                        List<Map<String, Object>> list = new ArrayList<>();
                        Map<String, Object> mapTemp;
                        JSONObject json;
                        for(Object o : jsonArray)
                        {
                            mapTemp = new HashMap<>();
                            json = (JSONObject) o;
                            mapTemp.put("platformNo", json.getString("platformNo"));
                            mapTemp.put("idCardTime", json.getString("idCardTime"));
                            mapTemp.put("phoneTime", json.getString("phoneTime"));
                            list.add(mapTemp);
                        }
                        multipleApp.setPlatformDetails(list);
                    }
                }
                return multipleApp;
            }
        });
    }

    //    public static Blacklist callMultiAppApiS1(String idNo, String name, String phone)
    //    {
    //        Map<String, Object> body = new HashMap<>();
    //        body.put("apiKey", APP_KEY);
    //        body.put("channelNo", "CH0426533894");
    //        body.put("interfaceName", "multipleApplications");
    //        body.put("timestamp", System.currentTimeMillis());
    //        Map<String, Object> payload = new HashMap<>();
    //        payload.put("idCard", idNo);
    //        payload.put("name", name);
    //        payload.put("phone", phone);
    //        Map<String, Object> signMap = new HashMap<>();
    //        signMap.putAll(body);
    //        signMap.putAll(payload);
    //        SortedMap sortedMap = MapUtils.sortKey(signMap);
    //        String orgStr = JSON.toJSONString(sortedMap);
    //        try
    //        {
    //            String sign = SignUtils.sign(orgStr, SECURITY_KEY);
    //            body.put("payload", payload);
    //            body.put("sign", sign);
    //            HttpEntity entity = HttpUtils.postData(MULTIPLE_APP_API, getHeader(), JSON.toJSONString(body));
    //            String resp = EntityUtils.toString(entity, "UTF-8");
    //            System.out.println(resp);
    //            return JSON.parseObject(resp, Blacklist.class);
    //        }
    //        catch(Exception e)
    //        {
    //            logger.error("Error occurred while revoke dasheng data api");
    //        }
    //        return null;
    //    }
    //    public static void main(String[] args)
    //    {
    //        callMultiAppApiS1("370503198409120910","李庆飞","13548759635");
    //    }

    //    public void callMultiAppApiS2(String orderNo) throws MetadataCoreException, IOException
    //    {
    //        Map<String, Object> body = new HashMap<>();
    //        body.put("apiKey", APP_KEY);
    //        body.put("orderNo", orderNo);
    //        body.put("timestamp", System.currentTimeMillis());
    //
    //        SortedMap sortedMap = MapUtils.sortKey(body);
    //        String orgStr = JSON.toJSONString(sortedMap);
    //        body.put("sign", SignUtils.sign(orgStr, SECURITY_KEY));
    //
    //        HttpEntity entity = HttpUtils.get(MULTIPLE_APP_API_2, null, body);
    //        String resp = EntityUtils.toString(entity, "UTF-8");
    //        System.out.println(resp);
    //    }

    /**
     * 获取大圣数据的请求凭证，先在缓存中查找，未找到才强制刷新
     */
    private String getAuthToken() throws MetadataAPIException
    {
        String token = redisDao.getDSAuthToken();
        if(StringUtils.isNullBlank(token))
        {
            // 如果缓存中未找到大圣数据的请求凭证，则强制刷新
            token = refreshAuthToken();
        }
        return token;
    }

    /**
     * 强制刷新大圣数据的请求凭证
     */
    private String refreshAuthToken() throws MetadataAPIException
    {
        DSAuthToken authToken = obtainAuthToken();
        String token = authToken.getToken();
        Long expiration = authToken.getExpire();
        Long ex = (expiration - System.currentTimeMillis() - 600000) / 1000;
        redisDao.setDSAuthToken(token, ex);
        return token;
    }

    private <T> T getDataForGet(String url, String InterfaceName, Map<String, String> header, Map<String, Object> data,
            BusinessAbstract<T> businessAbstract) throws MetadataAPIException
    {
        try
        {
            HttpEntity entity = HttpUtils.get(url, header, data);
            String resp = EntityUtils.toString(entity, Charset.UTF_8);
            logger.info("DS API response: {}", resp);
            JSONObject res = JSON.parseObject(resp);
            if(res.getIntValue("code") == 102)
            {
                // 如果token过期则强制刷新后再请求一次
                header.put("auth_token", refreshAuthToken());
                entity = HttpUtils.get(url, header, data);
                resp = EntityUtils.toString(entity, Charset.UTF_8);
                logger.info("DS API response: {}", resp);
                res = JSON.parseObject(resp);
            }
            if(res.getIntValue("code") == 200)
            {
                return businessAbstract.getSuccessData(res);
            }
        }
        catch(Exception e)
        {
            logger.error("Failed to revoke DaSheng {} api: {}", InterfaceName, e.getMessage());
        }
        throw new MetadataAPIException("error to revoke DaSheng " + InterfaceName + " data api");
    }

    private <T> T getDataForPost(String url, String InterfaceName, Map<String, String> header, Map<String, Object> body,
            BusinessAbstract<T> businessAbstract) throws MetadataAPIException
    {
        try
        {
            HttpEntity entity = HttpUtils.postData(url, header, JSON.toJSONString(body));
            String resp = EntityUtils.toString(entity, Charset.UTF_8);
            logger.info("DS API response: {}", resp);
            JSONObject res = JSON.parseObject(resp);
            if(res.getIntValue("code") == 102)
            {
                // 如果token过期则强制刷新后再请求一次
                header.put("auth_token", refreshAuthToken());
                entity = HttpUtils.postData(url, header, JSON.toJSONString(body));
                resp = EntityUtils.toString(entity, Charset.UTF_8);
                logger.info("DS API response: {}", resp);
                res = JSON.parseObject(resp);
            }
            if(res.getIntValue("code") == 200)
            {
                return businessAbstract.getSuccessData(res);
            }
        }
        catch(Exception e)
        {
            logger.error("Failed to revoke DaSheng {} api: {}", InterfaceName, e.getMessage());
        }
        throw new MetadataAPIException("error to revoke DaSheng " + InterfaceName + " data api");
    }
}
