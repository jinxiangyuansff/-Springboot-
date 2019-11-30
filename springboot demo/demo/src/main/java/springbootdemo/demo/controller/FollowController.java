package springbootdemo.demo.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import springbootdemo.demo.event.EventProducer;
import springbootdemo.demo.model.Event;
import springbootdemo.demo.model.Page;
import springbootdemo.demo.model.User;
import springbootdemo.demo.service.FollowService;
import springbootdemo.demo.service.UserService;
import springbootdemo.demo.util.CommunityConstant;
import springbootdemo.demo.util.CommunityUtil;
import springbootdemo.demo.util.hostHolder;

@Controller
public class FollowController implements CommunityConstant
{
  @Autowired
  private FollowService followService;

  @Autowired
  private hostHolder holder;

  @Autowired
  private UserService userService;

  @Autowired
  private EventProducer eventProducer;

  @RequestMapping(path = "/follow", method = RequestMethod.POST)
  @ResponseBody
  public String follow(int entityType, int entityId) {
      User user = holder.getUser();
      followService.follow(user.getId(), entityType, entityId);

      // 触发关注事件
      Event event = new Event()
              .setTopic(TOPIC_FOLLOW)
              .setUserId(holder.getUser().getId())
              .setEntityType(entityType)
              .setEntityId(entityId)
              .setEntityUserId(entityId);
      eventProducer.fireEvent(event);

      return CommunityUtil.getJSONString(0, "已关注!");
  }

  @RequestMapping(path = "/unfollow", method = RequestMethod.POST)
    @ResponseBody
    public String unfollow(int entityType, int entityId) {
        User user = holder.getUser();
        followService.unfollow(user.getId(), entityType, entityId);
        return CommunityUtil.getJSONString(0, "已取消关注!");
    }

    @RequestMapping(path = "/followees/{userId}", method = RequestMethod.GET)
    public String getFollowees(@PathVariable("userId") int userId, Page page, Model model) {
        User user = userService.findUserById(userId);
        if (user == null) {
            throw new RuntimeException("该用户不存在!");
        }
        model.addAttribute("user", user);
        page.setLimit(5);
        page.setPath("/followees/" + userId);
        page.setRows((int) followService.findFolloweeCount(userId, ENTITY_TYPE_USER));
        List<Map<String, Object>> userList = followService.findFollowees(userId, page.getOffset(), page.getLimit());
        if (userList != null) {
            for (Map<String, Object> map : userList) {
                User u = (User) map.get("user");
                map.put("hasFollowed", hasFollowed(u.getId()));
            }
        }
        model.addAttribute("users", userList);
        return "/site/followee";
    }

    @RequestMapping(path = "/followers/{userId}", method = RequestMethod.GET)
    public String getFollowers(@PathVariable("userId") int userId, Page page, Model model) {
        User user = userService.findUserById(userId);
        if (user == null) {
            throw new RuntimeException("该用户不存在!");
        }
        model.addAttribute("user", user);
        page.setLimit(5);
        page.setPath("/followers/" + userId);
        page.setRows((int) followService.findFollowerCount(ENTITY_TYPE_USER, userId));
        List<Map<String, Object>> userList = followService.findFollowers(userId, page.getOffset(), page.getLimit());
        if (userList != null) {
            for (Map<String, Object> map : userList) {
                User u = (User) map.get("user");
                map.put("hasFollowed", hasFollowed(u.getId()));
            }
        }
        model.addAttribute("users", userList);
        return "/site/follower";
    }
    
  
  



    







    private boolean hasFollowed(int userId) {
      if (holder.getUser() == null) {
          return false;
      }
      return followService.hasFollowed(holder.getUser().getId(), ENTITY_TYPE_USER, userId);
  }
}
