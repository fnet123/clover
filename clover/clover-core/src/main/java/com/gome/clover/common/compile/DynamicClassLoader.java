package com.gome.clover.common.compile;

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
 * Time: 20:24
 */
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class DynamicClassLoader extends ClassLoader {

    public DynamicClassLoader(ClassLoader parent) {
        super(parent);
    }

    @SuppressWarnings("unchecked")
    public Class loadClass(String classPath, String className)
            throws ClassNotFoundException {
        try {
            String url = classPathParser(classPath)
                    + classNameParser(className);
            System.out.println(url);
            URL myUrl = new URL(url);
            URLConnection connection = myUrl.openConnection();
            InputStream input = connection.getInputStream();
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            int data = input.read();
            while (data != -1) {
                buffer.write(data);
                data = input.read();
            }
            input.close();
            byte[] classData = buffer.toByteArray();
            return defineClass(noSuffix(className), classData, 0,
                    classData.length);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String pathParser(String path) {
        return path.replaceAll("\\\\", "/");
    }

    private String classPathParser(String path) {
        String classPath = pathParser(path);
        if (!classPath.startsWith("file:")) {
            classPath = "file:" + classPath;
        }
        if (!classPath.endsWith("/")) {
            classPath = classPath + "/";
        }
        return classPath;
    }

    private String classNameParser(String className) {
        return className.substring(0, className.lastIndexOf(".")).replaceAll(
                "\\.", "/")
                + className.substring(className.lastIndexOf("."));
    }

    private String noSuffix(String className) {
        return className.substring(0, className.lastIndexOf("."));
    }

    public static void main(String[] arguments) throws Exception {
        String classPath = "D:\\Java\\apache-tomcat-6.0.18\\bin\\";
        String className = "com.gome.clover.core.job.client.WYMyJob1.class";
        Object object = new DynamicClassLoader(DynamicClassLoader.class.getClassLoader())
                .loadClass(classPath, className).newInstance();
        System.err.println(object);

    }
}