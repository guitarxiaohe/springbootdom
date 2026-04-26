package com.xiaohe.web.utils;

import com.xiaohe.web.domain.WX;
import org.mybatis.logging.Logger;
import org.mybatis.logging.LoggerFactory;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

public class SignUtil {
    public static WX accessToken = null;
    private final static Logger logger = LoggerFactory.getLogger(SignUtil.class);
    /**
     * 这里是自定义 Token，需和你在公众号后台提交的 Token 保持一致
     */
    private static final String TOKEN = "familySchool";

    /**
     * 校验签名
     *
     * @param signature 签名
     * @param timestamp 时间戳
     * @param nonce     随机数
     * @return 布尔值
     */
    public static boolean checkSignature(String signature, String timestamp, String nonce) {
        String checktext = null;
        if (null != signature) {
            // 对ToKen,timestamp,nonce 按字典排序
            String[] paramArr = new String[]{TOKEN, timestamp, nonce};
            Arrays.sort(paramArr);
            // 将排序后的结果拼成一个字符串
            String content = paramArr[0].concat(paramArr[1]).concat(paramArr[2]);

            try {
                MessageDigest md = MessageDigest.getInstance("SHA-1");
                // 对接后的字符串进行sha1加密
                byte[] digest = md.digest(content.getBytes());
                checktext = byteToStr(digest);
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }
        // 将加密后的字符串与signature进行对比
        return checktext != null && checktext.equals(signature.toUpperCase());
    }

    /**
     * 将字节数组转化为16进制字符串
     *
     * @param byteArrays 字符数组
     * @return 字符串
     */
    private static String byteToStr(byte[] byteArrays) {
        String str = "";
        for (int i = 0; i < byteArrays.length; i++) {
            str += byteToHexStr(byteArrays[i]);
        }
        return str;
    }

    /**
     * 将字节转化为十六进制字符串
     *
     * @param myByte 字节
     * @return 字符串
     */
    private static String byteToHexStr(byte myByte) {
        char[] Digit = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        char[] tampArr = new char[2];
        tampArr[0] = Digit[(myByte >>> 4) & 0X0F];
        tampArr[1] = Digit[myByte & 0X0F];
        String str = new String(tampArr);
        return str;
    }


    /**
     * 获取接口访问凭证
     * 刷新 access_token 110 分钟刷新一次,服务器启动的时候刷新一次（access_token 有效期是120分钟，我设置的是每 110 分钟刷新一次）
     *
     * @return 认证信息
     */
//    @Scheduled(initialDelay = 1000, fixedDelay = 60 * 1000 * 110)
//    private static WX getAccessToken() {
//        String requestUrl = WxConstant.TOKEN_URL.replace("APPID", WxConstant.GZH_APP_ID)
//                .replace("SECRET", WxConstant.GZH_SECRET);
//        // 发起GET请求获取凭证
//        JSONObject jsonObject = JSONObject.parseObject(HttpUtil.doGet(requestUrl, "GET", null));
//        if (null != jsonObject) {
//            try {
//                TokenUtil.accessToken = new WX();
//                TokenUtil.accessToken.setAccessToken(jsonObject.getString("access_token"));
//                TokenUtil.accessToken.setExpiresIn(jsonObject.getIntValue("expires_in"));
//                logger.info("AccessToken>>>>>>>>>>>>>" + jsonObject.toString());
//            } catch (Exception e) {
//                TokenUtil.accessToken = null;
//                // 获取token失败
//                logger.error(e.getMessage());
//            }
//        }
//        return TokenUtil.accessToken;
//    }
    /**
     * 获取一个月有多少天
     *
     * @param month
     * @return
     */
    public static int countDaysInMonth(int year, int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        // 注意：月份需要减1
        calendar.set(Calendar.MONTH, month - 1);
        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    /**
     * 获取上月最后一天
     * @return
     */
    public static String getLastDayOfPreviousMonth() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -1);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        Date lastDayOfPreviousMonth = calendar.getTime();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        return dateFormat.format(lastDayOfPreviousMonth);
    }
    public static void main(String[] args) {

    }

}
