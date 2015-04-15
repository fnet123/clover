package com.gome.clover.common.encryption;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

/**
 * 采用MD5加密解密
 * @author wangyue-ds6 tfq
 * @datetime 2011-10-13
 */
public class MD5Util {
    public static final String ENCODE = "UTF-8";  //UTF-8

    /***
     * MD5加码 生成32位md5码
     */
    public static String encryptMD5(String inStr){
        MessageDigest md5 = null;
        try{
            md5 = MessageDigest.getInstance("MD5");
        }catch (Exception e){
            System.out.println(e.toString());
            e.printStackTrace();
            return "";
        }
        char[] charArray = inStr.toCharArray();
        byte[] byteArray = new byte[charArray.length];

        for (int i = 0; i < charArray.length; i++)
            byteArray[i] = (byte) charArray[i];
        byte[] md5Bytes = md5.digest(byteArray);
        StringBuffer hexValue = new StringBuffer();
        for (int i = 0; i < md5Bytes.length; i++){
            int val = ((int) md5Bytes[i]) & 0xff;
            if (val < 16)
                hexValue.append("0");
            hexValue.append(Integer.toHexString(val));
        }
        return hexValue.toString();

    }

    /***
     * MD5加码 生成32位md5码
     */
    public static String encryptMD5WithKey(String encryptStr,String encryptKey){

        byte k_ipa[] = new byte[64];
        byte k_opal[] = new byte[64];
        byte strBytes[];
        byte value[];
        try
        {
            strBytes = encryptStr.getBytes(ENCODE);
            value = encryptKey.getBytes(ENCODE);
        }
        catch(UnsupportedEncodingException e)
        {
            strBytes = encryptStr.getBytes();
            value = encryptKey.getBytes();
        }
        Arrays.fill(k_ipa, strBytes.length, 64, (byte) 54);
        Arrays.fill(k_opal, strBytes.length, 64, (byte)92);
        for(int i = 0; i < strBytes.length; i++)
        {
            k_ipa[i] = (byte)(strBytes[i] ^ 0x36);
            k_opal[i] = (byte)(strBytes[i] ^ 0x5c);
        }

        MessageDigest md;
        try
        {
            md = MessageDigest.getInstance("MD5");
        }
        catch(NoSuchAlgorithmException e)
        {
            e.printStackTrace();
            return null;
        }
        md.update(k_ipa);
        md.update(value);
        byte dg[] = md.digest();
        md.reset();
        md.update(k_opal);
        md.update(dg, 0, 16);
        dg = md.digest();
        return toHex(dg);
    }

    /**
     * 加密解密算法 执行一次加密，两次解密
     */
    public static String convertMD5(String inStr){

        char[] a = inStr.toCharArray();
        for (int i = 0; i < a.length; i++){
            a[i] = (char) (a[i] ^ 't');
        }
        String s = new String(a);
        return s;

    }
    public static String toHex(byte input[])
    {
        if(input == null)
            return null;
        StringBuffer output = new StringBuffer(input.length * 2);
        for(int i = 0; i < input.length; i++)
        {
            int current = input[i] & 0xff;
            if(current < 16)
                output.append("0");
            output.append(Integer.toString(current, 16));
        }

        return output.toString();
    }

    // 测试主函数
    public static void main(String args[]) {
       /* String s = new String("wangyue-ds6");
        System.out.println("原始：" + s);
        System.out.println("MD5后：" + encryptMD5(s));
        //System.out.println("加密的：" + convertMD5(s));
        System.out.println("解密的：" + convertMD5(convertMD5(s))); */
        String key ="clover";
        String s = new String("clover");
        System.out.println("原始：" + s);
        System.out.println("MD5后:" + encryptMD5WithKey(s,key));
        System.out.println("解密的：" + convertMD5(convertMD5(s)));

    }
}
