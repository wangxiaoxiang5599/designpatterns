package com.test.wxx.prototype.deep;

import com.alibaba.fastjson.JSON;
import lombok.Data;

import java.io.*;
import java.util.List;

@Data
public class ConcretePrototype implements Cloneable, Serializable {
    private int age;
    private String name;
    private List<String> hobbies;

    @Override
    public ConcretePrototype clone() {
        try {
            return (ConcretePrototype)super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * IO序列化实现深度clone
     */
    /*public ConcretePrototype deepClone(){
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(this);
            ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
            ObjectInputStream ois = new ObjectInputStream(bis);
            return (ConcretePrototype)ois.readObject();
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }*/

    /**
     * jdk源码ArrayList方法
     * 利用底层数组赋值
     * 实现深度克隆
     */
    /*public ConcretePrototype deepClone(){
        try {
            ConcretePrototype concretePrototype = (ConcretePrototype) super.clone();
            concretePrototype.hobbies = (List)((ArrayList)concretePrototype.hobbies).clone();
            return concretePrototype;
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }*/

    /**
     * Json实现深度克隆
     * @return
     */
    public ConcretePrototype deepClone(){
        try {
            return JSON.parseObject(JSON.toJSONString(this), ConcretePrototype.class);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }



}
