// 声明当前类属于 dto 包
package com.example.dto;

// @Schema：Swagger 注解，用于在 API 文档中描述字段含义和示例值
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 注册请求 DTO
 *
 * 当客户端发送 POST /api/auth/register 请求时，
 * Spring 会将请求体中的 JSON 自动反序列化为这个对象。
 * 例如请求体：{"username": "test", "password": "123456", "email": "test@example.com"}
 * 会被转换为 RegisterRequest(username="test", password="123456", email="test@example.com")
 */
@Schema(description = "注册请求")
public class RegisterRequest {

    // 用户名 —— 必填，用于唯一标识一个用户
    @Schema(description = "用户名", example = "zhangsan")
    private String username;
    // 密码 —— 必填，服务端会用 BCrypt 加密后存储到数据库
    @Schema(description = "密码", example = "123456")
    private String password;
    // 邮箱 —— 选填，用户的联系方式
    @Schema(description = "邮箱", example = "zhangsan@example.com")
    private String email;

    /**
     * 无参构造函数 —— JSON 反序列化必需
     */
    public RegisterRequest() {
    }

    /**
     * 全参构造函数 —— 方便代码中直接创建实例
     */
    public RegisterRequest(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }

    // ===== username 的 getter/setter =====
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    // ===== password 的 getter/setter =====
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    // ===== email 的 getter/setter =====
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
}
