package yxl.testapp.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yxl
 * @date: 2022/9/13 下午9:34
 */
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    @Autowired
    private CheckHandle checkHandle;


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        List<String> list=new ArrayList<>();
        list.add("/user/**");
        registry.addInterceptor(checkHandle).addPathPatterns(list);
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        //匹配所有的路径
        registry.addMapping("/**")
                .allowCredentials(true)
                .allowedHeaders("*")
                .allowedMethods("GET","POST","PUT","DELETE")
                .allowedOriginPatterns("*");
    }
}
