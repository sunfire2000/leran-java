// 声明当前类属于 config 包
package com.example.config;

// 导入 SpringDoc OpenAPI 相关类
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.Components;

// 导入 Spring 注解
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Swagger / OpenAPI 配置
 *
 * 访问地址：http://localhost:8081/swagger-ui.html
 *
 * 功能：
 * - 自动扫描所有 Controller 生成 API 文档
 * - 支持在线调试接口
 * - 支持 JWT Token 认证
 */
@Configuration
public class SwaggerConfig {

    /**
     * 配置 OpenAPI 文档信息
     */
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                // 文档基本信息
                .info(new Info()
                        .title("Learn Java API 文档")       // 标题
                        .description("Spring Boot + JWT + MySQL 示例项目") // 描述
                        .version("1.0.0")                   // 版本
                        .contact(new Contact()
                                .name("开发者")
                                .email("dev@example.com")))
                // 配置 JWT 认证
                .components(new Components()
                        .addSecuritySchemes("bearer-jwt", new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .description("输入 JWT Token，格式：Bearer {token}")))
                // 全局添加安全要求（所有接口都可以使用 JWT）
                .addSecurityItem(new SecurityRequirement().addList("bearer-jwt"));
    }
}
