package codes.meo.dockerregistrylib;

import java.util.ArrayList;
import java.util.List;

public class RepositoryResponse {

    private final List<String> repositories = new ArrayList<>();

    public List<String> getRepositories() {
        return repositories;
    }
}
