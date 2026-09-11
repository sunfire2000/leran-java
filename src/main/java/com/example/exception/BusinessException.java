// 声明当前类属于 exception 包
// exception 包集中存放项目中所有自定义异常类
package com.example.exception;

/**
 * 业务异常类
 *
 * ========== 为什么要自定义异常？ ==========
 * 业务错误（如"用户名已存在"、"人员不存在"）和系统错误（如 NullPointerException）
 * 是两类完全不同的问题：
 *   - 系统错误：代码 bug，应该返回 500 并记录日志排查
 *   - 业务错误：用户操作问题，应该返回明确的错误码和中文提示
 * 自定义异常让这两类错误可以被"分类处理"。
 *
 * ========== 为什么继承 RuntimeException 而不是 Exception？ ==========
 * Java 异常分两类：
 *   1. 受检异常（Checked Exception，继承 Exception）：
 *      编译器强制要求 try-catch 或 throws 声明，不处理就编译不过
 *      适合"调用方必须处理"的场景，如 IOException
 *   2. 非受检异常（Unchecked Exception，继承 RuntimeException）：
 *      编译器不强制处理，适合"业务规则不满足"的场景
 * 业务异常如果强制 try-catch，每一层代码都要写throws，代码会被淹没，
 * 所以业界惯例：业务异常一律继承 RuntimeException。
 */
public class BusinessException extends RuntimeException {

    /**
     * 业务错误码
     * 与前端约定好的一套数字编码，前端根据 code 做不同处理：
     *   400 = 参数/请求错误
     *   401 = 未登录或登录过期（跳登录页）
     *   404 = 资源不存在
     *   500 = 服务器内部错误
     */
    private final int code;

    /**
     * 单参数构造函数 —— 默认错误码 400（最常见的业务错误）
     * 用法：throw new BusinessException("用户名已存在");
     */
    public BusinessException(String message) {
        this(400, message);
    }

    /**
     * 全参构造函数
     * 用法：throw new BusinessException(404, "人员不存在");
     *
     * super(message)：把错误信息传给父类 RuntimeException 保存，
     * 之后可以通过 getMessage() 取出来
     */
    public BusinessException(int code, String message) {
        super(message);
        this.code = code;
    }

    /**
     * 全局异常处理器（GlobalExceptionHandler）会通过这个方法取出错误码，
     * 放入统一响应 Result 中返回给前端
     */
    public int getCode() {
        return code;
    }
}
