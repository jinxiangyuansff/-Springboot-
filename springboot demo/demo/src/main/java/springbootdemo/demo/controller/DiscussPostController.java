package springbootdemo.demo.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import springbootdemo.demo.model.Comment;
import springbootdemo.demo.model.DiscussPost;
import springbootdemo.demo.model.Page;
import springbootdemo.demo.model.User;
import springbootdemo.demo.service.CommentService;
import springbootdemo.demo.service.DiscussPostService;
import springbootdemo.demo.service.UserService;
import springbootdemo.demo.util.CommunityConstant;
import springbootdemo.demo.util.CommunityUtil;
import springbootdemo.demo.util.hostHolder;

@Controller
@RequestMapping("/discuss")
public class DiscussPostController  implements CommunityConstant
{
    @Autowired
    private DiscussPostService discussPostService;

    @Autowired
    private hostHolder holder;

    @Autowired
    private UserService userService ;

    @Autowired
     private CommentService commentService;

    @RequestMapping(path = "/add", method = RequestMethod.POST)
    @ResponseBody
    public String addDiscussPost(String title, String content) {
        User user = holder.getUser();
        if (user == null) {
            return CommunityUtil.getJSONString(403, "您还没有登录");
        }
        DiscussPost discussPost = new DiscussPost();
        discussPost.setUserId(user.getId());
        discussPost.setTitle(title);
        discussPost.setContent(content);
        discussPost.setCreateTime(new Date());
        discussPostService.addDiscussPost(discussPost);
                   
        // 报错的情况以后统一处理
        return CommunityUtil.getJSONString(0, "发布成功！");
    }

    /*
    帖子详情
    */
    @RequestMapping(path = "/detail/{discussPostId}", method = RequestMethod.GET)
    public String getDiscussPost(@PathVariable("discussPostId") int discussPostId, Model model,Page page) {

        DiscussPost discussPost = discussPostService.findDiscussPostById(discussPostId);
        model.addAttribute("post", discussPost);
        //查帖子作者
        User user = userService.findUserById(discussPost.getUserId());
        model.addAttribute("user", user);

        // 查评论分页信息
        page.setLimit(5);
        page.setPath("/discuss/detail/" + discussPostId);
        page.setRows(discussPost.getCommentCount());

        // 评论：给帖子的评论
        // 回复：给评论的评论

        // 评论列表
        List<Comment> commentList = commentService.findCommentsByEntity(
            1, discussPost.getId(), page.getOffset(), page.getLimit());

        // 评论VO列表
        List<Map<String, Object>> commentVoList = new ArrayList<>();
        if (commentList != null) {
            for (Comment comment : commentList) {
                // 一个评论的VO
                Map<String, Object> commentVo = new HashMap<>();
                // 评论
                commentVo.put("comment", comment);
                // 作者
                commentVo.put("user", userService.findUserById(comment.getUserId()));
                // 回复列表
                List<Comment> replyList = commentService.findCommentsByEntity(
                        ENTITY_TYPE_COMMENT, comment.getId(), 0, Integer.MAX_VALUE);
                // 回复VO列表
                List<Map<String, Object>> replyVoList = new ArrayList<>();
                if (replyList != null) {
                    for (Comment reply : replyList) {
                        Map<String, Object> replyVo = new HashMap<>();
                        replyVo.put("reply", reply);
                        replyVo.put("user", userService.findUserById(reply.getUserId()));
                        // 回复的目标
                        User target = reply.getTargetId() == 0 ? null : userService.findUserById(reply.getTargetId());
                        replyVo.put("target", target);
                        replyVoList.add(replyVo);
                    }
                }

                commentVo.put("replys", replyVoList);
                // 回复数量
                int replyCount = commentService.findCommentCount(ENTITY_TYPE_COMMENT, comment.getId());
                commentVo.put("replyCount", replyCount);

                commentVoList.add(commentVo);
            }
        }

        model.addAttribute("comments", commentVoList);
        return "/site/discuss-detail";
    }
}