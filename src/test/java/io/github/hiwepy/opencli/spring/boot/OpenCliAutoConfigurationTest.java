package io.github.hiwepy.opencli.spring.boot;

import io.github.hiwepy.opencli.OpenCliClient;
import io.github.hiwepy.opencli.core.OpenCliExecutor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;

/**
 * 校验 Starter 能注册属性、执行器与 {@link OpenCliClient}。
 *
 * @author wandl
 * @since 1.0.0
 */
@SpringBootTest(
        classes = OpenCliAutoConfiguration.class,
        properties = {
                "opencli.enabled=true",
                "opencli.executable=opencli",
                "opencli.command-timeout-millis=120000"
        })
class OpenCliAutoConfigurationTest {

    @Autowired
    private OpenCliStarterProperties properties;

    @Autowired
    private OpenCliExecutor executor;

    @Autowired
    private OpenCliClient openCliClient;

    /**
     * 验证核心 Bean 可用且客户端持有同一执行器引用。
     */
    @Test
    void shouldRegisterOpenCliBeans() {
        assertNotNull(properties);
        assertNotNull(executor);
        assertNotNull(openCliClient);
        assertNotNull(openCliClient.getExecutor());
        assertSame(executor, openCliClient.getExecutor());
    }
}
