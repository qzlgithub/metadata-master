package com.mingdong.core.util;

import com.mingdong.common.util.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;

public class CaptchaUtils
{
    private static final Font FONT = new Font("Verdana", Font.ITALIC | Font.BOLD, 28);   // 默认字体
    private static final int WIDTH = 120;  // 验证码显示默认宽度
    private static final int HEIGHT = 40;  // 验证码显示默认高度
    private static Logger logger = LoggerFactory.getLogger(CaptchaUtils.class);

    private CaptchaUtils()
    {
    }

    @SuppressWarnings("unused")
    public static void outputWithText(String text, OutputStream outputStream)
    {
        graphicsImage(text.toCharArray(), outputStream, WIDTH, HEIGHT);
    }

    public static void outputWithText(String text, OutputStream outputStream, int width, int height)
    {
        graphicsImage(text.toCharArray(), outputStream, width, height);
    }

    /**
     * 画随机码图
     */
    private static void graphicsImage(char[] strs, OutputStream out, int width, int height)
    {
        try
        {
            BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = (Graphics2D) bi.getGraphics();
            AlphaComposite ac3;
            Color color;
            int len = strs.length;
            g.setColor(Color.WHITE);
            g.fillRect(0, 0, width, height);
            // 随机画干扰的蛋蛋
            for(int i = 0; i < 15; i++)
            {
                color = color(150, 250);
                g.setColor(color);
                g.drawOval(NumberUtils.number(width), NumberUtils.number(height), 5 + NumberUtils.number(10),
                        5 + NumberUtils.number(10));// 画蛋蛋，有蛋的生活才精彩
            }
            g.setFont(FONT);
            int h = height - ((height - FONT.getSize()) >> 1), w = width / len, size = w - FONT.getSize() + 1;
            /* 画字符串 */
            for(int i = 0; i < len; i++)
            {
                ac3 = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.7f);// 指定透明度
                g.setComposite(ac3);
                color = new Color(20 + NumberUtils.number(110), 20 + NumberUtils.number(110),
                        20 + NumberUtils.number(110));// 对每个字符都用随机颜色
                g.setColor(color);
                g.drawString(strs[i] + "", (width - (len - i) * w) + size, h - 4);
            }
            ImageIO.write(bi, "png", out);
            out.flush();
        }
        catch(IOException e)
        {
            logger.error("Error occurred while generate captcha image: " + e.getMessage());
        }
        finally
        {
            if(out != null)
            {
                try
                {
                    out.close();
                }
                catch(IOException e)
                {
                    logger.error("Error occurred while close output stream: " + e.getMessage());
                }
            }
        }
    }

    private static Color color(int fc, int bc)
    {
        if(fc > 255)
        {
            fc = 255;
        }
        if(bc > 255)
        {
            bc = 255;
        }
        int r = fc + NumberUtils.number(bc - fc);
        int g = fc + NumberUtils.number(bc - fc);
        int b = fc + NumberUtils.number(bc - fc);
        return new Color(r, g, b);
    }
}