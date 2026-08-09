package io.github.easy4j.opencli.spring.boot;

import io.github.easy4j.opencli.OpenCliClient;
import io.github.easy4j.opencli.OpenCliProperties;
import io.github.easy4j.opencli.core.OpenCliExecutor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for {@link OpenCliAutoConfiguration.OpenCliFacadeBeansConfiguration}.
 *
 * @author [@Loong Wan](https://github.com/loong10k)
 * @since 1.0.0
 */
@DisplayName("OpenCliFacadeBeansConfiguration Tests")
class OpenCliFacadeBeansConfigurationTest {

    private final OpenCliAutoConfiguration.OpenCliFacadeBeansConfiguration facade =
            new OpenCliAutoConfiguration.OpenCliFacadeBeansConfiguration();

    private OpenCliClient createClient() {
        OpenCliProperties props = new OpenCliProperties();
        props.setExecutable("opencli");
        return new OpenCliClient(props, new OpenCliExecutor(props));
    }

    @Test
    @DisplayName("openCliMetaClient returns non-null")
    void openCliMetaClient() {
        assertThat(facade.openCliMetaClient(createClient())).isNotNull();
    }

    @Test
    @DisplayName("openCliBrowserClient returns non-null")
    void openCliBrowserClient() {
        assertThat(facade.openCliBrowserClient(createClient())).isNotNull();
    }

    @Test
    @DisplayName("codexOpenCliClient returns non-null")
    void codexOpenCliClient() {
        assertThat(facade.codexOpenCliClient(createClient())).isNotNull();
    }

    @Test
    @DisplayName("cursorOpenCliClient returns non-null")
    void cursorOpenCliClient() {
        assertThat(facade.cursorOpenCliClient(createClient())).isNotNull();
    }

    @Test
    @DisplayName("claudeOpenCliClient returns non-null")
    void claudeOpenCliClient() {
        assertThat(facade.claudeOpenCliClient(createClient())).isNotNull();
    }

    @Test
    @DisplayName("chatgptOpenCliClient returns non-null")
    void chatgptOpenCliClient() {
        assertThat(facade.chatgptOpenCliClient(createClient())).isNotNull();
    }

    @Test
    @DisplayName("geminiOpenCliClient returns non-null")
    void geminiOpenCliClient() {
        assertThat(facade.geminiOpenCliClient(createClient())).isNotNull();
    }

    @Test
    @DisplayName("jimengOpenCliClient returns non-null")
    void jimengOpenCliClient() {
        assertThat(facade.jimengOpenCliClient(createClient())).isNotNull();
    }

    @Test
    @DisplayName("npmOpenCliClient returns non-null")
    void npmOpenCliClient() {
        assertThat(facade.npmOpenCliClient(createClient())).isNotNull();
    }

    @Test
    @DisplayName("arxivOpenCliClient returns non-null")
    void arxivOpenCliClient() {
        assertThat(facade.arxivOpenCliClient(createClient())).isNotNull();
    }

    @Test
    @DisplayName("wikipediaOpenCliClient returns non-null")
    void wikipediaOpenCliClient() {
        assertThat(facade.wikipediaOpenCliClient(createClient())).isNotNull();
    }

    @Test
    @DisplayName("binanceOpenCliClient returns non-null")
    void binanceOpenCliClient() {
        assertThat(facade.binanceOpenCliClient(createClient())).isNotNull();
    }
}
