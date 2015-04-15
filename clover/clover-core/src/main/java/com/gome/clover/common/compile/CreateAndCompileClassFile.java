package com.gome.clover.common.compile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;

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
 * Module Desc:clover Create And Compile Class File
 * User: wangyue-ds6 || stark_summer@qq.com
 * Date: 2014/11/26
 * Time: 19:31
 */
public class CreateAndCompileClassFile {

    public static Class compileAndLoading(String classPath, String className){
        String packageStr = className.substring(0,className.lastIndexOf("."));
        String clazzName = className.substring(className.lastIndexOf(".")+1);
        String separator = File.separator;
        File file = new File(classPath+separator+clazzName+".java");
        PrintWriter out;
        try {
            out = new PrintWriter(new FileOutputStream(file));
            StringBuilder sb = new StringBuilder();
            sb.append("package ").append(packageStr).append(" ; ").append("public class ").append(clazzName)
                    .append("{}");
            out.println(sb.toString());
            //关闭文件流
            out.flush();
            out.close();
            com.sun.tools.javac.Main javac = new com.sun.tools.javac.Main();
            String[] args = new String[] {"-d",System.getProperty("user.dir"),clazzName+".java"};
            int status = javac.compile(args);
            if(0==status){
             /*   return  new DynamicClassLoader(DynamicClassLoader.class.getClassLoader())
                        .loadClass(classPath, className + ".class");*/
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static void main(String args[]){
       CreateAndCompileClassFile.compileAndLoading(System.getProperty("user.dir"),"com.gome.sb.SB");
    }
}
