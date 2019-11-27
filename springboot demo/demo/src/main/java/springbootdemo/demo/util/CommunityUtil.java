package springbootdemo.demo.util;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.DigestUtils;
import java.util.UUID;
public class CommunityUtil
{
    public static String generateUUID() {
        return UUID.randomUUID().toString().replace("-","");
    }

    // MD5加密 只能加密不能解密，采用加盐加密
    public static String md5(String key) {
        if(StringUtils.isBlank(key)) {
            return null;
        }
        return DigestUtils.md5DigestAsHex(key.getBytes());
    }
    
   
}