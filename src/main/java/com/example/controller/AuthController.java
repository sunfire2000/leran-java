// 声明当前类属于 controller 包
// controller 包存放控制器，负责接收 HTTP 请求、调用 Service 处理业务、返回 JSON 响应
package com.example.controller;

// ========== 导入项目内部类 ==========

// 导入登录请求 DTO，Spring 会自动将请求体 JSON 反序列化为这个对象
import com.example.dto.LoginRequest;
// 导入注册请求 DTO
import com.example.dto.RegisterRequest;
// 导入统一响应封装类，所有接口都返回 Result<T> 格式
import com.example.dto.Result;
// 导入用户业务服务，包含注册和登录的业务逻辑
import com.example.service.UserService;

// ========== 导入 Spring MVC 注解 ==========

// @RestController + @RequestMapping + @PostMapping 等注解的集合导入
// @RestController：声明为 REST 控制器，返回值自动序列化为 JSON
// @RequestMapping：定义基础 URL 前缀
// @PostMapping：将 HTTP POST 请求映射到方法
// @RequestBody：将请求体中的 JSON 绑定到方法参数
import org.springframework.web.bind.annotation.*;

// ========== 导入 Swagger 注解 ==========
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

// ========== 导入 Java 标准库 ==========

// Map：键值对集合，用于接收 Service 返回的数据并传给 Result 封装
import java.util.Map;

/**
 * 认证控制器 —— 处理用户注册和登录的 HTTP 请求
 *
 * 控制器是请求的入口，职责很简单：
 * 1. 接收请求（@PostMapping）
 * 2. 调用 Service 处理业务
 * 3. 封装响应（Result）返回给客户端
 *
 * 注意：控制器不应该包含业务逻辑，业务逻辑应放在 Service 层
 */
@Tag(name = "认证管理", description = "用户注册和登录接口")
@RestController  // REST 控制器，返回值自动转为 JSON
@RequestMapping("/api/auth")  // 基础路径：所有接口以 /api/auth 开头
public class AuthController {

    // 用户业务服务，通过构造函数注入
    private final UserService userService;

    /**
     * 构造函数注入 —— Spring 启动时自动传入 UserService 实例
     */
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    /**
     * 用户注册接口
     *
     * 请求方式：POST /api/auth/register
     * 请求体示例：{"username": "test", "password": "123456", "email": "test@example.com"}
     * 成功响应：{"code":200, "message":"注册成功", "data":{"id":1, "username":"test", "email":"..."}}
     * 失败响应：{"code":400, "message":"用户名已存在", "data":null}
     *
     * @param request 注册请求体，@RequestBody 告诉 Spring 将 JSON 反序列化为 RegisterRequest 对象
     * @return 统一格式的响应结果
     */
    @Operation(summary = "用户注册", description = "创建新用户账号")
    @PostMapping("/register")  // 完整路径 = /api/auth + /register = /api/auth/register
    public Result<Map<String, Object>> register(@RequestBody RegisterRequest request) {
        try {
            // 调用 Service 层执行注册业务
            Map<String, Object> data = userService.register(request);
            // 注册成功，返回 200 + 用户信息
            return Result.success("注册成功", data);
        } catch (RuntimeException e) {
            // 注册失败（如用户名已存在），返回 400 + 错误信息
            return Result.error(400, e.getMessage());
        }
    }

    /**
     * 用户登录接口
     *
     * 请求方式：POST /api/auth/login
     * 请求体示例：{"username": "test", "password": "123456"}
     * 成功响应：{"code":200, "message":"登录成功", "data":{"token":"eyJ...", "username":"test"}}
     * 失败响应：{"code":401, "message":"用户名或密码错误", "data":null}
     *
     * @param request 登录请求体
     * @return 统一格式的响应结果，成功时包含 JWT Token
     */
    @Operation(summary = "用户登录", description = "登录并获取 JWT Token")
    @PostMapping("/login")  // 完整路径 = /api/auth + /login = /api/auth/login
    public Result<Map<String, Object>> login(@RequestBody LoginRequest request) {
        try {
            // 调用 Service 层执行登录业务
            Map<String, Object> data = userService.login(request);
            // 登录成功，返回 200 + Token
            return Result.success("登录成功", data);
        } catch (RuntimeException e) {
            // 登录失败，返回 401（未授权）+ 错误信息
            return Result.error(401, e.getMessage());
        }
    }
}
