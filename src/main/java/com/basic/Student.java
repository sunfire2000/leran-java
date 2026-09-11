package com.basic;

/**
 * 第 4 课的"图纸"：Student 类
 *
 * ========== 类是什么？ ==========
 * 类 = 图纸 / 模板，对象 = 按图纸造出来的具体实物。
 *   Student 类      → 一张"学生"图纸
 *   new Student(...) → 按图纸造出一个具体的学生（对象）
 *
 * ========== Java 的硬性规定 ==========
 * 一个 .java 文件里只能有一个 public 类，且文件名必须和类名一致。
 * 所以这个类必须放在 Student.java 文件里。
 * （你的项目里 Person.java 装 Person 类，就是这个规则）
 *
 * 对比 JS：JS 的 class 可以写在任何文件里，文件和类没有绑定关系。
 */
public class Student {

    // ========== 1. 字段（成员变量）：每个对象各存一份 ==========

    // 没有 static → 实例字段，属于"每个对象自己"
    // 张三的 name 和李四的 name 互不影响
    String name;
    int age;

    // ========== 2. static 字段：全类共享一份 ==========

    // 有 static → 静态字段，属于"类本身"，所有对象共用
    // 用来统计一共创建了多少个学生
    static int totalCount = 0;

    // ========== 3. 构造方法：new 的时候自动执行 ==========

    /**
     * 构造方法的特点：
     *   - 方法名必须和类名完全一样
     *   - 没有返回类型（连 void 都不写）
     *   - 在 new Student(...) 时自动被调用
     *
     * 对比 JS：constructor(name, age) { this.name = name; }
     */
    public Student(String name, int age) {
        // this = "当前正在被创建的这个对象"
        // this.name 是对象的字段，name 是传进来的参数
        // Java 的 this 永远指向当前实例，不会像 JS 那样乱变（没有箭头函数/bind 的问题）
        this.name = name;
        this.age = age;

        // 每创建一个学生，共享计数器 +1
        totalCount++;
        this.privateMethod();
    }

    // ========== 4. 实例方法：必须通过对象调用 ==========

    /**
     * 没有 static → 实例方法
     * 调用方式：stu.introduce()（必须先有对象 stu）
     * 方法里的 this.name 就是"调用它的那个对象的 name"
     */
    public void introduce() {
        System.out.println("大家好，我叫" + this.name + "，今年" + this.age + "岁");
    }

    /**
     * 普通方法也可以有返回值和参数，和 L03 学的完全一样
     * 只是去掉了 static，变成了"要先 new 对象才能调用"
     */
    public boolean isAdult() {
        return this.age >= 18;
    }
    
    private void privateMethod() {
        System.out.println("This is a private method.");
    }

    // ========== 5. static 方法：属于类，通过类名直接调用 ==========

    /**
     * 有 static → 静态方法
     * 调用方式：Student.showTotalCount()（不需要 new，类名直接用）
     *
     * 注意：static 方法里不能使用 this（因为它不属于任何具体对象）
     */
    public static void showTotalCount() {
        System.out.println("当前共有 " + totalCount + " 个学生");
        new Student("temp", 0).privateMethod();

        // this.name  ← 如果写这行会编译报错：static 方法里没有"当前对象"
    }
}
