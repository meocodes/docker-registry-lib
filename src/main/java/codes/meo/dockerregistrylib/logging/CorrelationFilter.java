package codes.meo.dockerregistrylib.logging;

import org.slf4j.MDC;

import javax.annotation.Priority;
import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.util.UUID;

@Provider
@Priority(100)
public class CorrelationFilter implements ClientRequestFilter {

    @Override
    public void filter(ClientRequestContext requestContext) throws IOException {
        MDC.put("CorrelationId", createCorrelationId());
    }

    private String createCorrelationId() {
        return UUID.randomUUID().toString();
    }
}
