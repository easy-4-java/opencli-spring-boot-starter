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
import static org.junit.jupiter.api.Assertions.assertTrue;

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

    /**
     * 当 {@code leadingArguments} 为 null 且设置了 {@code browser-profile} 时，应自动创建列表。
     */
    @Test
    void browserProfileCreatesLeadingArgumentsWhenNull() {
        OpenCliStarterProperties props = new OpenCliStarterProperties();
        props.setBrowserProfile("test-profile");
        props.setLeadingArguments(null);
        new OpenCliAutoConfiguration().openCliExecutor(props);
        assertNotNull(props.getLeadingArguments());
        assertEquals("--profile", props.getLeadingArguments().get(0));
        assertEquals("test-profile", props.getLeadingArguments().get(1));
    }

    /**
     * 当 {@code leadingArguments} 已包含 {@code --profile} 时，不应重复添加。
     */
    @Test
    void browserProfileDoesNotDuplicateExistingProfile() {
        OpenCliStarterProperties props = new OpenCliStarterProperties();
        props.setBrowserProfile("work");
        java.util.List<String> leading = new java.util.ArrayList<>();
        leading.add("--profile");
        leading.add("existing");
        props.setLeadingArguments(leading);
        new OpenCliAutoConfiguration().openCliExecutor(props);
        assertEquals(2, props.getLeadingArguments().size());
        assertEquals("--profile", props.getLeadingArguments().get(0));
        assertEquals("existing", props.getLeadingArguments().get(1));
    }

    /**
     * 当未设置 {@code browser-profile} 时，不应修改 {@code leadingArguments}。
     */
    @Test
    void noBrowserProfileDoesNotModifyLeadingArguments() {
        OpenCliStarterProperties props = new OpenCliStarterProperties();
        props.setBrowserProfile(null);
        props.setLeadingArguments(new java.util.ArrayList<>());
        new OpenCliAutoConfiguration().openCliExecutor(props);
        assertTrue(props.getLeadingArguments().isEmpty());
    }

    /**
     * 验证 {@code openCliAvailabilityChecker} Bean 可以创建。
     */
    @Test
    void availabilityCheckerBeanCreation() {
        OpenCliAvailabilityChecker checker = new OpenCliAutoConfiguration().openCliAvailabilityChecker();
        assertNotNull(checker);
    }

    /**
     * 验证 {@code openCliClient} Bean 可以创建。
     */
    @Test
    void clientBeanCreation() {
        OpenCliStarterProperties props = new OpenCliStarterProperties();
        props.setExecutable("opencli");
        OpenCliAutoConfiguration config = new OpenCliAutoConfiguration();
        OpenCliExecutor exec = config.openCliExecutor(props);
        OpenCliClient client = config.openCliClient(props, exec);
        assertNotNull(client);
        assertSame(exec, client.getExecutor());
    }

    /**
     * 验证 {@code openCliCliStartupChecker} Bean 可以创建。
     */
    @Test
    void startupCheckerBeanCreation() {
        OpenCliStarterProperties props = new OpenCliStarterProperties();
        props.setExecutable("/nonexistent/opencli");
        OpenCliAutoConfiguration config = new OpenCliAutoConfiguration();
        OpenCliExecutor exec = config.openCliExecutor(props);
        OpenCliAvailabilityChecker checker = config.openCliAvailabilityChecker();
        org.springframework.mock.env.MockEnvironment env = new org.springframework.mock.env.MockEnvironment();
        OpenCliCliStartupChecker startupChecker = config.openCliCliStartupChecker(exec, props, checker, env);
        assertNotNull(startupChecker);
    }
}
