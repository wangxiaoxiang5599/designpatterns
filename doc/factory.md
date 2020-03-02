# �����ܹ�ʦ�ڹ��ķ�֮���ģʽ

## 2.�ܹ�ʦ�ڹ��ķ�֮���ģʽ

### 2.1.Ŀ��

1��ͨ���Ա������ݵ�ѧϰ���˽����ģʽ��������

2���������ģʽ�ܰ����ǽ����Щ���⡣

3����������ģʽ����ʷ������Ӧ�ó�����

### 2.2.���ݶ�λ

�������ģʽ���ǲ����ԣ������ú����ģʽ�ܰ������Ǹ��õؽ��ʵ�����⣬���ģʽ����Ҫ���ǽ�����ģʽ���춼���ã����Լ�ȴ�޸�֪�����ǰ����ģʽ��Ϊһ��ר�⣬��Ҫ��ѧϰ���ģʽ������ܽᾭ��ģ��Ѿ���Ϊ�Լ����á�ѧ���ģʽҲ�Ƕ�����ҵ������ת������ʵ�ֵ�һ�ַǳ���Ч�ķ�ʽ��

### 2.3.�ع�������ԭ��

| ���ԭ��     | ����                                                         |
| ------------ | ------------------------------------------------------------ |
| ����ԭ��     | ����չ���ţ����޸Ĺر�                                       |
| ��������ԭ�� | ͨ������ʹ���������ģ�鲻�໥Ӱ�죬ʵ������ϡ�             |
| ��һְ��ԭ�� | һ���ࡢ�ӿڡ�����ֻ��һ���¡�                               |
| �ӿڸ���ԭ�� | ������֤�ӿڵĴ����ԣ��ͻ��˲�Ӧ����������Ҫ�Ľӿڡ�         |
| �����ط���   | �ֽ�����֪��ԭ��һ�����������������֪����Խ��Խ�á�       |
| �����滻ԭ�� | ���������չ����Ĺ��ܵ����ܸı丸��ԭ�еĹ��ܡ�             |
| �ϳɸ���ԭ�� | ����ʹ�ö�����ϡ��ۺϣ�����ʹ�ü̳й�ϵ�ﵽ���븴�õ�Ŀ�ġ� |

### 2.4.���ģʽ����

д�����ŵĴ���

���õ��ع���Ŀ

�����ܶ��������ģʽ�������

Spring����һ�������ģʽ�õ����쾡�µľ����ܣ���ʵ������������ܿ�����������һһ�о٣�

| ���ģʽ���� | ����                  |
| ------------ | --------------------- |
| ����ģʽ     | BeanFactory           |
| װ����ģʽ   | BeanWrapper           |
| ����ģʽ     | AopProxy              |
| ί��ģʽ     | DispatcherServlet     |
| ����ģʽ     | HandlerMapping        |
| ������ģʽ   | HandlerAdapter        |
| ģ��ģʽ     | JdbcTemplate          |
| �۲���ģʽ   | ContextLoaderListener |

Χ�� Spring �� IOC��AOP��MVC��JDBC
������˼·չ���������������������ƽ���˳��

| ����       | ����       | Ӣ��              |
| ---------- | ---------- | ----------------- |
| ������ģʽ | ����ģʽ   | Factory Pattern   |
|            | ����ģʽ   | Singleton Pattern |
|            | ԭ��ģʽ   | Prototype Pattern |
| �ṹ��ģʽ | ������ģʽ | Adapter Pattern   |
|            | װ����ģʽ | Decorator Patter  |
|            | ����ģʽ   | Proxy Pattern     |
| ��Ϊ��ģʽ | ����ģʽ   | Strategy Pattern  |
|            | ģ��ģʽ   | Template Pattern  |
|            | ί��ģʽ   | Delegate Pattern  |
|            | �۲���ģʽ | Observer Pattern  |

## 3.����ģʽ���

### 3.1.����ģʽ����ʷ����

ԭʼ����Ը����㣨û�й�������ũ�����С������**�򵥹���**������
��������ҵ������ˮ��(**��������**���Բ�����)���ִ���ҵ��������(**���󹤳�**����ʿ��)

### 3.2.�򵥹���ģʽ

#### 3.2.1.����

**�򵥹���ģʽ��Simple Factory Pattern��**��ָ��һ���������������������һ�ֲ�Ʒ���ʵ����
����������GOF 23�����ģʽ���򵥹��������ڹ����ฺ�𴴽��Ķ�����ٵĳ������ҿͻ���ֻ��Ҫ
���빤����Ĳ�����������δ���������߼�����Ҫ���ġ�

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
        System.out.println("¼��Java�γ�");
    }
}

public class CourseFactory {
    public ICourse create(Class<? extends ICourse> clazz){
        // ����
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

�򵥹���ģʽ�� JDK Դ��Ҳ���޴����ڣ������������ٸ����ӣ����� Calendar �࣬��
Calendar.getInstance()����������򿪵���Calendar�ľ��崴���ࣺ

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

����һ����Ҿ���ʹ�õ� logback�����ǿ��Կ��� LoggerFactory ���ж�����صķ���
getLogger()��

```
    public static Logger getLogger(String name) {
        ILoggerFactory iLoggerFactory = getILoggerFactory();
        return iLoggerFactory.getLogger(name);
    }

    public static Logger getLogger(Class clazz) {
        return getLogger(clazz.getName());
    }
```

#### 3.2.4.��ȱ��

- �ŵ�
  - ��
- ȱ��
  - �������ְ����Թ��أ���������չ���ڸ��ӵĲ�Ʒ�ṹ��

### 3.3.��������ģʽ

#### 3.3.1.����

**��������ģʽ��Factory Method Pattern**����ָ����һ����������Ľӿڣ�����ʵ������ӿڵ���
������ʵ�����ĸ��࣬�������������ʵ�����Ƴٵ������н��С��ڹ�������ģʽ���û�ֻ��Ҫ������
���Ʒ��Ӧ�Ĺ�����������Ĵ���ϸ�ڣ����Ҽ����µĲ�Ʒ���Ͽ���ԭ��

#### 3.3.2.demo

```
public class FactoryMethodTest {
    public static void main(String[] args) {
        // Python�γ̹���
        ICourseFactory factory = new PythonCourseFactory();
        ICourse course = factory.create();
        course.record();

        // Java�γ̹���
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
        System.out.println("¼��Java�γ�");
    }
}

public interface ICourse {
    void record();
}
```

![img](http://woshiamiaojiang.gitee.io/image-hosting/FactoryMethod.png)

ApplicationContext���ǹ�������ģʽ

��������logback�й�������ģʽ��Ӧ�ã�������ͼ��OK�ˣ�

![img](http://woshiamiaojiang.gitee.io/image-hosting/FactoryMethod2.png)

- �����������������³�����
  1. ����������Ҫ�����ظ��Ĵ��롣
  2. �ͻ��ˣ�Ӧ�ò㣩�������ڲ�Ʒ��ʵ����α�������ʵ�ֵ�ϸ�ڡ�
  3. һ����ͨ����������ָ�������ĸ�����
- ��������Ҳ��ȱ�㣺
  1. ��ĸ������׹��࣬���Ӹ��Ӷȡ�
  2. ������ϵͳ�ĳ����Ժ�����Ѷȡ�

### 3.4.���󹤳�ģʽ

#### 3.4.1.����

**���󹤳�ģʽ��AbastractFactory Pattern��**��ָ�ṩһ������һϵ����ػ��໥��������Ľӿڣ�����ָ�����Ǿ�����ࡣ�ͻ��ˣ�Ӧ�ò㣩�������ڲ�Ʒ��ʵ����α�������ʵ�ֵ�ϸ�ڣ�ǿ������һ
ϵ����صĲ�Ʒ��������ͬһ��Ʒ�壩һ��ʹ�ô���������Ҫ�����ظ��Ĵ��롣��Ҫ�ṩһ����Ʒ��
�Ŀ⣬���еĲ�Ʒ��ͬ���Ľӿڳ��֣��Ӷ�ʹ�ͻ��˲������ھ���ʵ�֡�

������󹤳�֮ǰ������Ҫ�˽���������**��Ʒ�ȼ��ṹ**��**��Ʒ��**���������ͼ��

![image-20200225191403439](http://woshiamiaojiang.gitee.io/image-hosting/image-20200225191403439.png)

�ٿ����������ͼ��������С�������Ǿ���Ϊ**����Ĺ���**����**���Ĺ���**����**���Ź���**����**��������**��
**ÿ��Ʒ�ƵĹ���**������**ϴ�»�**��**��ˮ��**��**�յ�**��

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
 * ���󹤳�CourseFactory�ࣺ
 * ���󹤳����û��������
 * ��Spring��Ӧ�õ���Ϊ�㷺��һ�����ģʽ
 * ������չ
 */
public abstract class CourseFactory {
    public void init(){
        System.out.println("��ʼ����������");
    }
    protected abstract INote createNote();
    protected abstract IVideo createVideo();
}

/**
 * ����Java��Ʒ��ľ��幤��JavaCourseFactory
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
 * ����Java��Ʒ�壬Java��ƵJavaVideo�ࣺJava��Ƶ
 */
public class JavaVideo implements IVideo {
    public void record() {
        System.out.println("¼��Java��Ƶ");
    }
}

/**
 * ¼����Ƶ��IVideo�ӿ�
 */
public interface IVideo {
    void record();
}

/**
 * ��չ��Ʒ�ȼ�Java���ñʼ�JavaNote�ࣺJava�ʼ�
 */
public class JavaNote implements INote {
    public void edit() {
        System.out.println("��дJava�ʼ�");
    }
}

/**
 * ���ñʼǣ�INote�ӿ�
 */
public interface INote {
    void edit();
}

// ����Python��Ʒ��ľ��幤��PythonCourseFactoryʡ�ԡ�����
```

![img](http://woshiamiaojiang.gitee.io/image-hosting/AbstractFactory.png)

#### 3.4.3.Դ��

AbstractFactory

AnnotationApplicationContext

Xml

�ʺϳ�ʱ�䲻�䶯�ĳ���

#### 3.4.3.��ȱ��

���󹤳�ȱ��

1. �涨�����п��ܱ������Ĳ�Ʒ���ϣ���Ʒ������չ�µĲ�Ʒ���ѣ���Ҫ�޸ĳ��󹤳��Ľӿڡ�
2. ������ϵͳ�ĳ����Ժ�����Ѷȡ�

### 3.5.�򵥹��� vs �������� vs ���󹤳�

�򵥹�������Ʒ�Ĺ���

���������������Ĺ���

���󹤳������Ӳ�Ʒ�Ĺ���

�򵥹�����������һ��ʵ���࣬�ڲ�ֱ�Ӹ����߼�������Ӧ�Ĳ�Ʒ��

�������������������и��ӿڶ���淶����ͬ�Ĳ�Ʒʹ�ò�ͬ��ʵ���๤�����ݹ淶�����󴴽���Ӧ�Ĳ�Ʒ����������ǵ�����

��������������һ���Ʒ�����󹤳�������һ����Ʒ��

### 3.6.��ҵ

1��������һ����Ҫ�����췽��˽�л���Ϊʲô��

��һ�������󹤳���Ͳ��ܣ��������˽�й��췽���Ͳ��ܱ�������á�
ֻ�й�����Ҫ������ʱ�����Ҫ˽�л���

2���ù���ģʽ���֧��ҵ�񳡾��������羳֧����֧������΢�š�����֧������������ͼ��

```
/**
 * description: ֧���ӿ�
 */
public interface IPay {
    /**
     * ֧������
     */
    void pay();
}

/**
 * description: ֧����֧��
 */
public class AliPay implements IPay {
    public void pay() {
        System.out.println("֧����֧��");
    }
}

/**
 * description: ΢��֧��
 */
public class WxPay implements IPay {
    public void pay() {
        System.out.println("΢��֧��");
    }
}

/**
 * description: ����֧��
 */
public class UniPay implements IPay {
    public void pay() {
        System.out.println("����֧��");
    }
}

/**
 * description: ƻ��֧��
 */
public class ApplePay implements IPay {
    public void pay() {
        System.out.println("ƻ��֧��");
    }
}

/**
 * description: ֧�����󹤳�
 */
public abstract class AbstractPayFactory {
    public void init() {
        System.out.println("��ʼ����������");
    }
}

/**
 * description: ����֧��
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
 * description: ����֧��
 */
public class ForeignPayFactory extends AbstractPayFactory {
    protected IPay createApplePay() {
        super.init();
        return new ApplePay();
    }
}

/**
 * description: ���󹤳���������
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