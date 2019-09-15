package springbootdemo.demo;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
public class hi {
    @RequestMapping("/test")
    public String Index() {
        return "ABCD";
    }
}
