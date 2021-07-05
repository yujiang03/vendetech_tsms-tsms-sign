package com.vendetech;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * 启动程序
 * 
 * @author vendetech
 */
@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class })
public class VendetechApplication
{
    public static void main(String[] args)
    {
        // System.setProperty("spring.devtools.restart.enabled", "false");
        SpringApplication.run(VendetechApplication.class, args);
        System.out.println("(♥◠‿◠)ﾉﾞ  └(^o^)┘ ☆☆☆ 死狗伊 ！！！项目居然启动成功 ☆☆☆ (^_^)∠※   ლ(´ڡ`ლ)ﾞ  \n" +
                " ☆☆☆ ☆☆☆  ☆☆☆  ( ^_^)ﾉﾞ   ﾞ \\(^_^ ) ☆☆☆  ☆☆☆  ☆☆☆  \n" +
                " ☆☆☆ ☆☆☆  ☆☆☆  ( ^_^)ﾉﾞ   ﾞ \\(^_^ ) ☆☆☆  ☆☆☆  ☆☆☆  \n" +
                " ☆☆☆ ☆☆☆  ☆☆☆  ( ^_^)ﾉﾞ   ﾞ \\(^_^ ) ☆☆☆  ☆☆☆  ☆☆☆  \n" +
                " ☆☆☆ ☆☆☆  ☆☆☆  ( ^_^)ﾉﾞ   ﾞ \\(^_^ ) ☆☆☆  ☆☆☆  ☆☆☆  \n" +
                " ☆☆☆ ☆☆☆  ☆☆☆  ( ^_^)ﾉﾞ   ﾞ \\(^_^ ) ☆☆☆  ☆☆☆  ☆☆☆  \n");
    }
}
