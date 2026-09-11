package com.basic;

// ArrayList 在 java.util 包里，不像 String 那样自动可用，必须手动 import
// （对比 JS：数组是内置的，不用 import 任何东西）
import java.util.ArrayList;
import java.util.Arrays;   // 数组的工具类，下面打印数组要用它

/**
 * 第 6 课：数组 与 ArrayList
 *
 * Java 有两种"数组"：
 *   1. 原生数组（int[]、String[]）：长度固定，快但不灵活
 *   2. ArrayList：长度可变，项目里 95% 用它
 *
 * 运行方式：main 方法上方点击 "Run"
 */
public class L06_ArrayList {

    public static void main(String[] args) {

        // ========== 1. 原生数组：长度定死，类型定死 ==========

        // JS:   const nums = [1, 2, 3, 4, 5];
        // Java: 类型[] 变量名 = {值...};
        int[] nums = {1, 2, 3, 4, 5};
        int[] num2 = {7, 8, 9};
        System.out.println(num2);

        // 另一种写法：先指定长度，内容默认值（int 默认 0）
        int[] scores = new int[3];   // [0, 0, 0]

        // 访问元素：和 JS 一样用下标，从 0 开始
        System.out.println(nums[0]);         // 1

        // ⚠️ 易混点警告：数组的 length 是【属性】，不带括号！
        //    数组.length      ← 属性
        //    字符串.length()   ← 方法
        //    ArrayList.size() ← 方法（马上讲）
        System.out.println(nums.length);     // 5

        // 数组的两大限制：
        // nums[5] = 6;       ← 去掉注释运行：ArrayIndexOutOfBoundsException 数组越界
        // nums[0] = "hello"; ← 编译报错：int 数组不能装字符串（JS 数组随便混装）

        // ⚠️ 打印数组的坑：直接 println 数组，打不出内容！
        // System.out.println(nums);   ← 输出 [I@24d46ca6 这样的"天书"
        //    解读：[ 表示"这是个数组"，I 表示 int 类型，@ 后面是哈希码（对象身份证号）
        //    原因：原生数组没有重写 toString()，默认只打印"我是谁"，不打印"我装了什么"
        //    对比 JS：console.log([1,2,3]) 直接显示内容，Java 数组没这么贴心
        // 正确写法：Arrays 工具类的 toString() 方法
        System.out.println(Arrays.toString(nums));   // [1, 2, 3, 4, 5]
        // 补充：二维数组要用 Arrays.deepToString() 才能打印出内容
        // 注意：ArrayList 重写了 toString()，所以直接 println(names) 就能显示内容（见第 2 节）

        // ========== 2. ArrayList：可变长的"数组"（项目主力） ==========

        // 声明：ArrayList<里面装的类型> 变量名 = new ArrayList<>();
        // 尖括号 <String> 叫"泛型"：规定这个列表只能装字符串
        //
        // 右边的 <> 叫"菱形运算符"（diamond）：类型不用写第二遍，编译器照着左边声明推断（Java 7+）
        //   完整写法：new ArrayList<String>()   ← String 写两遍，啰嗦（Java 7 以前只能这样）
        //   对比 TS：const names: Array<string> = new Array()  ← TS 连尖括号都能整个省略
        //     Java 必须留一对空尖括号，作为"请帮我推断"的标记
        //     （泛型是 Java 5 才加的，"不写尖括号"早被原始类型占用了，只能另造符号）
        //   ⚠️ 但 <> 本身不能省！new ArrayList() 是"原始类型"（raw type）：
        //     编译器直接关闭泛型检查，还会警告"未经检查或不安全的操作"，类型安全就没了
        //   补充：Java 10+ 左边也能省：var names = new ArrayList<String>();
        //     但 var + 右边空 <> 会推断成 ArrayList<Object>，别这么写
        ArrayList<String> names = new ArrayList<>();
        // JS arr.push("张三")  →  list.add("张三")
        names.add("张三");
        names.add("李四");
        names.add("王五");

        // JS arr.length        →  list.size()（是方法！）
        System.out.println("列表长度：" + names.size());   // 3

        // JS arr[0]            →  list.get(0)（不能用下标！）
        System.out.println("第一个：" + names.get(0));      // 张三

        // JS arr[1] = "赵六"    →  list.set(1, "赵六")
        names.set(1, "赵六");

        // JS arr.splice(2, 1)  →  list.remove(2)（按下标删）
        names.remove(2);

        // JS arr.includes("张三") → list.contains("张三")
        System.out.println("包含张三吗：" + names.contains("张三"));   // true

        // ========== 3. 遍历：两种方式 ==========

        names.add("王五");   // 加回来，现在有：张三、赵六、王五

        // 方式一：传统下标循环（和 JS 的 for 一样，就是 length 换成 size()）
        System.out.println("--- 下标遍历 ---");
        for (int i = 0; i < names.size(); i++) {
            System.out.println(i + ": " + names.get(i));
        }

        // 方式二：增强 for（for-each），对比 JS 的 for...of
        // JS:   for (const name of names) { ... }
        // Java: for (String name : names) { ... }   ← of 换成冒号
        System.out.println("--- 增强 for 遍历 ---");
        for (String name : names) {
            System.out.println(name);
        }

        // ========== 4. 装数字的列表：必须用包装类 ==========

        // 泛型里不能写基本类型：ArrayList<int> ← 编译报错！
        // 要用包装类 Integer（上一课讲过：int 的类版本）
        ArrayList<Integer> ages = new ArrayList<>();
        ages.add(25);        // 25 是 int，Java 自动装进 Integer（叫"自动装箱"）
        ages.add(18);

        int first = ages.get(0);   // 取出来自动拆回 int（自动拆箱）
        System.out.println("第一个年龄：" + first);

        // ========== 5. 实战：对象列表（项目里的真实场景） ==========

        // 用上一课的 Student 类，造一个"学生列表"
        ArrayList<Student> students = new ArrayList<>();
        students.add(new Student("小明", 20));
        students.add(new Student("小红", 17));
        students.add(new Student("小刚", 22));

        // 找出所有成年人 —— 遍历 + if + add 到新列表
        ArrayList<Student> adults = new ArrayList<>();
        for (Student stu : students) {
            if (stu.isAdult()) {         // 调用对象的实例方法
                adults.add(stu);
            }
        }
        System.out.println("成年学生数量：" + adults.size());   // 2

        // ========== 6. 对照你的项目 ==========
        //
        // PersonRepository 的 findAll() 返回的就是一个 List（ArrayList 的父类型）
        // PersonController 里 data.put("list", pageResult.getContent())
        // getContent() 拿到的就是这样一个对象列表，最后变成 JSON 数组返回给前端

        // ========== 本课小结 ==========
        // JS                Java 数组          ArrayList
        // arr.length        arr.length          list.size()    ← 三个长度写法全不同！
        // arr[0]            arr[0]              list.get(0)
        // arr.push(x)       做不到（定长）       list.add(x)
        // arr[i] = x        arr[i] = x          list.set(i, x)
        // for...of          for (int n : arr)   for (String s : list)
        // 混装任意类型       类型固定             泛型固定类型，基本类型用包装类
    }
}
