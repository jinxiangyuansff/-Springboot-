package springbootdemo.demo.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import springbootdemo.demo.model.DiscussPost;

@Mapper
public interface DiscussPostMapper
{
    List<DiscussPost> selectDiscussPosts(int userId, int offset, int limit);

    //如果需要动态的拼一个条件，在<if>里面使用，并且这个方法有且只有一个参数，这个参数前面必须取别名
    int selectDiscussPostRows(@Param("userId") int userId);

    int insertDiscussPost(DiscussPost discussPost);

    DiscussPost selectDiscussPostById(int id);

    int updateCommentCount(int id, int commentCount);

     
}