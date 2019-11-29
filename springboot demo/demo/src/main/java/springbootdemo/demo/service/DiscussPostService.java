package springbootdemo.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import springbootdemo.demo.dao.DiscussPostMapper;
import springbootdemo.demo.model.DiscussPost;
import springbootdemo.demo.util.SensitiveFilter;

@Service
public class DiscussPostService
{
  
  @Autowired
  private DiscussPostMapper discussPostMapper ;


  @Autowired
  private SensitiveFilter sensitiveFilter;



  public List<DiscussPost> findDiscussPosts(int userId, int offset, int limit) {
    return discussPostMapper.selectDiscussPosts(userId, offset, limit);
}

 
//增加帖子的方法
public int addDiscussPost(DiscussPost discussPost) {
  if (discussPost == null) {
      throw new IllegalArgumentException("参数不能为空！");
  }
  // 转义Html标签
  discussPost.setTitle(HtmlUtils.htmlEscape(discussPost.getTitle()));
  discussPost.setContent(HtmlUtils.htmlEscape(discussPost.getContent()));
  // 过滤敏感词
  discussPost.setTitle(sensitiveFilter.filter(discussPost.getTitle()));
  discussPost.setContent(sensitiveFilter.filter(discussPost.getContent()));

  return discussPostMapper.insertDiscussPost(discussPost);
}

public DiscussPost findDiscussPostById(int id) {
  return discussPostMapper.selectDiscussPostById(id);
}


// 更新帖子数量
public int updateCommentCount(int id, int commentCount) {
  return discussPostMapper.updateCommentCount(id, commentCount);
}








// 查询行数的方法
public int findDiscussPostRows(int userId) {
    return discussPostMapper.selectDiscussPostRows(userId);
      }


      
   }