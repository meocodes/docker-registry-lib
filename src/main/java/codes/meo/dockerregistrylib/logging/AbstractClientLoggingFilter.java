package codes.meo.dockerregistrylib.logging;

import org.slf4j.MDC;

import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientResponseContext;
import javax.ws.rs.core.MediaType;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.stream.Stream;

import static javax.ws.rs.core.MediaType.APPLICATION_FORM_URLENCODED_TYPE;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON_TYPE;

public abstract class AbstractClientLoggingFilter {

    protected static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    protected static final int DEFAULT_MAX_ENTITY_SIZE = 10000;
    protected final int maxEntitySize;

    public AbstractClientLoggingFilter(int maxEntitySize) {
        this.maxEntitySize = maxEntitySize;
    }

    protected boolean isSupported(MediaType mediaType) {
        return APPLICATION_JSON_TYPE.isCompatible(mediaType) || APPLICATION_FORM_URLENCODED_TYPE.isCompatible(mediaType);
    }

    protected void enrichDiagnosticContext(ClientRequestContext requestContext) {
        MDC.put("Direction", "OUT");
        MDC.put("Method", requestContext.getMethod());
        MDC.put("Path", requestContext.getUri().getPath());
        if (requestContext.getHeaders() != null && !requestContext.getHeaders().isEmpty()) {
            MDC.put("Headers", Objects.toString(requestContext.getHeaders()));
        }
    }

    protected void enrichDiagnosticContext(ClientResponseContext responseContext) {
        MDC.put("Direction", "IN");
        MDC.put("Status", String.valueOf(responseContext.getStatus()));
        if (responseContext.getHeaders() != null && !responseContext.getHeaders().isEmpty()) {
            MDC.put("Headers", Objects.toString(responseContext.getHeaders()));
        }
    }

    protected void clearDiagnosticContext() {
        Stream.of("Direction", "Headers", "Method", "Path", "Status").forEach(key -> MDC.remove(key));
    }
}
