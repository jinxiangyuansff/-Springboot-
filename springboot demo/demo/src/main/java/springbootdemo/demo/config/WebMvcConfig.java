package springbootdemo.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springbootdemo.demo.controller.interceptor.AlphaInterceptor;
import springbootdemo.demo.controller.interceptor.LoginRequiredInterceptor;
import springbootdemo.demo.controller.interceptor.LoginTicketInterceptor;



@Configuration
public class WebMvcConfig implements WebMvcConfigurer
{
   @Autowired
   private AlphaInterceptor alphaInterceptor;

 	// .excludePathPatterns 表示哪些路径不被拦截比方说一些静态资源，
    // .addPathPatterns 表示确定的拦截的路径

    @Autowired
    private LoginTicketInterceptor loginTicketInterceptor;

    @Autowired
    private LoginRequiredInterceptor loginRequiredInterceptor;
       
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(alphaInterceptor)
                .excludePathPatterns("/**/*.css", "/**/*.js", "/**/*.png", "/**/*.jpg", "/**/*.jpeg")
                .addPathPatterns("/register", "/login");

        registry.addInterceptor(loginTicketInterceptor)
        .excludePathPatterns("/**/*.css", "/**/*.js", "/**/*.png", "/**/*.jpg", "/**/*.jpeg");     
        
        registry.addInterceptor(loginRequiredInterceptor)
        .excludePathPatterns("/**/*.css", "/**/*.js", "/**/*.png", "/**/*.jpg", "/**/*.jpeg"); 

    }
  
}