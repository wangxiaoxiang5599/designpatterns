# 动态代理

代理模式是设计模式中非常重要的一种类型，而设计模式又是编程中非常重要的知识点，特别是在业务系统的重构中，更是有举足轻重的地位。代理模式从类型上来说，可以分为静态代理和动态代理两种类型。

在解释动态代理之前我们先理解一下静态代理：

### 首先你要明白静态代理的作用

我们有一个字体提供类，有多种实现（从磁盘，从网络，从系统）

```
public interface FontProvider {
    Font getFont(String name);
}

public abstract class ProviderFactory {
    public static FontProvider getFontProvider() {
        return new FontProviderFromDisk();
    }
}

public class Main() {
    public static void main(String[] args) {
        FontProvider fontProvider = ProviderFactory.getFontProvider();
        Font font = fontProvider.getFont("微软雅黑");
        ......
    }
}
```

现在我们希望给他加上一个缓存功能，我们可以用静态代理来完成

```
public class CachedFontProvider implements FontProvider {
    private FontProvider fontProvider;
    private Map<String, Font> cached;

    public CachedFontProvider(FontProvider fontProvider) {
        this.fontProvider = fontProvider;
    }

    public Font getFont(String name) {
        Font font = cached.get(name);
        if (font == null) {
            font = fontProvider.getFont(name);
            cached.put(name, font);
        }
        return font;
    }
}


/* 对工厂类进行相应修改，代码使用处不必进行任何修改。
   这也是面向接口编程以及工厂模式的一个好处 */
public abstract class ProviderFactory {
    public static FontProvider getFontProvider() {
        return new CachedFontProvider(new FontProviderFromDisk());
    }
}
```

当然，我们直接修改FontProviderFromDisk类也可以实现目的，但是我们还有FontProviderFromNet, FontProviderFromSystem等多种实现类，一一修改太过繁琐且易出错。况且将来还可能添加日志，权限检查，异常处理等功能显然用代理类更好一点。

然而今天的重点是：我们都知道牛逼轰轰的Spring AOP的实现的一种方式是使用JDK的动态代理（另一种是cglib），**大部分人也会用jdk的动态代理，不过没有研究过jdk的动态代理到底是怎么实现的**。今天就来揭开他的神秘面纱；

## 1. 原理源码剖析

### 首先我们先来讲一下JDK动态代理的实现原理

1.拿到被代理对象的引用，然后获取他的接口
2.JDK代理重新生成一个类，同时实现我们给的代理对象所实现的接口
3.把被代理对象的引用拿到了
4.重新动态生成一个class字节码
5.然后编译

**然后先实现一个动态代理，代码很简单了，就是实现**
`java.lang.reflect.InvocationHandler`**接口，并使用**
`java.lang.reflect.Proxy.newProxyInstance()`**方法生成代理对象**

```
/**
 * @author mark
 * @date 2018/3/30
 */
public class JdkInvocationHandler implements InvocationHandler {

    private ProductService target;

    public Object getInstance(ProductService target){
        this.target = target;
        Class clazz = this.target.getClass();
        // 参数1：被代理类的类加载器 参数2:被代理类的接口 参数3
        return Proxy.newProxyInstance(clazz.getClassLoader(),
                clazz.getInterfaces(),
                this);
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String currentDate  = simpleDateFormat.format(new Date());
        System.out.println("日期【"+currentDate + "】添加了一款产品");

        return method.invoke(this.target,args);
    }
}
```

被代理接口和实现

```
/**
 * 模仿产品Service
 * @author mark
 * @date 2018-03-30
 */
public interface ProductService {
    /**
     * 添加产品
     * @param productName
     */
    void addProduct(String productName);
}

/**
 * @author mark
 * @date 2018/3/30
 */
public class ProductServiceImpl implements ProductService{
    public void addProduct(String productName) {
        System.out.println("正在添加"+productName);
    }
}
```

测试类

```
public class Test {
    public static void main(String[] args) throws Exception {
        ProductService productService = new ProductServiceImpl();
        ProductService proxy = (ProductService) new JdkInvocationHandler().getInstance(productService);
        proxy.addProduct("iphone");

        // 这里我们将jdk生成的代理类输出了出来，方便后面分析使用
        byte[] bytes = ProxyGenerator.generateProxyClass("$Proxy0",new Class[]{productService.getClass()});

        FileOutputStream os = new FileOutputStream("Proxy0.class");
        os.write(bytes);
        os.close();
    }
}
```

结果输出

```
日期【2018-03-30】添加了一款产品
正在添加iphone

Process finished with exit code 0
```

上面我们实现动态动态代理的时候输出了代理类的字节码文件，现在来看一下字节码文件反编译过后的内容

```
import com.gwf.jdkproxy.ProductServiceImpl;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.lang.reflect.UndeclaredThrowableException;

// 继承了Proxy类
public final class $Proxy0 extends Proxy implements ProductServiceImpl {
    private static Method m1;
    private static Method m8;
    private static Method m2;
    private static Method m3;
    private static Method m5;
    private static Method m4;
    private static Method m7;
    private static Method m9;
    private static Method m0;
    private static Method m6;

    public $Proxy0(InvocationHandler var1) throws  {
        super(var1);
    }

....
....

/**
* 这里是代理类实现的被代理对象的接口的相同方法
*/
    public final void addProduct(String var1) throws  {
        try {
            // super.h 对应的是父类的h变量，他就是Proxy.nexInstance方法中的InvocationHandler参数
           // 所以这里实际上就是使用了我们自己写的InvocationHandler实现类的invoke方法
            super.h.invoke(this, m3, new Object[]{var1});
        } catch (RuntimeException | Error var3) {
            throw var3;
        } catch (Throwable var4) {
            throw new UndeclaredThrowableException(var4);
        }
    }

   

    public final Class getClass() throws  {
        try {
            return (Class)super.h.invoke(this, m7, (Object[])null);
        } catch (RuntimeException | Error var2) {
            throw var2;
        } catch (Throwable var3) {
            throw new UndeclaredThrowableException(var3);
        }
    }

....
....
// 在静态构造块中，代理类通过反射获取了被代理类的详细信息，比如各种方法
    static {
        try {
            m1 = Class.forName("java.lang.Object").getMethod("equals", Class.forName("java.lang.Object"));
            m8 = Class.forName("com.gwf.jdkproxy.ProductServiceImpl").getMethod("notify");
            m2 = Class.forName("java.lang.Object").getMethod("toString");
            m3 = Class.forName("com.gwf.jdkproxy.ProductServiceImpl").getMethod("addProduct", Class.forName("java.lang.String"));
            m5 = Class.forName("com.gwf.jdkproxy.ProductServiceImpl").getMethod("wait", Long.TYPE);
            m4 = Class.forName("com.gwf.jdkproxy.ProductServiceImpl").getMethod("wait", Long.TYPE, Integer.TYPE);
            m7 = Class.forName("com.gwf.jdkproxy.ProductServiceImpl").getMethod("getClass");
            m9 = Class.forName("com.gwf.jdkproxy.ProductServiceImpl").getMethod("notifyAll");
            m0 = Class.forName("java.lang.Object").getMethod("hashCode");
            m6 = Class.forName("com.gwf.jdkproxy.ProductServiceImpl").getMethod("wait");
        } catch (NoSuchMethodException var2) {
            throw new NoSuchMethodError(var2.getMessage());
        } catch (ClassNotFoundException var3) {
            throw new NoClassDefFoundError(var3.getMessage());
        }
    }
}
```

补充一下上面代母注释中的`super.h`

```
protected InvocationHandler h;

protected Proxy(InvocationHandler h) {
        Objects.requireNonNull(h);
        this.h = h;
    }

// 这个方法是Proxy的newProxyInstance方法，主要就是生成了上面的动态字节码文件
public static Object newProxyInstance(ClassLoader loader,
                                          Class<?>[] interfaces,
                                          InvocationHandler h)
        throws IllegalArgumentException
    {
        Objects.requireNonNull(h);

        final Class<?>[] intfs = interfaces.clone();
        final SecurityManager sm = System.getSecurityManager();
        if (sm != null) {
            checkProxyAccess(Reflection.getCallerClass(), loader, intfs);
        }

        /*
         * Look up or generate the designated proxy class.
         */
        Class<?> cl = getProxyClass0(loader, intfs);

        /*
         * Invoke its constructor with the designated invocation handler.
         */
        try {
            if (sm != null) {
                checkNewProxyPermission(Reflection.getCallerClass(), cl);
            }

            final Constructor<?> cons = cl.getConstructor(constructorParams);
       
            final InvocationHandler ih = h;
            if (!Modifier.isPublic(cl.getModifiers())) {
                AccessController.doPrivileged(new PrivilegedAction<Void>() {
                    public Void run() {
                        cons.setAccessible(true);
                        return null;
                    }
                });
            }
// 重点看这里，将我们传来的InvocationHandler参数穿给了构造函数
            return cons.newInstance(new Object[]{h});
        } catch (IllegalAccessException|InstantiationException e) {
            throw new InternalError(e.toString(), e);
        } catch (InvocationTargetException e) {
            Throwable t = e.getCause();
            if (t instanceof RuntimeException) {
                throw (RuntimeException) t;
            } else {
                throw new InternalError(t.toString(), t);
            }
        } catch (NoSuchMethodException e) {
            throw new InternalError(e.toString(), e);
        }
    }
```

以上就是jdk动态代理的内部实现过程，最后再次将上面的原理声明一遍，强化记忆
1.拿到被代理对象的引用，然后获取他的接口 (Proxy.getInstance方法)
2.JDK代理重新生成一个类，同时实现我们给的代理对象所实现的接口 （上面的反编译文件中实现了同样的接口）
3.把被代理对象的引用拿到了（上面被代理对象中在静态代码块中通过反射获取到的信息，以及我们实现的JdkInvocationHandler中的target）
4.重新动态生成一个class字节码
5.然后编译

## 2.自己手写一个动态代理

（声明：本代码只用作实例，很多细节没有考虑进去，比如，多接口的代理类，Object类的其他默认方法的代理，为确保原汁原味，一些模板引擎和commons工具类也没有使用；觉得不足的老铁们可以随意完善，记得评论区留言完善方法哦）

我们使用jdk代理的类名和方法名定义，已经执行思路，但是所有的实现都自己来写；

首先先定义出类结构

```
/**
 * 自定义类加载器
 * @author gaowenfeng
 * @date 2018/3/30
 */
public class MyClassLoader extends ClassLoader {

    /**
     * 通过类名称加载类字节码文件到JVM中
     * @param name 类名
     * @return 类的Class独享
     * @throws ClassNotFoundException
     */
    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        return super.findClass(name);
    }
}
/**
 * @desc 自己实现的代理类，用来生成字节码文件，并动态加载到JVM中
 * @author gaowenfeng
 * @date 2018/3/30
 */
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
            throws IllegalArgumentException{
        /**
         * 1.生成代理类的源代码
         * 2.将生成的源代码输出到磁盘，保存为.java文件
         * 3.编译源代码，并生成.java文件
         * 4.将class文件中的内容，动态加载到JVM中
         * 5.返回被代理后的代理对象
         */

        return null;

    }
}
/**
 * 自定义类加载器
 * @author gaowenfeng
 * @date 2018/3/30
 */
public class MyClassLoader extends ClassLoader {

    /**
     * 通过类名称加载类字节码文件到JVM中
     * @param name 类名
     * @return 类的Class独享
     * @throws ClassNotFoundException
     */
    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        return super.findClass(name);
    }
}
/**
 * @author gaowenfeng
 * @date 2018/3/30
 */
public class CustomInvocationHandler implements MyInvocationHandler {
    private ProductService target;

    public Object getInstance(ProductService target){
        this.target = target;
        Class clazz = this.target.getClass();
        // 参数1：被代理类的类加载器 参数2:被代理类的接口 参数3
        // 这里的MyClassLoader先用new的方式保证编译不报错，后面会修改
        return MyProxy.newProxyInstance(new MyClassLoader(),
                clazz.getInterfaces(),
                this);
    }
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String currentDate  = simpleDateFormat.format(new Date());
        System.out.println("日期【"+currentDate + "】添加了一款产品");

        return method.invoke(this.target,args);
    }
}
```

## 接下来我们来按照步骤一步一步的完善我们的类

生成代理类的源文件

```
/**
     * 生成代理类的源代码
     * @return
     */
    private static String genSesource(Class<?> interfaces){
        StringBuilder src = new StringBuilder();
        src.append("package com.gwf.custom;").append(ln)
                .append("import java.lang.reflect.Method;").append(ln)
                .append("public class $Proxy0 implements ").append(interfaces.getName()).append("{").append(ln)
                .append("private MyInvocationHandler h;").append(ln)
                .append("public $Proxy0(MyInvocationHandler h){").append(ln)
                .append("this.h=h;").append(ln)
                .append("}").append(ln);

        for(Method method:interfaces.getMethods()){
            src.append("public ").append(method.getReturnType()).append(" ").append(method.getName()).append("() {").append(ln)
                    .append("try {").append(ln)
                    .append("Method m = ").append(interfaces.getName()).append(".class.getMethod(\"").append(method.getName()).append("\");").append(ln)
                    .append("this.h.invoke(this, m, new Object[]{});").append(ln)
                    .append("}catch (Throwable e){").append(ln)
                    .append("e.printStackTrace();").append(ln)
                    .append("}").append(ln)
                    .append("}").append(ln);
        }
        src.append("}");

        return src.toString();

    }
```

2.将源文件保存到本地

```
// 1.生成代理类的源代码
            String src = genSesource(interfaces);
            // 2.将生成的源代码输出到磁盘，保存为.java文件
            String path = MyProxy.class.getResource("").getPath();
            File file = new File(path+"$Proxy0.java");

            FileWriter fw = new FileWriter(file);
            fw.write(src);
            fw.close();
```

3.编译源代码，并生成.java文件

```
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
            manager.close();
```

4.将class文件中的内容，动态加载到JVM中

```
public class MyClassLoader extends ClassLoader {

    private String baseDir;

    public MyClassLoader(){
        this.baseDir = MyClassLoader.class.getResource("").getPath();
    }

    /**
     * 通过类名称加载类字节码文件到JVM中
     * @param name 类名
     * @return 类的Class独享
     * @throws ClassNotFoundException
     */
    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        // 获取类名
        String className = MyClassLoader.class.getPackage().getName()+"."+name;
        if(null == baseDir) {
            throw new ClassNotFoundException();
        }

        // 获取类文件
        File file = new File(baseDir,name+".class");
        if(!file.exists()){
            throw new ClassNotFoundException();
        }

        // 将类文件转换为字节数组
        try(
        FileInputStream in = new FileInputStream(file);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ){
            byte[] buffer = new byte[1024];
            int len;
            while ((len = in.read(buffer))!=-1){
                out.write(buffer,0,len);
            }

            // 调用父类方法生成class实例
            return defineClass(className,out.toByteArray(),0,out.size());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
```

5.返回被代理后的代理对象

```
Constructor c = proxyClass.getConstructor(MyInvocationHandler.class);
            return c.newInstance(h);
```

最后看一下总体的MyProxy类 的 newProxyInstance方法

```
public static Object newProxyInstance(MyClassLoader loader,
                                          Class<?> interfaces,
                                          MyInvocationHandler h)
            throws IllegalArgumentException{
        /**
         * 1.生成代理类的源代码
         * 2.将生成的源代码输出到磁盘，保存为.java文件
         * 3.编译源代码，并生成.java文件
         * 4.将class文件中的内容，动态加载到JVM中
         * 5.返回被代理后的代理对象
         */
        try {
            // 1.生成代理类的源代码
            String src = genSesource(interfaces);
            // 2.将生成的源代码输出到磁盘，保存为.java文件
            String path = MyProxy.class.getResource("").getPath();
            File file = new File(path+"$Proxy0.java");

            FileWriter fw = new FileWriter(file);
            fw.write(src);
            fw.close();

            // 3.编译源代码，并生成.java文件
            JavaCompiler javaCompiler = ToolProvider.getSystemJavaCompiler();
            StandardJavaFileManager manager = javaCompiler.getStandardFileManager(null,null,null);
            Iterable iterable = manager.getJavaFileObjects(file);

            JavaCompiler.CompilationTask task = javaCompiler.getTask(null,manager,null,null,null,iterable);
            task.call();
            manager.close();

            // 4.将class文件中的内容，动态加载到JVM中
            Class proxyClass = loader.findClass("$Proxy0");

            // 5.返回被代理后的代理对象
            Constructor c = proxyClass.getConstructor(MyInvocationHandler.class);
            return c.newInstance(h);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;

    }
```

## 激动人心的时刻：测试运行

```
public class CustomClient {
    public static void main(String[] args){
        ProductService productService = new ProductServiceImpl();
        ProductService proxy = (ProductService) new CustomInvocationHandler().getInstance(productService);
        proxy.addProduct();
    }
}
```

运行结果

```
日期【2018-03-30】添加了一款产品
正在添加iphone

Process finished with exit code 0
```

总结：以上通过理解jdk动态代理的原理，自己手写了一个动态代理，里面涉及到的重点主要是代理类字节码的生成（这里采用通过反射强行生成源文件并编译的方法，其实应该可以直接生成字节码文件的，有兴趣的同学可以尝试）和将生成的类动态加载到JVM中（本次试验由于测试，比较简单，直接将类名硬编码到了系统里，正常应该是自动加载）