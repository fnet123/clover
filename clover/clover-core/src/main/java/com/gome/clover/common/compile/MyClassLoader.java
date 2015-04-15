package com.gome.clover.common.compile;

import com.gome.clover.common.tools.ClassUtil;
import com.gome.clover.core.job.ClientJob;
import org.apache.commons.codec.binary.Base64;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;

/**
 * 自定义类加载器
 */
public class MyClassLoader extends ClassLoader {

    private static String myClasspath;

    private static ArrayList loadClassList = new ArrayList();

    private static Hashtable loadClassHashTable = new Hashtable();

    public MyClassLoader() {
    	super(null); // 指定父类加载器为 null 
    }

    /**
     * 构造自定义的加载器 参数1：路径名
     */
    public MyClassLoader(String MyClasspath) {
    	super(null); // 指定父类加载器为 null 
        if (!MyClasspath.endsWith("\\")) {
            MyClasspath = MyClasspath + "\\";
        }
        this.myClasspath = MyClasspath;
    }

    /**
     * 设置加载路径 参数1：路径名
     */
    public void SetMyClasspath(String myClasspath) {
        if (!myClasspath.endsWith("\\")) {
            myClasspath = myClasspath + "\\";
        }
        this.myClasspath = myClasspath;
    }

    /**
     * 查找类并加载 参数1：文件名 被 loadClass() 调用
     */
    public Class findClass(String name) {
        byte[] classData = null;
        try {
            classData = loadClassData(name);
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        Class c = defineClass(name, classData, 0, classData.length);
        loadClassHashTable.put(name, c);
        System.out.println("加载" + name + "类成功。");
        return c;
    }

    /**
     * 读取文件字节码 参数1：文件名 被 findClass() 调用
     */
    private byte[] loadClassData(String name) throws IOException {
    	 String classname = name.replace('.', File.separatorChar) + ".class";
        System.out.println(classname);
        if (!(classname == null || classname == "")) {
            FileInputStream inFile = new FileInputStream(myClasspath +File.separatorChar+classname);
            byte[] classData = new byte[inFile.available()];
            inFile.read(classData);
            inFile.close();
            return classData;
        }
        System.out.println("读取字节码失败。");
        return null;
    }

    /**
     * 加载类 参数1：字节码数组 参数2：类名
     */
    public Class loadClass(byte[] classData, String className)
            throws ClassNotFoundException {
        Class c = defineClass(className, classData, 0, classData.length);
        loadClassHashTable.put(className, c);
        System.out.println("加载" + className + "类成功。");
        return c;
    }

    /**
     * 加载类 参数1：类名
     */
    public Class loadClass(String name) throws ClassNotFoundException {
        return loadClass(name, false);
    }

    /**
     * 加载类 参数1：类名 参数2：是否分析这个类
     */
    protected Class loadClass(String name, boolean resolve)
            throws ClassNotFoundException {
        byte[] classData = null;
        Class temp = null;
        // 如是系统类直接装载并记录后返回
        if (name.startsWith("java.")) {
            temp = findSystemClass(name); // 调用父类方法
            loadClassHashTable.put(name, temp);
            System.out.println("loadClass: SystemLoading " + name);
            return temp;
        }

        try {
            temp = findLoadedClass(name);// here need try again it is how to  work
            if (temp != null) {
                System.out.println(name + " have loaded");
                return (temp);
            }
            try {
                temp = findClass(name);
            } catch (Exception fnfe) {
            }
            if (temp == null) {
                temp = findSystemClass(name);
            }
            if (resolve && (temp != null)) {
                resolveClass(temp);
            }
            return temp;
        } catch (Exception e) {
            throw new ClassNotFoundException(e.toString());
        }
    }

  

 

    public static void main(String[] args) {
    	System.out.println();
        String classPath = System.getProperty("user.dir");
        MyClassLoader cl = new MyClassLoader(classPath);
        try {
            // 从tools.jar包中加载ContentSigner类
            //cl.loadClass("ContentSigner", "tools");
            String className = "com.gome.clover.job.MyJobWithSimpleJob";
            CreateAndCompileClassFile.compileAndLoading(classPath,className);

        	Class cls=cl.loadClass(className);
        	Object obj = cls.newInstance();
            String msg = "rO0ABXNyACJjb20uZ29tZS5jbG92ZXIuY29yZS5qb2IuQ2xpZW50Sm9iDQZqULjOYzACAApaABJpc1JlY292ZXJKb2JGcm9tREJMAAtleGVjdXRlVHlwZXQAMExjb20vZ29tZS9jbG92ZXIvY29yZS9qb2IvQ2xpZW50Sm9iJEV4ZWN1dGVUeXBlO1sADmZpeGVkQ2xpZW50SXBzdAATW0xqYXZhL2xhbmcvU3RyaW5nO1sADmZpeGVkU2VydmVySXBzcQB+AAJMAAJpcHQAEkxqYXZhL2xhbmcvU3RyaW5nO0wACGpvYkNsYXNzdAARTGphdmEvbGFuZy9DbGFzcztMAAxqb2JDbGFzc05hbWVxAH4AA0wACWpvYkRldGFpbHQAFkxvcmcvcXVhcnR6L0pvYkRldGFpbDtMAAdqb2JUeXBldAAsTGNvbS9nb21lL2Nsb3Zlci9jb3JlL2pvYi9DbGllbnRKb2IkSm9iVHlwZTtMAAd0cmlnZ2VydAAUTG9yZy9xdWFydHovVHJpZ2dlcjt4cAF+cgAuY29tLmdvbWUuY2xvdmVyLmNvcmUuam9iLkNsaWVudEpvYiRFeGVjdXRlVHlwZQAAAAAAAAAAEgAAeHIADmphdmEubGFuZy5FbnVtAAAAAAAAAAASAAB4cHQAA0FERHBwdAANMTAuMTQ0LjMzLjIxMnZyACZjb20uZ29tZS5jbG92ZXIuam9iLk15Sm9iV2l0aFNpbXBsZUpvYgAAAAAAAAAAAAAAeHB0ACZjb20uZ29tZS5jbG92ZXIuam9iLk15Sm9iV2l0aFNpbXBsZUpvYnNyAB1vcmcucXVhcnR6LmltcGwuSm9iRGV0YWlsSW1wbKvDyuwBWlSvAgAHWgAKZHVyYWJpbGl0eVoADXNob3VsZFJlY292ZXJMAAtkZXNjcmlwdGlvbnEAfgADTAAFZ3JvdXBxAH4AA0wACGpvYkNsYXNzcQB+AARMAApqb2JEYXRhTWFwdAAXTG9yZy9xdWFydHovSm9iRGF0YU1hcDtMAARuYW1lcQB+AAN4cAAAcHQAB3d5R3JvdXBxAH4AD3B0AAV3eUpvYn5yACpjb20uZ29tZS5jbG92ZXIuY29yZS5qb2IuQ2xpZW50Sm9iJEpvYlR5cGUAAAAAAAAAABIAAHhxAH4ACnQABUxPQ0FMc3IAKm9yZy5xdWFydHouaW1wbC50cmlnZ2Vycy5TaW1wbGVUcmlnZ2VySW1wbMwnIeqkAm6jAgAIWgAIY29tcGxldGVJAAtyZXBlYXRDb3VudEoADnJlcGVhdEludGVydmFsSQAOdGltZXNUcmlnZ2VyZWRMAAdlbmRUaW1ldAAQTGphdmEvdXRpbC9EYXRlO0wADG5leHRGaXJlVGltZXEAfgAaTAAQcHJldmlvdXNGaXJlVGltZXEAfgAaTAAJc3RhcnRUaW1lcQB+ABp4cgAob3JnLnF1YXJ0ei5pbXBsLnRyaWdnZXJzLkFic3RyYWN0VHJpZ2dlcsnRVzsN4PXuAgALSQASbWlzZmlyZUluc3RydWN0aW9uSQAIcHJpb3JpdHlaAAp2b2xhdGlsaXR5TAAMY2FsZW5kYXJOYW1lcQB+AANMAAtkZXNjcmlwdGlvbnEAfgADTAAOZmlyZUluc3RhbmNlSWRxAH4AA0wABWdyb3VwcQB+AANMAApqb2JEYXRhTWFwcQB+ABJMAAhqb2JHcm91cHEAfgADTAAHam9iTmFtZXEAfgADTAAEbmFtZXEAfgADeHAAAAAAAAAABQBwcHBxAH4AFHBxAH4AFHEAfgAVcQB+ABUAAAAAAAAAAAAAAAAAAAAAAHBzcgAOamF2YS51dGlsLkRhdGVoaoEBS1l0GQMAAHhwdwgAAAFKPIEUYHhwcQB+AB4=";
            ClientJob serverJob = (ClientJob) ClassUtil.BytesToObject(Base64.decodeBase64(msg));
          /*  Method m = obj.getClass().getMethod("sayHello", new Class[]{});
            Object v=m.invoke(obj, new Object[]{});*/
            System.err.println(obj);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}
