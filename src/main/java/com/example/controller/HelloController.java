// 声明当前类所属的包路径，Java 通过包来组织代码结构
// 类似文件系统的文件夹，com.example.controller 表示 controller 层
package com.example.controller;

// ========== 导入项目内部类 ==========

// 导入自定义的统一响应封装类 Result<T>
// 它可以将接口返回的数据包装成 {code, message, data} 的标准格式
import com.example.dto.Result;

// ========== 导入 Spring 框架注解 ==========

// @GetMapping：将 HTTP GET 请求映射到方法上
// 例如 @GetMapping("/hello") 表示 GET /api/hello 会调用该方法
import org.springframework.web.bind.annotation.GetMapping;

// @RequestMapping：定义该控制器的基础 URL 前缀
// 类上标注 @RequestMapping("/api") 后，该类所有接口的路径都以 /api 开头
import org.springframework.web.bind.annotation.RequestMapping;

// @RestController：声明这是一个 REST 控制器
// 它组合了 @Controller + @ResponseBody，表示该类中所有方法的返回值
// 都会直接序列化为 JSON 写入 HTTP 响应体（而不是跳转页面）
import org.springframework.web.bind.annotation.RestController;

// ========== 导入 Java 标准库 ==========

// HashMap：基于哈希表的 Map 实现，提供快速的键值对存取
import java.util.HashMap;
// Map：键值对集合的接口定义，HashMap 是它的一个实现类
// 面向接口编程：声明类型时用 Map，创建实例时用 HashMap
import java.util.Map;

// @RestController：将该类标记为 REST 控制器，Spring 启动时会自动扫描并注册它
// @RequestMapping("/api")：该类下所有接口的 URL 都以 /api 作为前缀
@RestController
@RequestMapping("/api")
public class HelloController {

    /**
     * 需要登录后才能访问的接口
     *
     * @GetMapping("/hello")：将 HTTP GET /api/hello 请求映射到这个方法
     * 完整路径 = 类上的 /api + 方法上的 /hello = /api/hello
     *
     * Result<Map<String, String>>：返回类型是统一响应封装，泛型内是 Map 数据
     * 该方法受 Spring Security 保护，请求时必须携带有效的 JWT Token
     */
    @GetMapping("/hello")
    public Result<Map<String, String>> sayHello() {
        // 创建一个 HashMap 实例，存储要返回的数据
        Map<String, String> data = new HashMap<>();
        // 放入键值对：key="message", value="Hello, World!"
        data.put("message", "Hello, World!");
        // 用 Result.success() 包装数据，返回 {code:200, message:"操作成功", data:{...}}
        return Result.success(data);
    }
}
