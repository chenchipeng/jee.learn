package com.jee.learn.manager.support.servlet.captcha;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import com.jee.learn.manager.config.shiro.security.CustomToken;

/**
 * 登录校验码 servlet<br/>
 * 访问路径注意是否被shiro拦截
 * 
 * @author ccp
 * @version 1.0<br/>
 *          修改记录:<br/>
 *          1.2018年10月17日 下午5:36:31 ccp 新建
 */
@WebServlet(urlPatterns = "/img/captcha.jpg")
public class CaptchaServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final int CAPTCHA_LENGTH = 4;

    private int w = 70;
    private int h = 26;

    public CaptchaServlet() {
        super();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String validateCode = req.getParameter(CustomToken.DEFAULT_CAPTCHA_PARAM); // AJAX验证，成功返回true
        if (StringUtils.isNotBlank(validateCode)) {
            resp.setContentType("application/json");
            resp.getOutputStream().print(validate(req, validateCode) ? "true" : "false");
        } else {
            this.doPost(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        createImage(req, resp);
    }

    /** 判断校验码是否相等 */
    private static boolean validate(HttpServletRequest request, String validateCode) {
        if(StringUtils.isBlank(validateCode)) {
            return false;
        }
        String code = (String) request.getSession().getAttribute(CustomToken.DEFAULT_CAPTCHA_PARAM);
        return validateCode.toUpperCase().equals(code.toUpperCase());
    }

    /** 生成校验码图片 */
    private void createImage(HttpServletRequest request, HttpServletResponse response) throws IOException {

        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        response.setContentType("image/jpeg");

        // 得到参数高，宽，都为数字时，则使用设置高宽，否则使用默认值
        String width = request.getParameter("width");
        String height = request.getParameter("height");
        if (StringUtils.isNumeric(width) && StringUtils.isNumeric(height)) {
            w = NumberUtils.toInt(width);
            h = NumberUtils.toInt(height);
        }

        BufferedImage image = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        Graphics g = image.getGraphics();

        // 生成背景
        createBackground(g);

        // 生成字符
        String s = createCharacter(g);
        request.getSession().setAttribute(CustomToken.DEFAULT_CAPTCHA_PARAM, s);

        g.dispose();
        OutputStream out = response.getOutputStream();
        ImageIO.write(image, "JPEG", out);
        out.close();

    }

    /** 获取颜色 */
    private Color getRandColor(int fc, int bc) {
        int f = fc;
        int b = bc;
        Random random = new Random();
        if (f > 255) {
            f = 255;
        }
        if (b > 255) {
            b = 255;
        }
        return new Color(f + random.nextInt(b - f), f + random.nextInt(b - f), f + random.nextInt(b - f));
    }

    /** 生成校验背景 */
    private void createBackground(Graphics g) {
        // 填充背景
        g.setColor(getRandColor(220, 250));
        g.fillRect(0, 0, w, h);
        // 加入干扰线条
        for (int i = 0; i < 8; i++) {
            g.setColor(getRandColor(40, 150));
            Random random = new Random();
            int x = random.nextInt(w);
            int y = random.nextInt(h);
            int x1 = random.nextInt(w);
            int y1 = random.nextInt(h);
            g.drawLine(x, y, x1, y1);
        }
    }

    /** 生成校验字符 */
    private String createCharacter(Graphics g) {
        char[] codeSeq = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'J', 'K', 'M', 'N', 'P', 'Q', 'R', 'S', 'T', 'U',
                'V', 'W', 'X', 'Y', 'Z', '2', '3', '4', '5', '6', '7', '8', '9' };
        String[] fontTypes = { "Arial", "Arial Black", "AvantGarde Bk BT", "Calibri" };
        Random random = new Random();
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < CAPTCHA_LENGTH; i++) {
            String r = String.valueOf(codeSeq[random.nextInt(codeSeq.length)]);// random.nextInt(10));
            g.setColor(new Color(50 + random.nextInt(100), 50 + random.nextInt(100), 50 + random.nextInt(100)));
            g.setFont(new Font(fontTypes[random.nextInt(fontTypes.length)], Font.BOLD, 26));
            g.drawString(r, 15 * i + 5, 19 + random.nextInt(8));
            s.append(r);
        }
        return s.toString();
    }

}
