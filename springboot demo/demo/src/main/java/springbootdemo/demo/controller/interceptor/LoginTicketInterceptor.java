package springbootdemo.demo.controller.interceptor;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import springbootdemo.demo.model.LoginTicket;
import springbootdemo.demo.model.User;
import springbootdemo.demo.service.UserService;
import springbootdemo.demo.util.CookieUtil;
import springbootdemo.demo.util.hostHolder;

@Component
public class LoginTicketInterceptor implements HandlerInterceptor
{
        
    @Autowired
    private UserService userService;

    @Autowired
     private hostHolder hostHolder;

     @Override
     public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
         // 从Cookie中获取凭证
         String ticket = CookieUtil.getValue(request, "ticket");
         if (ticket != null) {
             // 查询凭证
             LoginTicket loginTicket = userService.findLoginTicket(ticket);
             // 检查凭证是否有效
             if (loginTicket != null && loginTicket.getStatus() == 0 && loginTicket.getExpired().after(new Date())) {
                 // 根据凭证查用户
                 User user = userService.findUserById(loginTicket.getUserId());
                 // 在本次请求中持有用户，多线程，利用ThreadLocal
                 hostHolder.setUser(user);
             }
         }
         return true;
     } 

     @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        User user = hostHolder.getUser();
        if (user != null && modelAndView != null) {
            modelAndView.addObject("loginUser", user);
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        hostHolder.clear();
        SecurityContextHolder.clearContext();
    }
    
     
}