package cn.wildfirechat.app.tools;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.nio.file.Paths;
import java.util.Random;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {
    private static final Logger LOG = LoggerFactory.getLogger(Utils.class);
    public static String getRandomCode(int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(((int)(Math.random()*100))%10);
        }
        return sb.toString();
    }
    public static boolean isMobile(String mobile) {
        boolean flag = false;
        try {
            // 支持国内普通手机号（11位以13-19开头）或者国际E.164格式手机号（以+开头，后接1到15位数字）
            Pattern p = Pattern.compile("^(1[3-9]\\d{9})|(\\+[1-9]\\d{1,14})$");
            Matcher m = p.matcher(mobile);
            flag = m.matches();
        } catch (Exception e) {
            flag = false;
        }
        return flag;
    }

    public static String normalizeMobile(String mobile) {
        if (mobile == null || mobile.isEmpty()) {
            return mobile;
        }

        // 1. 去除空格、横杠、括号等干扰符
        String cleaned = mobile.replaceAll("[\\s\\-\\(\\)]", "");

        // 2. 将国际拨号冠码 00 转换为通用的符号 +
        if (cleaned.startsWith("00")) {
            cleaned = "+" + cleaned.substring(2);
        }

        // 3. 如果依然没有 + 前缀，且符合中国大陆手机号规则，自动补齐 +86
        if (!cleaned.startsWith("+") && cleaned.matches("^1[3-9]\\d{9}$")) {
            cleaned = "+86" + cleaned;
        }

        // 4. 记录归一化日志
        if (!cleaned.equals(mobile)) {
            LOG.info("Mobile normalized: {} -> {}", mobile, cleaned);
        }

        return cleaned;
    }

    public static String getSafeFileName(String fileName) {
        if (fileName == null || fileName.isEmpty()) {
            return UUID.randomUUID().toString();
        }

        // 使用 Paths.get 解析文件名
        try {
            String newName = Paths.get(fileName).getFileName().toString();
            if(!newName.isEmpty()) {
                return newName;
            }
        } catch (Exception e) {
            // 处理解析异常
            e.printStackTrace();
        }
        return UUID.randomUUID().toString();
    }

    public static void main(String[] args) {
        String filename1 = "/aa../../../hello.txt";
        String filename2 = "..\\..\\1.txt";
        System.out.println(getSafeFileName(filename1));
        System.out.println(getSafeFileName(filename2));
    }
}
