package com.basic;

/**
 * 第 2 课：流程控制（if / switch / for / while）
 *
 * 好消息：这一课和 JS 有 90% 一样，注意少数几个差异即可。
 * 运行方式：main 方法上方点击 "Run"
 */
public class L02_ControlFlow {

    public static void main(String[] args) {

        // ========== 1. if / else if / else（和 JS 完全一样） ==========

        int score = 85;

        // JS: if (score >= 90) { ... } else if (score >= 60) { ... } else { ... }
        if (score >= 90) {
            System.out.println("优秀");
        } else if (score >= 60) {
            System.out.println("及格");    // 85 分会走这里
        } else {
            System.out.println("不及格");
        }

        // ========== 2. 逻辑运算符（和 JS 完全一样） ==========

        int age = 20;
        boolean hasTicket = true;

        // && 与、|| 或、! 非 —— 和 JS 一模一样，也有短路特性
        if (age >= 18 && hasTicket) {
            System.out.println("可以入场");
        }

        // ========== 3. 比较运算符（和 JS 的数字比较一样） ==========
        // ==  !=  >  <  >=  <=
        // ⚠️ 唯一的大坑：字符串不能用 == 比较！要用 equals()
        //    本课先用数字，字符串的坑留到 L05 专门讲

        // ========== 4. switch（和 JS 一样，包括 break 穿透的坑） ==========

        int dayOfWeek = 3;

        // JS: switch (dayOfWeek) { case 1: ... break; ... }
        switch (dayOfWeek) {
            case 1:
                System.out.println("星期一");
                break;    // 没有 break 会"穿透"到下一个 case（JS 也这样）
            case 2:
                System.out.println("星期二");
                break;
            case 3:
                System.out.println("星期三");   // 走这里
                break;
            default:                          // 所有 case 都不匹配时走这里
                System.out.println("其他");
        }

        // ========== 5. for 循环（和 JS 的经典 for 完全一样） ==========

        // JS: for (let i = 1; i <= 5; i++) { ... }
        // 唯一区别：let 换成 int
        for (int i = 1; i <= 5; i++) {
            System.out.println("第 " + i + " 圈");
        }

        // 实战：计算 1 加到 100
        int sum = 0;
        for (int i = 1; i <= 100; i++) {
            sum = sum + i;    // 也可写成 sum += i;（和 JS 一样）
        }
        System.out.println("1加到100 = " + sum);   // 5050

        // ========== 6. while 循环（和 JS 一样） ==========

        // for 适合"知道循环几次"，while 适合"循环到某个条件不满足为止"
        int countdown = 3;
        while (countdown > 0) {
            System.out.println("倒计时：" + countdown);
            countdown--;
        }
        System.out.println("发射！");

        // ========== 7. break 和 continue（和 JS 完全一样） ==========

        // break：立刻跳出整个循环
        // 示例：找 1-100 中第一个能被 7 整除的数
        for (int i = 1; i <= 100; i++) {
            if (i % 7 == 0) {
                System.out.println("第一个能被7整除的数是：" + i);
                break;    // 找到就停，后面不用看了
            }
        }

        // continue：跳过本次，进入下一次循环
        // 示例：打印 1-10 中的奇数（跳过偶数）
        for (int i = 1; i <= 10; i++) {
            if (i % 2 == 0) {
                continue;    // 偶数直接跳过，不执行下面的打印
            }
            System.out.println("奇数：" + i);
        }

        // ========== 本课小结 ==========
        // if/for/while/break/continue 和 JS 完全一样，直接把 JS 经验搬过来
        // 唯一记住两件事：
        //   1. for 里的 let 要改成 int
        //   2. 字符串比较用 equals()，不用 ==（L05 详解）
    }
}
