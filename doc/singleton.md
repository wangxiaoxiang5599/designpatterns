## 三.单例模式详解

### 3.1.课程目标

1、掌握单例模式的应用场景。

2、掌握IDEA环境下的多线程调试方式。

3、掌握保证线程安全的单例模式策略。

4、掌握反射暴力攻击单例解决方案及原理分析。

5、序列化破坏单例的原理及解决方案。

6、掌握常见的单例模式写法。

### 3.2.内容定位

1、听说过单例模式，但不知道如何应用的人群。

2、单例模式是非常经典的高频面试题，希望通过面试单例彰显技术深度，顺利拿到Offer的人群。

### 3.3.单例模式的应用场景

单例模式（SingletonPattern）是指确保一个类在**任何情况下都绝对只有一个实例**，并提供**一个全局访问点**。单例模式是创建型模式。单例模式在现实生活中应用也非常广泛，例如，公司CEO、部门经
理 等 。 J2EE 标 准 中 的 **ServletContext** 、 **ServletContextConfig** 等 、 Spring 框 架 应 用 中 的
**ApplicationContext**、数据库的连接池**BDPool**等也都是单例形式。

### 3.4.饿汉式单例模式

#### 方法1.静态方法获得私有成员对象

```
/**
 * 优点：执行效率高，性能高，没有任何的锁
 * 缺点：某些情况下，可能会造成内存浪费
 */
public class HungrySingleton {
    //先静态、后动态 
    //先属性、后方法 
    //先上后下
    private static final HungrySingleton hungrySingleton = new HungrySingleton();

    private HungrySingleton(){}

    public static HungrySingleton getInstance(){
        return  hungrySingleton;
    }
}
```

#### 方法2.利用静态代码块与类同时加载的特性生成单例对象

```
//饿汉式静态块单例模式
public class HungryStaticSingleton {
    //先静态后动态
    //先上，后下
    //先属性后方法
    private static final HungryStaticSingleton hungrySingleton;

    //装个B
    static {
        hungrySingleton = new HungryStaticSingleton();
    }

    private HungryStaticSingleton(){}

    public static HungryStaticSingleton getInstance(){
        return  hungrySingleton;
    }
}
```

#### 类结构图

![img](https://gitee.com/woshiamiaojiang/image-hosting/raw/master/HungrySingleton.png)

优点：没有加任何锁、执行效率比较高，用户体验比懒汉式单例模式更好。

缺点：类加载的时候就初始化，不管用与不用都占着空间，浪费了内存，有可能“占着茅坑不拉屎”。

#### 源码

Spring中IoC容器ApplicationContext本身就是典型的饿汉式单例模式

### 3.5.懒汉式单例模式

#### 特点

懒汉式单例模式的特点是：被外部类调用的时候内部类才会加载。

#### 方法1.加大锁

```
/**
 * 优点：节省了内存,线程安全
 * 缺点：性能低
 */
//懒汉式单例模式在外部需要使用的时候才进行实例化
public class LazySimpleSingletion {
    private static LazySimpleSingletion instance;
    //静态块，公共内存区域 
    private LazySimpleSingletion(){}

    public synchronized static LazySimpleSingletion getInstance(){
        if(instance == null){
            instance = new LazySimpleSingletion();
        }
        return instance;
    }
}

public class ExectorThread implements Runnable {
    public void run() {
        LazySimpleSingletion instance = LazySimpleSingletion.getInstance();
        System.out.println(Thread.currentThread().getName() + ":" + instance);
    }
}

public class LazySimpleSingletonTest {
    public static void main(String[] args) {
        Thread t1 = new Thread(new ExectorThread());
        Thread t2 = new Thread(new ExectorThread());
        t1.start();
        t2.start();
        System.out.println("End");
    }
}
```

给getInstance()加上synchronized关键字，使这个方法变成线程同步方法：

当执行其中一个线程并调用getInstance()方法时，另一个线程在调用getInstance()
方法，线程的状态由 RUNNING 变成了 MONITOR，出现阻塞。直到第一个线程执行完，第二个线程
才恢复到RUNNING状态继续调用getInstance()方法

#### **线程切换调试**

![image-20200227132959169](https://gitee.com/woshiamiaojiang/image-hosting/raw/master/image-20200227132959169.png)

#### 方法2.双重检查锁

```
/**
 * 优点:性能高了，线程安全了
 * 缺点：可读性难度加大，不够优雅
 */
public class LazyDoubleCheckSingleton {
    // volatile解决指令重排序
    private volatile static LazyDoubleCheckSingleton instance;

    private LazyDoubleCheckSingleton() {
    }

    public static LazyDoubleCheckSingleton getInstance() {
        //检查是否要阻塞，第一个instance == null是为了创建后不再走synchronized代码，提高效率。可以理解是个开关。创建后这个开关就关上，后面的代码就不用执行了。
        if (instance == null) {
            synchronized (LazyDoubleCheckSingleton.class) {
                //检查是否要重新创建实例
                if (instance == null) {
                    instance = new LazyDoubleCheckSingleton();
                    //指令重排序的问题
                    //1.分配内存给这个对象 
                    //2.初始化对象
                    //3.设置 lazy 指向刚分配的内存地址
                }
            }
        }
        return instance;
    }
}

public class ExectorThread implements Runnable {
    public void run() {
        LazyDoubleCheckSingleton instance = LazyDoubleCheckSingleton.getInstance();
        System.out.println(Thread.currentThread().getName() + ":" + instance);
    }
}

public class LazySimpleSingletonTest {
    public static void main(String[] args) {
        Thread t1 = new Thread(new ExectorThread());
        Thread t2 = new Thread(new ExectorThread());
        t1.start();
        t2.start();
        System.out.println("End");
    }
}
```

当第一个线程调用 getInstance()方法时，第二个线程也可以调用。当第一个线程执行到
synchronized时会上锁，第二个线程就会变成 MONITOR状态，出现阻塞。此时，阻塞并不是基于整
个LazySimpleSingleton类的阻塞，而是在getInstance()方法内部的阻塞，只要逻辑不太复杂，对于
调用者而言感知不到。

但是，用到 synchronized 关键字总归要上锁，对程序性能还是存在一定影响的。难道就真的没有更好的方案吗？当然有。我们可以从类初始化的角度来考虑，看下面的代码，采用静态内部类的方式：

#### 方法3.静态内部类

```
/*
  ClassPath : LazyStaticInnerClassSingleton.class
              LazyStaticInnerClassSingleton$LazyHolder.class
   优点：写法优雅，利用了Java本身语法特点，性能高，避免了内存浪费,不能被反射破坏
   缺点：不优雅
 */
//这种形式兼顾饿汉式单例模式的内存浪费问题和 synchronized 的性能问题 
//完美地屏蔽了这两个缺点
//自认为史上最牛的单例模式的实现方式 
public class LazyStaticInnerClassSingleton {

    //使用 LazyInnerClassGeneral 的时候，默认会先初始化内部类 
    //如果没使用，则内部类是不加载的
    private LazyStaticInnerClassSingleton(){
        // if(LazyHolder.INSTANCE != null){
        //     throw new RuntimeException("不允许非法创建多个实例");
        // }
    }

    //每一个关键字都不是多余的，static 是为了使单例的空间共享，保证这个方法不会被重写、重载 
    private static LazyStaticInnerClassSingleton getInstance(){
        //在返回结果以前，一定会先加载内部类 
        return LazyHolder.INSTANCE;
    }

    //默认不加载 
    private static class LazyHolder{
        private static final LazyStaticInnerClassSingleton INSTANCE = new LazyStaticInnerClassSingleton();
    }
}
```

这种方式兼顾了饿汉式单例模式的内存浪费问题和 synchronized 的性能问题。内部类一定是要在方法调用之前初始化，巧妙地避免了线程安全问题。由于这种方式比较简单，我们就不带大家一步一步
调试了。

内部类语法特性 ： 内部类用时才加载

### 3.6.反射破坏单例

```
public class ReflectTest {

    public static void main(String[] args) {
        try {
            //在很无聊的情况下，进行破坏 
            Class<?> clazz = LazyStaticInnerClassSingleton.class;
            //通过反射获取私有的构造方法
            Constructor c = clazz.getDeclaredConstructor(null);
            //强制访问 
            c.setAccessible(true);
            //暴力初始化
            Object instance1 = c.newInstance();
            //调用了两次构造方法，相当于“new”了两次，犯了原则性错误 
            Object instance2 = c.newInstance();
            System.out.println(instance1);
            System.out.println(instance2);
            System.out.println(instance1 == instance2);
			// Enum
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}

com.gupaoedu.vip.pattern.singleton.lazy.LazyStaticInnerClassSingleton@64cee07
com.gupaoedu.vip.pattern.singleton.lazy.LazyStaticInnerClassSingleton@1761e840
false
```

大家有没有发现，上面介绍的单例模式的构造方法除了加上 private 关键字，没有做任何处理。如
果我们使用反射来调用其构造方法，再调用 getInstance()方法，应该有两个不同的实例。现在来看一
段测试代码，以LazyInnerClassSingleton为例：

显然，创建了两个不同的实例。现在，我们在其构造方法中做一些限制，一旦出现多次重复创建，
则直接抛出异常。所以需要在私有构造方法添加异常：

```
    private LazyStaticInnerClassSingleton(){
        if(LazyHolder.INSTANCE != null){
            throw new RuntimeException("不允许非法创建多个实例");
        }
    }
```

### 3.7.序列化破坏单例（扩展知识）

一个单例对象创建好后，有时候需要将对象序列化然后写入磁盘，下次使用时再从磁盘中读取对象
并进行反序列化，将其转化为内存对象。反序列化后的对象会重新分配内存，即重新创建。如果序列化
的目标对象为单例对象，就违背了单例模式的初衷，相当于破坏了单例，来看一段代码：

```
//反序列化导致破坏单例模式 
public class SeriableSingleton implements Serializable {
    //序列化
    //把内存中对象的状态转换为字节码的形式
    //把字节码通过IO输出流，写到磁盘上
    //永久保存下来，持久化
    
    //反序列化
    //将持久化的字节码内容，通过IO输入流读到内存中来
    //转化成一个Java对象
    
    // 饿汉式
    public  final static SeriableSingleton INSTANCE = new SeriableSingleton();
    private SeriableSingleton(){}
    public static SeriableSingleton getInstance(){
        return INSTANCE;
    }
    // private Object readResolve(){ return INSTANCE;}
}

public class SeriableSingletonTest {
    public static void main(String[] args) {
        SeriableSingleton s1 = null;
        SeriableSingleton s2 = SeriableSingleton.getInstance();
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream("SeriableSingleton.obj");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(s2);
            oos.flush();
            oos.close();
            FileInputStream fis = new FileInputStream("SeriableSingleton.obj");
            ObjectInputStream ois = new ObjectInputStream(fis);
            s1 = (SeriableSingleton)ois.readObject();
            ois.close();
            System.out.println(s1);
            System.out.println(s2);
            System.out.println(s1 == s2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

打印结果：
com.gupaoedu.vip.pattern.singleton.seriable.SeriableSingleton@68837a77
com.gupaoedu.vip.pattern.singleton.seriable.SeriableSingleton@4b6995df
false
```

从运行结果可以看出，反序列化后的对象和手动创建的对象是不一致的，实例化了两次，违背了单
例模式的设计初衷。那么，我们如何保证在序列化的情况下也能够实现单例模式呢？其实很简单，只需
要增加readResolve()方法即可。

再看运行结果，如下图所示。

```
com.gupaoedu.vip.pattern.singleton.seriable.SeriableSingleton@4b6995df
com.gupaoedu.vip.pattern.singleton.seriable.SeriableSingleton@4b6995df
true
```

大家一定会想：这是什么原因呢？为什么要这样写？看上去很神奇的样子，也让人有些费解。不如
我们一起来看看JDK的源码实现以了解清楚。我们进入ObjectInputStream类的readObject()方法，
代码如下：

```
public final Object readObject()
        throws IOException, ClassNotFoundException
    {
        if (enableOverride) {
            return readObjectOverride();
        }

        // if nested read, passHandle contains handle of enclosing object
        int outerHandle = passHandle;
        try {
            Object obj = readObject0(false);
            handles.markDependency(outerHandle, passHandle);
            ClassNotFoundException ex = handles.lookupException(passHandle);
            if (ex != null) {
                throw ex;
            }
            if (depth == 0) {
                vlist.doCallbacks();
            }
            return obj;
        } finally {
            passHandle = outerHandle;
            if (closed && depth == 0) {
                clear();
            }
        }
    }
```

我们发现，在readObject()方法中又调用了重写的readObject0()方法。进入readObject0()方法，
代码如下：

```
private Object readObject0(boolean unshared) throws IOException {
	...
    case TC_OBJECT:
    	return checkResolve(readOrdinaryObject(unshared));
    ...
}
```

我们看到TC_OBJECT中调用了ObjectInputStream的readOrdinaryObject()方法，看源码：

```
    private Object readOrdinaryObject(boolean unshared)
        throws IOException
    {
        if (bin.readByte() != TC_OBJECT) {
            throw new InternalError();
        }

        ObjectStreamClass desc = readClassDesc(false);
        desc.checkDeserialize();

        Class<?> cl = desc.forClass();
        if (cl == String.class || cl == Class.class
                || cl == ObjectStreamClass.class) {
            throw new InvalidClassException("invalid class descriptor");
        }

        Object obj;
        try {
            obj = desc.isInstantiable() ? desc.newInstance() : null;
        } catch (Exception ex) {
            throw (IOException) new InvalidClassException(
                desc.forClass().getName(),
                "unable to create instance").initCause(ex);
        }
		...

        return obj;
    }
```

我们发现调用了ObjectStreamClass的isInstantiable()方法，而isInstantiable()方法的代码如下：

```
    boolean isInstantiable() {
        requireInitialized();
        return (cons != null);
    }
```

上述代码非常简单，就是判断一下构造方法是否为空，构造方法不为空就返回true。这意味着只要
有无参构造方法就会实例化。

这时候其实还没有找到加上 readResolve()方法就避免了单例模式被破坏的真正原因。再回到
ObjectInputStream的readOrdinaryObject()方法，继续往下看：

```
    private Object readOrdinaryObject(boolean unshared)
        throws IOException
    {
        if (bin.readByte() != TC_OBJECT) {
            throw new InternalError();
        }

        ObjectStreamClass desc = readClassDesc(false);
        desc.checkDeserialize();

        Class<?> cl = desc.forClass();
        if (cl == String.class || cl == Class.class
                || cl == ObjectStreamClass.class) {
            throw new InvalidClassException("invalid class descriptor");
        }

        Object obj;
        try {
            obj = desc.isInstantiable() ? desc.newInstance() : null;
        } catch (Exception ex) {
            throw (IOException) new InvalidClassException(
                desc.forClass().getName(),
                "unable to create instance").initCause(ex);
        }

        ...
        if (obj != null &&
            handles.lookupException(passHandle) == null &&
            desc.hasReadResolveMethod())
        {
            Object rep = desc.invokeReadResolve(obj);
            if (unshared && rep.getClass().isArray()) {
                rep = cloneArray(rep);
            }
            if (rep != obj) {
                // Filter the replacement object
                if (rep != null) {
                    if (rep.getClass().isArray()) {
                        filterCheck(rep.getClass(), Array.getLength(rep));
                    } else {
                        filterCheck(rep.getClass(), -1);
                    }
                }
                handles.setObject(passHandle, obj = rep);
            }
        }

        return obj;
    }
```

判断无参构造方法是否存在之后，又调用了hasReadResolveMethod()方法，来看代码：

```
    boolean hasReadResolveMethod() {
        requireInitialized();
        return (readResolveMethod != null);
    }
```

上述代码逻辑非常简单，就是判断 readResolveMethod 是否为空，不为空就返回 true。那么
readResolveMethod是在哪里赋值的呢？通过全局查找知道，在私有方法 ObjectStreamClass()中给
readResolveMethod进行了赋值，来看代码：

```
    private final void requireInitialized() {
        if (!initialized)
            throw new InternalError("Unexpected call when not initialized");
    }
```

上面的逻辑其实就是通过反射找到一个无参的 readResolve()方法，并且保存下来。现在回到
ObjectInputStream 的 readOrdinaryObject()方法继续往下看，如果 readResolve()方法存在则调用
invokeReadResolve()方法，来看代码：

```
    Object invokeReadResolve(Object obj)
        throws IOException, UnsupportedOperationException
    {
        requireInitialized();
        if (readResolveMethod != null) {
            try {
                return readResolveMethod.invoke(obj, (Object[]) null);
            } catch (InvocationTargetException ex) {
                Throwable th = ex.getTargetException();
                if (th instanceof ObjectStreamException) {
                    throw (ObjectStreamException) th;
                } else {
                    throwMiscException(th);
                    throw new InternalError(th);  // never reached
                }
            } catch (IllegalAccessException ex) {
                // should not occur, as access checks have been suppressed
                throw new InternalError(ex);
            }
        } else {
            throw new UnsupportedOperationException();
        }
    }
```

我们可以看到，在invokeReadResolve()方法中用反射调用了readResolveMethod方法。

通过JDK源码分析我们可以看出，虽然增加 readResolve()方法返回实例解决了单例模式被破坏的
问题，但是实际上实例化了两次，只不过新创建的对象没有被返回而已。如果创建对象的动作发生频率加快，就意味着内存分配开销也会随之增大，难道真的就没办法从根本上解决问题吗？下面讲的注册式单例也许能帮助到你。

> 为什么添加了**readResolve()**方法就可以了?
>
> ObjectInputStream源码中，读取文件时写死判断是否有readResolve()方法，有调用这个方法，没有则重新创建对象。

### 3.8.注册式单例模式

**将每一个实例都缓存到统一的容器中，使用唯一表示获取实例。**

注册式单例模式又称为登记式单例模式，就是将每一个实例都登记到某一个地方，使用唯一的标识获取实例。注册式单例模式有两种：一种为**枚举式单例模式**，另一种为**容器式单例模式**。

#### 方法1. 枚举式单例模式

先来看枚举式单例模式的写法，来看代码，创建EnumSingleton类：

```
public enum EnumSingleton {
    INSTANCE;

    private Object data;

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public static EnumSingleton getInstance(){return INSTANCE;}
}
```

来看测试代码：

```
public class EnumSingletonTest {
    public static void main(String[] args) {
        try {
            EnumSingleton instance1 = null;
            EnumSingleton instance2 = EnumSingleton.getInstance();
            instance2.setData(new Object());
            FileOutputStream fos = new FileOutputStream("EnumSingleton.obj");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(instance2);
            oos.flush();
            oos.close();
            FileInputStream fis = new FileInputStream("EnumSingleton.obj");
            ObjectInputStream ois = new ObjectInputStream(fis);
            instance1 = (EnumSingleton) ois.readObject();
            ois.close();
            System.out.println(instance1.getData());
            System.out.println(instance2.getData());
            System.out.println(instance1.getData() == instance2.getData());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
java.lang.Object@2acf57e3
java.lang.Object@2acf57e3
true
```

没有做任何处理，我们发现运行结果和预期的一样。那么枚举式单例模式如此神奇，它的神秘之处
在哪里体现呢？下面通过分析源码来揭开它的神秘面纱。

下载一个非常好用的 Java反编译工具 Jad（下载地址：https://varaneckas.com/jad/），解压后
配置好环境变量（这里不做详细介绍），就可以使用命令行调用了。找到工程所在的Class目录，复制
EnumSingleton.class 所在的路径，如下图所示。

然后切换到命令行，切换到工程所在的Class目录，输入命令 jad 并在后面输入复制好的路径，在
Class 目录下会多出一个 EnumSingleton.jad 文件。打开 EnumSingleton.jad 文件我们惊奇地发现有
如下代码：

```
static { 
    INSTANCE = new EnumSingleton("INSTANCE", 0); 
    $VALUES = (new EnumSingleton[] { 
        INSTANCE 
    }); 
}
```

原来，枚举式单例模式在静态代码块中就给INSTANCE进行了赋值，是饿汉式单例模式的实现。至
此，我们还可以试想，序列化能否破坏枚举式单例模式呢？不妨再来看一下 JDK 源码，还是回到
ObjectInputStream的readObject0()方法：

```
    private Object readObject0(boolean unshared) throws IOException {
        ...
        case TC_ENUM:
        	return checkResolve(readEnum(unshared));
        ...
    }
```

我们看到，在readObject0()中调用了readEnum()方法，来看readEnum()方法的代码实现：

```
    private Enum<?> readEnum(boolean unshared) throws IOException {
        if (bin.readByte() != TC_ENUM) {
            throw new InternalError();
        }

        ObjectStreamClass desc = readClassDesc(false);
        if (!desc.isEnum()) {
            throw new InvalidClassException("non-enum class: " + desc);
        }

        int enumHandle = handles.assign(unshared ? unsharedMarker : null);
        ClassNotFoundException resolveEx = desc.getResolveException();
        if (resolveEx != null) {
            handles.markException(enumHandle, resolveEx);
        }

        String name = readString(false);
        Enum<?> result = null;
        Class<?> cl = desc.forClass();
        if (cl != null) {
            try {
                @SuppressWarnings("unchecked")
                Enum<?> en = Enum.valueOf((Class)cl, name);
                result = en;
            } catch (IllegalArgumentException ex) {
                throw (IOException) new InvalidObjectException(
                    "enum constant " + name + " does not exist in " +
                    cl).initCause(ex);
            }
            if (!unshared) {
                handles.setObject(enumHandle, result);
            }
        }

        handles.finish(enumHandle);
        passHandle = enumHandle;
        return result;
    }
```

我们发现，枚举类型其实通过类名和类对象类找到一个唯一的枚举对象。因此，枚举对象不可能被
类加载器加载多次。那么反射是否能破坏枚举式单例模式呢？来看一段测试代码：

```
    public static void main(String[] args) {
        try {
            Class clazz = EnumSingleton.class;
            Constructor c = clazz.getDeclaredConstructor();
            c.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
```

运行结果如下图所示。

![image-20200227191227392](https://gitee.com/woshiamiaojiang/image-hosting/raw/master/image-20200227191227392.png)

```
    protected Enum(String name, int ordinal) {
        this.name = name;
        this.ordinal = ordinal;
    }
```

我们再来做一个下面这样的测试：

```
    public static void main(String[] args) {
        try {
            Class clazz = EnumSingleton.class;
            Constructor c = clazz.getDeclaredConstructor(String.class, int.class);
            c.setAccessible(true);
            EnumSingleton enumSingleton = (EnumSingleton) c.newInstance("Tom", 666);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
```

运行结果如下图所示

![image-20200227191559008](https://gitee.com/woshiamiaojiang/image-hosting/raw/master/image-20200227191559008.png)

```
    @CallerSensitive
    public T newInstance(Object ... initargs)
        throws InstantiationException, IllegalAccessException,
               IllegalArgumentException, InvocationTargetException
    {
        if (!override) {
            if (!Reflection.quickCheckMemberAccess(clazz, modifiers)) {
                Class<?> caller = Reflection.getCallerClass();
                checkAccess(caller, clazz, null, modifiers);
            }
        }
        if ((clazz.getModifiers() & Modifier.ENUM) != 0)
            throw new IllegalArgumentException("Cannot reflectively create enum objects");
        ConstructorAccessor ca = constructorAccessor;   // read volatile
        if (ca == null) {
            ca = acquireConstructorAccessor();
        }
        @SuppressWarnings("unchecked")
        T inst = (T) ca.newInstance(initargs);
        return inst;
    }
```

从上述代码可以看到，在 newInstance()方法中做了强制性的判断，如果修饰符是Modifier.ENUM
枚举类型，则直接抛出异常。

到此为止，我们是不是已经非常清晰明了呢？枚举式单例模式也是《EffectiveJava》书中推荐的一种单例模式实现写法。JDK枚举的语法特殊性及反射也为枚举保驾护航，让枚举式单例模式成为一种比
较优雅的实现。

枚举源码

java.lang.Enum通过valueOf获得值

```
    public static <T extends Enum<T>> T valueOf(Class<T> enumType,
                                                String name) {
        T result = enumType.enumConstantDirectory().get(name);
        if (result != null)
            return result;
        if (name == null)
            throw new NullPointerException("Name is null");
        throw new IllegalArgumentException(
            "No enum constant " + enumType.getCanonicalName() + "." + name);
    }

    Map<String, T> enumConstantDirectory() {
        if (enumConstantDirectory == null) {
            T[] universe = getEnumConstantsShared();
            if (universe == null)
                throw new IllegalArgumentException(
                    getName() + " is not an enum type");
            Map<String, T> m = new HashMap<>(2 * universe.length);
            for (T constant : universe)
                m.put(((Enum<?>)constant).name(), constant);
            enumConstantDirectory = m;
        }
        return enumConstantDirectory;
    }
    private volatile transient Map<String, T> enumConstantDirectory = null;
```

**枚举模式的实例天然具有线程安全性，防止序列化与反射的特性。**

有点像饿汉式单例。创建时就将常量存放在map容器中。

优点：写法优雅。加载时就创建对象。线程安全。

缺点：不能大批量创建对象，否则会造成浪费。spring中不能使用它。

结论：如果不是特别重的对象，建议使用枚举单例模式，它是JVM天然的单例。

#### 方法2. 容器式单例

Spring改良枚举写出的改良方法：IOC容器

接下来看注册式单例模式的另一种写法，即容器式单例模式，创建ContainerSingleton类：

```
public class ContainerSingleton {

    private ContainerSingleton(){}

    private static Map<String,Object> ioc = new ConcurrentHashMap<String, Object>();

    public static Object getInstance(String className){
        Object instance = null;
        if(!ioc.containsKey(className)){
            try {
                instance = Class.forName(className).newInstance();
                ioc.put(className, instance);
            }catch (Exception e){
                e.printStackTrace();
            }
            return instance;
        }else{
            return ioc.get(className);
        }
    }
}
```

测试

```
public class ContainerSingletonTest {
    public static void main(String[] args) {
        Object instance1 = ContainerSingleton.getInstance("com.gupaoedu.vip.pattern.singleton.test.Pojo");
        Object instance2 = ContainerSingleton.getInstance("com.gupaoedu.vip.pattern.singleton.test.Pojo");
        System.out.println(instance1 == instance2);
    }
}
```

结果

```
true
```

容器式单例模式适用于实例非常多的情况，便于管理。但它是非线程安全的。到此，注册式单例模式介绍完毕。我们再来看看Spring中的容器式单例模式的实现代码：

```
public abstract class AbstractAutowireCapableBeanFactory extends AbstractBeanFactory
		implements AutowireCapableBeanFactory {
    
	/** Cache of unfinished FactoryBean instances: FactoryBean name --> BeanWrapper */
	private final Map<String, BeanWrapper> factoryBeanInstanceCache =
			new ConcurrentHashMap<String, BeanWrapper>(16);
}
```

容器为啥不能被反射破坏？秩序的维护者，创造了一个生态

### 3.9.线程单例实现ThreadLocal

最后赠送给大家一个彩蛋，讲讲线程单例实现 ThreadLocal。ThreadLocal 不能保证其创建的对象
是全局唯一的，但是能保证在单个线程中是唯一的，天生是线程安全的。下面来看代码：

```
public class ThreadLocalSingleton {
    private static final ThreadLocal<ThreadLocalSingleton> threadLocaLInstance =
            new ThreadLocal<ThreadLocalSingleton>(){
                @Override
                protected ThreadLocalSingleton initialValue() {
                    return new ThreadLocalSingleton();
                }
            };

    private ThreadLocalSingleton(){}

    public static ThreadLocalSingleton getInstance(){
        return threadLocaLInstance.get();
    }
}
```

写一下测试代码：

```
public class ThreadLocalSingletonTest {

    public static void main(String[] args) {
        System.out.println(ThreadLocalSingleton.getInstance());
        System.out.println(ThreadLocalSingleton.getInstance());
        System.out.println(ThreadLocalSingleton.getInstance());
        System.out.println(ThreadLocalSingleton.getInstance());
        System.out.println(ThreadLocalSingleton.getInstance());
        Thread t1 = new Thread(new ExectorThread());
        Thread t2 = new Thread(new ExectorThread());
        t1.start();
        t2.start();
        System.out.println("End");
    }
}
```

运行结果如下图所示。

```
com.gupaoedu.vip.pattern.singleton.threadlocal.ThreadLocalSingleton@1761e840
com.gupaoedu.vip.pattern.singleton.threadlocal.ThreadLocalSingleton@1761e840
com.gupaoedu.vip.pattern.singleton.threadlocal.ThreadLocalSingleton@1761e840
com.gupaoedu.vip.pattern.singleton.threadlocal.ThreadLocalSingleton@1761e840
com.gupaoedu.vip.pattern.singleton.threadlocal.ThreadLocalSingleton@1761e840
End
Thread-0:com.gupaoedu.vip.pattern.singleton.lazy.LazyDoubleCheckSingleton@551f86f1
Thread-1:com.gupaoedu.vip.pattern.singleton.lazy.LazyDoubleCheckSingleton@551f86f1
```

我们发现，在主线程中无论调用多少次，获取到的实例都是同一个，都在两个子线程中分别获取到
了不同的实例。那么 ThreadLocal是如何实现这样的效果的呢？我们知道，单例模式为了达到线程安全
的目的，会给方法上锁，以时间换空间。ThreadLocal 将所有的对象全部放在 ThreadLocalMap 中，为每个线程都提供一个对象，实际上是以空间换时间来实现线程隔离的。

**不是线程作为key，而是threadlocal本身。**

**ThreadLocal源码**

```
public T get() {
    Thread t = Thread.currentThread();
    ThreadLocalMap map = getMap(t);
    if (map != null) {
        ThreadLocalMap.Entry e = map.getEntry(this);
        if (e != null) {
            @SuppressWarnings("unchecked")
            T result = (T)e.value;
            return result;
        }
    }
    return setInitialValue();
}
```

### 4.0.源码

AbstractFactoryBean

```
	public final T getObject() throws Exception {
		if (isSingleton()) {
			return (this.initialized ? this.singletonInstance : getEarlySingletonInstance());
		}
		else {
			return createInstance();
		}
	}

	private T getEarlySingletonInstance() throws Exception {
		Class[] ifcs = getEarlySingletonInterfaces();
		if (ifcs == null) {
			throw new FactoryBeanNotInitializedException(
					getClass().getName() + " does not support circular references");
		}
		if (this.earlySingletonInstance == null) {
			this.earlySingletonInstance = (T) Proxy.newProxyInstance(
					this.beanClassLoader, ifcs, new EarlySingletonInvocationHandler());
		}
		return this.earlySingletonInstance;
	}
```

MyBatis的ErrorContext使用了ThreadLocal

```
public class ErrorContext {

  private static final ThreadLocal<ErrorContext> LOCAL = new ThreadLocal<>();

  private ErrorContext() {
  }

  public static ErrorContext instance() {
    ErrorContext context = LOCAL.get();
    if (context == null) {
      context = new ErrorContext();
      LOCAL.set(context);
    }
    return context;
  }
}
```

### 5.0.单例模式小结

**单例模式优点：**

1. 在内存中只有一个实例，减少了内存开销。
2. 可以避免资源的多重占用。
3. 设置全局访问点，严格控制访问。

**单例模式的缺点：**

1. 没有接口，扩展困难。
2. 如果要扩展单例对象，只有修改代码，没有其他途径。

**学习单例模式的知识重点总结**

1. 私有化构造器
2. 保证线程安全

单例模式可以保证内存里只有一个实例，减少了内存的开销，还可以避免对资源的多重占用。单例模式看起来非常简单，实现起来其实也非常简单，但是在面试中却是一个高频面试点。希望“小伙伴们”
通过本章的学习，对单例模式有了非常深刻的认识，在面试中彰显技术深度，提升核心竞争力，给面试
加分，顺利拿到录取通知（Offer）。

### 5.1.作业

1、解决容器式单例的线程安全问题。

两种方法：双重检查锁，利用ConcurrentHashMap#putIfAbsent()方法的原子性。

```
public class ContainerSingleton {

    private static Map<String, Object> ioc = new ConcurrentHashMap<String, Object>();

    private ContainerSingleton() {
        throw new RuntimeException("不可被实例化！");
    }

    // 方法一：双重检查锁
    public static Object getInstance(String className) {
        Object instance = null;
        if (!ioc.containsKey(className)) {
            synchronized (ContainerSingleton.class) {
                if (!ioc.containsKey(className)) {
                    try {
                        instance = Class.forName(className).newInstance();
                        ioc.put(className, instance);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return instance;
                } else {
                    return ioc.get(className);
                }
            }
        }
        return ioc.get(className);
    }

    // 方法二：利用ConcurrentHashMap#putIfAbsent()方法的原子性
    public static Object getInstance1(String className){
        Object instance = null;
        try {
            ioc.putIfAbsent(className, Class.forName(className).newInstance());
        }catch (Exception e){
            e.printStackTrace();
        }
        return ioc.get(className);
    }
}
```