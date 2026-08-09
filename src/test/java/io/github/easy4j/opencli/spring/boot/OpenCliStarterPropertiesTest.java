package io.github.easy4j.opencli.spring.boot;

import io.github.easy4j.opencli.OpenCliExecutionTarget;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for {@link OpenCliStarterProperties}.
 *
 * @author [@Loong Wan](https://github.com/loong10k)
 * @since 1.0.0
 */
@DisplayName("OpenCliStarterProperties Tests")
class OpenCliStarterPropertiesTest {

    @Test
    @DisplayName("PREFIX constant is 'opencli'")
    void prefixConstant() {
        assertThat(OpenCliStarterProperties.PREFIX).isEqualTo("opencli");
    }

    @Test
    @DisplayName("Default values are correct")
    void defaultValues() {
        OpenCliStarterProperties props = new OpenCliStarterProperties();
        assertThat(props.isEnabled()).isTrue();
        assertThat(props.isStartupCheckEnabled()).isTrue();
        assertThat(props.isFailFastOnUnavailable()).isFalse();
        assertThat(props.getBrowserProfile()).isNull();
        assertThat(props.getCenter()).isNotNull();
    }

    @Test
    @DisplayName("enabled getter/setter")
    void enabledGetterSetter() {
        OpenCliStarterProperties props = new OpenCliStarterProperties();
        props.setEnabled(false);
        assertThat(props.isEnabled()).isFalse();
    }

    @Test
    @DisplayName("startupCheckEnabled getter/setter")
    void startupCheckEnabledGetterSetter() {
        OpenCliStarterProperties props = new OpenCliStarterProperties();
        props.setStartupCheckEnabled(false);
        assertThat(props.isStartupCheckEnabled()).isFalse();
    }

    @Test
    @DisplayName("failFastOnUnavailable getter/setter")
    void failFastOnUnavailableGetterSetter() {
        OpenCliStarterProperties props = new OpenCliStarterProperties();
        props.setFailFastOnUnavailable(true);
        assertThat(props.isFailFastOnUnavailable()).isTrue();
    }

    @Test
    @DisplayName("browserProfile getter/setter")
    void browserProfileGetterSetter() {
        OpenCliStarterProperties props = new OpenCliStarterProperties();
        props.setBrowserProfile("work");
        assertThat(props.getBrowserProfile()).isEqualTo("work");
    }

    @Test
    @DisplayName("Inherited properties from OpenCliProperties work")
    void inheritedProperties() {
        OpenCliStarterProperties props = new OpenCliStarterProperties();
        props.setExecutable("/usr/bin/opencli");
        props.setCommandTimeoutMillis(5000L);
        props.setExecutionTarget(OpenCliExecutionTarget.LOCAL_PROCESS);
        List<String> leading = new ArrayList<>();
        leading.add("--verbose");
        props.setLeadingArguments(leading);

        assertThat(props.getExecutable()).isEqualTo("/usr/bin/opencli");
        assertThat(props.getCommandTimeoutMillis()).isEqualTo(5000L);
        assertThat(props.getExecutionTarget()).isEqualTo(OpenCliExecutionTarget.LOCAL_PROCESS);
        assertThat(props.getLeadingArguments()).containsExactly("--verbose");
    }

    @Test
    @DisplayName("center property is configurable")
    void centerProperty() {
        OpenCliStarterProperties props = new OpenCliStarterProperties();
        OpenCliCenterWsProperties center = new OpenCliCenterWsProperties();
        center.setEnabled(true);
        props.setCenter(center);
        assertThat(props.getCenter().isEnabled()).isTrue();
    }

    @Test
    @DisplayName("equals/hashCode/toString work")
    void equalsHashCodeToString() {
        OpenCliStarterProperties props1 = new OpenCliStarterProperties();
        OpenCliStarterProperties props2 = new OpenCliStarterProperties();
        assertThat(props1).isEqualTo(props2);
        assertThat(props1.hashCode()).isEqualTo(props2.hashCode());
        assertThat(props1.toString()).isNotEmpty();
    }
}
