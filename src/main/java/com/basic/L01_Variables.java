// package：声明这个文件属于哪个"文件夹"（包 = 目录，用点号分隔）
// 这个文件位于 src/main/java/com/basic/ 目录下
package com.basic;

/**
 * 第 1 课：变量与数据类型
 *
 * ========== 怎么运行这个文件？ ==========
 * 方法 1（推荐）：在 VS Code 中打开本文件，
 *   main 方法上方会出现 "Run | Debug" 两个小字，点击 "Run" 即可运行
 * 方法 2：右键编辑器空白处 → "Run Java"
 * 运行结果会显示在下方的 TERMINAL（终端）面板中
 *
 * ========== main 方法是什么？ ==========
 * JS 里直接写 console.log("hello") 保存后 node xxx.js 就能跑
 * Java 不允许"游离的代码"，所有代码必须写在类里，
 * 程序从固定的 main 方法开始执行，格式必须一字不差：
 *   public static void main(String[] args)
 * 现在不用理解每个单词，先记住这是"程序入口"的固定写法
 */
public class L01_Variables {

    public static void main(String[] args) {

        // ========== 1. 变量声明：Java 必须写清类型（和 JS 最大的区别） ==========

        // JS:   let age = 25;        （类型随便变）
        // Java: 类型 变量名 = 值;      （类型一旦确定，终身不变）
        int age = 25;                // int = 整数

        // JS:   let price = 9.9;
        double price = 9.9;          // double = 小数

        // JS:   let name = "张三";
        String name = "张三";         // String = 字符串（注意大写 S，必须用双引号）

        // JS:   let isStudent = true;
        boolean isStudent = true;    // boolean = 布尔，只有 true / false

        // ========== 2. 输出：console.log → System.out.println ==========

        System.out.println(name);    // 打印并换行
        System.out.println(age);

        // 字符串拼接：和 JS 一样用 +
        // JS:    console.log("我叫" + name + "，今年" + age + "岁");
        System.out.println("我叫" + name + "，今年" + age + "岁");

        // ========== 3. 强类型演示：类型不匹配会直接报错 ==========

        // age = "二十五";
        // ↑ 把上面这行的注释去掉试试：代码立刻标红，根本运行不了
        // JS 里 age 可以从数字变成字符串，Java 绝对不允许
        // 这就是"强类型"：编译阶段就帮你抓住类型错误

        // ========== 4. 常量：final ≈ JS 的 const ==========

        // JS:   const PI = 3.14;
        final double PI = 3.14;
        // PI = 3.15;   // 去掉注释试试：标红，常量不可修改

        // ========== 5. 变量运算（和 JS 一样） ==========

        int a = 10;
        int b = 3;
        System.out.println(a + b);   // 13
        System.out.println(a - b);   // 7
        System.out.println(a * b);   // 30
        System.out.println(a / b);   // 3  ← 注意！整数÷整数=整数，小数部分被丢弃
        System.out.println(a % b);   // 1  ← 取余数

        // 想得到小数结果，至少一边是小数：
        System.out.println(a / 3.0); // 3.3333...

        // ========== 6. 自增自减（和 JS 一样） ==========

        int count = 0;
        count++;              // 等价于 count = count + 1
        System.out.println(count);  // 1

        // ========== 本课小结 ==========
        // JS 概念          →  Java 对应
        // let age = 25     →  int age = 25;（必须先声明类型）
        // const PI = 3.14  →  final double PI = 3.14;
        // console.log()    →  System.out.println()
        // node 直接跑       →  必须包在类 + main 方法里
    }
}
