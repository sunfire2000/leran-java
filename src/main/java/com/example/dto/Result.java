// 声明当前类属于 dto 包
package com.example.dto;

/**
 * 统一 API 响应封装类
 *
 * 所有接口都返回相同格式的 JSON，方便前端统一处理：
 * {
 *   "code": 200,           // 状态码：200=成功，其他=失败
 *   "message": "操作成功",  // 提示信息
 *   "data": { ... }        // 实际数据（泛型 T，可以是任意类型）
 * }
 *
 * 使用泛型 <T> 使得 data 字段可以是任意类型（Map、List、自定义对象等）
 * 例如：Result<String> 表示 data 是字符串，Result<Map<...>> 表示 data 是 Map
 */
public class Result<T> {

    // 响应状态码：200 表示成功，其他值表示不同类型的错误
    private int code;
    // 响应消息：给前端/客户端看的提示文本
    private String message;
    // 响应数据：实际的业务数据，类型由泛型 T 决定
    private T data;

    /**
     * 无参构造函数 —— JSON 序列化时需要
     */
    public Result() {
    }

    /**
     * 全参构造函数
     */
    public Result(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    /**
     * 静态工厂方法 —— 快速构建成功响应（默认消息“操作成功”）
     * 用法：Result.success(data) 返回 {code:200, message:"操作成功", data:data}
     */
    public static <T> Result<T> success(T data) {
        return new Result<>(200, "操作成功", data);
    }

    /**
     * 静态工厂方法 —— 快速构建成功响应（自定义消息）
     * 用法：Result.success("注册成功", data)
     */
    public static <T> Result<T> success(String message, T data) {
        return new Result<>(200, message, data);
    }

    /**
     * 静态工厂方法 —— 快速构建错误响应
     * 用法：Result.error(400, "用户名已存在")
     *
     * @param code    错误状态码（如 400=参数错误，401=未授权）
     * @param message 错误信息
     */
    public static <T> Result<T> error(int code, String message) {
        return new Result<>(code, message, null);  // 错误时 data 为 null
    }

    // ===== code 的 getter/setter =====
    public int getCode() {
        return code;
    }
    public void setCode(int code) {
        this.code = code;
    }

    // ===== message 的 getter/setter =====
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }

    // ===== data 的 getter/setter =====
    public T getData() {
        return data;
    }
    public void setData(T data) {
        this.data = data;
    }
}
