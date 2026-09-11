// 声明当前类属于 dto 包（Data Transfer Object，数据传输对象）
// DTO 用于封装客户端与服务端之间传输的数据，解耦前端请求与内部实体
package com.example.dto;

// @Schema：Swagger 注解，用于在 API 文档中描述字段含义和示例值
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 登录请求 DTO
 *
 * 当客户端发送 POST /api/auth/login 请求时，
 * Spring 会将请求体中的 JSON 自动反序列化为这个对象。
 * 例如请求体：{"username": "test", "password": "123456"}
 * 会被转换为 LoginRequest(username="test", password="123456")
 */
@Schema(description = "登录请求")
public class LoginRequest {

    // 用户名 —— 对应 JSON 中的 "username" 字段
    @Schema(description = "用户名", example = "zhangsan")
    private String username;
    // 密码 —— 对应 JSON 中的 "password" 字段
    @Schema(description = "密码", example = "123456")
    private String password;

    /**
     * 无参构造函数
     * Jackson（Spring 默认的 JSON 库）反序列化时需要它，否则无法创建对象实例
     */
    public LoginRequest() {
    }

    /**
     * 全参构造函数 —— 方便在代码中直接 new LoginRequest(username, password)
     */
    public LoginRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // ===== 以下是 username 的 getter/setter =====
    // getter：获取用户名，Spring 序列化返回 JSON 时会调用它
    public String getUsername() {
        return username;
    }
    // setter：设置用户名，Spring 反序列化 JSON 时会调用它
    public void setUsername(String username) {
        this.username = username;
    }

    // ===== 以下是 password 的 getter/setter =====
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
}
