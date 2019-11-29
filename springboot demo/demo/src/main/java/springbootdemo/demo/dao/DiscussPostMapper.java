package springbootdemo.demo.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import springbootdemo.demo.model.DiscussPost;

@Mapper
public interface DiscussPostMapper
{
    List<DiscussPost> selectDiscussPosts(int userId, int offset, int limit);

    int selectDiscussPostRows(@Param("userId") int userId);

    DiscussPost selectDiscussPostById(int id);

    int insertDiscussPost(DiscussPost discussPost);
    
    int updateCommentCount(int id, int commentCount);

     
}