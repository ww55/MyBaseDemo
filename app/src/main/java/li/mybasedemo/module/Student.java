package li.mybasedemo.module;

/**
 * 创建时间: 2017/12/4
 * 创建人: Administrator
 * 功能描述:
 */

public class Student {
    private String name;
    private int age;

    public String getName() {
        return name;
    }

    public Student(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
