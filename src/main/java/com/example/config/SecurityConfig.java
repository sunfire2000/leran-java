// 声明当前类属于 config 包
// config 包存放配置类，用 Java 代码替代 XML 配置来定制 Spring 的行为
package com.example.config;

// ========== 导入项目内部类 ==========
// （UserDetailsService 已移至 UserDetailsConfig 配置类，避免循环依赖）

// ========== 导入 Spring Security 配置类 ==========

// @Bean：标记方法，告诉 Spring “把这个方法的返回值注册为容器中的 Bean”
// 类似于在 XML 中定义 <bean>，但用 Java 代码更直观
import org.springframework.context.annotation.Bean;

// @Configuration：标记为配置类，Spring 启动时会加载其中的所有 @Bean 方法
import org.springframework.context.annotation.Configuration;

// AuthenticationManager：认证管理器，负责处理登录认证请求
import org.springframework.security.authentication.AuthenticationManager;

// AuthenticationConfiguration：Spring Security 的认证配置入口
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;

// HttpSecurity：HTTP 安全配置构建器，用于配置 URL 访问规则、CSRF、会话等
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

// @EnableWebSecurity：启用 Spring Security 的 Web 安全功能
// 它会自动激活安全过滤器链，拦截所有 HTTP 请求进行认证/授权
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

// SessionCreationPolicy：会话创建策略枚举
// STATELESS 表示服务端不创建 Session（JWT 方案不需要服务端会话）
import org.springframework.security.config.http.SessionCreationPolicy;

// UserDetailsService：已移至 UserDetailsConfig 配置类中
// 避免 SecurityConfig ↔ JwtAuthenticationFilter 的循环依赖
// import org.springframework.security.core.userdetails.UserDetailsService;

// UsernameNotFoundException：已移至 UserDetailsConfig 配置类中
// import org.springframework.security.core.userdetails.UsernameNotFoundException;

// BCryptPasswordEncoder：BCrypt 密码加密器
// BCrypt 是一种单向哈希算法，适合存储密码（不可逆，且每次加密结果不同）
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

// PasswordEncoder：密码编码器接口，BCryptPasswordEncoder 是它的实现类
import org.springframework.security.crypto.password.PasswordEncoder;

// SecurityFilterChain：安全过滤器链，定义哪些请求需要认证、哪些可以直接访问
import org.springframework.security.web.SecurityFilterChain;

// UsernamePasswordAuthenticationFilter：Spring Security 默认的用户名密码认证过滤器
// 我们将 JWT 过滤器插入到它之前，实现自定义的 Token 认证
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Spring Security 安全配置类
 *
 * 这个类定义了应用的安全规则：
 * 1. 哪些 URL 可以直接访问（如登录/注册）
 * 2. 哪些 URL 需要登录后才能访问
 * 3. 如何验证用户身份（JWT Token）
 * 4. 如何加密存储密码（BCrypt）
 */
@Configuration  // 标记为 Spring 配置类
@EnableWebSecurity  // 启用 Web 安全功能
public class SecurityConfig {

    // JWT 认证过滤器，通过构造函数注入
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    /**
     * 构造函数注入 —— Spring 启动时自动传入 JwtAuthenticationFilter 实例
     * 这是 Spring 推荐的依赖注入方式（比 @Autowired 更明确、更易测试）
     */
    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    /**
     * 配置安全过滤器链 —— 定义 HTTP 请求的安全规则
     *
     * @param http HttpSecurity 构建器
     * @return 构建好的安全过滤器链
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // 1. 禁用 CSRF 防护
            // CSRF（跨站请求伪造）防护适用于有 Session 的传统 Web 应用
            // 我们使用 JWT 无状态认证，不需要 CSRF 防护
            .csrf(csrf -> csrf.disable())

            // 2. 设置会话策略为无状态
            // STATELESS = 服务端不创建 HttpSession，每次请求都通过 Token 认证
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

            // 3. 配置 URL 访问规则
            .authorizeHttpRequests(auth -> auth
                // /api/auth/** 下的所有接口（登录、注册）允许匿名访问
                .requestMatchers("/api/auth/**").permitAll()
                // /api/hello 允许匿名访问（测试用）
                .requestMatchers("/api/hello").permitAll()
                // /api/persons/** 允许匿名访问（CRUD 接口）
                .requestMatchers("/api/persons/**").permitAll()
                // Swagger 文档允许匿名访问
                .requestMatchers("/swagger-ui/**", "/swagger-ui.html", "/v3/api-docs/**").permitAll()
                // 其他所有请求都必须先通过认证（携带有效的 JWT Token）
                .anyRequest().authenticated()
            )

            // 4. 将 JWT 过滤器插入到 Spring Security 默认认证过滤器之前
            // 这样每次请求都会先经过 JWT 过滤器检查 Token
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();  // 构建并返回安全过滤器链
    }

    /**
     * 配置密码编码器 —— 使用 BCrypt 算法加密密码
     *
     * BCrypt 特点：
     * - 单向哈希，不可逆（无法从哈希值还原明文）
     * - 每次加密结果不同（自动加盐），防止彩虹表攻击
     * - 可通过 matches() 方法验证明文与哈希是否匹配
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 配置认证管理器
     *
     * AuthenticationManager 是 Spring Security 的认证入口，
     * 负责协调 UserDetailsService 和 PasswordEncoder 完成完整的认证流程
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
