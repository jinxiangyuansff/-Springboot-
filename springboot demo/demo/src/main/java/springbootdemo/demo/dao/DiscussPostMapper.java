package springbootdemo.demo.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import springbootdemo.demo.model.DiscussPost;

public interface DiscussPostMapper
{
    List<DiscussPost> selectDiscussPosts(int userId, int offset, int limit);

    int selectDiscussPostRows(@Param("userId") int userId);

    int insertDiscussPost(DiscussPost discussPost);

    
}