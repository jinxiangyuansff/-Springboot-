package springbootdemo.demo.util;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.DigestUtils;

import java.util.Map;
import java.util.UUID;

import com.alibaba.fastjson.JSONObject;
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

   
     public static String getJSONString(int code, String msg, Map<String, Object> map) {
		// 先定义一个JSON对象
        JSONObject json = new JSONObject();
        json.put("code", code);
        json.put("msg", msg);
    	// 把Hashmap打散放到json里面
        if (map != null) {
            for (String  key : map.keySet()) {
                json.put(key, map.get(key));
            }
        }
        return json.toJSONString();
    }

    public static String getJSONString(int code, String msg) {
        return getJSONString(code, msg, null);
    }

    public static String getJSONString(int code) {
        return getJSONString(code, null, null);
    }

    
}