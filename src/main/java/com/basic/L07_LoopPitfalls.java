package com.basic;

import java.util.ArrayList;

/**
 * 第 7 课：循环里的经典错误写法（避坑合集）
 *
 * L05 讲了"循环里用 + 拼字符串"，循环里的坑其实不止这一个。
 * 本课集中演示几个项目里真实高频出现的错误，每个都给出正确写法。
 * 运行方式：main 方法上方点击 "Run"
 */
public class L07_LoopPitfalls {

    public static void main(String[] args) {

        // ========== 1. 边遍历边删除：Java 会直接报错（Java 第一经典坑） ==========

        ArrayList<String> names = new ArrayList<>();
        names.add("张三");
        names.add("李四");
        names.add("王五");

        // 错误示范：增强 for 里调用 remove
        // for (String name : names) {
        //     if (name.equals("李四")) {
        //         names.remove(name);   // ← 取消注释运行：抛 ConcurrentModificationException
        //     }
        // }
        // 为什么报错？增强 for 底层是"迭代器"，遍历时列表被外部改动，迭代器立刻察觉并"快速失败"
        // 对比 JS：forEach 里 splice 不报错，但会静默跳过元素 —— 不报错的 bug 其实更可怕

        // 正确写法：removeIf()，一句话安全删除
        // JS 对比：names = names.filter(n => n !== "李四")（JS 是返回新数组，removeIf 是原地删）
        // name -> name.equals("李四") 就是 JS 的箭头函数，Java 写成 -> 而不是 =>
        names.removeIf(name -> name.equals("李四"));
        System.out.println(names);   // [张三, 王五]

        // ========== 2. 下标循环正序删除：不报错，但会漏删（JS 同款坑） ==========

        ArrayList<String> tags = new ArrayList<>();
        tags.add("a");
        tags.add("b");
        tags.add("b");   // 两个 "b" 挨着放，才能暴露问题
        tags.add("c");

        // 错误示范（这段真的运行，睁大眼睛看结果）：
        ArrayList<String> wrong = new ArrayList<>(tags);   // 复制一份来做实验
        for (int i = 0; i < wrong.size(); i++) {
            if (wrong.get(i).equals("b")) {
                wrong.remove(i);
            }
        }
        System.out.println("正序删除结果：" + wrong);   // [a, b, c] ← 漏删了一个 b！
        // 原因：删掉 i 位置的元素后，后面的元素集体前移一格，
        //       i++ 正好跳过了紧挨着的下一个 "b"。JS 里 for + splice 是一模一样的坑

        // 正确写法：倒序遍历，前移影响不到还没访问的下标
        for (int i = tags.size() - 1; i >= 0; i--) {
            if (tags.get(i).equals("b")) {
                tags.remove(i);
            }
        }
        System.out.println("倒序删除结果：" + tags);   // [a, c]
        // 当然，能写 removeIf 就别手写循环，一行搞定还不容易错

        // ========== 3. 死循环的两种经典成因 ==========

        // 成因一：忘记更新循环变量（while 高发区）
        // int i = 0;
        // while (i < 5) {
        //     System.out.println("循环中");   // i 永远是 0，程序卡死，只能强制停止
        // }                                   // 正确：循环体里记得写 i++

        // 成因二：浮点数做 != 判断（JS 同款坑：0.1 + 0.2 !== 0.3）
        // for (double d = 0.0; d != 1.0; d += 0.1) { ... }
        // 0.1 在二进制里无法精确表示，d 累加永远不等于 1.0 → 死循环
        // 正确：浮点数的循环条件用 < / >=，永远不要对浮点数用 == 或 !=

        // ========== 4. 循环里反复创建"明明可以复用"的对象 ==========

        // 错误示范：每次循环都 new 一个一模一样的东西
        // for (int i = 0; i < 1000; i++) {
        //     java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
        //     // 用 sdf 格式化日期……1000 次循环 new 了 1000 个相同的对象，纯属浪费
        // }
        // 正确：不变的東西提到循环外，只创建一次，循环里只管复用
        // 同理：正则的 Pattern.compile()、数据库连接，都属于"必须放循环外"的东西

        // ========== 5. 包装类的隐藏开销（Java 特有，JS 里没有这个概念） ==========

        // long 是基本类型，存的就是数字本身；Long 是包装类，存的是对象
        // 用 Long 做累加：每次 += 都要先拆箱取值、算完再装箱新建对象
        long start1 = System.currentTimeMillis();
        long sum1 = 0;
        for (int i = 0; i < 10000000; i++) {
            sum1 += i;   // 基本类型，CPU 直接算，无任何额外开销
        }
        long end1 = System.currentTimeMillis();
        System.out.println("long  累加 1 千万次：" + (end1 - start1) + " ms（sum=" + sum1 + "）");

        long start2 = System.currentTimeMillis();
        Long sum2 = 0L;
        for (int i = 0; i < 10000000; i++) {
            sum2 += i;   // 包装类，每次都拆箱 + 装箱，偷偷干了好多活
        }
        long end2 = System.currentTimeMillis();
        System.out.println("Long  累加 1 千万次：" + (end2 - start2) + " ms（sum=" + sum2 + "）");
        // 两个 sum 结果一样，但耗时差好几倍。结论：数学运算优先用 int/long/double 基本类型

        // ========== 6. 实战预告：循环里查数据库（N+1 问题，后端第一性能杀手） ==========
        //
        // for (Long id : ids) {
        //     userRepository.findById(id);   // 循环 100 次 = 发 100 次 SQL！
        // }
        // 正确：循环外一次查回来：userRepository.findAllById(ids)
        // 以后写 Spring Boot 项目时，务必想起这一条

        // ========== 本课小结 ==========
        // 1. 遍历中删除：用 removeIf()，别在循环里直接 remove（Java 会抛异常）
        // 2. 下标循环删除要倒序，正序会漏删（JS 的 splice 同款坑）
        // 3. 死循环两大成因：忘记更新变量、浮点数用 != 比较
        // 4. 不变的对象提到循环外创建，别每次循环都 new
        // 5. 大量数学运算用基本类型，避开包装类的装箱开销
        // 6. 循环里发 SQL 是 N+1 问题，项目里要改成批量查询
        // （回顾：循环里拼字符串用 StringBuilder，见 L05 第 5 节）
    }
}
