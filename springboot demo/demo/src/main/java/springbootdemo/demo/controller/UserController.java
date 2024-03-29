package springbootdemo.demo.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServletResponse;
import springbootdemo.demo.annotation.LoginRequired;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;

import springbootdemo.demo.model.User;
import springbootdemo.demo.service.UserService;
import springbootdemo.demo.util.CommunityUtil;
import springbootdemo.demo.util.hostHolder;

@Controller
@RequestMapping("/user")
public class UserController
{
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Value("${demo.path.upload}")
    private String uploadPath;

    @Value("${demo.path.domain}")
    private String domain;

    @Value("${server.servlet.context-path}")
    private String contextPath;

    @Autowired
    private UserService userService;

    @Autowired
    private hostHolder holder;

    @LoginRequired
    @RequestMapping(path = "/setting", method = RequestMethod.GET)
    public String getSettingPage() {
        return "/site/setting";
    }

    
    @LoginRequired
    @RequestMapping(path = "/upload", method = RequestMethod.POST)
    public String uploadHeader(MultipartFile headerImage, Model model) {
        if (headerImage == null) {
            model.addAttribute("error", "你还没有上传图片");
            return "/site/setting";
        }

      // 上传这个文件，要更换一下文件名不能用上传的文件名
      String fileName = headerImage.getOriginalFilename();// 得到原始的文件名
      String suffix = fileName.substring(fileName.lastIndexOf('.'));
      if (StringUtils.isBlank(suffix)) {
          model.addAttribute("error", "文件的格式不正确");
          return "/site/setting";
      }
      // 生成随机的文件名
      fileName = CommunityUtil.generateUUID() + suffix;
      // 确定文件存放的位置
      File dest = new File(uploadPath + "/" +fileName);
      try {
          headerImage.transferTo(dest);
      } catch (IOException e) {
          logger.error("上传文件失败：" + e.getMessage());
          throw new RuntimeException("上传文件失败，服务器发生异常" + e);
      }
      // 更新当前用户头像的路径（web路径）
      // http://localhost:8080/community/user/header/xxx.png
      User user = holder.getUser();
      String headerUrl = domain + contextPath + "/user/header/" + fileName;
      userService.updateHeader(user.getId(), headerUrl);
      return "redirect:/index";
     }

     @RequestMapping(path = "/header/{fileName}", method = RequestMethod.GET)
    public void getHeader(@PathVariable("fileName") String fileName, HttpServletResponse httpServletResponse) {
        // 服务器存放路径
        fileName = uploadPath + "/" + fileName;
        // 文件的后缀
        String suffix = fileName.substring(fileName.lastIndexOf('.'));
        // 响应图片
        httpServletResponse.setContentType("image/" + suffix);
       	// 因为图片是二进制，所以要先获得字节流
        try (
                OutputStream os = httpServletResponse.getOutputStream();
                FileInputStream fis = new FileInputStream(fileName);
        ) {
            byte[] buffer = new byte[1024];
            int b = 0;
            while ((b = fis.read(buffer)) != -1) {
                os.write(buffer, 0, b);
            }
        } catch (IOException e) {
            logger.error("读取头像失败：" + e.getMessage());
        }
    }
}