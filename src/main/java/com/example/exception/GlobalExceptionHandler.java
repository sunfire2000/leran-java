// 声明当前类属于 exception 包
package com.example.exception;

// 导入统一响应封装类
import com.example.dto.Result;

// ========== 导入 Spring MVC 异常处理注解 ==========

// @RestControllerAdvice：全局异常处理器的核心注解
// 它是 @ControllerAdvice + @ResponseBody 的组合：
//   - @ControllerAdvice：声明这个类可以"拦截"所有 Controller 抛出的异常（本质是 AOP 思想）
//   - @ResponseBody：返回值自动序列化为 JSON（和 @RestController 同理）
import org.springframework.web.bind.annotation.RestControllerAdvice;

// @ExceptionHandler：声明该方法处理哪种类型的异常
import org.springframework.web.bind.annotation.ExceptionHandler;

// MethodArgumentNotValidException：@Valid 参数校验失败时 Spring 抛出的异常
import org.springframework.web.bind.MethodArgumentNotValidException;
// FieldError：包含具体哪个字段、什么原因的校验失败信息
import org.springframework.validation.FieldError;

// HttpMessageNotReadableException：请求体 JSON 格式错误（如少个引号、类型不匹配）时抛出
import org.springframework.http.converter.HttpMessageNotReadableException;

/**
 * 全局异常处理器
 *
 * ========== 工作原理（AOP 思想） ==========
 * Spring 在调用 Controller 方法时套了一层"代理"：
 *
 *   请求 → Spring 代理 → Controller 方法
 *              ↑               ↓ 抛出异常
 *              └── 捕获异常 ←──┘
 *                  ↓
 *          查找 @RestControllerAdvice 类中
 *          标注了 @ExceptionHandler(对应异常.class) 的方法
 *                  ↓
 *          执行该方法，把返回值作为响应返回给前端
 *
 * 好处：Controller 里完全不用写 try-catch，异常处理逻辑集中在一个地方。
 *
 * ========== 异常匹配规则 ==========
 * 抛出一个异常时，Spring 会找"最精确匹配"的处理方法：
 *   抛出 BusinessException → 优先匹配 handleBusinessException
 *   抛出 NullPointerException → 没有精确匹配 → 沿继承链向上找 → 匹配 handleException(Exception)
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 处理参数校验失败（@Valid 注解校验不通过时抛出）
     *
     * 例如：新增人员时 name 为空字符串，@NotBlank 校验失败，
     * Spring 抛出 MethodArgumentNotValidException，进入此方法。
     *
     * 返回示例：{"code":400, "message":"姓名不能为空", "data":null}
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<Void> handleValidationException(MethodArgumentNotValidException e) {
        // 一个请求可能有多个字段校验失败，把所有错误信息拼接起来一次告诉前端
        StringBuilder sb = new StringBuilder();
        for (FieldError fieldError : e.getBindingResult().getFieldErrors()) {
            if (sb.length() > 0) {
                sb.append("；");
            }
            // fieldError.getDefaultMessage() 就是注解上写的 message，如"姓名不能为空"
            sb.append(fieldError.getDefaultMessage());
        }
        return Result.error(400, sb.toString());
    }

    /**
     * 处理业务异常（我们自己抛的 BusinessException）
     *
     * 例如：查询 ID=999 的人员，不存在，Service 抛出 BusinessException(404, "人员不存在")
     * 返回示例：{"code":404, "message":"人员不存在", "data":null}
     */
    @ExceptionHandler(BusinessException.class)
    public Result<Void> handleBusinessException(BusinessException e) {
        // 取出异常里携带的错误码和错误信息，原样返回给前端
        return Result.error(e.getCode(), e.getMessage());
    }

    /**
     * 处理请求体格式错误（JSON 语法错误、字段类型不匹配等）
     *
     * 例如：前端把 age 传成了字符串 "abc"，Jackson 反序列化失败
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public Result<Void> handleNotReadableException(HttpMessageNotReadableException e) {
        return Result.error(400, "请求体格式错误，请检查 JSON 格式和字段类型");
    }

    /**
     * 兜底处理：所有没被上面方法捕获的异常（系统级错误）
     *
     * 例如：NullPointerException、数据库连接失败等
     * 这些是"代码 bug"，不应该把真实错误信息暴露给前端（有安全风险），
     * 统一返回"服务器内部错误"，真实异常打印到后端日志供排查。
     */
    @ExceptionHandler(Exception.class)
    public Result<Void> handleException(Exception e) {
        // 打印完整异常堆栈到控制台/日志，方便开发者定位问题
        // 第 2 课会换成规范的日志写法
        e.printStackTrace();
        return Result.error(500, "服务器内部错误，请稍后重试");
    }
}
