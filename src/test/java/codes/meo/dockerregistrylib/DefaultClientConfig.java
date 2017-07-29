package codes.meo.dockerregistrylib;

import codes.meo.dockerregistrylib.logging.ClientRequestLoggingFilter;
import codes.meo.dockerregistrylib.logging.ClientResponseLoggingFilter;
import codes.meo.dockerregistrylib.logging.CorrelationFilter;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.ClientProperties;

public class DefaultClientConfig extends ClientConfig {

    public DefaultClientConfig() {
        register(CorrelationFilter.class);
        register(ClientRequestLoggingFilter.class);
        register(ClientResponseLoggingFilter.class);
        property(ClientProperties.CONNECT_TIMEOUT, 500);
        property(ClientProperties.READ_TIMEOUT, 100);
    }
}
