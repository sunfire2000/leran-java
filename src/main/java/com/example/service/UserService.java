// 声明当前类属于 service 包
// service 包存放业务逻辑层，负责处理核心业务规则（注册、登录、数据处理等）
package com.example.service;

// ========== 导入项目内部类 ==========

// 导入登录请求 DTO，封装登录时前端传来的数据
import com.example.dto.LoginRequest;
// 导入注册请求 DTO，封装注册时前端传来的数据
import com.example.dto.RegisterRequest;
// 导入用户实体类，对应数据库中的 users 表
import com.example.entity.User;
// 导入用户数据访问层，负责与数据库交互
import com.example.repository.UserRepository;
// 导入 JWT 工具类，用于登录成功后生成 Token
import com.example.util.JwtUtil;

// ========== 导入 Spring Security ==========

// PasswordEncoder：密码编码器接口，用于加密和校验密码
import org.springframework.security.crypto.password.PasswordEncoder;

// ========== 导入 Spring 注解 ==========

// @Service：告诉 Spring “这是一个业务服务组件”
// Spring 启动时会自动创建实例并注册到容器中，其他类可以通过构造函数注入使用
import org.springframework.stereotype.Service;

// ========== 导入 Java 标准库 ==========

// HashMap：键值对集合，用于组装返回给前端的数据
import java.util.HashMap;
// Map：键值对接口
import java.util.Map;

/**
 * 用户业务服务 —— 处理注册和登录的核心业务逻辑
 *
 * 业务层是 Controller 和 Repository 之间的中间层：
 * Controller（接收请求）→ Service（处理业务）→ Repository（操作数据库）
 */
@Service  // 注册为 Spring Bean
public class UserService {

    // 用户数据访问层 —— 用于查询/保存用户数据
    private final UserRepository userRepository;
    // 密码编码器 —— 用于加密明文密码和校验密码
    private final PasswordEncoder passwordEncoder;
    // JWT 工具类 —— 用于登录成功后生成 Token
    private final JwtUtil jwtUtil;

    /**
     * 构造函数注入 —— Spring 启动时自动传入这三个 Bean
     *
     * 为什么用构造函数注入而不是 @Autowired？
     * - 字段可以声明为 final，保证不可变
     * - 依赖关系明确，方便单元测试
     * - Spring 官方推荐的方式
     */
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    /**
     * 用户注册
     *
     * 业务流程：
     * 1. 检查用户名是否已存在
     * 2. 将密码用 BCrypt 加密（数据库中不存储明文密码）
     * 3. 保存用户到数据库
     * 4. 返回用户基本信息（不含密码）
     *
     * @param request 注册请求 DTO，包含 username、password、email
     * @return 注册成功的用户信息（id、username、email）
     * @throws RuntimeException 如果用户名已存在
     */
    public Map<String, Object> register(RegisterRequest request) {
        // 第一步：检查用户名是否已被注册
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("用户名已存在");
        }

        // 第二步：创建用户实体并设置属性
        User user = new User();
        user.setUsername(request.getUsername());
        // 重点：密码必须加密后存储！BCrypt 每次加密结果不同，但都能正确校验
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEmail(request.getEmail());

        // 第三步：保存到数据库（JPA 会自动生成 INSERT SQL）
        userRepository.save(user);

        // 第四步：组装返回数据（不包含密码，避免泄露）
        Map<String, Object> result = new HashMap<>();
        result.put("id", user.getId());            // 数据库自动生成的用户 ID
        result.put("username", user.getUsername()); // 用户名
        result.put("email", user.getEmail());       // 邮箱
        return result;
    }

    /**
     * 用户登录
     *
     * 业务流程：
     * 1. 根据用户名查找用户
     * 2. 校验密码是否匹配
     * 3. 生成 JWT Token 返回给客户端
     *
     * @param request 登录请求 DTO，包含 username、password
     * @return 包含 token 和用户名的 Map
     * @throws RuntimeException 如果用户名不存在或密码错误
     */
    public Map<String, Object> login(LoginRequest request) {
        // 第一步：根据用户名查找用户
        // findByUsername 返回 Optional<User>，用 orElseThrow 处理“找不到”的情况
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("用户名或密码错误"));
        // ↑ 注意：这里故意不说“用户名不存在”，防止攻击者探测哪些用户名已注册

        // 第二步：校验密码
        // passwordEncoder.matches(明文, 哈希值)：将输入的明文与数据库中的哈希值比对
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("用户名或密码错误");
        }

        // 第三步：密码正确，生成 JWT Token
        String token = jwtUtil.generateToken(user.getUsername());

        // 第四步：组装返回数据
        Map<String, Object> result = new HashMap<>();
        result.put("token", token);                  // JWT Token，客户端后续请求需携带它
        result.put("username", user.getUsername());   // 用户名
        return result;
    }
}
