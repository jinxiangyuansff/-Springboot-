package springbootdemo.demo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DemoApplicationTests {

	@Test
	public void contextLoads() {
		int a = 5;
		System.out.println(a);
	}

	@Test
	public void helloTest()
	{  String b="ad";
		
		System.out.println("你好");
		 System.out.println(b);
	
	}

}
