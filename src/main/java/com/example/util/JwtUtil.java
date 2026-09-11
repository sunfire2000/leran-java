// 声明当前类属于 util 包
// util 包存放工具类，提供与业务无关的通用功能
package com.example.util;

// ========== 导入 JWT 库（jjwt） ==========

// Claims：JWT 的载荷部分（Payload），存储 Token 中携带的所有声明信息
// 类似于一个 Map，包含 sub（主题）、iat（签发时间）、exp（过期时间）等
import io.jsonwebtoken.Claims;

// Jwts：jjwt 库的核心入口类，提供 builder 和 parser 来创建/解析 Token
import io.jsonwebtoken.Jwts;

// Decoders：解码工具类，用于将 Base64 编码的密钥还原为字节数组
import io.jsonwebtoken.io.Decoders;

// Keys：密钥工具类，用于将字节数组转换为 HMAC-SHA 签名算法所需的 SecretKey
import io.jsonwebtoken.security.Keys;

// ========== 导入 Spring 注解 ==========

// @Value：从配置文件（application.properties）中读取配置项的值
// 例如 @Value("${jwt.secret}") 会读取 jwt.secret=xxx 的值
import org.springframework.beans.factory.annotation.Value;

// @Component：告诉 Spring “这是一个组件”，启动时自动创建实例并管理
import org.springframework.stereotype.Component;

// ========== 导入 Java 标准库 ==========

// SecretKey：Java 加密框架中的密钥接口，用于 HMAC-SHA 签名
import javax.crypto.SecretKey;
// Date：Java 日期类，用于设置 Token 的签发时间和过期时间
import java.util.Date;

/**
 * JWT 工具类 - 负责 Token 的生成与解析
 *
 * JWT（JSON Web Token）是一种无状态的认证方案，由三部分组成：
 * 1. Header（头部）  —— 声明算法类型（如 HS256）
 * 2. Payload（载荷） —— 存储用户信息（如用户名、过期时间）
 * 3. Signature（签名） —— 用密钥对前两部分签名，防止篡改
 *
 * 工作流程：
 * 登录成功 → 服务端生成 Token → 返回给客户端
 * 后续请求 → 客户端携带 Token → 服务端验证签名 → 提取用户信息
 */
@Component  // 注册为 Spring Bean，其他类可以通过构造函数注入使用
public class JwtUtil {

    // 从 application.properties 中读取 jwt.secret 的值
    // 这是用于签名的密钥（Base64 编码），必须保密
    @Value("${jwt.secret}")
    private String secret;

    // 从 application.properties 中读取 jwt.expiration 的值
    // Token 的有效期（毫秒），当前配置为 86400000 = 24 小时
    @Value("${jwt.expiration}")
    private long expiration;

    /**
     * 获取签名密钥
     * 将 Base64 编码的字符串解码为字节数组，再转换为 HMAC-SHA 算法所需的 SecretKey
     */
    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);  // Base64 解码
        return Keys.hmacShaKeyFor(keyBytes);  // 生成 HMAC-SHA 密钥
    }

    /**
     * 生成 JWT Token
     *
     * @param username 用户名，会嵌入到 Token 的 subject 字段中
     * @return 生成的 Token 字符串，格式如：eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0In0.xxx
     */
    public String generateToken(String username) {
        return Jwts.builder()        // 创建 Token 构建器
                .subject(username)    // 设置主题（通常是用户名）
                .issuedAt(new Date()) // 设置签发时间为当前时间
                .expiration(new Date(System.currentTimeMillis() + expiration))  // 设置过期时间
                .signWith(getSigningKey())  // 用密钥签名，防止篡改
                .compact();           // 压缩为紧凑格式（即最终的 Token 字符串）
    }

    /**
     * 从 Token 中提取用户名
     *
     * @param token JWT Token 字符串
     * @return 用户名（Token 的 subject 字段）
     */
    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();  // 从载荷中获取 subject
    }

    /**
     * 校验 Token 是否有效
     *
     * 验证内容包括：签名是否正确、是否已过期、格式是否合法
     * @param token JWT Token 字符串
     * @return true=有效，false=无效或已过期
     */
    public boolean validateToken(String token) {
        try {
            extractAllClaims(token);  // 尝试解析，如果成功说明 Token 有效
            return true;
        } catch (Exception e) {
            // 解析失败（签名错误、已过期等）→ Token 无效
            return false;
        }
    }

    /**
     * 解析 Token 并提取所有载荷信息（内部方法）
     *
     * @param token JWT Token 字符串
     * @return Claims 对象，包含 Token 中的所有声明信息
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parser()              // 创建 Token 解析器
                .verifyWith(getSigningKey())  // 设置验证密钥
                .build()                  // 构建解析器
                .parseSignedClaims(token) // 解析并验证 Token
                .getPayload();            // 获取载荷（Claims）
    }
}
