package codes.meo.dockerregistrylib;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assume.assumeTrue;

public class RegistryIT {

    Registry registry = new Registry("http://localhost:5000", new DefaultClientConfig());

    @Before
    public void beforeEach() {
        assumeTrue(registry.isHealthy());
    }

    @Test
    public void getRepositories() {
        List<String> repos = registry.getRepositories();
        assertThat(repos).contains("library/ubuntu");
    }

    @Test
    public void getTags() {
        List<String> tags = registry.getTags("library/ubuntu");
        assertThat(tags).contains("latest");
    }
}
