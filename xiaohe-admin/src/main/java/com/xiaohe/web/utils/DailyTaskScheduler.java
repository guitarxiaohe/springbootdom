package com.xiaohe.web.utils;

import java.util.Calendar;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author : GuitarXiaohe
 * @version V1.0
 * @Project: lthr-serve
 * @Package com.xiaohe.web.utils
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @date Date : 2024年03月26日 10:53
 */


public class DailyTaskScheduler {
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public void startDailyTask(Runnable task, int hourOfDay, int minute, int second) {
        // 获取当前时间
        Calendar now = Calendar.getInstance();
        Calendar dailyRunTime = Calendar.getInstance();

        // 设置每天执行的具体时间
        dailyRunTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
        dailyRunTime.set(Calendar.MINUTE, minute);
        dailyRunTime.set(Calendar.SECOND, second);

        // 如果当前时间已经过了每天的运行时间，则设置为明天执行
        if (now.after(dailyRunTime)) {
            dailyRunTime.add(Calendar.DATE, 1);
        }

        long initialDelay = dailyRunTime.getTimeInMillis() - now.getTimeInMillis();
        long dayInMs = TimeUnit.DAYS.toMillis(1);

        // 以固定延迟时间重复执行任务
        scheduler.scheduleAtFixedRate(task, initialDelay, dayInMs, TimeUnit.MILLISECONDS);
    }


}