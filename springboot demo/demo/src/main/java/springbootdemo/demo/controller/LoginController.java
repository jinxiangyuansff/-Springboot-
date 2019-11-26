package springbootdemo.demo.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController
{

  @RequestMapping("/index")
   public String home()
   {
       return "OK";
   }

  
}
