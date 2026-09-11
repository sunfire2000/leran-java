// 声明当前类属于 config 包
package com.example.config;

// ========== 导入项目内部类 ==========

// 导入 UserRepository，用于根据用户名查询数据库中的用户记录
import com.example.repository.UserRepository;

// ========== 导入 Spring 注解 ==========

// @Bean：将方法返回值注册为 Spring 容器中的 Bean
import org.springframework.context.annotation.Bean;

// @Configuration：标记为配置类，Spring 启动时会加载其中的 @Bean 方法
import org.springframework.context.annotation.Configuration;

// ========== 导入 Spring Security ==========

// UserDetailsService：Spring Security 提供的用户详情服务接口
// 负责根据用户名加载用户信息，认证和授权时都会调用它
import org.springframework.security.core.userdetails.UserDetailsService;

// UsernameNotFoundException：当根据用户名找不到用户时抛出的异常
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * 用户详情服务配置类
 *
 * 为什么要单独拆出来？
 * 因为 SecurityConfig 通过构造函数注入了 JwtAuthenticationFilter，
 * 而 JwtAuthenticationFilter 又依赖 UserDetailsService，
 * 如果 UserDetailsService 也定义在 SecurityConfig 中，就会形成循环依赖：
 *
 *   SecurityConfig → JwtAuthenticationFilter → UserDetailsService(在SecurityConfig中) → SecurityConfig ✘
 *
 * 拆分后依赖链变为：
 *   SecurityConfig → JwtAuthenticationFilter → UserDetailsService(在UserDetailsConfig中) ✔
 */
@Configuration  // 标记为 Spring 配置类
public class UserDetailsConfig {

    /**
     * 配置用户详情服务 —— 告诉 Spring Security 如何根据用户名查找用户
     *
     * Spring Security 认证时会调用这个服务加载用户信息：
     * 1. 收到用户名（从 JWT Token 中提取）
     * 2. 调用此方法，从数据库查找用户
     * 3. 将用户信息封装为 UserDetails 对象返回
     *
     * @param userRepository 用户数据访问层，Spring 自动注入
     * @return UserDetailsService 实例
     */
    @Bean
    public UserDetailsService userDetailsService(UserRepository userRepository) {
        // 使用 Lambda 表达式实现 UserDetailsService 接口
        // 参数 username 是从 JWT Token 中提取的用户名
        return username -> userRepository.findByUsername(username)
                // 如果找到用户，将其转换为 Spring Security 的 UserDetails 对象
                .map(user -> org.springframework.security.core.userdetails.User.builder()
                        .username(user.getUsername())    // 设置用户名
                        .password(user.getPassword())    // 设置加密后的密码
                        .roles("USER")                  // 设置角色为普通用户
                        .build())
                // 如果未找到，抛出“用户不存在”异常
                .orElseThrow(() -> new UsernameNotFoundException("用户不存在: " + username));
    }
}
