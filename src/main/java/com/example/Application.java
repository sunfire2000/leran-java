// 声明当前类所属的包路径
// com.example 是根包，Spring Boot 会扫描该包及其所有子包下的组件
package com.example;

// ========== 导入 Spring Boot 核心类 ==========

// SpringApplication：Spring Boot 的启动引导类
// 负责创建 Spring 应用上下文、启动内嵌 Web 服务器、触发自动配置等
import org.springframework.boot.SpringApplication;

// @SpringBootApplication：Spring Boot 的核心组合注解，一个注解顶三个：
//   1. @Configuration    —— 标记为配置类，可以定义Bean
//   2. @EnableAutoConfiguration —— 开启自动配置，Spring Boot 根据依赖自动推断配置
//   3. @ComponentScan    —— 组件扫描，自动发现 @Controller、@Service、@Repository 等
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 应用程序启动类
 *
 * 这是整个 Spring Boot 项目的入口，main 方法启动后：
 * 1. 初始化 Spring 应用上下文（IoC 容器）
 * 2. 扫描并注册所有组件（Controller、Service、Repository 等）
 * 3. 启动内嵌的 Tomcat 服务器（默认监听配置的端口）
 * 4. 应用就绪，开始接收 HTTP 请求
 *
 * 数据库使用文件模式，数据持久化到本地文件，重启后不丢失
 */
@SpringBootApplication
public class Application {

    /**
     * 主方法 - Java 应用程序的入口
     *
     * @param args 命令行参数，可通过命令行向应用传递配置（如 --server.port=9090）
     */
    public static void main(String[] args) {
        // SpringApplication.run() 做三件事：
        // 1. 创建 ApplicationContext（Spring 容器，管理所有 Bean 的生命周期）
        // 2. 启动内嵌 Web 服务器（默认 Tomcat）
        // 3. 阻塞等待，直到应用完全启动
        SpringApplication.run(Application.class, args);
    }
}
