# 二、架构师内功心法之设计模式

## 2.架构师内功心法之设计模式

### 2.1.目标

1、通过对本章内容的学习，了解设计模式的由来。

2、介绍设计模式能帮我们解决哪些问题。

3、剖析工厂模式的历史由来及应用场景。

### 2.2.内容定位

不用设计模式并非不可以，但是用好设计模式能帮助我们更好地解决实际问题，设计模式最重要的是解耦。设计模式天天都在用，但自己却无感知。我们把设计模式作为一个专题，主要是学习设计模式是如何总结经验的，把经验为自己所用。学设计模式也是锻炼将业务需求转换技术实现的一种非常有效的方式。

### 2.3.回顾软件设计原则

| 设计原则     | 解释                                                         |
| ------------ | ------------------------------------------------------------ |
| 开闭原则     | 对扩展开放，对修改关闭                                       |
| 依赖倒置原则 | 通过抽象使各个类或者模块不相互影响，实现松耦合。             |
| 单一职责原则 | 一个类、接口、方法只做一件事。                               |
| 接口隔离原则 | 尽量保证接口的纯洁性，客户端不应该依赖不需要的接口。         |
| 迪米特法则   | 又叫最少知道原则，一个类对其所依赖的类知道得越少越好。       |
| 里氏替换原则 | 子类可以扩展父类的功能但不能改变父类原有的功能。             |
| 合成复用原则 | 尽量使用对象组合、聚合，而不使用继承关系达到代码复用的目的。 |

### 2.4.设计模式总览

写出优雅的代码

更好地重构项目

经典框架都在用设计模式解决问题

Spring就是一个把设计模式用得淋漓尽致的经典框架，其实从类的命名就能看出来，我来一一列举：

| 设计模式名称 | 举例                  |
| ------------ | --------------------- |
| 工厂模式     | BeanFactory           |
| 装饰器模式   | BeanWrapper           |
| 代理模式     | AopProxy              |
| 委派模式     | DispatcherServlet     |
| 策略模式     | HandlerMapping        |
| 适配器模式   | HandlerAdapter        |
| 模板模式     | JdbcTemplate          |
| 观察者模式   | ContextLoaderListener |

围绕 Spring 的 IOC、AOP、MVC、JDBC
这样的思路展开，根据其设计类型来设计讲解顺序：

| 类型       | 名称       | 英文              |
| ---------- | ---------- | ----------------- |
| 创建型模式 | 工厂模式   | Factory Pattern   |
|            | 单例模式   | Singleton Pattern |
|            | 原型模式   | Prototype Pattern |
| 结构型模式 | 适配器模式 | Adapter Pattern   |
|            | 装饰器模式 | Decorator Patter  |
|            | 代理模式   | Proxy Pattern     |
| 行为性模式 | 策略模式   | Strategy Pattern  |
|            | 模板模式   | Template Pattern  |
|            | 委派模式   | Delegate Pattern  |
|            | 观察者模式 | Observer Pattern  |

## 3.工厂模式详解

### 3.1.工厂模式的历史由来

原始社会自给自足（没有工厂）、农耕社会小作坊（**简单工厂**，民间酒
坊）、工业革命流水线(**工厂方法**，自产自销)、现代产业链代工厂(**抽象工厂**，富士康)

### 3.2.简单工厂模式

#### 3.2.1.定义

**简单工厂模式（Simple Factory Pattern）**是指由一个工厂对象决定创建出哪一种产品类的实例，
但它不属于GOF 23种设计模式。简单工厂适用于工厂类负责创建的对象较少的场景，且客户端只需要
传入工厂类的参数，对于如何创建对象的逻辑不需要关心。

#### 3.2.2.demo

```
public class SimpleFactoryTest {
    public static void main(String[] args) {
        CourseFactory factory = new CourseFactory();
        ICourse course = factory.create(JavaCourse.class);
        course.record();
    }
}

public class JavaCourse implements ICourse {
    public void record() {
        System.out.println("录制Java课程");
    }
}

public class CourseFactory {
    public ICourse create(Class<? extends ICourse> clazz){
        // 反射
        try {
            if (null != clazz) {
                return clazz.newInstance();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
```

![img](http://woshiamiaojiang.gitee.io/image-hosting/SimpleFactory.png)

- Calendar.getInstance()
- LoggerFactory.getLogger()

简单工厂模式在 JDK 源码也是无处不在，现在我们来举个例子，例如 Calendar 类，看
Calendar.getInstance()方法，下面打开的是Calendar的具体创建类：

```
    private static Calendar createCalendar(TimeZone zone,
                                           Locale aLocale)
    {
        CalendarProvider provider =
            LocaleProviderAdapter.getAdapter(CalendarProvider.class, aLocale)
                                 .getCalendarProvider();
        if (provider != null) {
            try {
                return provider.getInstance(zone, aLocale);
            } catch (IllegalArgumentException iae) {
                // fall back to the default instantiation
            }
        }

        Calendar cal = null;

        if (aLocale.hasExtensions()) {
            String caltype = aLocale.getUnicodeLocaleType("ca");
            if (caltype != null) {
                switch (caltype) {
                case "buddhist":
                cal = new BuddhistCalendar(zone, aLocale);
                    break;
                case "japanese":
                    cal = new JapaneseImperialCalendar(zone, aLocale);
                    break;
                case "gregory":
                    cal = new GregorianCalendar(zone, aLocale);
                    break;
                }
            }
        }
        if (cal == null) {
            // If no known calendar type is explicitly specified,
            // perform the traditional way to create a Calendar:
            // create a BuddhistCalendar for th_TH locale,
            // a JapaneseImperialCalendar for ja_JP_JP locale, or
            // a GregorianCalendar for any other locales.
            // NOTE: The language, country and variant strings are interned.
            if (aLocale.getLanguage() == "th" && aLocale.getCountry() == "TH") {
                cal = new BuddhistCalendar(zone, aLocale);
            } else if (aLocale.getVariant() == "JP" && aLocale.getLanguage() == "ja"
                       && aLocale.getCountry() == "JP") {
                cal = new JapaneseImperialCalendar(zone, aLocale);
            } else {
                cal = new GregorianCalendar(zone, aLocale);
            }
        }
        return cal;
    }
```

还有一个大家经常使用的 logback，我们可以看到 LoggerFactory 中有多个重载的方法
getLogger()：

```
    public static Logger getLogger(String name) {
        ILoggerFactory iLoggerFactory = getILoggerFactory();
        return iLoggerFactory.getLogger(name);
    }

    public static Logger getLogger(Class clazz) {
        return getLogger(clazz.getName());
    }
```

#### 3.2.4.优缺点

- 优点
  - 简单
- 缺点
  - 工厂类的职责相对过重，不易于扩展过于复杂的产品结构。

### 3.3.工厂方法模式

#### 3.3.1.定义

**工厂方法模式（Factory Method Pattern**）是指定义一个创建对象的接口，但让实现这个接口的类
来决定实例化哪个类，工厂方法让类的实例化推迟到子类中进行。在工厂方法模式中用户只需要关心所
需产品对应的工厂，无须关心创建细节，而且加入新的产品符合开闭原则。

#### 3.3.2.demo

```
public class FactoryMethodTest {
    public static void main(String[] args) {
        // Python课程工厂
        ICourseFactory factory = new PythonCourseFactory();
        ICourse course = factory.create();
        course.record();

        // Java课程工厂
        factory = new JavaCourseFactory();
        course = factory.create();
        course.record();
    }
}

public class JavaCourseFactory implements ICourseFactory {
    public ICourse create() {
        return new JavaCourse();
    }
}

public interface ICourseFactory {
    ICourse create();
}

public class JavaCourse implements ICourse {
    public void record() {
        System.out.println("录制Java课程");
    }
}

public interface ICourse {
    void record();
}
```

![img](http://woshiamiaojiang.gitee.io/image-hosting/FactoryMethod.png)

ApplicationContext就是工厂方法模式

再来看看logback中工厂方法模式的应用，看看类图就OK了：

![img](http://woshiamiaojiang.gitee.io/image-hosting/FactoryMethod2.png)

- 工厂方法适用于以下场景：
  1. 创建对象需要大量重复的代码。
  2. 客户端（应用层）不依赖于产品类实例如何被创建、实现等细节。
  3. 一个类通过其子类来指定创建哪个对象。
- 工厂方法也有缺点：
  1. 类的个数容易过多，增加复杂度。
  2. 增加了系统的抽象性和理解难度。

### 3.4.抽象工厂模式

#### 3.4.1.定义

**抽象工厂模式（AbastractFactory Pattern）**是指提供一个创建一系列相关或相互依赖对象的接口，无须指定他们具体的类。客户端（应用层）不依赖于产品类实例如何被创建、实现等细节，强调的是一
系列相关的产品对象（属于同一产品族）一起使用创建对象需要大量重复的代码。需要提供一个产品类
的库，所有的产品以同样的接口出现，从而使客户端不依赖于具体实现。

讲解抽象工厂之前，我们要了解两个概念**产品等级结构**和**产品族**，看下面的图：

![image-20200225191403439](http://woshiamiaojiang.gitee.io/image-hosting/image-20200225191403439.png)

再看下面的这张图，最左侧的小房子我们就认为**具体的工厂**，有**美的工厂**，有**海信工厂**，有**格力工厂**。
**每个品牌的工厂**都生产**洗衣机**、**热水器**和**空调**。

![image-20200225191741688](http://woshiamiaojiang.gitee.io/image-hosting/image-20200225191741688.png)

```
public class AbstractFactoryTest {
    public static void main(String[] args) {
        JavaCourseFactory factory = new JavaCourseFactory();
        factory.createNote().edit();
        factory.createVideo().record();
    }
}

/**
 * 抽象工厂CourseFactory类：
 * 抽象工厂是用户的主入口
 * 在Spring中应用得最为广泛的一种设计模式
 * 易于扩展
 */
public abstract class CourseFactory {
    public void init(){
        System.out.println("初始化基础数据");
    }
    protected abstract INote createNote();
    protected abstract IVideo createVideo();
}

/**
 * 创建Java产品族的具体工厂JavaCourseFactory
 */
public class JavaCourseFactory extends CourseFactory {
    public INote createNote() {
        super.init();
        return new JavaNote();
    }
    public IVideo createVideo() {
        super.init();
        return new JavaVideo();
    }
}

/**
 * 创建Java产品族，Java视频JavaVideo类：Java视频
 */
public class JavaVideo implements IVideo {
    public void record() {
        System.out.println("录制Java视频");
    }
}

/**
 * 录播视频：IVideo接口
 */
public interface IVideo {
    void record();
}

/**
 * 扩展产品等级Java课堂笔记JavaNote类：Java笔记
 */
public class JavaNote implements INote {
    public void edit() {
        System.out.println("编写Java笔记");
    }
}

/**
 * 课堂笔记：INote接口
 */
public interface INote {
    void edit();
}

// 创建Python产品族的具体工厂PythonCourseFactory省略。。。
```

![img](http://woshiamiaojiang.gitee.io/image-hosting/AbstractFactory.png)

#### 3.4.3.源码

AbstractFactory

AnnotationApplicationContext

Xml

适合长时间不变动的场景

#### 3.4.3.优缺点

抽象工厂缺点

1. 规定了所有可能被创建的产品集合，产品族中扩展新的产品困难，需要修改抽象工厂的接口。
2. 增加了系统的抽象性和理解难度。

### 3.5.简单工厂 vs 工厂方法 vs 抽象工厂

简单工厂：产品的工厂

工厂方法：工厂的工厂

抽象工厂：复杂产品的工厂

简单工厂：工厂是一个实体类，内部直接根据逻辑创建对应的产品。

工厂方法：工厂首先有个接口定义规范。不同的产品使用不同的实体类工厂根据规范和需求创建对应的产品。这就是它们的区别。

工厂方法是生产一类产品，抽象工厂是生产一个产品族

### 3.6.作业

1、工厂类一定需要将构造方法私有化吗，为什么？

不一定。抽象工厂类就不能，否则父类的私有构造方法就不能被子类调用。
只有工厂需要单例的时候才需要私有化。

2、用工厂模式设计支付业务场景，包含跨境支付，支付宝、微信、银联支付，并画出类图。

```
/**
 * description: 支付接口
 */
public interface IPay {
    /**
     * 支付方法
     */
    void pay();
}

/**
 * description: 支付宝支付
 */
public class AliPay implements IPay {
    public void pay() {
        System.out.println("支付宝支付");
    }
}

/**
 * description: 微信支付
 */
public class WxPay implements IPay {
    public void pay() {
        System.out.println("微信支付");
    }
}

/**
 * description: 银联支付
 */
public class UniPay implements IPay {
    public void pay() {
        System.out.println("银联支付");
    }
}

/**
 * description: 苹果支付
 */
public class ApplePay implements IPay {
    public void pay() {
        System.out.println("苹果支付");
    }
}

/**
 * description: 支付抽象工厂
 */
public abstract class AbstractPayFactory {
    public void init() {
        System.out.println("初始化基础数据");
    }
}

/**
 * description: 国内支付
 */
public class ChinaPayFactory extends AbstractPayFactory {
    protected IPay createAliPay() {
        super.init();
        return new AliPay();
    }

    protected IPay createWxPay() {
        super.init();
        return new WxPay();
    }

    protected IPay createUniPay() {
        super.init();
        return new UniPay();
    }
}

/**
 * description: 国外支付
 */
public class ForeignPayFactory extends AbstractPayFactory {
    protected IPay createApplePay() {
        super.init();
        return new ApplePay();
    }
}

/**
 * description: 抽象工厂方法测试
 */
public class AbstractPayFactoryTest {
    public static void main(String[] args) {
        ChinaPayFactory chinaPayFactory = new ChinaPayFactory();
        chinaPayFactory.createAliPay().pay();
        chinaPayFactory.createWxPay().pay();
        chinaPayFactory.createUniPay().pay();

        ForeignPayFactory foreignPayFactory = new ForeignPayFactory();
        foreignPayFactory.createApplePay().pay();
    }
}
```

![img](http://woshiamiaojiang.gitee.io/image-hosting/FactoryPatternHomework.png)