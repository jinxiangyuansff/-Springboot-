package springbootdemo.demo.Web;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController
{

  @RequestMapping("/idnex")
   public String home()
   {
       return "OK";
   }

  
}
