package codes.meo.dockerregistrylib;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.junit.Rule;
import org.junit.Test;

import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Java6Assertions.assertThat;

/**
 * Unit tests for {@link Registry}.
 */
public class RegistryTest {

    @Rule
    public WireMockRule wireMock = new WireMockRule(8375);

    Registry registry = new Registry("http://localhost:8375", new DefaultClientConfig());

    @Test
    public void isHealthyOk() {
        stubFor(get(urlEqualTo("/v2/"))
                .willReturn(aResponse().withStatus(200)));
        assertThat(registry.isHealthy()).isTrue();
    }

    @Test
    public void isHealthyNotFound() {
        stubFor(get(urlEqualTo("/v2/"))
                .willReturn(aResponse().withStatus(404)));
        assertThat(registry.isHealthy()).isFalse();
    }

    @Test
    public void isHealthyTimeout() {
        stubFor(get(urlEqualTo("/v2/"))
                .willReturn(aResponse().withStatus(200).withFixedDelay(600)));
        assertThat(registry.isHealthy()).isFalse();
    }

    @Test
    public void getRepositoriesEmptyList() {
        stubFor(get(urlEqualTo("/v2/_catalog"))
                .willReturn(aResponse().withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"repositories\":[]}")));
        List<String> repos = registry.getRepositories();
        assertThat(repos).isEmpty();
    }

    @Test
    public void getTags() {
        stubFor(get(urlEqualTo("/v2/library/ubuntu/tags/list"))
                .willReturn(aResponse().withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"name\":\"library/ubuntu\",\"tags\":[\"latest\"]}")));
        List<String> tags = registry.getTags("library/ubuntu");
        assertThat(tags).containsOnly("latest");
    }
}
