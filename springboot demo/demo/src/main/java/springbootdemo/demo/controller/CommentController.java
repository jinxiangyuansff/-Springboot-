package springbootdemo.demo.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import springbootdemo.demo.model.Comment;
import springbootdemo.demo.service.CommentService;
//import springbootdemo.demo.service.DiscussPostService;
import springbootdemo.demo.util.hostHolder;

@Controller
@RequestMapping("/comment")
public class CommentController
{
    @Autowired
    private CommentService commentService;

    @Autowired
    private hostHolder holder;

  //  @Autowired
  //  private EventProducer eventProducer;

   // @Autowired
  //  private DiscussPostService discussPostService;


    @RequestMapping(path = "/add/{discussPostId}", method = RequestMethod.POST)
    public String addComment(@PathVariable("discussPostId") int discussPostId, Comment comment) {
        // 得到当前用户的 ID
        comment.setUserId(holder.getUser().getId());
        comment.setStatus(0);
        comment.setCreateTime(new Date());
        commentService.addComment(comment);
        return "redirect:/discuss/detail/" + discussPostId;
    }

}