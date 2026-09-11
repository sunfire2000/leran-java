// 声明当前类属于 service 包
// service 包存放业务逻辑层，处理具体的业务规则
package com.example.service;

// 导入项目内部类
import com.example.entity.Person;
import com.example.exception.BusinessException;
import com.example.repository.PersonRepository;

// @Service：标记为业务服务组件
import org.springframework.stereotype.Service;

// 导入分页相关类
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

// 导入 Java 标准库
import java.util.List;

/**
 * 人员业务服务
 *
 * 职责：
 * 1. 处理人员相关的业务逻辑
 * 2. 调用 Repository 进行数据操作
 * 3. 数据校验和转换
 */
@Service
public class PersonService {

    // 人员数据访问接口，通过构造函数注入
    private final PersonRepository personRepository;

    /**
     * 构造函数注入
     */
    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    /**
     * 新增人员
     *
     * @param person 人员信息
     * @return 保存后的人员对象（包含自动生成的ID）
     */
    public Person create(Person person) {
        return personRepository.save(person);
    }

    /**
     * 根据ID查询人员
     *
     * @param id 人员ID
     * @return 人员对象
     * @throws BusinessException 人员不存在时抛出 404 业务异常
     *
     * 改造说明：不再返回 null，而是用 Optional.orElseThrow 直接抛异常。
     * 好处：调用方（Controller）不用写 if (person == null) 判断，
     *       异常会被 GlobalExceptionHandler 统一捕获并返回标准错误响应。
     */
    public Person getById(Long id) {
        // orElseThrow：Optional 里有值就返回，没值就抛出指定异常
        // 等价于：if (!optional.isPresent()) throw new BusinessException(...); return optional.get();
        return personRepository.findById(id)
                .orElseThrow(() -> new BusinessException(404, "人员不存在"));
    }

    /**
     * 查询所有人员（分页）
     *
     * @param page 页码（从0开始）
     * @param size 每页大小
     * @param name 姓名模糊搜索（可选）
     * @param gender 性别筛选（可选）
     * @return 分页结果
     */
    public Page<Person> list(int page, int size, String name, String gender) {
        // 创建分页对象，按ID降序排列
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());

        // 根据条件查询
        if (name != null && !name.isEmpty()) {
            return personRepository.findByNameContaining(name, pageable);
        }
        if (gender != null && !gender.isEmpty()) {
            return personRepository.findByGender(gender, pageable);
        }

        // 无条件查询所有
        return personRepository.findAll(pageable);
    }

    /**
     * 更新人员
     *
     * @param id     人员ID
     * @param person 更新的人员信息
     * @return 更新后的人员对象
     * @throws BusinessException ID 为空或人员不存在时抛出业务异常
     */
    public Person update(Long id, Person person) {
        // ID 为空是请求参数问题，返回 400
        if (id == null) {
            throw new BusinessException(400, "ID不能为空");
        }
        // 先查询是否存在，不存在返回 404
        if (!personRepository.existsById(id)) {
            throw new BusinessException(404, "人员不存在");
        }
        // 设置ID，JPA 会根据ID判断是更新而不是新增
        person.setId(id);
        return personRepository.save(person);
    }

    /**
     * 删除人员
     *
     * @param id 人员ID
     * @throws BusinessException 人员不存在时抛出 404 业务异常
     *
     * 改造说明：不再返回 boolean 表示成败，删除失败直接抛异常。
     * 方法能正常执行完 = 成功；执行不完 = 异常。这是 Java 异常机制的设计初衷。
     */
    public void delete(Long id) {
        if (!personRepository.existsById(id)) {
            throw new BusinessException(404, "人员不存在");
        }
        personRepository.deleteById(id);
    }
}
