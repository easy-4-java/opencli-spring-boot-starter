package io.github.easy4j.opencli.spring.boot;

import io.github.easy4j.opencli.core.OpenCliExecutor;
import io.github.easy4j.opencli.core.availability.OpenCliAvailabilityChecker;
import io.github.easy4j.opencli.core.availability.OpenCliAvailabilityReport;
import io.github.easy4j.opencli.core.availability.OpenCliAvailabilityStatus;
import io.github.easy4j.opencli.exception.OpenCliStartupException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.env.Environment;

/**
 * 应用启动时探测本机 {@code opencli} 是否可用（远程 Agent 模式自动跳过）。
 *
 * @author wandl
 * @since 1.0.0
 */
@Slf4j
@RequiredArgsConstructor
public class OpenCliCliStartupChecker implements ApplicationRunner {

    private final OpenCliExecutor openCliExecutor;
    private final OpenCliStarterProperties openCliProperties;
    private final OpenCliAvailabilityChecker availabilityChecker;
    private final Environment environment;

    /**
     * 启动阶段执行 {@code opencli list} 探测。
     */
    @Override
    public void run(ApplicationArguments args) {
        OpenCliAvailabilityReport report = availabilityChecker.check(openCliExecutor);
        String configSnapshot = buildEffectiveConfigSnapshot();

        if (report.getStatus() == OpenCliAvailabilityStatus.SKIPPED_REMOTE_MODE) {
            log.info(
                    "OpenCLI startup check skipped (remote mode): {} effectiveConfig={}",
                    report.toDiagnosticMessage(),
                    configSnapshot);
            return;
        }

        if (report.isAvailable()) {
            log.info(
                    "OpenCLI ready: {} effectiveConfig={}",
                    report.toDiagnosticMessage(),
                    configSnapshot);
            return;
        }

        String message = report.toDiagnosticMessage()
                + "。请确认 opencli.enabled=true 且 opencli.executable 指向可执行的 opencli。"
                + " effectiveConfig={" + configSnapshot + "}";
        if (openCliProperties.isFailFastOnUnavailable()) {
            throw new OpenCliStartupException(message, report);
        }
        log.warn("OpenCLI startup check failed (fail-fast disabled): {}", message);
    }

    private String buildEffectiveConfigSnapshot() {
        String profiles = environment.getProperty("spring.profiles.active", "(unset)");
        return "profiles=" + profiles
                + ", opencli.enabled=" + openCliProperties.isEnabled()
                + ", opencli.executable=" + openCliProperties.getExecutable()
                + ", opencli.execution-target=" + openCliProperties.getExecutionTarget()
                + ", opencli.startup-check-enabled=" + openCliProperties.isStartupCheckEnabled()
                + ", opencli.fail-fast-on-unavailable=" + openCliProperties.isFailFastOnUnavailable()
                + ", opencliStarterOnClasspath=" + isOpenCliStarterOnClasspath();
    }

    private static boolean isOpenCliStarterOnClasspath() {
        try {
            Class.forName("io.github.easy4j.opencli.spring.boot.OpenCliAutoConfiguration");
            return true;
        } catch (ClassNotFoundException ex) {
            return false;
        }
    }
}
