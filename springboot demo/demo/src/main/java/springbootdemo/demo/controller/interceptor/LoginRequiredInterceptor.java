package springbootdemo.demo.controller.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import java.lang.reflect.Method;
import springbootdemo.demo.annotation.LoginRequired;
import springbootdemo.demo.util.hostHolder;

@Component
public class LoginRequiredInterceptor implements HandlerInterceptor
{
     @Autowired
     hostHolder holder;
     // 在请求最初判断是否是登录状态
     @Override
     public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception
     {
           // 首先我们只拦截方法，其他的我们不管
        if (handler instanceof HandlerMethod) {
            // 进行类型转换
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            // 拦截到的Method的对象
            Method method = handlerMethod.getMethod();
            // 取到对象的注解
            LoginRequired loginRequired = method.getAnnotation(LoginRequired.class);
            if (loginRequired != null && holder.getUser() == null) {
                response.sendRedirect(request.getContextPath() + "/login");
                return false;
            }
        }
        return true;
    }
  }
     








