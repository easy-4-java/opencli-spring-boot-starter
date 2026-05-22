package io.github.hiwepy.opencli.spring.boot;

import io.github.hiwepy.opencli.center.ws.OpenCliCenterWebSocketPath;
import io.github.hiwepy.opencli.center.ws.OpenCliWsAgentConnectionProperties;
import lombok.Data;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

/**
 * {@code opencli.center.ws.*} — 边缘反向 WebSocket Agent 连接配置。
 */
@Data
public class OpenCliCenterWsProperties {

    /**
     * 是否注册 {@link io.github.hiwepy.opencli.center.ws.OpenCliWsReverseAgentClient} Bean。
     */
    private boolean enabled = false;

    @NestedConfigurationProperty
    private OpenCliWsAgentConnectionProperties connection = new OpenCliWsAgentConnectionProperties();

    /** 中心 HTTP API 根 URL。 */
    public String getCentralApiBaseUrl() {
        return connection.getCentralApiBaseUrl();
    }

    public void setCentralApiBaseUrl(String url) {
        connection.setCentralApiBaseUrl(url);
    }

    /** 本节点对中心可见的 Agent URL。 */
    public String getAgentAdvertiseUrl() {
        return connection.getAgentAdvertiseUrl();
    }

    public void setAgentAdvertiseUrl(String url) {
        connection.setAgentAdvertiseUrl(url);
    }

    public OpenCliCenterWebSocketPath getWebSocketPath() {
        return connection.getWebSocketPath();
    }

    public void setWebSocketPath(OpenCliCenterWebSocketPath path) {
        connection.setWebSocketPath(path);
    }
}
