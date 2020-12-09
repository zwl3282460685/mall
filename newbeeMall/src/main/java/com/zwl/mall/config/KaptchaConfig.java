package com.zwl.mall.config;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.Properties;

//验证码配置类
@Component
public class KaptchaConfig {
    @Bean
    public DefaultKaptcha getDefaultKaptcha(){
        DefaultKaptcha defaultKaptcha = new DefaultKaptcha();
        Properties properties = new Properties();
        properties.put("kaptcha.border", "no"); //图片边框
        properties.put("kaptcha.textproducer.font.color", "black"); //字体颜色
        properties.put("kaptcha.image.width", "150");  // 图片宽
        properties.put("kaptcha.image.height", "40");  // 图片高
        properties.put("kaptcha.textproducer.font.size", "30"); // 字体大小
        properties.put("kaptcha.session.key", "verifyCode"); //
        properties.put("kaptcha.textproducer.char.space", "4"); // 验证码长度

        Config config = new Config(properties);
        defaultKaptcha.setConfig(config);
        return defaultKaptcha;
    }
}