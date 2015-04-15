package com.gome.clover.common.compile;

import com.gome.clover.common.tools.ClassUtil;
import com.gome.clover.core.job.ClientJob;
import org.apache.commons.codec.binary.Base64;
import org.junit.Test;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * ━━━━━━神兽出没━━━━━━
 * 　　　┏┓　　　┏┓
 * 　　┏┛┻━━━┛┻┓
 * 　　┃　　　　　　　┃
 * 　　┃　　　━　　　┃
 * 　　┃　┳┛　┗┳　┃
 * 　　┃　　　　　　　┃
 * 　　┃　　　┻　　　┃
 * 　　┃　　　　　　　┃
 * 　　┗━┓　　　┏━┛
 * 　　　　┃　　　┃神兽保佑, 永无BUG!
 * 　　　　┃　　　┃Code is far away from bug with the animal protecting
 * 　　　　┃　　　┗━━━┓
 * 　　　　┃　　　　　　　┣┓
 * 　　　　┃　　　　　　　┏┛
 * 　　　　┗┓┓┏━┳┓┏┛
 * 　　　　　┃┫┫　┃┫┫
 * 　　　　　┗┻┛　┗┻┛
 * ━━━━━━感觉萌萌哒━━━━━━
 * Module Desc:clover
 * User: wangyue-ds6 || stark_summer@qq.com
 * Date: 2014/11/26
 * Time: 18:56
 */
public class TestCompile {
    @Test
    public void create(){
        File file = null;
        try {
            file = File.createTempFile("JavaRuntime", ".java", new File(System.getProperty("user.dir")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        String filename = file.getName();
        String classname = "com.gome.sb.JavaRuntime";
        //将代码输出到文件
        PrintWriter out = null;
        try {
            out = new PrintWriter(new FileOutputStream(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        StringBuilder sb = new StringBuilder();
        sb.append("package com.gome.sb; ").append("public class ").append(filename.substring(0,filename.indexOf(".")-1))
                .append("{}");

        out.println(sb.toString());
        //关闭文件流
        out.flush();
        out.close();

    }
    @Test
    public void compile(){
        /* com.sun.tools.javac.Main javac = new com.sun.tools.javac.Main();
        String[] args = new String[] {"-d",System.getProperty("user.dir"),"JavaRuntime4836122228587967142.java"};
        int status = javac.compile(args);
        System.err.println("status"+status);*/
        URLClassLoader loader= null;
        try {
            loader = new URLClassLoader(new URL[]{new URL("file:D:\\Java\\apache-tomcat-6.0.18\\bin\\com\\gome\\clover\\core\\job\\client\\")});
            Object WYMyJob1= loader.loadClass("WYMyJob1").newInstance();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }


    }
    @Test
    public void getObject(){
        String str ="rO0ABXNyACJjb20uZ29tZS5jbG92ZXIuY29yZS5qb2IuU2VydmVySm9its+L9tdn6kgCAAB4cgAiY29tLmdvbWUuY2xvdmVyLmNvcmUuam9iLkNsaWVudEpvYg0GalC4zmMwAgAITAALZXhlY3V0ZVR5cGV0ADBMY29tL2dvbWUvY2xvdmVyL2NvcmUvam9iL0NsaWVudEpvYiRFeGVjdXRlVHlwZTtbAA5maXhlZENsaWVudElwc3QAE1tMamF2YS9sYW5nL1N0cmluZztbAA5maXhlZFNlcnZlcklwc3EAfgADTAACaXB0ABJMamF2YS9sYW5nL1N0cmluZztMAAhqb2JDbGFzc3QAEUxqYXZhL2xhbmcvQ2xhc3M7TAAJam9iRGV0YWlsdAAWTG9yZy9xdWFydHovSm9iRGV0YWlsO0wAB2pvYlR5cGV0ACxMY29tL2dvbWUvY2xvdmVyL2NvcmUvam9iL0NsaWVudEpvYiRKb2JUeXBlO0wAB3RyaWdnZXJ0ABRMb3JnL3F1YXJ0ei9UcmlnZ2VyO3hwfnIALmNvbS5nb21lLmNsb3Zlci5jb3JlLmpvYi5DbGllbnRKb2IkRXhlY3V0ZVR5cGUAAAAAAAAAABIAAHhyAA5qYXZhLmxhbmcuRW51bQAAAAAAAAAAEgAAeHB0AANBRERwcHQADTEwLjE0NC4zMy4yMTJwc3IAHW9yZy5xdWFydHouaW1wbC5Kb2JEZXRhaWxJbXBsq8PK7AFaVK8CAAdaAApkdXJhYmlsaXR5WgANc2hvdWxkUmVjb3ZlckwAC2Rlc2NyaXB0aW9ucQB+AARMAAVncm91cHEAfgAETAAIam9iQ2xhc3NxAH4ABUwACmpvYkRhdGFNYXB0ABdMb3JnL3F1YXJ0ei9Kb2JEYXRhTWFwO0wABG5hbWVxAH4ABHhwAABwdAALcmVtb3RlLWpvYnN2cQB+AABzcgAVb3JnLnF1YXJ0ei5Kb2JEYXRhTWFwn7CD6L+psMsCAAB4cgAmb3JnLnF1YXJ0ei51dGlscy5TdHJpbmdLZXlEaXJ0eUZsYWdNYXCCCOjD+8VdKAIAAVoAE2FsbG93c1RyYW5zaWVudERhdGF4cgAdb3JnLnF1YXJ0ei51dGlscy5EaXJ0eUZsYWdNYXAT5i6tKHYKzgIAAloABWRpcnR5TAADbWFwdAAPTGphdmEvdXRpbC9NYXA7eHABc3IAEWphdmEudXRpbC5IYXNoTWFwBQfawcMWYNEDAAJGAApsb2FkRmFjdG9ySQAJdGhyZXNob2xkeHA/QAAAAAAADHcIAAAAEAAAAAF0AAdqb2JJbmZvc3EAfgABcQB+AAxwcHEAfgAOdnIAJWNvbS5nb21lLmNsb3Zlci5jb3JlLmpvYi5jbGllbnQuTXlKb2IAAAAAAAAAAAAAAHhwc3EAfgAPAABwdAAHREVGQVVMVHEAfgAecHQABk15Sm9iMX5yACpjb20uZ29tZS5jbG92ZXIuY29yZS5qb2IuQ2xpZW50Sm9iJEpvYlR5cGUAAAAAAAAAABIAAHhxAH4AC3QABlJFTU9URXNyAChvcmcucXVhcnR6LmltcGwudHJpZ2dlcnMuQ3JvblRyaWdnZXJJbXBsiAb06o3becICAAVMAAZjcm9uRXh0ABtMb3JnL3F1YXJ0ei9Dcm9uRXhwcmVzc2lvbjtMAAdlbmRUaW1ldAAQTGphdmEvdXRpbC9EYXRlO0wADG5leHRGaXJlVGltZXEAfgAnTAAQcHJldmlvdXNGaXJlVGltZXEAfgAnTAAJc3RhcnRUaW1lcQB+ACd4cgAob3JnLnF1YXJ0ei5pbXBsLnRyaWdnZXJzLkFic3RyYWN0VHJpZ2dlcsnRVzsN4PXuAgALSQASbWlzZmlyZUluc3RydWN0aW9uSQAIcHJpb3JpdHlaAAp2b2xhdGlsaXR5TAAMY2FsZW5kYXJOYW1lcQB+AARMAAtkZXNjcmlwdGlvbnEAfgAETAAOZmlyZUluc3RhbmNlSWRxAH4ABEwABWdyb3VwcQB+AARMAApqb2JEYXRhTWFwcQB+ABBMAAhqb2JHcm91cHEAfgAETAAHam9iTmFtZXEAfgAETAAEbmFtZXEAfgAEeHAAAAAAAAAABQBwcHBxAH4AIHBxAH4AIHB0AAh0cmlnZ2VyMXNyABlvcmcucXVhcnR6LkNyb25FeHByZXNzaW9uAAAAAuR+Lw8CAAJMAA5jcm9uRXhwcmVzc2lvbnEAfgAETAAIdGltZVpvbmV0ABRMamF2YS91dGlsL1RpbWVab25lO3hwdAAOMC8xMCAqICogKiAqID9zcgAac3VuLnV0aWwuY2FsZW5kYXIuWm9uZUluZm8k0dPOAB1xmwIACEkACGNoZWNrc3VtSQAKZHN0U2F2aW5nc0kACXJhd09mZnNldEkADXJhd09mZnNldERpZmZaABN3aWxsR01UT2Zmc2V0Q2hhbmdlWwAHb2Zmc2V0c3QAAltJWwAUc2ltcGxlVGltZVpvbmVQYXJhbXNxAH4AMFsAC3RyYW5zaXRpb25zdAACW0p4cgASamF2YS51dGlsLlRpbWVab25lMbPp9XdErKECAAFMAAJJRHEAfgAEeHB0AA1Bc2lhL1NoYW5naGFpBChB7AAAAAABt3QAAAAAAAB1cgACW0lNumAmduqypQIAAHhwAAAABAG3dAABvNMAAe5igAA27oBwdXIAAltKeCAEtRKxdZMCAAB4cAAAABP/39rgHcAAAf/stijAEAAA//Jqdl3AADL/8pESoNgAAP/yxoQKQAAy//MGjbOYAAAAB4B2LwAAMgAHq0Gc2AAAAAfu3INAADIACCBqSdgAAAAIZAUwQAAyAAiVkvbYAAAACNtupYAAMgAJDPxsGAAAAAlQl1KAADIACYIlGRgAAAAJxb//gAAyAAn3TcYYAAAAHsSTMsAAAHBwcHNyAA5qYXZhLnV0aWwuRGF0ZWhqgQFLWXQZAwAAeHB3CAAAAUnrhWuweHgAdAAOREVGQVVMVC5NeUpvYjFxAH4AI3NxAH4AJQAAAAAAAAAFAHBwcHEAfgAScHEAfgASdAAOREVGQVVMVC5NeUpvYjF0AA5ERUZBVUxULk15Sm9iMXNxAH4AK3QADjAvMTAgKiAqICogKiA/c3EAfgAvdAANQXNpYS9TaGFuZ2hhaQQoQewAAAAAAbd0AAAAAAAAdXEAfgA1AAAABAG3dAABvNMAAe5igAA27oBwdXEAfgA3AAAAE//f2uAdwAAB/+y2KMAQAAD/8mp2XcAAMv/ykRKg2AAA//LGhApAADL/8waNs5gAAAAHgHYvAAAyAAerQZzYAAAAB+7cg0AAMgAIIGpJ2AAAAAhkBTBAADIACJWS9tgAAAAI226lgAAyAAkM/GwYAAAACVCXUoAAMgAJgiUZGAAAAAnFv/+AADIACfdNxhgAAAAexJMywAAAcHBwc3EAfgA5dwgAAAFJ64VrsHg=";
        ClientJob clientJob = (ClientJob)ClassUtil.BytesToObject(Base64.decodeBase64(str));
        String className = clientJob.getJobDetail().getJobClass().getName();
        System.err.println("className:"+className);
    }


}
