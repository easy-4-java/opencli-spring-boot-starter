package io.github.hiwepy.opencli.spring.boot;

import io.github.hiwepy.opencli.OpenCliClient;
import io.github.hiwepy.opencli.core.OpenCliExecutor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 注册 OpenCLI Java SDK 在 Spring 环境下的单例 Bean。
 * <p>
 * 暴露共享 {@link OpenCliExecutor} 与 {@link OpenCliClient}，保证各门面复用同一执行器实例
 * （对远程 HTTP 连接池与超时配置尤为重要）。
 * </p>
 *
 * @author wandl
 * @since 1.0.0
 */
@Configuration
@ConditionalOnClass(OpenCliExecutor.class)
@EnableConfigurationProperties(OpenCliStarterProperties.class)
@ConditionalOnProperty(
        prefix = OpenCliStarterProperties.PREFIX,
        name = "enabled",
        havingValue = "true",
        matchIfMissing = true)
public class OpenCliAutoConfiguration {

    /**
     * 构造与配置绑定的 OpenCLI 执行器。
     *
     * @param properties {@code opencli.*} 绑定结果
     * @return 子进程或远程 Agent 的统一入口
     */
    @Bean
    @ConditionalOnMissingBean
    public OpenCliExecutor openCliExecutor(OpenCliStarterProperties properties) {
        return new OpenCliExecutor(properties);
    }

    /**
     * 构造 SDK 顶层客户端，注入已存在的执行器 Bean。
     *
     * @param properties 与执行器一致的配置
     * @param executor     共享执行器
     * @return {@link OpenCliClient}
     */
    @Bean
    @ConditionalOnMissingBean
    public OpenCliClient openCliClient(OpenCliStarterProperties properties, OpenCliExecutor executor) {
        return new OpenCliClient(properties, executor);
    }
}
