package springbootdemo.demo.util;

import org.springframework.stereotype.Component;

import springbootdemo.demo.model.User;

/*
 代替session，作为一个容器
*/
@Component
public class hostHolder
{
    private ThreadLocal<User> users = new ThreadLocal<>();

    public void setUser(User user) {
        users.set(user);
    }
      
    public User getUser() {
        return users.get();
    }

    public void clear() {
        users.remove();
    }














}