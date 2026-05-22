package io.github.hiwepy.opencli.spring.boot;

import io.github.hiwepy.opencli.OpenCliClient;
import io.github.hiwepy.opencli.adapter.browser.chatgpt.ChatgptOpenCliClient;
import io.github.hiwepy.opencli.adapter.browser.claude.ClaudeOpenCliClient;
import io.github.hiwepy.opencli.adapter.desktop.codex.CodexOpenCliClient;
import io.github.hiwepy.opencli.adapter.desktop.cursor.CursorOpenCliClient;
import io.github.hiwepy.opencli.browser.OpenCliBrowserClient;
import io.github.hiwepy.opencli.center.ws.OpenCliWsReverseAgentClient;
import io.github.hiwepy.opencli.core.OpenCliExecutor;
import io.github.hiwepy.opencli.meta.OpenCliMetaClient;
import java.util.ArrayList;
import java.util.List;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

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
        applyBrowserProfilePrefix(properties);
        return new OpenCliExecutor(properties);
    }

    /**
     * 将 {@code opencli.browser-profile} 写入 {@code leadingArguments}（{@code --profile}）。
     */
    private static void applyBrowserProfilePrefix(OpenCliStarterProperties properties) {
        if (!StringUtils.hasText(properties.getBrowserProfile())) {
            return;
        }
        List<String> leading = properties.getLeadingArguments();
        if (leading == null) {
            leading = new ArrayList<>();
            properties.setLeadingArguments(leading);
        }
        boolean hasProfile = false;
        for (String token : leading) {
            if ("--profile".equals(token)) {
                hasProfile = true;
                break;
            }
        }
        if (!hasProfile) {
            leading.add(0, "--profile");
            leading.add(1, properties.getBrowserProfile().trim());
        }
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

    /**
     * 可选：中心反向 WebSocket Agent（{@code opencli.center.ws.enabled=true}）。
     */
    @Bean(destroyMethod = "close")
    @ConditionalOnProperty(prefix = "opencli.center.ws", name = "enabled", havingValue = "true")
    @ConditionalOnMissingBean
    public OpenCliWsReverseAgentClient openCliWsReverseAgentClient(
            OpenCliStarterProperties properties, OpenCliExecutor executor) {
        return new OpenCliWsReverseAgentClient(properties, properties.getCenter().getConnection());
    }

    /** 可选门面 Bean：{@code opencli.facade.beans.enabled=true} 时注册常用强类型客户端。 */
    @Configuration
    @ConditionalOnProperty(prefix = "opencli.facade.beans", name = "enabled", havingValue = "true")
    static class OpenCliFacadeBeansConfiguration {

        @Bean
        @ConditionalOnMissingBean
        public OpenCliMetaClient openCliMetaClient(OpenCliClient client) {
            return client.meta();
        }

        @Bean
        @ConditionalOnMissingBean
        public OpenCliBrowserClient openCliBrowserClient(OpenCliClient client) {
            return client.browser();
        }

        @Bean
        @ConditionalOnMissingBean
        public CodexOpenCliClient codexOpenCliClient(OpenCliClient client) {
            return client.codex();
        }

        @Bean
        @ConditionalOnMissingBean
        public CursorOpenCliClient cursorOpenCliClient(OpenCliClient client) {
            return client.cursor();
        }

        @Bean
        @ConditionalOnMissingBean
        public ClaudeOpenCliClient claudeOpenCliClient(OpenCliClient client) {
            return client.claude();
        }

        @Bean
        @ConditionalOnMissingBean
        public ChatgptOpenCliClient chatgptOpenCliClient(OpenCliClient client) {
            return client.chatgpt();
        }
    }
}
