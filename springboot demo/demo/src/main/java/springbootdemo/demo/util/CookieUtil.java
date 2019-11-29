package springbootdemo.demo.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

public class CookieUtil
{
    public static String getValue(HttpServletRequest httpServletRequest, String name) {
        if (httpServletRequest == null || name == null) {
            throw new IllegalArgumentException("参数为空！");
        }

        Cookie[] cookies = httpServletRequest.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(name)) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}