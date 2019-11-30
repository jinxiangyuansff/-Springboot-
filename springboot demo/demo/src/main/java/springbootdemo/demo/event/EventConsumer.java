package springbootdemo.demo.event;

import com.alibaba.fastjson.JSONObject;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import springbootdemo.demo.model.DiscussPost;
import springbootdemo.demo.model.Event;
import springbootdemo.demo.service.DiscussPostService;
import springbootdemo.demo.util.CommunityConstant;



@Component
public class EventConsumer implements CommunityConstant
{
    private static final Logger logger = LoggerFactory.getLogger(EventConsumer.class);

    @Autowired
    private DiscussPostService discussPostService;
     

    // 消费发帖事件
    @KafkaListener(topics = {TOPIC_PUBLISH})
    public void handlePublishMessage(ConsumerRecord record) {
        if (record == null || record.value() == null) {
            logger.error("消息的内容为空!");
            return;
        }

        Event event = JSONObject.parseObject(record.value().toString(), Event.class);
        if (event == null) {
            logger.error("消息格式错误!");
            return;
        }

        DiscussPost post = discussPostService.findDiscussPostById(event.getEntityId());
      //  elasticsearchService.saveDiscussPost(post);
    }




}