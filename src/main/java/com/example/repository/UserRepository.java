// 声明当前类属于 repository 包
// repository 包存放数据访问层接口，负责与数据库交互（CRUD 操作）
package com.example.repository;

// ========== 导入项目内部类 ==========

// 导入 User 实体类，告诉 JpaRepository 操作的是哪张表
import com.example.entity.User;

// ========== 导入 Spring Data JPA ==========

// JpaRepository：Spring Data JPA 提供的核心接口
// 继承它后自动获得 save、findById、findAll、delete 等通用 CRUD 方法
// 无需手写 SQL，Spring 会根据方法名自动生成 SQL 查询
import org.springframework.data.jpa.repository.JpaRepository;

// @Repository：告诉 Spring “这是一个数据访问组件”
// Spring 会自动创建该接口的实现类（通过动态代理），并注册到容器中
import org.springframework.stereotype.Repository;

// ========== 导入 Java 标准库 ==========

// Optional：Java 8 引入的容器类，表示“值可能存在也可能不存在”
// 比直接返回 null 更安全，强制调用方处理“找不到”的情况
import java.util.Optional;

/**
 * 用户数据访问接口
 *
 * 这是一个接口，不需要写实现类！
 * Spring Data JPA 会在运行时自动生成实现类（通过动态代理）。
 *
 * 继承 JpaRepository<User, Long> 后自动获得：
 * - save(user)         → 插入或更新用户
 * - findById(id)       → 根据 ID 查询
 * - findAll()          → 查询所有用户
 * - deleteById(id)     → 根据 ID 删除
 * - count()            → 统计总数
 * ... 等等
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // ↑ JpaRepository<User, Long> 中：
    //   User = 操作的实体类型（对应 users 表）
    //   Long = 主键的类型（对应 User.id 字段）

    /**
     * 根据用户名查询用户
     *
     * Spring Data JPA 会根据方法名自动生成 SQL：
     * SELECT * FROM users WHERE username = ?
     *
     * 返回 Optional<User>：
     * - 找到用户 → Optional.of(user)
     * - 未找到  → Optional.empty()
     * 调用方可用 .orElseThrow() 或 .ifPresent() 安全处理
     */
    Optional<User> findByUsername(String username);

    /**
     * 检查用户名是否已存在
     *
     * 自动生成 SQL：SELECT COUNT(*) > 0 FROM users WHERE username = ?
     * 返回 true=已存在，false=不存在
     */
    boolean existsByUsername(String username);
}
