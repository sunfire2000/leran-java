package com.basic;

/**
 * 第 3 课：方法（Method）
 *
 * JS 里函数可以写在水里的任何地方：
 *   function add(a, b) { return a + b; }
 *
 * Java 里方法必须写在类里面，而且必须声明：
 *   1. 返回值的类型（没有返回值就写 void）
 *   2. 每个参数的类型
 *
 * 运行方式：main 方法上方点击 "Run"
 */
public class L03_Method {

    public static void main(String[] args) {

        // ========== 1. 调用方法（和 JS 调用函数一样） ==========

        int result = add(3, 5);
        System.out.println("3 + 5 = " + result);

        // 方法返回值可以直接参与表达式
        System.out.println("结果翻倍：" + add(3, 5) * 2);

        // ========== 2. 调用无返回值的方法（void） ==========

        sayHello("张三");

        // ========== 3. 方法重载演示 ==========
        // 同一个名字 add，传不同类型的参数，Java 自动选对版本

        System.out.println(add(3, 5));          // 调用 int 版本 → 8
        System.out.println(add(3.5, 5.5));      // 调用 double 版本 → 9.0
        System.out.println(add(1, 2, 3));       // 调用三参数版本 → 6

        // ========== 4. 方法的返回值可以直接当值用 ==========

        int big = max(10, 20);
        System.out.println("较大值：" + big);

        // ========== 本课小结 ==========
        // JS 函数                →  Java 方法
        // function add(a, b)     →  static int add(int a, int b)
        // 参数没有类型            →  每个参数必须声明类型
        // 返回值可有可无          →  必须声明返回类型，没有就写 void
        // 没有重载（后面覆盖前面） →  重载：同名不同参数，自动选择
    }

    // ========== 方法的定义 ==========

    /**
     * 两数相加（int 版本）
     *
     * 方法定义的结构，逐词解释：
     *   public  —— 谁都能调用（权限修饰符，后续课程详解）
     *   static  —— 属于类本身，不需要 new 就能调用
     *              （现在先记住：main 是 static，它直接调用的方法也必须加 static）
     *   int     —— 返回值类型：这个方法算完会吐出一个整数
     *   add     —— 方法名
     *   (int a, int b) —— 参数列表：类型 + 名字，逗号分隔
     *
     * 对比 JS：function add(a, b) { return a + b; }
     */
    public static int add(int a, int b) {
        return a + b;    // return 把结果交还给调用者，同时方法结束
    }

    // ========== 方法重载（Overload）：Java 有，JS 没有！ ==========

    /**
     * 同名方法 add，但参数是 double —— 这是另一个独立的方法
     *
     * JS 里同名函数后面的会覆盖前面的；
     * Java 里允许同名方法共存，靠"参数列表"区分，这叫重载。
     * 调用时 Java 根据你传的参数类型自动选择最匹配的版本。
     */
    public static double add(double a, double b) {
        return a + b;
    }

    // 重载也可以参数个数不同
    public static int add(int a, int b, int c) {
        return a + b + c;
    }

    /**
     * 无返回值方法：返回类型写 void
     *
     * void = "什么都不返回"，类似 JS 里没有 return 的函数
     * 调用时不能接收结果：int x = sayHello("张三"); ← 编译报错
     */
    public static void sayHello(String name) {
        System.out.println("你好，" + name + "！");
        // void 方法可以不写 return；写了也只能是单独的 return;（提前结束方法）
    }

    /**
     * 练习示例：返回两个数中的较大值
     */
    public static int max(int a, int b) {
        if (a > b) {
            return a;
        }
        return b;    // if 不成立才走到这里
    }
}
