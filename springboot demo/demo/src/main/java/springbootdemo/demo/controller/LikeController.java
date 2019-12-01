package springbootdemo.demo.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import springbootdemo.demo.event.EventProducer;
import springbootdemo.demo.model.Event;
import springbootdemo.demo.model.User;
import springbootdemo.demo.service.LikeService;
import springbootdemo.demo.util.CommunityConstant;
import springbootdemo.demo.util.CommunityUtil;
import springbootdemo.demo.util.hostHolder;



@Controller
public class LikeController implements CommunityConstant
{
    @Autowired
    private LikeService likeService;

    @Autowired
    private hostHolder holder;

    @Autowired
    private EventProducer eventProducer;

    @RequestMapping(path = "/like", method = RequestMethod.POST)
    @ResponseBody
    public String like(int entityType, int entityId, int entityUserId, int postId) {
        User user = holder.getUser();
        // 点赞
        likeService.like(user.getId(), entityType, entityId, entityUserId);
        // 获取数量
        long likeCount = likeService.findEntityLikeCount(entityType, entityId);
        // 获取状态
        int likeStatus = likeService.findEntityLikeStatus(user.getId(), entityType, entityId);

        Map<String, Object> map = new HashMap<>();
        map.put("likeCount", likeCount);
        map.put("likeStatus", likeStatus);

        // 触发点赞事件
        if (likeStatus == 1) {
            Event event = new Event()
            .setTopic(TOPIC_LIKE)
            .setUserId(holder.getUser().getId())
            .setEntityType(entityType)
            .setEntityId(entityId)
            .setEntityUserId(entityUserId)
            .setData("postId", postId);
               
            eventProducer.fireEvent(event);
        }

        return CommunityUtil.getJSONString(0, null, map);
    }


}