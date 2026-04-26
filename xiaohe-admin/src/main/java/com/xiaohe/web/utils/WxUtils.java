package com.xiaohe.web.utils;

import com.alibaba.fastjson.JSON;

import com.xiaohe.common.core.redis.RedisCache;
import com.xiaohe.common.exception.GlobalException;
import com.xiaohe.web.domain.*;
import com.xiaohe.web.domain.Entity.SysWorkerUser;
import com.xiaohe.web.domain.Export.SysRecords;
import com.xiaohe.web.domain.WxDataAnalyze.WXWeekDataDto;
import com.xiaohe.web.domain.WxDataAnalyze.WxDataPorfileDto;
import com.xiaohe.web.domain.WxDataAnalyze.WxMonthDataDto;

import com.xiaohe.web.domain.WxPhone.WxCode;
import com.xiaohe.web.domain.WxPhone.WxGetPhoneVo;
import com.xiaohe.web.domain.WxTemplate.*;

import com.xiaohe.web.mapper.SysWorkerUserMapper;
import com.xiaohe.web.service.SysRecordsService;
import com.xiaohe.web.service.WxUserService;

import com.xiaohe.web.wxConfig.WxConstant;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static com.xiaohe.common.utils.SecurityUtils.getLoginUser;

@Component
public  class WxUtils {

    @Resource
    private RedisCache redisCache;

    @Resource
    private WxUserService wxUserService;

    @Resource
    private SysRecordsService sysRecordsService;


    @Resource
    private SysWorkerUserMapper sysWorkerUserMapper;


    private static SimpleDateFormat formatHmh = new SimpleDateFormat("yyyyMMdd");
    public static SimpleDateFormat formatHmh1 = new SimpleDateFormat(
            "yyyyMMdd");


    /**
     * 得到指定日期的一天的的最后时刻23:59:59
     *
     * @param date
     * @return
     */
    public Date getFinallyDate(Date date) {
        String temp = formatHmh.format(date);
        temp += " 23:59:59";

        try {
            return formatHmh1.parse(temp);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 得到指定日期的一天的开始时刻00:00:00
     *
     * @param date
     * @return
     */
    public Date getStartDate(Date date) {
        String temp = formatHmh.format(date);

        try {
            return formatHmh1.parse(temp);
        } catch (Exception e) {
            return null;
        }
    }


    /**
     * 审核结果通知
     *
     * @param data void
     */
    public void sendExamine(WxDataVo data, Long accountsId, Long userId) {
        try {
            SysWorkerUser sysWorkerUser = sysWorkerUserMapper.selectById(userId);

            //  取出微信token 如果没有或者过期重新获取
            String wxToken = redisCache.getCacheObject("access_token");
            // String wxToken = redisCache.getCacheObject(openId);
            if (wxToken == null) {
                wxToken = getWxaccess_token("access_token");
            }
            String url = WxConstant.WX_TEMPLATE_EXAMINE_URL + wxToken;
            WxMessageVo wxMessageVo = new WxMessageVo();
            // 语言
            wxMessageVo.setLang("zh_CN");
            // 点击跳转小程序页面 带入参数小程序进入页面接收
            wxMessageVo.setPage("pages/accounts/index?accountsId=" + accountsId);
            // 模板id
            wxMessageVo.setTemplate_id(WxConstant.EXAMINE_TEMPLATE_ID);
            // openid
            wxMessageVo.setTouser(sysWorkerUser.getOpenId());
            // 设置类型
            wxMessageVo.setMiniprogram_state(WxConstant.WX_TEMPLATE_VERSION);

            wxMessageVo.setData(data);
            String jsonString = JSON.toJSONString(wxMessageVo);
            //  发送消息
            String result = HttpUtil.doPost(url, jsonString);
            System.out.println("发送结果==>" + result);

            SysRecords sysRecords = new SysRecords();

            // 微信审核时查询个人信息
            SysWorkerUser sysWorkerUser1 = sysWorkerUserMapper.selectById(getLoginUser().getUserId());
            // 发送人
            sysRecords.setSendBy(sysWorkerUser1.getName());
            // 发送人手机号
            sysRecords.setSendByPhone(sysWorkerUser1.getPhone());
            // 信息模板类型
            sysRecords.setSendType("审核结果通知模板");
            // 接收人
            sysRecords.setAcceptBy(sysWorkerUser.getName());
            // 接收人
            sysRecords.setAcceptPhone(sysWorkerUser.getPhone());
            // 接收人openId
            sysRecords.setAcceptOpenId(sysWorkerUser.getOpenId());
            // 创建时间
            sysRecords.setCreateTime(new Date());

            // 如果发送消息错误，记录到访客记录中状态为发送失败
            WxGetPhoneVo wxGetPhoneVo = JSON.parseObject(result, WxGetPhoneVo.class);
            if (wxGetPhoneVo.getErrcode() != 0) {
                // 失败状态
                sysRecords.setSendState("2");
                sysRecords.setErrorMsg(wxGetPhoneVo.getErrmsg());

            } else {
                // 成功状态
                sysRecords.setSendState("1");
                sysRecords.setErrorMsg("发送成功");
            }
            sysRecordsService.add(sysRecords);
//            if (wxGetPhoneVo.getErrcode() != 0) {
//                throw new GlobalException("发送消息错误=>" + wxGetPhoneVo.getErrmsg());
//            }
        } catch (Exception e) {
            throw new GlobalException("审核结果通知=>" + e);
        }
    }


    /**
     * 审核提醒
     *
     * @param data void
     */
    public void examineReminder(WxExamineReminderVo data, Long accountsId, Long userId) {
        try {
            SysWorkerUser sysWorkerUser = sysWorkerUserMapper.selectById(userId);

            //  取出微信token 如果没有或者过期重新获取
            String wxToken = redisCache.getCacheObject("access_token");
            // String wxToken = redisCache.getCacheObject(openId);
            if (wxToken == null) {
                wxToken = getWxaccess_token("access_token");
            }
            String url = WxConstant.WX_TEMPLATE_EXAMINE_URL + wxToken;
            WxMessageVo wxMessageVo = new WxMessageVo();
            // 语言
            wxMessageVo.setLang("zh_CN");
            // 点击跳转小程序页面 带入参数小程序进入页面接收
            wxMessageVo.setPage("pages/accounts/index?accountsId=" + accountsId);
            // 模板id
            wxMessageVo.setTemplate_id(WxConstant.EXAMINE_TEMPLATE_ID);
            // openid
            wxMessageVo.setTouser(sysWorkerUser.getOpenId());
            // 设置类型
            wxMessageVo.setMiniprogram_state(WxConstant.WX_TEMPLATE_VERSION);

            wxMessageVo.setData(data);
            String jsonString = JSON.toJSONString(wxMessageVo);
            //  发送消息
            String result = HttpUtil.doPost(url, jsonString);
            System.out.println("发送结果==>" + result);

            SysRecords sysRecords = new SysRecords();

            // 微信审核时查询个人信息
            SysWorkerUser sysWorkerUser1 = sysWorkerUserMapper.selectById(getLoginUser().getUserId());
            // 发送人
            sysRecords.setSendBy(sysWorkerUser1.getName());
            // 发送人手机号
            sysRecords.setSendByPhone(sysWorkerUser1.getPhone());
            // 信息模板类型
            sysRecords.setSendType("审核结果通知模板");
            // 接收人
            sysRecords.setAcceptBy(sysWorkerUser.getName());
            // 接收人
            sysRecords.setAcceptPhone(sysWorkerUser.getPhone());
            // 接收人openId
            sysRecords.setAcceptOpenId(sysWorkerUser.getOpenId());
            // 创建时间
            sysRecords.setCreateTime(new Date());

            // 如果发送消息错误，记录到访客记录中状态为发送失败
            WxGetPhoneVo wxGetPhoneVo = JSON.parseObject(result, WxGetPhoneVo.class);
            if (wxGetPhoneVo.getErrcode() != 0) {
                // 失败状态
                sysRecords.setSendState("2");
                sysRecords.setErrorMsg(wxGetPhoneVo.getErrmsg());

            } else {
                // 成功状态
                sysRecords.setSendState("1");
                sysRecords.setErrorMsg("发送成功");
            }
            sysRecordsService.add(sysRecords);
//            if (wxGetPhoneVo.getErrcode() != 0) {
//                throw new GlobalException("发送消息错误=>" + wxGetPhoneVo.getErrmsg());
//            }
        } catch (Exception e) {
            throw new GlobalException("审核结果通知=>" + e);
        }
    }

    /**
     * 发送到工人 (每日提醒记功)
     */
    public  void dayReminder()  {
        List<SysWorkerUser> sysWorkerUsers = sysWorkerUserMapper.selectList(null);
        //  取出微信token 如果没有或者过期重新获取
        String wxToken = redisCache.getCacheObject("access_token");
        if (wxToken == null) {
            wxToken = getWxaccess_token("access_token");
        }

        // wxToken = getWxaccess_token(openId);
        String url = WxConstant.WX_TEMPLATE_VISITOR_URL + wxToken;
        WxMessageVo wxMessageVo = new WxMessageVo();

        WxDayReminderVo wxDayReminderVo = new WxDayReminderVo();
        // 语言
        wxMessageVo.setLang("zh_CN");
        // 点击跳转小程序页面
        wxMessageVo.setPage("pages/examine/index");
        // 模板id
        wxMessageVo.setTemplate_id(WxConstant.VISITOR_TEMPLATE_ID);

        // 设置类型
        wxMessageVo.setMiniprogram_state(WxConstant.WX_TEMPLATE_VERSION);
        Thing1Vo thing3Vo = new Thing1Vo();
        thing3Vo.setValue("澜庭浩然打卡提醒");
        wxDayReminderVo.setThing1(thing3Vo);

        Thing1Vo thing1Vo = new Thing1Vo();
        thing1Vo.setValue(String.valueOf(new Date()));
        wxDayReminderVo.setDate3(thing1Vo);

        Thing1Vo thing2Vo = new Thing1Vo();
        thing2Vo.setValue("记得今天在小程序上打卡哦!");
        wxDayReminderVo.setThing2(thing2Vo);

        wxMessageVo.setData(wxDayReminderVo);
        String jsonString = JSON.toJSONString(wxMessageVo);
//        循环所有人获取openid 发送提醒模版
        sysWorkerUsers.forEach(t -> {
            wxMessageVo.setTouser(t.getOpenId());
            //  发送消息
            String result = HttpUtil.doPost(url, jsonString);
            System.out.println("发送结果==>" + result);

            // 如果发送消息错误，记录到访客记录中状态为发送失败
            WxGetPhoneVo wxGetPhoneVo = JSON.parseObject(result, WxGetPhoneVo.class);
            if (wxGetPhoneVo.getErrcode() != 0) {
                // 失败状态

            } else {
                // 成功状态
            }
        });



    }


    /**
     * 定时任务 提醒记功
     */
    public     void timingDayReminder() {
        DailyTaskScheduler scheduler = new DailyTaskScheduler();
        Runnable task = () -> {
            // 这里放置每天18点需要执行的代码
            try {
                System.out.println("aasss");
                dayReminder();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };

        // 启动定时任务，每天18点执行
        scheduler.startDailyTask(task, 11, 55, 0);
    }


    /**
     * 获取小程序月访问量
     *
     * @param begin_date, String end_date
     * @return List<WxMonthDataDto>
     */
    public List<WxMonthDataDto> getMonthData(String begin_date, String end_date) throws Exception {
        WxMonthDataDto wxMonthDataDto1 = new WxMonthDataDto();

        Calendar c = Calendar.getInstance();
        c.add(Calendar.MONTH, -1);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String gtimelast = sdf.format(c.getTime()); //上月
        System.out.println(gtimelast);
        int lastMonthMaxDay = c.getActualMaximum(Calendar.DAY_OF_MONTH);
        System.out.println(lastMonthMaxDay);
        c.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH), lastMonthMaxDay, 23, 59, 59);

        //按格式输出
        String gtime = sdf.format(c.getTime()); //上月最后一天
        System.out.println(gtime);
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMM01");
        String gtime2 = sdf2.format(c.getTime()); //上月第一天
        System.out.println(gtime2);

        // 如果没有传结束日期 获取上月第一天
        if (begin_date == null) {
            wxMonthDataDto1.setBegin_date(gtime2);
        } else {
            wxMonthDataDto1.setBegin_date(begin_date);
        }
        // 如果没有传结束日期 获取上月最后一天
        if (end_date == null) {
            wxMonthDataDto1.setEnd_date(gtime);
        } else {
            wxMonthDataDto1.setEnd_date(end_date);
        }

        String wxToken = redisCache.getCacheObject("access_token");
        if (wxToken == null) {
            wxToken = getAccessToken();
        }

        // 请求微信官方
        String result = HttpUtil.doPost(WxConstant.MONTH_URL + wxToken, JSON.toJSONString(wxMonthDataDto1));

        ExceptionDto exceptionDto = JSON.parseObject(result, ExceptionDto.class);
        if (exceptionDto.getErrcode() != null) {
            throw new Exception("获取小程序月访问量错误=>" + exceptionDto.getErrmsg());
        }
        WxMonthDataDto WxMonthDataDto = JSON.parseObject(result, WxMonthDataDto.class);
        List<WxMonthDataDto> list = WxMonthDataDto.getList();

        System.out.println("获取小程序月访问量" + result);


        return list;
    }

    /**
     * 获取用户访问小程序数据概况 WX_DATA_PROFILE_URL
     */
    public List<WxDataPorfileDto> getWxDataprofile() throws Exception {
        String wxToken = redisCache.getCacheObject("access_token");
        if (wxToken == null) {
            wxToken = getAccessToken();
        }

        // 创建Calendar对象
        Calendar calendar = Calendar.getInstance();

        // 将日期设置为昨天
        calendar.add(Calendar.DATE, -1);

        // 获取昨天的年、月、日
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1; // 月份从0开始，所以要加1
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        WxDataPorfileDto wxDataPorfileVo = new WxDataPorfileDto();
        StringBuffer begin_date = new StringBuffer();
        begin_date.append(year).append(month).append(day);
        wxDataPorfileVo.setBegin_date(String.valueOf(begin_date));
        // 获取当天日期
        wxDataPorfileVo.setEnd_date(String.valueOf(begin_date));
        // 请求微信官方
        String result = HttpUtil.doPost(WxConstant.WX_WEEK_TENDENCY_URL + wxToken, JSON.toJSONString(wxDataPorfileVo));
        ExceptionDto exceptionDto = JSON.parseObject(result, ExceptionDto.class);
        if (exceptionDto.getErrcode() != null) {
            throw new Exception("获取用户访问小程序数据概况=>" + exceptionDto.getErrmsg());
        }
        WxDataPorfileDto wxDataPorfileDto = JSON.parseObject(result, WxDataPorfileDto.class);
        return wxDataPorfileDto.getList();
    }

    /**
     * 获取微信token
     *
     * @return appid=wx48db18326b277d33
     * 小程序秘钥=0f5a570391583a10f2d7344b727be896
     */
    public String getWxaccess_token(String openId) {
        String url = "https://api.weixin.qq.com/cgi-bin/token?";
        url += "grant_type=client_credential";
        url += "&appid=" + WxConstant.APP_ID;
        url += "&secret=" + WxConstant.SECRET;
        String result = HttpUtil.doGet(url);
        String token = JSON.parseObject(result, WX.class).getAccessToken();
        redisCache.setCacheObject("access_token", token, 1, TimeUnit.HOURS);
        return token;
    }


    /**
     * 获取微信token
     *
     * @return appid=wx48db18326b277d33
     * 小程序秘钥=0f5a570391583a10f2d7344b727be896
     */
    public String getAccessToken() {
        String url = "https://api.weixin.qq.com/cgi-bin/token?";
        url += "grant_type=client_credential";
        url += "&appid=" + WxConstant.APP_ID;
        url += "&secret=" + WxConstant.SECRET;
        String result = HttpUtil.doGet(url);
        if (result == null || result.isEmpty()) {
            System.out.println("返回值为空！");
        }
        String token = JSON.parseObject(result, WX.class).getAccessToken();
        redisCache.setCacheObject("access_token", token, JSON.parseObject(result, WX.class).getExpiresIn(), TimeUnit.SECONDS);
        return token;
    }


    /**
     * 解密手机号
     * POST https://api.weixin.qq.com/wxa/business/getuserphonenumber?access_token=ACCESS_TOKEN
     */
    public String getWxPhone(String code) throws Exception {
        String accessoToken = getAccessToken();
        String url = "https://api.weixin.qq.com/wxa/business/getuserphonenumber";
        url += "?access_token=" + accessoToken;


        WxCode wxCode = new WxCode();
        wxCode.setCode(code);
        String jsonString = JSON.toJSONString(wxCode);

        String result = HttpUtil.doPost(url, jsonString);

        if (result == null || result.isEmpty()) {
            System.out.println("返回值为空！");
        }
        WxGetPhoneVo wxGetPhoneVo = JSON.parseObject(result, WxGetPhoneVo.class);

        if (wxGetPhoneVo.getErrcode() != 0) {
            throw new Exception("发送消息错误=>" + wxGetPhoneVo.getErrmsg());
        }
        System.out.println(result);
        return wxGetPhoneVo.getPhone_info().getPhoneNumber();
    }


    /**
     * 获取openid
     *
     * @param code
     * @return
     */

    public String getOpenId(String code) throws Exception {
        //  String url = "https://api.weixin.qq.com/wxa/getpluginopenpid";
        String url = "https://api.weixin.qq.com/sns/jscode2session";
        url += "?access_token=" + getAccessToken();
        url += "&appid=" + WxConstant.APP_ID;
        url += "&secret=" + WxConstant.SECRET;
        url += "&js_code=" + code;
        String s = HttpUtil.doGet(url);
        if (s.equals("null")) {
            throw new Exception("未知错误" + s);
        }
        System.out.println(s);
        return JSON.parseObject(s, WxUser.class).getOpenId();
    }

    /**
     * 该接口用于获取用户访问小程序数据 周趋势
     *
     * @params begin_date end_date
     */
    public WXWeekDataDto getWxWeekTendency() {
        WxDataPorfileDto wxDataPorfileVo = new WxDataPorfileDto();
        String wxToken = redisCache.getCacheObject("access_token");
        if (wxToken == null) {
            wxToken = getAccessToken();
        }
        // 创建 Calendar 对象并设置为当前日期
        Calendar calendar = Calendar.getInstance();

        // 将日期调整到本周第一天（星期一）
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        if (dayOfWeek != Calendar.MONDAY) {
            calendar.add(Calendar.DATE, -1 * (dayOfWeek - Calendar.MONDAY));
        }

        // 上一周
        LocalDate d1 = LocalDate.now().minusWeeks(1).with(TemporalAdjusters.previous(DayOfWeek.SUNDAY)).plusDays(1L);
        LocalDate d2 = LocalDate.now().minusWeeks(1).with(TemporalAdjusters.next(DayOfWeek.MONDAY)).minusDays(1L);
        System.out.println("上一周开始日期: " + d1);
        System.out.println("上一周结束日期: " + d2);


        // 计算上周的起始日期和结束日期
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        String startDate = dateFormat.format(calendar.getTime());
        calendar.add(Calendar.DATE, 6);
        String endDate = dateFormat.format(calendar.getTime());
        wxDataPorfileVo.setBegin_date(startDate);
        // 获取最后一天
        calendar.add(Calendar.DATE, 6);
        // 获取当天日期
        wxDataPorfileVo.setEnd_date(endDate);
        // 请求微信官方
        String result = HttpUtil.doPost(WxConstant.WX_WEEK_TENDENCY_URL + wxToken, JSON.toJSONString(wxDataPorfileVo));
        ExceptionDto exceptionDto = JSON.parseObject(result, ExceptionDto.class);
        if (exceptionDto.getErrcode() != null) {
            throw new GlobalException("获取用户访问小程序数据 周趋势=>" + exceptionDto.getErrmsg());
        }
        WXWeekDataDto wxWeekDataDto = JSON.parseObject(result, WXWeekDataDto.class);
        return wxWeekDataDto;
    }

    /**
     * 该接口用于获取用户访问小程序数据 日趋势
     *
     * @params begin_date end_date
     */
    public void getWxDayTendency() {
//    ?access_token=ACCESS_TOKEN


        String url = "https://api.weixin.qq.com/datacube/getweanalysisappiddailyvisittrend";
//        ?access_token=
    }

    /**
     * 获取用户访问小程序数据 月趋势
     *
     * @params begin_date end_date
     */
    public void getWxMouthTendency() {
//    ?access_token=ACCESS_TOKEN


        String url = "https://api.weixin.qq.com/datacube/getweanalysisappidmonthlyvisittrend";
//        ?access_token=
    }


}
