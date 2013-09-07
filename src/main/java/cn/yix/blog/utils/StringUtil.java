package cn.yix.blog.utils;

import org.apache.log4j.Logger;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created with IntelliJ IDEA.
 * User: yxdave
 * Date: 13-5-26
 * Time: 下午12:27
 */
public class StringUtil {
    private static Logger logger = Logger.getLogger(StringUtil.class);
    public static String parseMd5(String str){
        try {
            MessageDigest md5Digest = MessageDigest.getInstance("MD5");
            byte[] md5Bytes = md5Digest.digest(str.getBytes());
            return buildMd5String(md5Bytes);
        } catch (NoSuchAlgorithmException e) {
            logger.error(e);
        }
        return null;
    }

    public static String createPwdMd5(String uid,String pwd){
        String step1 = parseMd5(uid)+parseMd5(pwd);
        return parseMd5(step1);
    }

    private static String buildMd5String(byte[] md5Bytes) {
        StringBuilder md5String = new StringBuilder();
        for (byte b:md5Bytes){
            int num = b>=0?b:b+256;
            if (num<16){
                md5String.append("0");
            }
            md5String.append(Integer.toHexString(num));
        }
        return md5String.toString();
    }

    public static String fullToHalf(String sourceString){
        StringBuilder targetBuilder = new StringBuilder();
        for (int i=0;i<sourceString.length();i++){
            String cacheChar = sourceString.substring(i,i+1);
            if ("　".equals(cacheChar)){
                targetBuilder.append(" ");
                continue;
            }
            try {
                byte[] b = cacheChar.getBytes("unicode");
                if (b[2]==-1){
                    b[3]=(byte)(b[3]+32);
                    b[2]=0;
                    targetBuilder.append(new String(b,"unicode"));
                }else {
                    targetBuilder.append(cacheChar);
                }
            } catch (UnsupportedEncodingException ignored) {
            }
        }
        return targetBuilder.toString();
    }
}
