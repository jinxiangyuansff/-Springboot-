package springbootdemo.demo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import springbootdemo.demo.util.SensitiveFilter;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = DemoApplication.class)
public class wordTest
{
    @Autowired
    private SensitiveFilter sensitiveFilter;



    @Test
    public void testWord()
    {
        String text1 = "这里可以赌博,可以嫖娼,可以吸毒,可以开票,哈哈哈!";
        text1 = sensitiveFilter.filter(text1) ;
        System.out.println(text1); 
    }
  
   

}