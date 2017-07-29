package codes.meo.dockerregistrylib.logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Priority;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.ext.Provider;
import javax.ws.rs.ext.WriterInterceptor;
import javax.ws.rs.ext.WriterInterceptorContext;
import java.io.IOException;
import java.io.OutputStream;

@Provider
@Priority(300)
public class ClientRequestLoggingFilter extends AbstractClientLoggingFilter implements ClientRequestFilter, WriterInterceptor {

    private static Logger LOG = LoggerFactory.getLogger(ClientRequestLoggingFilter.class);
    private static final String ENTITY_STREAM_PROPERTY = "ClientRequestLoggingFilter.entityStream";

    public ClientRequestLoggingFilter() {
        super(DEFAULT_MAX_ENTITY_SIZE);
    }

    public ClientRequestLoggingFilter(int maxEntitySize) {
        super(maxEntitySize);
    }

    @Override
    public void filter(ClientRequestContext requestContext) throws IOException {

        enrichDiagnosticContext(requestContext);

        if (requestContext.hasEntity() && isSupported(requestContext.getMediaType())) {
            final OutputStream stream = new LoggingStream(requestContext.getEntityStream(), maxEntitySize);
            requestContext.setEntityStream(stream);
            requestContext.setProperty(ENTITY_STREAM_PROPERTY, stream);
        } else {
            LOG.info("");
        }

        clearDiagnosticContext();
    }

    @Override
    public void aroundWriteTo(WriterInterceptorContext context) throws IOException, WebApplicationException {
        final LoggingStream stream = (LoggingStream) context.getProperty(ENTITY_STREAM_PROPERTY);
        context.proceed();
        if (stream != null) {
            LOG.info(stream.getStringBuilder(DEFAULT_CHARSET).toString().replace("\n", "").replace("\r", ""));
        }
    }
}
