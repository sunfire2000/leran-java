// 声明当前类属于 entity 包
// entity 包存放数据库实体类，与数据库表一一对应
package com.example.entity;

// ========== 导入 JPA 注解 ==========

// @Entity：标记为 JPA 实体类，Hibernate 会自动将其映射到数据库表
import jakarta.persistence.Entity;
// @Table：指定映射的表名
import jakarta.persistence.Table;
// @Id：标记主键字段
import jakarta.persistence.Id;
// @GeneratedValue：主键生成策略
import jakarta.persistence.GenerationType;
import jakarta.persistence.GeneratedValue;
// @Column：指定列的属性
import jakarta.persistence.Column;

// @Schema：Swagger 注解，用于在 API 文档中描述字段含义和示例值
import io.swagger.v3.oas.annotations.media.Schema;

// ========== 导入 Bean Validation 校验注解（JSR-303 规范） ==========

// @NotBlank：字符串不能为 null，且去除首尾空格后长度必须大于 0
import jakarta.validation.constraints.NotBlank;
// @Size：字符串长度限制
import jakarta.validation.constraints.Size;
// @Min / @Max：数值范围限制
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;
// @Pattern：正则表达式校验（注意：null 值视为通过，所以选填字段也可以安全使用）
import jakarta.validation.constraints.Pattern;
// @Email：邮箱格式校验
import jakarta.validation.constraints.Email;

/**
 * 人员实体类
 *
 * 对应数据库表：person
 * 用于演示增删改查（CRUD）操作
 */
@Schema(description = "人员信息")
@Entity  // 标记为 JPA 实体
@Table(name = "person")  // 对应数据库表名
public class Person {

    /**
     * 主键，自增
     */
    @Schema(description = "主键ID（新增时不用传，更新/删除时必传）", example = "1")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 姓名（必填，最长 50 个字符）
     */
    @Schema(description = "姓名", example = "张三")
    @NotBlank(message = "姓名不能为空")
    @Size(max = 50, message = "姓名不能超过 50 个字符")
    @Column(nullable = false, length = 50)
    private String name;

    /**
     * 性别（选填，但传了就必须是"男"或"女"）
     */
    @Schema(description = "性别", example = "男")
    @Pattern(regexp = "男|女", message = "性别只能是 男 或 女")
    @Column(length = 10)
    private String gender;

    /**
     * 年龄（选填，0-150 之间）
     */
    @Schema(description = "年龄", example = "25")
    @Min(value = 0, message = "年龄不能小于 0")
    @Max(value = 150, message = "年龄不能超过 150")
    @Column
    private Integer age;

    /**
     * 手机号（选填，但传了就必须是合法的大陆手机号格式）
     */
    @Schema(description = "手机号", example = "13800138000")
    @Pattern(regexp = "^1\\d{10}$", message = "手机号格式不正确")
    @Column(length = 20)
    private String phone;

    /**
     * 邮箱（选填，但传了就必须是合法邮箱格式）
     */
    @Schema(description = "邮箱", example = "zhangsan@example.com")
    @Email(message = "邮箱格式不正确")
    @Column(length = 100)
    private String email;

    /**
     * 地址（选填，最长 255 个字符）
     */
    @Schema(description = "地址", example = "北京市朝阳区")
    @Size(max = 255, message = "地址不能超过 255 个字符")
    @Column(length = 255)
    private String address;

    // ========== 无参构造函数（JPA 必需） ==========
    public Person() {
    }

    // ========== 全参构造函数 ==========
    public Person(String name, String gender, Integer age, String phone, String email, String address) {
        this.name = name;
        this.gender = gender;
        this.age = age;
        this.phone = phone;
        this.email = email;
        this.address = address;
    }

    // ========== Getter 和 Setter ==========

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
