// 声明当前类属于 config 包
package com.example.config;

// ========== 导入项目内部类 ==========

// 导入 JWT 工具类，用于解析和验证 Token
import com.example.util.JwtUtil;

// ========== 导入 Servlet API ==========

// FilterChain：过滤器链接口，调用 doFilter() 将请求传递给下一个过滤器
import jakarta.servlet.FilterChain;
// ServletException：Servlet 处理过程中可能抛出的异常
import jakarta.servlet.ServletException;
// HttpServletRequest：封装 HTTP 请求的对象，可以获取请求头、参数、URL 等
import jakarta.servlet.http.HttpServletRequest;
// HttpServletResponse：封装 HTTP 响应的对象，可以设置状态码、响应头等
import jakarta.servlet.http.HttpServletResponse;

// ========== 导入 Spring Security ==========

// UsernamePasswordAuthenticationToken：认证令牌对象
// 表示一个已认证的用户身份，包含用户信息和权限列表
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

// SecurityContextHolder：安全上下文持有器（线程本地变量）
// 存储当前请求的认证信息，后续代码通过它获取“当前登录用户”
import org.springframework.security.core.context.SecurityContextHolder;

// UserDetails：Spring Security 的用户详情接口，包含用户名、密码、权限等
import org.springframework.security.core.userdetails.UserDetails;
// UserDetailsService：根据用户名加载用户详情的服务接口
import org.springframework.security.core.userdetails.UserDetailsService;

// WebAuthenticationDetailsSource：构建 Web 认证详情（包含 IP、SessionId 等）
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

// @Component：注册为 Spring Bean
import org.springframework.stereotype.Component;

// OncePerRequestFilter：保证每个请求只过滤一次的基类过滤器
// 继承它比直接实现 Filter 更安全，避免重复过滤（如转发、重定向时）
import org.springframework.web.filter.OncePerRequestFilter;

// ========== 导入 Java 标准库 ==========

// IOException：输入输出异常，Servlet 操作中可能抛出
import java.io.IOException;

/**
 * JWT 认证过滤器 —— 拦截每个 HTTP 请求，检查是否携带有效的 JWT Token
 *
 * 工作流程：
 * 1. 从请求头 Authorization 中提取 Token
 * 2. 验证 Token 是否有效（签名正确、未过期）
 * 3. 从 Token 中提取用户名，加载用户详情
 * 4. 将认证信息存入 SecurityContext，后续代码就知道“当前用户是谁”
 * 5. 将请求传递给下一个过滤器
 */
@Component  // 注册为 Spring Bean
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    // ↑ 继承 OncePerRequestFilter：保证每个 HTTP 请求只执行一次过滤

    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    /**
     * 构造函数注入 —— Spring 启动时自动传入依赖的 Bean
     */
    public JwtAuthenticationFilter(JwtUtil jwtUtil, UserDetailsService userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    /**
     * 核心过滤方法 —— 每个 HTTP 请求都会经过这里
     *
     * @param request    HTTP 请求对象
     * @param response   HTTP 响应对象
     * @param filterChain 过滤器链，调用 doFilter() 将请求传递给下一个过滤器
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        // 第一步：从请求头中获取 Authorization 字段
        // 客户端发送请求时应设置为：Authorization: Bearer <token>
        String authHeader = request.getHeader("Authorization");

        // 第二步：检查 Authorization 头是否存在，以及是否以 "Bearer " 开头
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            // 没有 Token 或格式不对，直接放行（交给后续的安全规则判断是否允许访问）
            filterChain.doFilter(request, response);
            return;  // 结束当前方法
        }

        // 第三步：截取 "Bearer " 后面的部分，得到纯 Token 字符串
        // "Bearer eyJhbGci..." → "eyJhbGci..."
        String token = authHeader.substring(7);  // "Bearer " 长度为 7

        // 第四步：验证 Token 是否有效
        if (jwtUtil.validateToken(token)) {
            // Token 有效，提取用户名
            String username = jwtUtil.extractUsername(token);

            // 根据用户名加载完整的用户详情（从数据库查询）
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            // 第五步：创建认证令牌，表示“这个用户已经通过认证”
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                            userDetails,     // 已认证的用户对象
                            null,            // 凭证（已用 Token 认证，不需要密码）
                            userDetails.getAuthorities()  // 用户权限列表（如 ROLE_USER）
                    );
            // 设置认证详情（包含客户端 IP、SessionId 等附加信息）
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            // 第六步：将认证信息存入 SecurityContext（线程本地变量）
            // 后续代码可通过 SecurityContextHolder.getContext().getAuthentication() 获取当前用户
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        // 第七步：无论 Token 是否有效，都将请求传递给下一个过滤器
        // 如果 Token 无效，SecurityContext 中没有认证信息，
        // 后续的 Security 规则会拒绝访问并返回 401
        filterChain.doFilter(request, response);
    }
}
