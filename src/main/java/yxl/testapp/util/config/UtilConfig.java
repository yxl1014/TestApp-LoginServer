package yxl.testapp.util.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import yxl.testapp.util.ProtocolUtil;

/**
 * @author yxl
 * @date: 2022/9/12 下午2:05
 * 工具bean载入配置文件
 */

@Configuration
public class UtilConfig {

    @Bean
    public ProtocolUtil getProtocolUtil() {
        return new ProtocolUtil();
    }
}
