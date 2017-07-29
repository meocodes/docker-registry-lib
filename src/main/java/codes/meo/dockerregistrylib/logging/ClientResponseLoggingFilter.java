package codes.meo.dockerregistrylib.logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Priority;
import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientResponseContext;
import javax.ws.rs.client.ClientResponseFilter;
import javax.ws.rs.ext.Provider;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

@Provider
@Priority(300)
public class ClientResponseLoggingFilter extends AbstractClientLoggingFilter implements ClientResponseFilter {

    private static Logger LOG = LoggerFactory.getLogger(ClientResponseLoggingFilter.class);

    public ClientResponseLoggingFilter() {
        super(DEFAULT_MAX_ENTITY_SIZE);
    }

    public ClientResponseLoggingFilter(int maxEntitySize) {
        super(maxEntitySize);
    }

    @Override
    public void filter(ClientRequestContext requestContext, ClientResponseContext responseContext) throws IOException {

        enrichDiagnosticContext(responseContext);

        if (responseContext.hasEntity() && isSupported(responseContext.getMediaType())) {
            final StringBuilder sb = new StringBuilder();
            responseContext.setEntityStream(logInboundEntity(sb, responseContext.getEntityStream(), DEFAULT_CHARSET));
            LOG.info("{}", sb.toString().replace("\n", "").replace("\r", ""));
        } else {
            LOG.info("");
        }

        clearDiagnosticContext();
    }

    private InputStream logInboundEntity(final StringBuilder b, InputStream stream, final Charset charset) throws IOException {
        if (!stream.markSupported()) {
            stream = new BufferedInputStream(stream);
        }
        stream.mark(maxEntitySize + 1);
        final byte[] entity = new byte[maxEntitySize + 1];
        final int entitySize = stream.read(entity);
        b.append(new String(entity, 0, Math.min(entitySize, maxEntitySize), charset));
        if (entitySize > maxEntitySize) {
            b.append("...more...");
        }
        stream.reset();
        return stream;
    }
}
