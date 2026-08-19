package io.github.easy4j.opencli.spring.boot;

import io.github.easy4j.opencli.OpenCliClient;
import io.github.easy4j.opencli.adapter.browser.chatgpt.ChatgptOpenCliClient;
import io.github.easy4j.opencli.adapter.browser.claude.ClaudeOpenCliClient;
import io.github.easy4j.opencli.adapter.browser.gemini.GeminiOpenCliClient;
import io.github.easy4j.opencli.adapter.browser.jimeng.JimengOpenCliClient;
import io.github.easy4j.opencli.adapter.desktop.codex.CodexOpenCliClient;
import io.github.easy4j.opencli.adapter.desktop.cursor.CursorOpenCliClient;
import io.github.easy4j.opencli.adapter.publicapi.arxiv.ArxivOpenCliClient;
import io.github.easy4j.opencli.adapter.publicapi.binance.BinanceOpenCliClient;
import io.github.easy4j.opencli.adapter.publicapi.npm.NpmOpenCliClient;
import io.github.easy4j.opencli.adapter.publicapi.wikipedia.WikipediaOpenCliClient;
import io.github.easy4j.opencli.browser.OpenCliBrowserClient;
import io.github.easy4j.opencli.center.ws.OpenCliWsReverseAgentClient;
import io.github.easy4j.opencli.core.OpenCliExecutor;
import io.github.easy4j.opencli.core.availability.OpenCliAvailabilityChecker;
import io.github.easy4j.opencli.meta.OpenCliMetaClient;
import java.util.ArrayList;
import java.util.List;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;

@Configuration
@ConditionalOnClass(OpenCliExecutor.class)
@EnableConfigurationProperties(OpenCliStarterProperties.class)
@ConditionalOnProperty(
        prefix = OpenCliStarterProperties.PREFIX,
        name = "enabled",
        havingValue = "true",
        matchIfMissing = true)
public class OpenCliAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public OpenCliExecutor openCliExecutor(OpenCliStarterProperties properties) {
        applyBrowserProfilePrefix(properties);
        return new OpenCliExecutor(properties);
    }

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

    @Bean
    @ConditionalOnMissingBean
    public OpenCliClient openCliClient(OpenCliStarterProperties properties, OpenCliExecutor executor) {
        return new OpenCliClient(properties, executor);
    }

    @Bean
    @ConditionalOnMissingBean
    public OpenCliAvailabilityChecker openCliAvailabilityChecker() {
        return new OpenCliAvailabilityChecker();
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(
            prefix = OpenCliStarterProperties.PREFIX,
            name = "startup-check-enabled",
            havingValue = "true",
            matchIfMissing = true)
    public OpenCliCliStartupChecker openCliCliStartupChecker(
            OpenCliExecutor openCliExecutor,
            OpenCliStarterProperties openCliProperties,
            OpenCliAvailabilityChecker availabilityChecker,
            Environment environment) {
        return new OpenCliCliStartupChecker(
                openCliExecutor, openCliProperties, availabilityChecker, environment);
    }

    @Bean(destroyMethod = "close")
    @ConditionalOnProperty(prefix = "opencli.center.ws", name = "enabled", havingValue = "true")
    @ConditionalOnMissingBean
    public OpenCliWsReverseAgentClient openCliWsReverseAgentClient(
            OpenCliStarterProperties properties, OpenCliExecutor executor) {
        return new OpenCliWsReverseAgentClient(properties, properties.getCenter().getConnection());
    }

 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
    /** 可选门面 Bean：{@code opencli.facade.beans.enabled=true} 时注册常用强类型客户端。 */
    @Configuration
    @ConditionalOnProperty(prefix = "opencli.facade.beans", name = "enabled", havingValue = "true")
    static class OpenCliFacadeBeansConfiguration {
        /**
         * <p>Open cli meta client.</p>
         * @param client the client
         * @return the open cli meta client
         */

        @Bean
        @ConditionalOnMissingBean
        public OpenCliMetaClient openCliMetaClient(OpenCliClient client) {
            return client.meta();
        }
        /**
         * <p>Open cli browser client.</p>
         * @param client the client
         * @return the open cli browser client
         */

        @Bean
        @ConditionalOnMissingBean
        public OpenCliBrowserClient openCliBrowserClient(OpenCliClient client) {
            return client.browser();
        }
        /**
         * <p>Codex open cli client.</p>
         * @param client the client
         * @return the codex open cli client
         */

        @Bean
        @ConditionalOnMissingBean
        public CodexOpenCliClient codexOpenCliClient(OpenCliClient client) {
            return client.codex();
        }
        /**
         * <p>Cursor open cli client.</p>
         * @param client the client
         * @return the cursor open cli client
         */

        @Bean
        @ConditionalOnMissingBean
        public CursorOpenCliClient cursorOpenCliClient(OpenCliClient client) {
            return client.cursor();
        }
        /**
         * <p>Claude open cli client.</p>
         * @param client the client
         * @return the claude open cli client
         */

        @Bean
        @ConditionalOnMissingBean
        public ClaudeOpenCliClient claudeOpenCliClient(OpenCliClient client) {
            return client.claude();
        }
        /**
         * <p>Chatgpt open cli client.</p>
         * @param client the client
         * @return the chatgpt open cli client
         */

        @Bean
        @ConditionalOnMissingBean
        public ChatgptOpenCliClient chatgptOpenCliClient(OpenCliClient client) {
            return client.chatgpt();
        }
        /**
         * <p>Gemini open cli client.</p>
         * @param client the client
         * @return the gemini open cli client
         */

        @Bean
        @ConditionalOnMissingBean
        public GeminiOpenCliClient geminiOpenCliClient(OpenCliClient client) {
            return client.gemini();
        }
        /**
         * <p>Jimeng open cli client.</p>
         * @param client the client
         * @return the jimeng open cli client
         */

        @Bean
        @ConditionalOnMissingBean
        public JimengOpenCliClient jimengOpenCliClient(OpenCliClient client) {
            return client.jimeng();
        }
        /**
         * <p>Npm open cli client.</p>
         * @param client the client
         * @return the npm open cli client
         */

        @Bean
        @ConditionalOnMissingBean
        public NpmOpenCliClient npmOpenCliClient(OpenCliClient client) {
            return client.npm();
        }
        /**
         * <p>Arxiv open cli client.</p>
         * @param client the client
         * @return the arxiv open cli client
         */

        @Bean
        @ConditionalOnMissingBean
        public ArxivOpenCliClient arxivOpenCliClient(OpenCliClient client) {
            return client.arxiv();
        }
        /**
         * <p>Wikipedia open cli client.</p>
         * @param client the client
         * @return the wikipedia open cli client
         */

        @Bean
        @ConditionalOnMissingBean
        public WikipediaOpenCliClient wikipediaOpenCliClient(OpenCliClient client) {
            return client.wikipedia();
        }
        /**
         * <p>Binance open cli client.</p>
         * @param client the client
         * @return the binance open cli client
         */

        @Bean
        @ConditionalOnMissingBean
        public BinanceOpenCliClient binanceOpenCliClient(OpenCliClient client) {
            return client.binance();
        }
    }
}
