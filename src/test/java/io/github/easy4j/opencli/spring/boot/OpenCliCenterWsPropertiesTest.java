package io.github.easy4j.opencli.spring.boot;

import io.github.easy4j.opencli.center.ws.OpenCliCenterWebSocketPath;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for {@link OpenCliCenterWsProperties}.
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
@DisplayName("OpenCliCenterWsProperties Tests")
class OpenCliCenterWsPropertiesTest {

    @Test
    @DisplayName("Default enabled is false")
    void defaultEnabledIsFalse() {
        OpenCliCenterWsProperties props = new OpenCliCenterWsProperties();
        assertThat(props.isEnabled()).isFalse();
    }

    @Test
    @DisplayName("enabled can be set and read")
    void enabledGetterSetter() {
        OpenCliCenterWsProperties props = new OpenCliCenterWsProperties();
        props.setEnabled(true);
        assertThat(props.isEnabled()).isTrue();
    }

    @Test
    @DisplayName("connection is non-null by default")
    void connectionIsNonNull() {
        OpenCliCenterWsProperties props = new OpenCliCenterWsProperties();
        assertThat(props.getConnection()).isNotNull();
    }

    @Test
    @DisplayName("centralApiBaseUrl delegates to connection")
    void centralApiBaseUrlDelegation() {
        OpenCliCenterWsProperties props = new OpenCliCenterWsProperties();
        props.setCentralApiBaseUrl("https://example.com/api");
        assertThat(props.getCentralApiBaseUrl()).isEqualTo("https://example.com/api");
    }

    @Test
    @DisplayName("agentAdvertiseUrl delegates to connection")
    void agentAdvertiseUrlDelegation() {
        OpenCliCenterWsProperties props = new OpenCliCenterWsProperties();
        props.setAgentAdvertiseUrl("https://agent.example.com");
        assertThat(props.getAgentAdvertiseUrl()).isEqualTo("https://agent.example.com");
    }

    @Test
    @DisplayName("webSocketPath delegates to connection")
    void webSocketPathDelegation() {
        OpenCliCenterWsProperties props = new OpenCliCenterWsProperties();
        OpenCliCenterWebSocketPath path = OpenCliCenterWebSocketPath.NODES_WS;
        props.setWebSocketPath(path);
        assertThat(props.getWebSocketPath()).isEqualTo(path);
    }
}
