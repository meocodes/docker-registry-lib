package codes.meo.dockerregistrylib;

import java.util.List;

public class TagResponse {

    private String name;
    private List<String> tags;

    public String getName() {
        return name;
    }

    public List<String> getTags() {
        return tags;
    }
}
