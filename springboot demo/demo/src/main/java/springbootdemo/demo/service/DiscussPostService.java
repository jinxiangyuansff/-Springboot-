package springbootdemo.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import springbootdemo.demo.dao.DiscussPostMapper;
import springbootdemo.demo.model.DiscussPost;

@Service
public class DiscussPostService
{
  @Autowired
  private DiscussPostMapper discussPostMapper ;

  public List<DiscussPost> findDiscussPosts(int userId, int offset, int limit) {
    return discussPostMapper.selectDiscussPosts(userId, offset, limit);
}
// 查询行数的方法
public int findDiscussPostRows(int userId) {
    return discussPostMapper.selectDiscussPostRows(userId);
}


}