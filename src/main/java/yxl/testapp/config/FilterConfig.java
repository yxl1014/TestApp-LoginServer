package yxl.testapp.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import yxl.testapp.filter.DoFilter;

/**
 * @author yxl
 * @date: 2022/8/30 下午12:34
 */

@Configuration
public class FilterConfig {
    @Bean
    public FilterRegistrationBean myFilterRegistrationBean() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean(getDoFilter());
        //添加过滤路径
        filterRegistrationBean.addUrlPatterns("/user/*");
        return filterRegistrationBean;
    }

    @Bean
    public DoFilter getDoFilter(){
        return new DoFilter();
    }
}
