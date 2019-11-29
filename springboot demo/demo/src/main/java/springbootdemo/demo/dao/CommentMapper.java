package springbootdemo.demo.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import springbootdemo.demo.model.Comment;

@Mapper
public interface CommentMapper{

  // 根据实体来查询到的评论，要分页
  List<Comment> selectCommentsByEntity(int entityType, int entityId, int offset, int limit);

  int selectCountByEntity(int entityType, int entityId);

  int insertComment(Comment comment);
  
}

