package springbootdemo.demo.controller;

import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
public class AlphaController 
{
    @RequestMapping(value="/emp", method=RequestMethod.GET)
    @ResponseBody
    public Map<String,Object>  getEmp() {
        Map<String,Object> map = new HashMap<>();
        map.put("name","张三");
        map.put("sal",8000);
        map.put("age",30);
      return map ; 
    }
    
}