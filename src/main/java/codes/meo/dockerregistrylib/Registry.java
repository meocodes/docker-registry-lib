package codes.meo.dockerregistrylib;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.Configuration;
import java.util.List;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

public class Registry {

    private static final String BASE_PATH = "/v2/";
    private static final String CATALOG_PATH = BASE_PATH + "_catalog";
    private static final String TAGS_PATH = BASE_PATH + "{name}/tags/list";

    private final Client client;
    private final String host;

    public Registry(String host) {
        this(host, null);
    }

    public Registry(String host, Configuration config) {
        this.host = host;
        if (config == null) {
            this.client = ClientBuilder.newClient();
        } else {
            this.client = ClientBuilder.newClient(config);
        }
    }

    public List<String> getRepositories() {
        return client.target(host).path(CATALOG_PATH)
                .request(APPLICATION_JSON)
                .get(RepositoryResponse.class).getRepositories();
    }

    public List<String> getTags(String name) {
        return client.target(host).path(TAGS_PATH).resolveTemplate("name", name, false)
                .request(APPLICATION_JSON)
                .get(TagResponse.class).getTags();
    }

    public boolean isHealthy() {
        try {
            return client.target(host).path(BASE_PATH).request().get().getStatus() == 200;
        } catch (Exception e) {
            return false;
        }
    }
}
