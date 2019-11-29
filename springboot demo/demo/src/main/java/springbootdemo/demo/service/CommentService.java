package springbootdemo.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.HtmlUtils;

import springbootdemo.demo.dao.CommentMapper;
import springbootdemo.demo.model.Comment;
import springbootdemo.demo.util.CommunityConstant;
import springbootdemo.demo.util.SensitiveFilter;


// REQUIRED: 支持当前事务(外部事务),如果不存在则创建新事务.
// REQUIRES_NEW: 创建一个新事务,并且暂停当前事务(外部事务).
// NESTED: 如果当前存在事务(外部事务),则嵌套在该事务中执行(独立的提交和回滚),否则就会REQUIRED一样.
// 注解实现事务 
@Service
public class CommentService implements CommunityConstant
{
    @Autowired
    private CommentMapper commentMapper;
     
    @Autowired
    private SensitiveFilter sensitiveFilter;
  
    @Autowired
    private DiscussPostService discussPostService;

    //增加一个评论
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public int addComment(Comment comment)
    {
        if (comment == null) {
            throw new IllegalArgumentException("参数不能为空");
        }

        // 添加评论
        comment.setContent(HtmlUtils.htmlEscape(comment.getContent()));
        comment.setContent(sensitiveFilter.filter(comment.getContent()));
        int rows = commentMapper.insertComment(comment);

        // 更新帖子评论数量
        if (comment.getEntityType() == ENTITY_TYPE_POST) {
            // 查到实体对应的评论数量
            int count = commentMapper.selectCountByEntity(comment.getEntityType(), comment.getEntityId());
            discussPostService.updateCommentCount(comment.getEntityId(), count);
        }

        return rows;

    }









    public List<Comment> findCommentsByEntity(int entityType, int entityId, int offset, int limit) {
        return commentMapper.selectCommentsByEntity(entityType, entityId, offset, limit);
    }

    public int findCommentCount(int entityType, int entityId) {
        return commentMapper.selectCountByEntity(entityType, entityId);
    }
       
    
}