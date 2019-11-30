package springbootdemo.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

@Configuration
public class RedisConfig
{
    // 我们要通过 Template 访问数据库，那么它要想具备访问数据库的能力就要能创建连接，而连接又是由连接工厂创建的，所以要把连接工厂注入进来
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        // 使得Template有访问数据库的能力
        template.setConnectionFactory(redisConnectionFactory);

        // 设置key的序列化的方式
        template.setKeySerializer(RedisSerializer.string());
        // 设置value的序列化方式
        template.setValueSerializer(RedisSerializer.json());
        // 设置hash的key的序列化的方式
        template.setHashKeySerializer(RedisSerializer.string());
        // 设置hash的value的序列化的方式
        template.setHashValueSerializer(RedisSerializer.json());

        template.afterPropertiesSet();
        return template;
    }

}