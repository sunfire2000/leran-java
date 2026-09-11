package com.basic;

/**
 * 第 5 课：字符串（String）
 *
 * 本课解开 Java 最经典的坑：为什么字符串不能用 == 比较
 * 运行方式：main 方法上方点击 "Run"
 */
public class L05_String {

    public static void main(String[] args) {

        // ========== 1. 字符串不可变（Immutable）—— 和 JS 一样 ==========

        String s = "hello";
        s = s + " world";
        // 看起来像"修改"了 s，实际上是：
        //   创建了一个全新的字符串 "hello world"，让 s 指向它
        //   原来的 "hello" 还在内存里，没人用了，等 GC 回收
        // JS 的字符串同样不可变，这一条可以直接平移经验
        System.out.println(s);   // hello world

        // ========== 2. == 和 equals 的区别（本课重点！） ==========

        String a = "hello";              // 字面量写法
        String b = "hello";              // 字面量写法
        String c = new String("hello");  // new 出来的

        // 字面量有"字符串池"优化：内容相同的字面量只存一份
        // 所以 a 和 b 指向池子里同一个对象
        System.out.println(a == b);        // true（地址相同）
        System.out.println(a.equals(b));   // true（内容相同）

        // new String() 强制在池子外面新建一个对象，地址不同
        System.out.println(a == c);        // false！（地址不同）
        System.out.println(a.equals(c));   // true（内容相同）

        // ========== 结论（背下来） ==========
        // 比较字符串内容，永远用 equals()，永远不要用 ==
        // == 比较的是"是不是同一个对象"（内存地址）
        // equals 比较的是"内容是否一样"
        //
        // 错误示范（项目里真实出现过的 bug）：
        //   if (user.getStatus() == "ACTIVE") { ... }   ← 有时灵有时不灵，灾难
        // 正确写法：
        //   if ("ACTIVE".equals(user.getStatus())) { ... }
        //   技巧：把已知字符串放前面调用 equals，可以避免 null 指针异常

        // ========== 3. 常用方法（和 JS 对照表） ==========

        String str = "  Hello Java  ";

        // JS 属性 .length        → Java 是方法 .length()（带括号！）
        System.out.println(str.length());            // 14

        // JS trim()              → 一样，去掉首尾空格
        System.out.println(str.trim());              // "Hello Java"

        String t = str.trim();
        // JS charAt(0) / str[0]  → charAt(0)（Java 不支持 str[0] 写法）
        System.out.println(t.charAt(0));             // H

        // JS includes("Java")    → contains("Java")
        System.out.println(t.contains("Java"));      // true

        // JS startsWith/endsWith → 一样
        System.out.println(t.startsWith("Hello"));   // true

        // JS indexOf             → 一样，找不到返回 -1
        System.out.println(t.indexOf("Java"));       // 6

        // JS slice(6)            → substring(6)
        System.out.println(t.substring(6));          // Java

        // JS toUpperCase()       → 一样
        System.out.println(t.toUpperCase());         // HELLO JAVA

        // JS replace 只换第一个   → Java 的 replace 是【全部替换】（注意区别！）
        System.out.println("aabaa".replace("a", "x")); // xxbxx

        // JS toLowerCase         → equalsIgnoreCase 是 Java 特色，忽略大小写比较
        System.out.println("ABC".equalsIgnoreCase("abc"));  // true

        // 判空：
        // "".isEmpty()   → true（长度为0）
        // "  ".isBlank() → true（全是空格也算空，Java 11+）

        // ========== 4. 格式化输出（对比 JS 模板字符串） ==========

        String name = "张三";
        int age = 25;

        // JS:   `我叫${name}，今年${age}岁`
        // Java: String.format("我叫%s，今年%d岁", name, age)
        //   %s = 字符串占位符，%d = 整数占位符
        String intro = String.format("我叫%s，今年%d岁", name, age);
        System.out.println(intro);

        // ========== 5. 循环拼接字符串：用 StringBuilder（了解即可） ==========

        // 错误示范：循环里用 + 拼接
        String bad = "";
        for (int i = 1; i <= 5; i++) {
            // 每次 + 都创建一个新字符串对象，1000 次循环 = 1000 个废弃对象
            bad = bad + i + "、";
        }
        System.out.println(bad);   // 1、2、3、4、5、（结果一样，但底层很浪费）

        // 正确写法：StringBuilder 是可变的字符串，拼接不产生废弃对象
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i <= 5; i++) {
            sb.append(i).append("、");   // append 可以链式调用
        }
        System.out.println(sb.toString());   // 1、2、3、4、5、

        // 眼见为实：放大到 1 万次循环，对比两种方式的耗时
        long start1 = System.currentTimeMillis();
        String slow = "";
        for (int i = 0; i < 10000; i++) {
            slow += i;   // 每次都把前面攒的内容完整复制一遍，越到后面越慢
        }
        long end1 = System.currentTimeMillis();
        System.out.println("+ 拼接 1 万次耗时：" + (end1 - start1) + " ms");

        long start2 = System.currentTimeMillis();
        StringBuilder fast = new StringBuilder();
        for (int i = 0; i < 10000; i++) {
            fast.append(i);   // 原地追加，不复制旧内容
        }
        long end2 = System.currentTimeMillis();
        System.out.println("StringBuilder 1 万次耗时：" + (end2 - start2) + " ms");
        // 典型结果：+ 要几十上百 ms，StringBuilder 只要几 ms
        // 试试把 10000 改成 100000 再跑一次，差距会拉到几百倍
        // （JS 引擎对 += 有优化所以感觉不到，Java 里必须自己避开这个坑）

        // ========== 本课小结 ==========
        // 1. 字符串不可变，"修改"其实是创建新对象
        // 2. 比较内容用 equals()，== 比的是地址 ← 本课最重要的规则
        // 3. 常用方法和 JS 大同小异，注意 .length() 是方法、replace 全替换
        // 4. 格式化用 String.format + 占位符（%s %d）
        // 5. 循环拼接用 StringBuilder
    }
}
