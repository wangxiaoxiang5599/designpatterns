package com.test.wxx.prototype.general;

public class ConcretePrototype implements IPrototype {

    private int age;

    private String name;

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ConcretePrototype clone() {
        ConcretePrototype clone = new ConcretePrototype();
        clone.setAge(this.age);
        clone.setName(this.name);
        return clone;
    }

    @Override
    public String toString() {
        return "ConcretePrototype{" +
                "age=" + age +
                ", name='" + name + '\'' +
                '}';
    }
}
