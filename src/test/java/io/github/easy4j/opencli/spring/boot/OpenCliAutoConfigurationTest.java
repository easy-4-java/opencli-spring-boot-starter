package io.github.easy4j.opencli.spring.boot;

import io.github.easy4j.opencli.OpenCliClient;
import io.github.easy4j.opencli.core.OpenCliExecutor;
import io.github.easy4j.opencli.core.availability.OpenCliAvailabilityChecker;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;

/**
 * 校验 Starter 能注册属性、执行器与 {@link OpenCliClient}。
 *
 * @author [@Loong Wan](https://github.com/loong10k)
 * @since 1.0.0
 */
@SpringBootTest(
        classes = OpenCliAutoConfiguration.class,
        properties = {
                "opencli.enabled=true",
                "opencli.executable=opencli",
                "opencli.command-timeout-millis=120000",
                "opencli.startup-check-enabled=false"
        })
class OpenCliAutoConfigurationTest {

    @Autowired
    private OpenCliStarterProperties properties;

    @Autowired
    private OpenCliExecutor executor;

    @Autowired
    private OpenCliClient openCliClient;

    @Autowired
    private OpenCliAvailabilityChecker availabilityChecker;

    /**
     * 验证核心 Bean 可用且客户端持有同一执行器引用。
     */
    @Test
    void shouldRegisterOpenCliBeans() {
        assertNotNull(properties);
        assertNotNull(executor);
        assertNotNull(openCliClient);
        assertNotNull(availabilityChecker);
        assertNotNull(openCliClient.getExecutor());
        assertSame(executor, openCliClient.getExecutor());
    }

    /**
     * {@code opencli.browser-profile} 应写入 {@code leadingArguments} 的 {@code --profile} 前缀。
     */
    @Test
    void browserProfileMapsToLeadingArguments() {
        OpenCliStarterProperties props = new OpenCliStarterProperties();
        props.setBrowserProfile("work");
        OpenCliExecutor exec = new OpenCliAutoConfiguration().openCliExecutor(props);
        assertNotNull(exec);
        assertNotNull(props.getLeadingArguments());
        assertEquals("--profile", props.getLeadingArguments().get(0));
        assertEquals("work", props.getLeadingArguments().get(1));
    }
}
