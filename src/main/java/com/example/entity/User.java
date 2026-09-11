// 声明当前类属于 entity 包
// entity 包存放 JPA 实体类，每个实体对应数据库中的一张表
package com.example.entity;

// ========== 导入 JPA 注解 ==========
// jakarta.persistence 是 Java EE 的 JPA 规范包（Spring Boot 3.x 使用 jakarta 命名空间）
// * 通配符表示导入该包下的所有注解，包括 @Entity、@Table、@Id、@Column 等
import jakarta.persistence.*;

/**
 * 用户实体类 —— 对应数据库中的 users 表
 *
 * JPA（Java Persistence API）通过这个类的注解自动建表：
 * - 类名 User → 表名 users（由 @Table 指定）
 * - 每个字段 → 表中的一列
 * - 字段类型 → 列的数据类型（String → VARCHAR，Long → BIGINT 等）
 */
@Entity   // 标记为 JPA 实体，Spring Data JPA 会管理它的生命周期
@Table(name = "users")  // 指定对应的数据库表名为 "users"
public class User {

    @Id   // 标记为主键字段
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // @GeneratedValue：主键生成策略
    // IDENTITY 表示由数据库自动生成自增 ID（如 1, 2, 3...）
    private Long id;

    @Column(unique = true, nullable = false, length = 50)
    // @Column：定义列的约束条件
    // unique=true  → 用户名不能重复
    // nullable=false → 不能为空
    // length=50    → 最大长度 50 个字符
    private String username;

    @Column(nullable = false)
    // 密码字段，存储的是 BCrypt 加密后的哈希值（不是明文）
    private String password;

    @Column(length = 100)
    // 邮箱字段，最大长度 100 个字符，允许为空
    private String email;

    /**
     * 无参构造函数 —— JPA 创建实体实例时需要
     */
    public User() {
    }

    /**
     * 全参构造函数 —— 方便在代码中创建用户对象
     */
    public User(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }

    // ===== id 的 getter/setter =====
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
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
