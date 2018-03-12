package com.mingdong.mis.model.metadata;

import com.mingdong.core.constant.TrueOrFalse;
import com.mingdong.mis.constant.Field;
import com.mingdong.mis.model.IMetadata;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MultipleApp implements IMetadata
{
    private String orderNo;
    private Integer hit;
    private Integer status;
    private String msg;//返回说明
    /*
     * 注意事项：
     * （1）Type：表示次数的级别
     * 当实际次数为0的时候，type的值为0，当实际次数非0时，type=(实际的次数-1)/dataSize+1；
     * （2）Rane：表示次数的范围
     * 前缀相同的Type和Range字段的之间的转换公式为：
     * Type 为 0，range 为 0
     * 否则：Range的范围为：[(type-1)*dataSize + 1,type * dataSize]
     * （3）日期的格式为：年月["上旬"|"中旬"|"下旬"]
     * [1,10]：上旬
     * [11-20]：中旬
     * 其他日期为下旬
     */
    private Integer dataSize;//模糊range范围
    private Integer idCardMpTimesType;//证件查询次数级别
    private String idCardMpTimesRange;//证件查询平台数
    private String idCardFirstTime;//证件最早出现日期
    private String idCardEndTime;//证件最近出现日期
    private Integer phoneMpTimesType;//手机查询次数级别
    private String phoneMpTimesRange;//手机查询平台数
    private String phoneFirstTime;//手机最早出现日期
    private String phoneEndTime;//手机最近出现日期
    private Integer todayIdCardApplicationTimeType;//证件当日查询次数级别
    private String todayIdCardApplicationTimeRange;//证件当日查询平台数
    private Integer lastThreeDayIdCardApplicationTimeType;//证件3天内查询次数级别
    private String lastThreeDayIdCardApplicationTimeRange;//证件3天内查询平台数
    private Integer lastSevenDayIdCardApplicationTimeType;//证件7天内查询次数级别
    private String lastSevenDayIdCardApplicationTimeRange;//证件7天内查询平台数
    private Integer lastFifteenDayIdCardApplicationTimeType;//证件15天内查询次数级别
    private String lastFifteenDayIdCardApplicationTimeRange;//证件15天内查询平台数
    private Integer lastAMonthIdCardApplicationTimeType;//证件1个月内查询次数级别
    private String lastAMonthIdCardApplicationTimeRange;//证件1个月内查询平台数
    private Integer lastTwoMonthIdCardApplicationTimeType;//证件2个月内查询次数级别
    private String lastTwoMonthIdCardApplicationTimeRange;//证件2个月内查询平台数
    private Integer lastThreeMonthIdCardApplicationTimeType;//证件3个月内查询次数级别
    private String lastThreeMonthIdCardApplicationTimeRange;//证件3个月内查询平台数
    private Integer todayPhoneApplicationTimeType;//手机当日查询次数级别
    private String todayPhoneApplicationTimeRange;//手机当日查询平台数
    private Integer lastThreeDayPhoneApplicationTimeType;//手机3天内查询次数级别
    private String lastThreeDayPhoneApplicationTimeRange;//手机3天内查询平台数
    private Integer lastSevenDayPhoneApplicationTimeType;//手机7天内查询次数级别
    private String lastSevenDayPhoneApplicationTimeRange;//手机7天内查询平台数
    private Integer lastFifteenDayPhoneApplicationTimeType;//手机15天内查询次数级别
    private String lastFifteenDayPhoneApplicationTimeRange;//手机15天内查询平台数
    private Integer lastAMonthPhoneApplicationTimeType;//手机1个月内查询次数级别
    private String lastAMonthPhoneApplicationTimeRange;//手机1个月内查询平台数
    private Integer lastTwoMonthPhoneApplicationTimeType;//手机2个月内查询次数级别
    private String lastTwoMonthPhoneApplicationTimeRange;//手机2个月内查询平台数
    private Integer lastThreeMonthPhoneApplicationTimeType;//手机3个月内查询次数级别
    private String lastThreeMonthPhoneApplicationTimeRange;//手机3个月内查询平台数
    /*
     * platformDetails 说明
     *   platformNo String 平台序号
     *   idCardTime String 证件出现日期
     *   phoneTime String 手机出现日期
     */
    private List<Map<String, Object>> platformDetails;//平台详情

    public Integer getDataSize()
    {
        return dataSize;
    }

    public void setDataSize(Integer dataSize)
    {
        this.dataSize = dataSize;
    }

    public Integer getIdCardMpTimesType()
    {
        return idCardMpTimesType;
    }

    public void setIdCardMpTimesType(Integer idCardMpTimesType)
    {
        this.idCardMpTimesType = idCardMpTimesType;
    }

    public String getIdCardMpTimesRange()
    {
        return idCardMpTimesRange;
    }

    public void setIdCardMpTimesRange(String idCardMpTimesRange)
    {
        this.idCardMpTimesRange = idCardMpTimesRange;
    }

    public String getIdCardFirstTime()
    {
        return idCardFirstTime;
    }

    public void setIdCardFirstTime(String idCardFirstTime)
    {
        this.idCardFirstTime = idCardFirstTime;
    }

    public String getIdCardEndTime()
    {
        return idCardEndTime;
    }

    public void setIdCardEndTime(String idCardEndTime)
    {
        this.idCardEndTime = idCardEndTime;
    }

    public Integer getPhoneMpTimesType()
    {
        return phoneMpTimesType;
    }

    public void setPhoneMpTimesType(Integer phoneMpTimesType)
    {
        this.phoneMpTimesType = phoneMpTimesType;
    }

    public String getPhoneMpTimesRange()
    {
        return phoneMpTimesRange;
    }

    public void setPhoneMpTimesRange(String phoneMpTimesRange)
    {
        this.phoneMpTimesRange = phoneMpTimesRange;
    }

    public String getPhoneFirstTime()
    {
        return phoneFirstTime;
    }

    public void setPhoneFirstTime(String phoneFirstTime)
    {
        this.phoneFirstTime = phoneFirstTime;
    }

    public String getPhoneEndTime()
    {
        return phoneEndTime;
    }

    public void setPhoneEndTime(String phoneEndTime)
    {
        this.phoneEndTime = phoneEndTime;
    }

    public Integer getTodayIdCardApplicationTimeType()
    {
        return todayIdCardApplicationTimeType;
    }

    public void setTodayIdCardApplicationTimeType(Integer todayIdCardApplicationTimeType)
    {
        this.todayIdCardApplicationTimeType = todayIdCardApplicationTimeType;
    }

    public String getTodayIdCardApplicationTimeRange()
    {
        return todayIdCardApplicationTimeRange;
    }

    public void setTodayIdCardApplicationTimeRange(String todayIdCardApplicationTimeRange)
    {
        this.todayIdCardApplicationTimeRange = todayIdCardApplicationTimeRange;
    }

    public Integer getLastThreeDayIdCardApplicationTimeType()
    {
        return lastThreeDayIdCardApplicationTimeType;
    }

    public void setLastThreeDayIdCardApplicationTimeType(Integer lastThreeDayIdCardApplicationTimeType)
    {
        this.lastThreeDayIdCardApplicationTimeType = lastThreeDayIdCardApplicationTimeType;
    }

    public String getLastThreeDayIdCardApplicationTimeRange()
    {
        return lastThreeDayIdCardApplicationTimeRange;
    }

    public void setLastThreeDayIdCardApplicationTimeRange(String lastThreeDayIdCardApplicationTimeRange)
    {
        this.lastThreeDayIdCardApplicationTimeRange = lastThreeDayIdCardApplicationTimeRange;
    }

    public Integer getLastSevenDayIdCardApplicationTimeType()
    {
        return lastSevenDayIdCardApplicationTimeType;
    }

    public void setLastSevenDayIdCardApplicationTimeType(Integer lastSevenDayIdCardApplicationTimeType)
    {
        this.lastSevenDayIdCardApplicationTimeType = lastSevenDayIdCardApplicationTimeType;
    }

    public String getLastSevenDayIdCardApplicationTimeRange()
    {
        return lastSevenDayIdCardApplicationTimeRange;
    }

    public void setLastSevenDayIdCardApplicationTimeRange(String lastSevenDayIdCardApplicationTimeRange)
    {
        this.lastSevenDayIdCardApplicationTimeRange = lastSevenDayIdCardApplicationTimeRange;
    }

    public Integer getLastFifteenDayIdCardApplicationTimeType()
    {
        return lastFifteenDayIdCardApplicationTimeType;
    }

    public void setLastFifteenDayIdCardApplicationTimeType(Integer lastFifteenDayIdCardApplicationTimeType)
    {
        this.lastFifteenDayIdCardApplicationTimeType = lastFifteenDayIdCardApplicationTimeType;
    }

    public String getLastFifteenDayIdCardApplicationTimeRange()
    {
        return lastFifteenDayIdCardApplicationTimeRange;
    }

    public void setLastFifteenDayIdCardApplicationTimeRange(String lastFifteenDayIdCardApplicationTimeRange)
    {
        this.lastFifteenDayIdCardApplicationTimeRange = lastFifteenDayIdCardApplicationTimeRange;
    }

    public Integer getLastAMonthIdCardApplicationTimeType()
    {
        return lastAMonthIdCardApplicationTimeType;
    }

    public void setLastAMonthIdCardApplicationTimeType(Integer lastAMonthIdCardApplicationTimeType)
    {
        this.lastAMonthIdCardApplicationTimeType = lastAMonthIdCardApplicationTimeType;
    }

    public String getLastAMonthIdCardApplicationTimeRange()
    {
        return lastAMonthIdCardApplicationTimeRange;
    }

    public void setLastAMonthIdCardApplicationTimeRange(String lastAMonthIdCardApplicationTimeRange)
    {
        this.lastAMonthIdCardApplicationTimeRange = lastAMonthIdCardApplicationTimeRange;
    }

    public Integer getLastTwoMonthIdCardApplicationTimeType()
    {
        return lastTwoMonthIdCardApplicationTimeType;
    }

    public void setLastTwoMonthIdCardApplicationTimeType(Integer lastTwoMonthIdCardApplicationTimeType)
    {
        this.lastTwoMonthIdCardApplicationTimeType = lastTwoMonthIdCardApplicationTimeType;
    }

    public String getLastTwoMonthIdCardApplicationTimeRange()
    {
        return lastTwoMonthIdCardApplicationTimeRange;
    }

    public void setLastTwoMonthIdCardApplicationTimeRange(String lastTwoMonthIdCardApplicationTimeRange)
    {
        this.lastTwoMonthIdCardApplicationTimeRange = lastTwoMonthIdCardApplicationTimeRange;
    }

    public Integer getLastThreeMonthIdCardApplicationTimeType()
    {
        return lastThreeMonthIdCardApplicationTimeType;
    }

    public void setLastThreeMonthIdCardApplicationTimeType(Integer lastThreeMonthIdCardApplicationTimeType)
    {
        this.lastThreeMonthIdCardApplicationTimeType = lastThreeMonthIdCardApplicationTimeType;
    }

    public String getLastThreeMonthIdCardApplicationTimeRange()
    {
        return lastThreeMonthIdCardApplicationTimeRange;
    }

    public void setLastThreeMonthIdCardApplicationTimeRange(String lastThreeMonthIdCardApplicationTimeRange)
    {
        this.lastThreeMonthIdCardApplicationTimeRange = lastThreeMonthIdCardApplicationTimeRange;
    }

    public Integer getTodayPhoneApplicationTimeType()
    {
        return todayPhoneApplicationTimeType;
    }

    public void setTodayPhoneApplicationTimeType(Integer todayPhoneApplicationTimeType)
    {
        this.todayPhoneApplicationTimeType = todayPhoneApplicationTimeType;
    }

    public String getTodayPhoneApplicationTimeRange()
    {
        return todayPhoneApplicationTimeRange;
    }

    public void setTodayPhoneApplicationTimeRange(String todayPhoneApplicationTimeRange)
    {
        this.todayPhoneApplicationTimeRange = todayPhoneApplicationTimeRange;
    }

    public Integer getLastThreeDayPhoneApplicationTimeType()
    {
        return lastThreeDayPhoneApplicationTimeType;
    }

    public void setLastThreeDayPhoneApplicationTimeType(Integer lastThreeDayPhoneApplicationTimeType)
    {
        this.lastThreeDayPhoneApplicationTimeType = lastThreeDayPhoneApplicationTimeType;
    }

    public String getLastThreeDayPhoneApplicationTimeRange()
    {
        return lastThreeDayPhoneApplicationTimeRange;
    }

    public void setLastThreeDayPhoneApplicationTimeRange(String lastThreeDayPhoneApplicationTimeRange)
    {
        this.lastThreeDayPhoneApplicationTimeRange = lastThreeDayPhoneApplicationTimeRange;
    }

    public Integer getLastSevenDayPhoneApplicationTimeType()
    {
        return lastSevenDayPhoneApplicationTimeType;
    }

    public void setLastSevenDayPhoneApplicationTimeType(Integer lastSevenDayPhoneApplicationTimeType)
    {
        this.lastSevenDayPhoneApplicationTimeType = lastSevenDayPhoneApplicationTimeType;
    }

    public String getLastSevenDayPhoneApplicationTimeRange()
    {
        return lastSevenDayPhoneApplicationTimeRange;
    }

    public void setLastSevenDayPhoneApplicationTimeRange(String lastSevenDayPhoneApplicationTimeRange)
    {
        this.lastSevenDayPhoneApplicationTimeRange = lastSevenDayPhoneApplicationTimeRange;
    }

    public Integer getLastFifteenDayPhoneApplicationTimeType()
    {
        return lastFifteenDayPhoneApplicationTimeType;
    }

    public void setLastFifteenDayPhoneApplicationTimeType(Integer lastFifteenDayPhoneApplicationTimeType)
    {
        this.lastFifteenDayPhoneApplicationTimeType = lastFifteenDayPhoneApplicationTimeType;
    }

    public String getLastFifteenDayPhoneApplicationTimeRange()
    {
        return lastFifteenDayPhoneApplicationTimeRange;
    }

    public void setLastFifteenDayPhoneApplicationTimeRange(String lastFifteenDayPhoneApplicationTimeRange)
    {
        this.lastFifteenDayPhoneApplicationTimeRange = lastFifteenDayPhoneApplicationTimeRange;
    }

    public Integer getLastAMonthPhoneApplicationTimeType()
    {
        return lastAMonthPhoneApplicationTimeType;
    }

    public void setLastAMonthPhoneApplicationTimeType(Integer lastAMonthPhoneApplicationTimeType)
    {
        this.lastAMonthPhoneApplicationTimeType = lastAMonthPhoneApplicationTimeType;
    }

    public String getLastAMonthPhoneApplicationTimeRange()
    {
        return lastAMonthPhoneApplicationTimeRange;
    }

    public void setLastAMonthPhoneApplicationTimeRange(String lastAMonthPhoneApplicationTimeRange)
    {
        this.lastAMonthPhoneApplicationTimeRange = lastAMonthPhoneApplicationTimeRange;
    }

    public Integer getLastTwoMonthPhoneApplicationTimeType()
    {
        return lastTwoMonthPhoneApplicationTimeType;
    }

    public void setLastTwoMonthPhoneApplicationTimeType(Integer lastTwoMonthPhoneApplicationTimeType)
    {
        this.lastTwoMonthPhoneApplicationTimeType = lastTwoMonthPhoneApplicationTimeType;
    }

    public String getLastTwoMonthPhoneApplicationTimeRange()
    {
        return lastTwoMonthPhoneApplicationTimeRange;
    }

    public void setLastTwoMonthPhoneApplicationTimeRange(String lastTwoMonthPhoneApplicationTimeRange)
    {
        this.lastTwoMonthPhoneApplicationTimeRange = lastTwoMonthPhoneApplicationTimeRange;
    }

    public Integer getLastThreeMonthPhoneApplicationTimeType()
    {
        return lastThreeMonthPhoneApplicationTimeType;
    }

    public void setLastThreeMonthPhoneApplicationTimeType(Integer lastThreeMonthPhoneApplicationTimeType)
    {
        this.lastThreeMonthPhoneApplicationTimeType = lastThreeMonthPhoneApplicationTimeType;
    }

    public String getLastThreeMonthPhoneApplicationTimeRange()
    {
        return lastThreeMonthPhoneApplicationTimeRange;
    }

    public void setLastThreeMonthPhoneApplicationTimeRange(String lastThreeMonthPhoneApplicationTimeRange)
    {
        this.lastThreeMonthPhoneApplicationTimeRange = lastThreeMonthPhoneApplicationTimeRange;
    }

    public List<Map<String, Object>> getPlatformDetails()
    {
        return platformDetails;
    }

    public void setPlatformDetails(List<Map<String, Object>> platformDetails)
    {
        this.platformDetails = platformDetails;
    }

    public Integer getStatus()
    {
        return status;
    }

    public void setStatus(Integer status)
    {
        this.status = status;
    }

    public String getMsg()
    {
        return msg;
    }

    public void setMsg(String msg)
    {
        this.msg = msg;
    }

    public String getOrderNo()
    {
        return orderNo;
    }

    public void setOrderNo(String orderNo)
    {
        this.orderNo = orderNo;
    }

    public Integer getHit()
    {
        return hit;
    }

    @Override
    public boolean isHit()
    {
        return TrueOrFalse.TRUE.equals(hit);
    }

    public void setHit(Integer hit)
    {
        this.hit = hit;
    }

    @Override
    public Map<String, Object> response()
    {
        Map<String, Object> map = new HashMap<>();
        if(-1 != hit)
        {
            map.put(Field.IS_HIT, hit);
            map.put("orderNo", orderNo);
        }
        else
        {
            map.put("orderNo", orderNo);
            map.put("status", status);
            map.put("msg", msg);
            if(TrueOrFalse.TRUE.equals(status))
            {
                map.put("dataSize", dataSize);//模糊 range 范围，表示后面带次数的字段模糊区间。 如： 3
                map.put("idCardMpTimesType", idCardMpTimesType);//证件查询次数级别，0,1,2,3……
                map.put("idCardMpTimesRange", idCardMpTimesRange);//证件查询平台数，0， [1,3],[4,6]……以下 type 以此类推
                map.put("idCardFirstTime", idCardFirstTime);//证件最早出现日期
                map.put("idCardEndTime", idCardEndTime);//证件最近出现日期
                map.put("phoneMpTimesType", phoneMpTimesType);//手机查询次数级别
                map.put("phoneMpTimesRange", phoneMpTimesRange);//手机查询平台数
                map.put("phoneFirstTime", phoneFirstTime);//手机最早出现日期
                map.put("phoneEndTime", phoneEndTime);//手机最近出现日期
                map.put("todayIdCardApplicationTimeType", todayIdCardApplicationTimeType);//证件当日查询次数级别
                map.put("todayIdCardApplicationTimeRange", todayIdCardApplicationTimeRange);//证件当日查询平台数
                map.put("lastThreeDayIdCardApplicationTimeType", lastThreeDayIdCardApplicationTimeType);//证件3天内查询次数级别
                map.put("lastThreeDayIdCardApplicationTimeRange", lastThreeDayIdCardApplicationTimeRange);//证件3天内查询平台数
                map.put("lastSevenDayIdCardApplicationTimeType", lastSevenDayIdCardApplicationTimeType);//证件7天内查询次数级别
                map.put("lastSevenDayIdCardApplicationTimeRange", lastSevenDayIdCardApplicationTimeRange);//证件7天内查询平台数
                map.put("lastFifteenDayIdCardApplicationTimeType",
                        lastFifteenDayIdCardApplicationTimeType);//证件15天内查询次数级别
                map.put("lastFifteenDayIdCardApplicationTimeRange",
                        lastFifteenDayIdCardApplicationTimeRange);//证件15天内查询平台数
                map.put("lastAMonthIdCardApplicationTimeType", lastAMonthIdCardApplicationTimeType);//证件1个月内查询次数级别
                map.put("lastAMonthIdCardApplicationTimeRange", lastAMonthIdCardApplicationTimeRange);//证件1个月内查询平台数
                map.put("lastTwoMonthIdCardApplicationTimeType", lastTwoMonthIdCardApplicationTimeType);//证件2个月内查询次数级别
                map.put("lastTwoMonthIdCardApplicationTimeRange", lastTwoMonthIdCardApplicationTimeRange);//证件2个月内查询平台数
                map.put("lastThreeMonthIdCardApplicationTimeType",
                        lastThreeMonthIdCardApplicationTimeType);//证件3个月内查询次数级别
                map.put("lastThreeMonthIdCardApplicationTimeRange",
                        lastThreeMonthIdCardApplicationTimeRange);//证件3个月内查询平台数
                map.put("todayPhoneApplicationTimeType", todayPhoneApplicationTimeType);//手机当日查询次数级别
                map.put("todayPhoneApplicationTimeRange", todayPhoneApplicationTimeRange);//手机当日查询平台数
                map.put("lastThreeDayPhoneApplicationTimeType", lastThreeDayPhoneApplicationTimeType);//手机3天内查询次数级别
                map.put("lastThreeDayPhoneApplicationTimeRange", lastThreeDayPhoneApplicationTimeRange);//手机3天内查询平台数
                map.put("lastSevenDayPhoneApplicationTimeType", lastSevenDayPhoneApplicationTimeType);//手机7天内查询次数级别
                map.put("lastSevenDayPhoneApplicationTimeRange", lastSevenDayPhoneApplicationTimeRange);//手机7天内查询平台数
                map.put("lastFifteenDayPhoneApplicationTimeType", lastFifteenDayPhoneApplicationTimeType);//手机15天内查询次数级别
                map.put("lastFifteenDayPhoneApplicationTimeRange",
                        lastFifteenDayPhoneApplicationTimeRange);//手机15天内查询平台数
                map.put("lastAMonthPhoneApplicationTimeType", lastAMonthPhoneApplicationTimeType);//手机1个月内查询次数级别
                map.put("lastAMonthPhoneApplicationTimeRange", lastAMonthPhoneApplicationTimeRange);//手机1个月内查询平台数
                map.put("lastTwoMonthPhoneApplicationTimeType", lastTwoMonthPhoneApplicationTimeType);//手机2个月内查询次数级别
                map.put("lastTwoMonthPhoneApplicationTimeRange", lastTwoMonthPhoneApplicationTimeRange);//手机2个月内查询平台数
                map.put("lastThreeMonthPhoneApplicationTimeType", lastThreeMonthPhoneApplicationTimeType);//手机3个月内查询次数级别
                map.put("lastThreeMonthPhoneApplicationTimeRange",
                        lastThreeMonthPhoneApplicationTimeRange);//手机3个月内查询平台数
                map.put("platformDetails", platformDetails);//平台详情
            }
        }
        return map;
    }

    @Override
    public String getThirdNo()
    {
        return orderNo;
    }

    @Override
    public boolean isSaveLog()
    {
        return -1 != hit;
    }
}
