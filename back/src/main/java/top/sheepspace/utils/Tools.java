package top.sheepspace.utils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.time.format.DateTimeFormatter;

/**
 * Description:
 *
 * @Project: MicroTodo
 * @Package: top.sheepspace.utils
 * @Author: Yang Xiong Email:2829727259@qq.com
 * @Version: jdk1.8.0_131
 * @Time 2024-05-06 - 21:49
 */
public class Tools {
    public static Date getNowDateTime() {
        // 获取当前时间
        LocalDateTime now = LocalDateTime.now();
        // 转换为Date类型
        return Date.from(now.atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * 获取当前时间的字符串表示
     */
    public static String getNowDateTimeString() {
        // 获取当前时间
        LocalDateTime now = LocalDateTime.now();
        // 定义MySQL的datetime格式
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        // 格式化当前时间为MySQL的datetime格式
        return now.format(formatter);
    }
}
