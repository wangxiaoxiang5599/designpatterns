package com.test.wxx.proxy.dynamicproxy.handwriting.writeself;

import javax.tools.JavaCompiler;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class MyProxy {

    /**
     * 生成代理对象
     * @param loader 类加载器，用于加载被代理类的类文件
     * @param interfaces 被代理类的接口
     * @param h 自定义的InvocationHandler接口,用于具体代理方法的执行
     * @return 返回被代理后的代理对象
     * @throws IllegalArgumentException
     */
    public static Object newProxyInstance(MyClassLoader loader,
                                          Class<?>[] interfaces,
                                          MyInvocationHandler h)
            throws IllegalArgumentException
    {
        /**
         * 1.生成代理类的源代码
         * 2.将生成的源代码输出到磁盘，保存为.java文件
         * 3.编译源代码，并生成.java文件
         * 4.将class文件中的内容，动态加载到JVM中
         * 5.返回被代理后的代理对象
         */

        String src = genSesource(interfaces);
        // 2.将生成的源代码输出到磁盘，保存为.java文件
        String path = MyProxy.class.getResource("").getPath();
        File file = new File(path+"$Proxy0.java");

        FileWriter fw;
        try {
            fw = new FileWriter(file);
            fw.write(src);
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 3.编译源代码，并生成.java文件
        // 获取java编译器
        JavaCompiler javaCompiler = ToolProvider.getSystemJavaCompiler();
        // 标注java文件管理器，用来获取java字节码文件
        StandardJavaFileManager manager = javaCompiler.getStandardFileManager(null,null,null);
        Iterable iterable = manager.getJavaFileObjects(file);

        // 创建task，通过java字节码文件将类信息加载到JVM中
        JavaCompiler.CompilationTask task = javaCompiler.getTask(null,manager,null,null,null,iterable);
        // 开始执行task
        task.call();
        // 关闭管理器
        try {
            manager.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


        // 4.将class文件中的内容，动态加载到JVM中
        Class proxyClass = null;
        try {
            proxyClass = loader.findClass("$Proxy0");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }


        Constructor c = null;
        try {
            c = proxyClass.getConstructor(MyInvocationHandler.class);
            return c.newInstance(h);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        return null;

    }

    /**
     * 生成代理类的源代码
     * @return
     */
    private static String genSesource(Class<?>[] interfaces) {
        /**
         * 无参数方法源码模拟
         */
/*        StringBuilder src = new StringBuilder();
        src.append("package com.test.wxx.proxy.dynamicproxy.handwriting.writeself;").append("\n")
                .append("import com.test.wxx.proxy.dynamicproxy.handwriting.writeself.MyInvocationHandler;").append("\n")
                .append("import java.lang.reflect.Method;").append("\n")
                .append("public class $Proxy0 implements ").append(interfaces[0].getName()).append("{").append("\n")
                .append("private MyInvocationHandler h;").append("\n")
                .append("public $Proxy0(MyInvocationHandler h){").append("\n")
                .append("this.h=h;").append("\n")
                .append("}").append("\n");


        for(Method method:interfaces[0].getMethods()){
            src.append("public ").append(method.getReturnType()).append(" ").append(method.getName()).append("() {").append("\n")
                    .append("try {").append("\n")
                    .append("Method m = ").append(interfaces[0].getName()).append(".class.getMethod(\"").append(method.getName()).append("\");").append("\n")
                    .append("this.h.invoke(this, m, new Object[]{});").append("\n")
                    .append("}catch (Throwable e){").append("\n")
                    .append("e.printStackTrace();").append("\n")
                    .append("}").append("\n")
                    .append("}").append("\n");
        }

        src.append("}");

        return src.toString();*/

        /**
         * 有参数方法源码模拟
         */
        StringBuffer sb = new StringBuffer();
        sb.append(MyProxy.class.getPackage() + ";" + "\n");
        sb.append("import " + interfaces[0].getName() + ";" + "\n");
        sb.append("import java.lang.reflect.*;" + "\n");
        sb.append("public class $Proxy0 implements " + interfaces[0].getName() + "{" + "\n");
        sb.append("MyInvocationHandler h;" + "\n");
        sb.append("public $Proxy0(MyInvocationHandler h) { " + "\n");
        sb.append("this.h = h;");
        sb.append("}" + "\n");
        for (Method m : interfaces[0].getMethods()){
            Class<?>[] params = m.getParameterTypes();

            StringBuffer paramNames = new StringBuffer();
            StringBuffer paramValues = new StringBuffer();
            StringBuffer paramClasses = new StringBuffer();

            for (int i = 0; i < params.length; i++) {
                Class clazz = params[i];
                String type = clazz.getName();
                String paramName = toLowerFirstCase(clazz.getSimpleName());
                paramNames.append(type + " " +  paramName);
                paramValues.append(paramName);
                paramClasses.append(clazz.getName() + ".class");
                if(i > 0 && i < params.length-1){
                    paramNames.append(",");
                    paramClasses.append(",");
                    paramValues.append(",");
                }
            }

            sb.append("public " + m.getReturnType().getName() + " " + m.getName() + "(" + paramNames.toString() + ") {" + "\n");
            sb.append("try{" + "\n");
            sb.append("Method m = " + interfaces[0].getName() + ".class.getMethod(\"" + m.getName() + "\",new Class[]{" + paramClasses.toString() + "});" + "\n");
            sb.append((hasReturnValue(m.getReturnType()) ? "return " : "") + getCaseCode("this.h.invoke(this,m,new Object[]{" + paramValues + "})",m.getReturnType()) + ";" + "\n");
            sb.append("}catch(Error _ex) { }");
            sb.append("catch(Throwable e){" + "\n");
            sb.append("throw new UndeclaredThrowableException(e);" + "\n");
            sb.append("}");
            sb.append(getReturnEmptyCode(m.getReturnType()));
            sb.append("}");
        }
        sb.append("}" + "\n");
        return sb.toString();
    }

    private static String toLowerFirstCase(String src){
        char [] chars = src.toCharArray();
        chars[0] += 32;
        return String.valueOf(chars);
    }

    private static String getReturnEmptyCode(Class<?> returnClass){
        if(mappings.containsKey(returnClass)){
            return "return 0;";
        }else if(returnClass == void.class){
            return "";
        }else {
            return "return null;";
        }
    }

    private static String getCaseCode(String code,Class<?> returnClass){
        if(mappings.containsKey(returnClass)){
            return "((" + mappings.get(returnClass).getName() +  ")" + code + ")." + returnClass.getSimpleName() + "Value()";
        } else if (returnClass != void.class) {
            return "(" + returnClass.getName() + ")" + code;
        }
        return code;
    }

    private static boolean hasReturnValue(Class<?> clazz){
        return clazz != void.class;
    }

    private static Map<Class,Class> mappings = new HashMap<Class, Class>();
    static {
        mappings.put(int.class,Integer.class);
    }
}
