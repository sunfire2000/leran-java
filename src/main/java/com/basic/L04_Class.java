package com.basic;

/**
 * 第 4 课：类与对象（使用篇）
 *
 * 本文件演示如何使用 Student 类，运行本文件的 main 方法。
 * 重点理解：
 *   1. new 出来的每个对象，字段各自独立
 *   2. static 成员，所有对象共享一份
 *   3. L03 留下的悬念：为什么 main 里直接调用的方法必须加 static
 */
public class L04_Class {

    public static void main(String[] args) {

        // ========== 1. new：按图纸造对象 ==========

        // JS:   const stu1 = new Student("张三", 20);
        // Java: 类型要写在变量前面
        Student stu1 = new Student("张三", 20);
        Student stu2 = new Student("李四", 17);

        // ========== 2. 每个对象的字段各自独立 ==========

        // stu1 和 stu2 是两个不同的对象，内存里各占一块地
        System.out.println(stu1.name);   // 张三
        System.out.println(stu2.name);   // 李四

        // 改 stu1 不影响 stu2
        stu1.age = 21;
        System.out.println(stu1.age);    // 21
        System.out.println(stu2.age);    // 还是 17

        // ========== 3. 实例方法：对象.方法名() ==========

        stu1.introduce();   // 我叫张三...
        stu2.introduce();   // 我叫李四...
        // 同一个 introduce 方法，谁调用它，this 就指向谁

        System.out.println("张三成年了吗：" + stu1.isAdult());   // true
        System.out.println("李四成年了吗：" + stu2.isAdult());   // false

        // ========== 4. static 成员：所有对象共享 ==========
        Student stu3 = new Student("王五", 100);
       
        // 上面 new 了 2 个学生，共享计数器变成了 2
        // 调用静态方法用"类名.方法名()"，不需要对象
        Student.showTotalCount();   // 当前共有 2 个学生

        // ========== 5. 揭晓 L03 的悬念：static 之谜 ==========
        //
        // main 方法是 static 的 → 它执行时"没有任何对象"，只有类
        // 所以 main 里直接写 add(3, 5)，这个 add 必须也是 static（同样属于类）
        // 如果 add 不加 static，它就成了实例方法，必须先 new 一个对象才能调用
        //
        // 试试：把 Student.java 里 introduce() 的调用改成
        //   Student.introduce();   ← 编译报错！实例方法不能用类名调用
        //   stu1.introduce();      ← 正确：通过对象调用

        // ========== 6. 对照你的项目 ==========
        //
        // 你项目里的 Person.java 就是和 Student 一样的类：
        //   字段（name、age...）+ 构造方法 + getter/setter
        //   PersonController 里 personService.create(person)
        //   就是"对象.方法名()"的调用方式
        //
        // 唯一的区别：项目里的对象大多不是你手动 new 的，
        // 而是 Spring 帮你 new 好再"注入"进来（这就是 IOC，后续课程讲）

        // ========== 本课小结 ==========
        // 概念          JS                          Java
        // 创建对象      new Student("张三", 20)      Student s = new Student("张三", 20);
        // 构造函数      constructor(name, age)       public Student(String name, int age)
        // this         指向会变（箭头函数/bind）      永远指向当前对象，很老实
        // 静态成员      static count                static int totalCount（一样）
        // 文件规则      随便放                       一个 public 类一个文件，同名
    }
}
