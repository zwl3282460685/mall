package ltd.newbee.mall.controller;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Controller
public class KaptchaController {

    @Autowired
    private DefaultKaptcha defaultKaptcha;

    @GetMapping("/kaptcha")
    public void defaultKaptcha(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException {
        byte[] captchaOutputStream = null;
        ByteArrayOutputStream imgOutputStream = new ByteArrayOutputStream();
        try {
            //生成验证码字符串
            String verifyCode = defaultKaptcha.createText();
            //生成的验证码字符串保存到session中
            httpServletRequest.getSession().setAttribute("verifyCode", verifyCode);
            //创建验证码图片
            BufferedImage challenge = defaultKaptcha.createImage(verifyCode);
            //将验证码图片写入imgOutputStream中
            ImageIO.write(challenge, "jpg", imgOutputStream);
        } catch (IllegalArgumentException e) {
            httpServletResponse.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        captchaOutputStream = imgOutputStream.toByteArray();
        httpServletResponse.setHeader("Cache-Control", "no-store");
        httpServletResponse.setHeader("Pragma", "no-cache");
        httpServletResponse.setDateHeader("Expires", 0);
        httpServletResponse.setContentType("image/jpeg");
        ServletOutputStream responseOutputStream = httpServletResponse.getOutputStream();
        responseOutputStream.write(captchaOutputStream);
        responseOutputStream.flush();
        responseOutputStream.close();
    }

    @GetMapping("/verify")
    @ResponseBody
    public String verify(@RequestParam("code") String code, HttpSession session){
        if(StringUtils.isEmpty(code)){
            return "验证码不能为空";
        }
        String kaptchaCode = session.getAttribute("verifyCode") + "";
        if(StringUtils.isEmpty(kaptchaCode) || !code.equals(kaptchaCode)){
            return "验证码错误";
        }
        return "验证成功";
    }
}
