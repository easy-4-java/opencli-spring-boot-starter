package io.github.easy4j.opencli.spring.boot;

import io.github.easy4j.opencli.core.availability.OpenCliAvailabilityChecker;
import io.github.easy4j.opencli.exception.OpenCliStartupException;
import org.junit.jupiter.api.Test;
import org.springframework.boot.DefaultApplicationArguments;
import org.springframework.mock.env.MockEnvironment;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * {@link OpenCliCliStartupChecker} 行为测试。
 */
class OpenCliCliStartupCheckerTest {

    @Test
    void shouldFailFastWhenCliUnavailable() {
        OpenCliStarterProperties properties = new OpenCliStarterProperties();
        properties.setExecutable("/nonexistent/opencli-startup-test");
        properties.setFailFastOnUnavailable(true);
        OpenCliCliStartupChecker checker = new OpenCliCliStartupChecker(
                new OpenCliAutoConfiguration().openCliExecutor(properties),
                properties,
                new OpenCliAvailabilityChecker(),
                new MockEnvironment());

        assertThrows(OpenCliStartupException.class,
                () -> checker.run(new DefaultApplicationArguments(new String[0])));
    }

    @Test
    void shouldWarnOnlyWhenFailFastDisabled() {
        OpenCliStarterProperties properties = new OpenCliStarterProperties();
        properties.setExecutable("/nonexistent/opencli-startup-test");
        properties.setFailFastOnUnavailable(false);
        OpenCliCliStartupChecker checker = new OpenCliCliStartupChecker(
                new OpenCliAutoConfiguration().openCliExecutor(properties),
                properties,
                new OpenCliAvailabilityChecker(),
                new MockEnvironment());

        assertDoesNotThrow(() -> checker.run(new DefaultApplicationArguments(new String[0])));
    }
}
