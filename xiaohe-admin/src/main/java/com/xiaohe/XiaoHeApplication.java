package com.xiaohe;

import com.xiaohe.web.utils.WxUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.text.SimpleDateFormat;
import java.util.TimeZone;



/**
 * 启动程序
 * 
 * @author xiaohe
 */
//@ComponentScan("com.xiaohe")
@EnableScheduling
@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class })
public class XiaoHeApplication
{

    public static void main(String[] args)
    {
        SimpleDateFormat dateFormat= new SimpleDateFormat("HH:mm:ss MM月dd日 ");
        dateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));



        // System.setProperty("spring.devtools.restart.enabled", "false");
        SpringApplication.run(XiaoHeApplication.class, args);
        WxUtils wxUtils = new WxUtils();
        wxUtils.timingDayReminder();
        System.out.println("(◕‿◕)♡  SpringBoot GuitarXiaohe——setve 优雅启动 ♡\n" +
                "        ♪♫♬ ～代码的旋律开始演奏～ ♬♫♪\n" +
                "        .---------.     .---------.\n" +
                "        /   O   O  \\\\   /    ●    \\\\\n" +
                "        |           |   |          |\n" +
                "        \\\\    ▽    /     \\\\   ▽   /\n" +
                "        '--------'       '------'\n" +
                "服务已就绪，静待君临 ✨");
    }
}
