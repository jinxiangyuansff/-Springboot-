package springbootdemo.demo.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthoritiesContainer;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import springbootdemo.demo.dao.LoginTicketMapper;
import springbootdemo.demo.dao.UserMapper;
import springbootdemo.demo.model.LoginTicket;
import springbootdemo.demo.model.User;
import springbootdemo.demo.util.CommunityConstant;
import springbootdemo.demo.util.CommunityUtil;
import springbootdemo.demo.util.MailClient;
import springbootdemo.demo.util.RedisKeyUtil;

@Service 
public class UserService implements CommunityConstant
{
   @Autowired
   private UserMapper userMapper ;
   
   @Autowired
    private MailClient mailClient;
  
    //用redis改进
   // @Autowired
   // private LoginTicketMapper loginTicketMapper;

   @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private TemplateEngine templateEngine;

    @Value("${demo.path.domain}")
    private String domain;

    @Value("${server.servlet.context-path")
    private String contextPath;

 public User findUserById(int id)
   {
       return userMapper.selectById(id) ;
   }

   public Map<String, Object> register(User user) {
    Map<String, Object> map = new HashMap<>();

    // 空值判断处理
    if (user == null) {
        throw new IllegalArgumentException("参数不能为空");
    }
    if (StringUtils.isBlank(user.getUsername())) {
        map.put("usernameMsg","账号不能为空");
        return map;
    }
    if (StringUtils.isBlank(user.getPassword())) {
        map.put("passwordMsg","密码不能为空");
        return map;
    }
    if (StringUtils.isBlank(user.getEmail())) {
        map.put("emailMsg","邮箱不能为空");
        return map;
    }

    // 验证账号
    User u = userMapper.selectByName(user.getUsername());
    if (u != null) {
        map.put("usernameMsg","该账号已存在");
        return map;
    }

    // 验证邮箱
    u = userMapper.selectByEmail(user.getEmail());
    if (u != null) {
        map.put("emailMsg","该邮箱已经存在");
        return map;
    }

    // 注册用户
    user.setSalt(CommunityUtil.generateUUID().substring(0, 5));
    user.setPassword(CommunityUtil.md5(user.getPassword()+user.getSalt()));
    user.setType(0);
    user.setStatus(0);
    user.setActivationCode(CommunityUtil.generateUUID());
    user.setHeaderUrl(String.format("http://image.nowcoder.com/head/%dt.png", new Random().nextInt(1000)));
    user.setCreateTime(new Date());
    userMapper.insertUser(user);

    // 激活邮件
    Context context = new Context();
    context.setVariable("email", user.getEmail());
    // http://localhost:8080/community/activation/101/code
    String url = domain + contextPath + "/activation/" + user.getId() + "/" + user.getActivationCode();
    context.setVariable("url",url);
    String content = templateEngine.process("/mail/activation",context);
    mailClient.sendMail(user.getEmail(), "激活账号", content);

    return map;
}

  
   public int avtivation(int userId, String code) {
    User user = userMapper.selectById(userId);
    if (user.getStatus() == 1) {
        return ACTIVATION_REPATE;
    } else if (user.getActivationCode().equals(code)) {
        userMapper.updateStatus(userId,1);
        return ACTIVATION_SUCCESS;
    }
    else {
        return ACTIVATION_FAILURE;
    }
 }

 
 public Map<String,Object> login(String username, String password, long expiredSeconds)
 {
    Map<String, Object> map = new HashMap<>();
    if(StringUtils.isBlank(username)) {
        map.put("usernameMsg", "账号不能为空");
    }
    if(StringUtils.isBlank(password)) {
        map.put("passwordMsg", "密码不能为空");
    }

    // 验证账号
    User user = userMapper.selectByName(username);
    if (user == null) {
        map.put("usernameMsg", "该账号不存在");
        return map;
    }
    // 验证状态
    if(user.getStatus() == 0) {
        map.put("usernameMsg", "该账号未激活");
        return map;
    }

    // 验证密码
    password = CommunityUtil.md5(password + user.getSalt());
    if (!user.getPassword().equals(password)) {
        map.put("passwordMsg", "密码不正确");
        return map;
    }

    // 生成登录凭证
    LoginTicket loginTicket = new LoginTicket();
    loginTicket.setUserId(user.getId());
    loginTicket.setTicket(CommunityUtil.generateUUID());
    loginTicket.setStatus(0);
    loginTicket.setExpired(new Date(System.currentTimeMillis() + expiredSeconds * 1000));
    //loginTicketMapper.insertLoginTicket(loginTicket);
    String redisKey = RedisKeyUtil.getTicketKey(loginTicket.getTicket());
        redisTemplate.opsForValue().set(redisKey, loginTicket);

    map.put("ticket", loginTicket.getTicket());
    return map;
   }



   public void logout(String ticket)
    {
        //loginTicketMapper.updateStatus(ticket, 1);
        String redisKey = RedisKeyUtil.getTicketKey(ticket);
        LoginTicket loginTicket = (LoginTicket) redisTemplate.opsForValue().get(redisKey);
        loginTicket.setStatus(1);
        redisTemplate.opsForValue().set(redisKey, loginTicket);

    }

    public LoginTicket findLoginTicket(String ticket) {
            // return loginTicketMapper.selectByTicket(ticket);
            String redisKey = RedisKeyUtil.getTicketKey(ticket);
            return (LoginTicket) redisTemplate.opsForValue().get(redisKey);
            }

    public int updateHeader(int userId, String headerUrl) {
                return userMapper.updateHeader(userId, headerUrl);
            }

            public User findUserByName(String name) {
                return userMapper.selectByName(name);
            }


            // 1.优先从缓存中取值
    private User getCache(int userId) {
        String redisKey = RedisKeyUtil.getUserKey(userId);
        return (User) redisTemplate.opsForValue().get(redisKey);
    }

    // 2.取不到时初始化缓存数据
    private User initCache(int userId) {
//        System.out.println(111);
        User user = userMapper.selectById(userId);
        String redisKey = RedisKeyUtil.getUserKey(userId);
        redisTemplate.opsForValue().set(redisKey, user, 3600, TimeUnit.SECONDS);
        return user;
    }

    // 3.数据变更时清除缓存数据
    private void clearCache(int userId) {
        String redisKey = RedisKeyUtil.getUserKey(userId);
        redisTemplate.delete(redisKey);
    }

    

    // 获得权限
    public Collection<? extends GrantedAuthority> getAuthorities(int userId) {
        User user = this.findUserById(userId);

        List<GrantedAuthority> list = new ArrayList<>();
        list.add(new GrantedAuthority() {

            @Override
            public String getAuthority() {
                switch (user.getType()) {
                    case 1:
                        return AUTHORITY_ADMIN;
                    case 2:
                        return AUTHORITY_MODERATOR;
                    default:
                        return AUTHORITY_USER;
                }
            }
        });
        return list;
    }
            
            

            
 }





  
