package com.xiaohe.web.controller.ws;

import com.xiaohe.web.utils.SignUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping("/wx")
public class WxConfigContrller {


    /**
     * 小程序id
     */
    private String appId = "wx48db18326b277d33";
    /**
     * 小程序秘钥
     */
    private String secret = "0f5a570391583a10f2d7344b727be896";

    /** 推送服务token： familySchool
     * 推送服务秘钥：	hG4jyv1FG3wCmUA50pWxjxCgxKdLMUisg65OtG3iw9B
     */

        @GetMapping("/auth")
        public void authPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

            request.getParameter("signature");
            if (1 > 0) {
                String signature = request.getParameter("signature");
                String timestamp = request.getParameter("timestamp");
                String nonce = request.getParameter("nonce");
                String echostr = request.getParameter("echostr");
                SignUtil.checkSignature(signature, timestamp, nonce);
                response.getOutputStream().println(echostr);
            }
        }

//    /**
//     * 获取手机号
//     * @param data
//     * @return
//     */
//    @PostMapping("/getPhoneNumber")
//    public Object getPhoneNumber(@RequestBody Map<String,Object> data)
//    {
//        //通过appid和secret来获取token
//        //WXContent.APPID是自定义的全局变量
//        String tokenUrl = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential";
//        tokenUrl+="&appid="+appId;
//        tokenUrl+="&secret=%s="+secret;
//        JSONObject token = JSON.parseObject(HttpUtil.doGet(tokenUrl));
//
//        //通过token和code来获取用户手机号
//        String url = "https://api.weixin.qq.com/wxa/business/getuserphonenumber?access_token=" + token.getString("access_token");
//
//        //封装请求体
//        Map<String, String> paramMap = new HashMap<>();
//        paramMap.put("code", data.get("code").toString());
//
//        //封装请求头
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//
//        HttpEntity<Map<String, String>> httpEntity = new HttpEntity<>(paramMap,headers);
//
//        //通过RestTemplate发送请求，获取到用户手机号码
//        RestTemplate restTemplate = new RestTemplate();
//        ResponseEntity<Object> response = restTemplate.postForEntity(url, httpEntity, Object.class);
//
//        //返回到前端展示
//        return response.getBody();
//    }


}

