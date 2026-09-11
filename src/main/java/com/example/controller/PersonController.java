// 声明当前类属于 controller 包
package com.example.controller;

// 导入项目内部类
import com.example.entity.Person;
import com.example.dto.Result;
import com.example.exception.BusinessException;
import com.example.service.PersonService;

// 导入 Spring MVC 注解
import org.springframework.web.bind.annotation.*;

// @Valid：加在 Controller 方法参数上，触发 Bean Validation 校验
// 校验失败会抛出 MethodArgumentNotValidException，被全局异常处理器捕获
import jakarta.validation.Valid;

// 导入分页相关类
import org.springframework.data.domain.Page;

// 导入 Swagger 注解
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;

// 导入 Java 标准库
import java.util.HashMap;
import java.util.Map;

/**
 * 人员管理控制器
 *
 * 提供人员的增删改查（CRUD）接口
 * 基础路径：/api/persons
 *
 * 接口规范：查询用 GET，增删改用 POST
 */
@Tag(name = "人员管理", description = "人员的增删改查接口")
@RestController
@RequestMapping("/api/persons")
public class PersonController {

    // 人员业务服务，通过构造函数注入
    private final PersonService personService;

    /**
     * 构造函数注入
     */
    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    /**
     * 新增人员
     *
     * 请求方式：POST /api/persons/add
     * 请求体示例：{"name":"张三","gender":"男","age":25,"phone":"13800138000","email":"zhang@example.com","address":"北京市"}
     */
    @Operation(summary = "新增人员", description = "创建一个新的人员记录")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "创建成功"),
        @ApiResponse(responseCode = "400", description = "参数错误")
    })
    @PostMapping("/add")
    public Result<Person> add(@Valid @RequestBody Person person) {
        // @Valid 会在进入方法前自动校验 person 各字段上的注解（@NotBlank 等）
        // 校验失败直接抛异常，根本走不到这里，所以方法里一行校验代码都不用写
        Person saved = personService.create(person);
        return Result.success("创建成功", saved);
    }

    /**
     * 根据ID查询人员
     *
     * 请求方式：GET /api/persons/detail?id=1
     */
    @Operation(summary = "查询人员详情", description = "根据ID查询单个人员信息")
    @GetMapping("/detail")
    public Result<Person> detail(@Parameter(description = "人员ID", example = "1") @RequestParam Long id) {
        // 人员不存在时 Service 会抛出 BusinessException(404)，
        // 全局异常处理器自动转为 {"code":404,"message":"人员不存在"}，这里不用写任何判断
        return Result.success(personService.getById(id));
    }

    /**
     * 查询人员列表（分页 + 搜索）
     *
     * 请求方式：GET /api/persons/list
     */
    @Operation(summary = "查询人员列表", description = "分页查询人员列表，支持姓名搜索和性别筛选")
    @GetMapping("/list")
    public Result<Map<String, Object>> list(
            @Parameter(description = "页码，从0开始", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "每页大小", example = "10") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "姓名模糊搜索", example = "张") @RequestParam(required = false) String name,
            @Parameter(description = "性别筛选", example = "男") @RequestParam(required = false) String gender) {

        Page<Person> pageResult = personService.list(page, size, name, gender);

        // 封装分页结果
        Map<String, Object> data = new HashMap<>();
        data.put("list", pageResult.getContent());       // 当前页数据
        data.put("total", pageResult.getTotalElements()); // 总条数
        data.put("pages", pageResult.getTotalPages());    // 总页数
        data.put("page", pageResult.getNumber());         // 当前页码
        data.put("size", pageResult.getSize());           // 每页大小

        return Result.success(data);
    }

    /**
     * 更新人员
     *
     * 请求方式：POST /api/persons/update
     * 请求体示例：{"id":1,"name":"李四","gender":"女","age":26}
     */
    @Operation(summary = "更新人员", description = "根据ID更新人员信息")
    @PostMapping("/update")
    public Result<Person> update(@Valid @RequestBody Person person) {
        // 校验交给 @Valid；ID 为空、人员不存在等情况由 Service 抛异常
        Person updated = personService.update(person.getId(), person);
        return Result.success("更新成功", updated);
    }

    /**
     * 删除人员
     *
     * 请求方式：POST /api/persons/delete
     * 请求体示例：{"id":1}
     */
    @Operation(summary = "删除人员", description = "根据ID删除人员")
    // delete 接口用 Map 接收请求体，Swagger 无法自动推断结构，手动指定请求体示例
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
        description = "删除请求体",
        content = @Content(
            mediaType = "application/json",
            examples = @ExampleObject(value = "{\"id\": 1}")
        )
    )
    @PostMapping("/delete")
    public Result<Void> delete(@RequestBody Map<String, Long> params) {
        Long id = params.get("id");
        if (id == null) {
            // 手动抛业务异常的写法：和 Service 里抛的异常一样会被全局处理器捕获
            throw new BusinessException("ID不能为空");
        }
        // Service 的 delete 返回 void：能执行完就是成功，失败会抛异常
        personService.delete(id);
        return Result.success("删除成功", null);
    }
}
