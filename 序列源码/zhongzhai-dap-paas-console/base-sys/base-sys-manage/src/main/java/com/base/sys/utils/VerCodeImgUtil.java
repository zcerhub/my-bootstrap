package com.base.sys.utils;

import org.springframework.util.Base64Utils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;

/**
 * 验证码工具类:用于验证码的生成、验证、存储等操作；
 * 验证码生效时间为60s，超过60s自动生效
 * 缓存中存储10000个验证码，超过10000个自动放弃最早的验证码
 */
public class VerCodeImgUtil {
    //缓存验证码,缓存10000个
    public static Map<String,Object> VERCODE_MAP= new LinkedHashMap<String,Object>(16,0.75f,true) {
        protected boolean removeEldestEntry(Map.Entry<String,Object> eldest) {
            return size() > MAX_SIZE;
        }
    };
    //验证码超时时间
    public static String VERIFY_CODE_KEY="verifyCode";
    //验证码缓存的大小设置
    private static Integer MAX_SIZE=10000;
    //验证码生效的时间-单位s
    private static Integer CODE_INVALID_TIME=60;

    /**
     * 获取验证码
     * @param verifyCode
     * @return
     */
    public static String getVerifyCode(String verifyCode) {
        Object obj=VERCODE_MAP.remove(verifyCode.toLowerCase());
        if(obj!=null){
            Long verifyCreateTime=Long.parseLong(obj.toString());
            //当验证码小于1分钟则继续生效，否则不生效
            if(System.currentTimeMillis()-verifyCreateTime<CODE_INVALID_TIME*1000l){
                return verifyCode;
            }
        }
        return null;
    }
    /**
     * 获取固定长度的随机字符串
     * @param length
     * @return
     */
    public static String getRandomString(int length) {
        String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(62);
            sb.append(str.charAt(number));
        }
        VERCODE_MAP.put(sb.toString().toLowerCase(),System.currentTimeMillis());
        return sb.toString();
    }

    /**
     * 验证码转图片Base64
     * @param varCode
     * @return
     */
    public static String getVerCodeImage(String varCode) {
        String result=null;
        int width = 102, height = 32;//定义验证码图片的大小
        BufferedImage buffImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);//在内存中创建图象
        Graphics2D g = buffImg.createGraphics();//为内存中要创建的图像生成画布，用于“作画”

        //画一个白色矩形，作为验证码背景
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, width, height);
        //画一个黑色矩形边框
        g.setColor(Color.BLACK);
        g.drawRect(0, 0, width - 1, height - 1);

        //画40条灰色的随机干扰线
        g.setColor(Color.GRAY);
        Random random = new Random();        //设置随机种子
        for (int i = 0; i < 40; i++) {        //设置40条干扰线
            int x1 = random.nextInt(width);
            int y1 = random.nextInt(height);
            int x2 = random.nextInt(10);//返回0到10之间一个随机数
            int y2 = random.nextInt(10);
            g.drawLine(x1, y1, x1 + x2, y1 + y2);
        }

        //创建字体
        Font font = new Font("Times New Roman", Font.PLAIN, 32);
        g.setFont(font);
        char[] zifu=varCode.toCharArray();
        for (int i = 0; i < zifu.length; i++) {    //取得4位数的随机字符串
            int red = random.nextInt(255);
            int green = random.nextInt(255);
            int blue = random.nextInt(255);
            g.setColor(new Color(red, green, blue));   //获得一个随机红蓝绿的配合颜色
            g.drawString(String.valueOf(zifu[i]), 24 * i + 6, 25);//把该数字用画笔在画布画出，并指定数字的坐标
        }

        buffImg.flush();//清除缓冲的图片
        result=BufferedImageToBase64(buffImg);
        g.dispose();//释放资源

        return result;
    }

    /**
     * BufferedImage 编码转换为 base64
     * @param bufferedImage
     * @return
     */
    private static String BufferedImageToBase64(BufferedImage bufferedImage) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();//io流
        try {
            ImageIO.write(bufferedImage, "png", baos);//写入流中
        } catch (IOException e) {
//            Debug.logError(e, module);
        }
        byte[] bytes = baos.toByteArray();//转换成字节
        return  Base64Utils.encodeToString(bytes);
    }

}
