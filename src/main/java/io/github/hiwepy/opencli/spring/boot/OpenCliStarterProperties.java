package io.github.hiwepy.opencli.spring.boot;

import io.github.hiwepy.opencli.OpenCliProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * OpenCLI Spring Boot 配置绑定。
 * <p>
 * 继承 SDK {@link OpenCliProperties}，应用侧以 {@code opencli.*} 配置
 * 本地可执行文件、超时、环境变量，以及 {@code execution-target}、{@code remote-agent-base-url}
 * 等远程 Agent 选项。
 * </p>
 *
 * @author wandl
 * @since 1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ConfigurationProperties(prefix = OpenCliStarterProperties.PREFIX)
public class OpenCliStarterProperties extends OpenCliProperties {

    /**
     * 配置前缀，与 {@link OpenCliProperties} 文档中的 Spring 绑定示例一致。
     */
    public static final String PREFIX = "opencli";

    /**
     * 是否启用本 Starter 的自动配置 Bean。
     */
    private boolean enabled = true;

    /**
     * 是否在应用启动时执行本机 {@code opencli list} 探测（远程 Agent 模式自动跳过）。
     */
    private boolean startupCheckEnabled = true;

    /**
     * 启动探测失败时是否中断应用启动；默认 false 仅打 WARN，生产可设为 true。
     */
    private boolean failFastOnUnavailable = false;

    /**
     * Chrome profile/context 别名，映射为 {@link #getLeadingArguments()} 的 {@code --profile <name>} 前缀。
     */
    private String browserProfile;

    @org.springframework.boot.context.properties.NestedConfigurationProperty
    private OpenCliCenterWsProperties center = new OpenCliCenterWsProperties();
}
