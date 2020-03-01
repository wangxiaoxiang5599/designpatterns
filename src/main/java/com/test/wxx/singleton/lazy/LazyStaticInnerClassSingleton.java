package com.test.wxx.singleton.lazy;

/**
 * 优点：写法优雅。利用java本身语法特点，性能高，避免内存浪费。
 * 缺点：能够被反射破坏
 */
public class LazyStaticInnerClassSingleton {

    private LazyStaticInnerClassSingleton(){}

    /**
     * 优点：防止反射破坏
     * 缺点：不优雅
     */
    /*private LazyStaticInnerClassSingleton(){
        if(LazyHolder.INSTANCE != null){
            throw new RuntimeException("不允许非法访问！");
        }
    }
*/
    public static LazyStaticInnerClassSingleton getInstance(){
        return LazyHolder.INSTANCE;
    }

    private static class LazyHolder{
        private static final LazyStaticInnerClassSingleton INSTANCE
                = new LazyStaticInnerClassSingleton();
    }
}
