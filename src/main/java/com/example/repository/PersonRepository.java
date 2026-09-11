// 声明当前类属于 repository 包
// repository 包存放数据访问层接口，负责与数据库交互
package com.example.repository;

// 导入 Person 实体类
import com.example.entity.Person;

// JpaRepository：Spring Data JPA 提供的接口，继承后自动获得 CRUD 方法
import org.springframework.data.jpa.repository.JpaRepository;
// @Repository：标记为仓库组件，Spring 自动管理
import org.springframework.stereotype.Repository;

// 导入分页相关类
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * 人员数据访问接口
 *
 * 继承 JpaRepository 后自动获得以下方法：
 * - save(entity)       保存（新增/更新）
 * - findById(id)       根据ID查询
 * - findAll()          查询所有
 * - deleteById(id)     根据ID删除
 * - count()            统计数量
 */
@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {

    /**
     * 根据姓名模糊查询（自动推导 SQL）
     * Spring Data JPA 根据方法名自动生成：WHERE name LIKE %?%
     */
    Page<Person> findByNameContaining(String name, Pageable pageable);

    /**
     * 根据性别查询
     */
    Page<Person> findByGender(String gender, Pageable pageable);
}
