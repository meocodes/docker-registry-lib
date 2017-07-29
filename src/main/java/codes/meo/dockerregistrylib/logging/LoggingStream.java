package codes.meo.dockerregistrylib.logging;

import java.io.ByteArrayOutputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;

public class LoggingStream extends FilterOutputStream {

    private final StringBuilder sb = new StringBuilder();
    private final ByteArrayOutputStream baos = new ByteArrayOutputStream();
    private final int maxEntitySize;

    LoggingStream(OutputStream out, int maxEntitySize) {
        super(out);
        this.maxEntitySize = maxEntitySize;
    }

    StringBuilder getStringBuilder(Charset charset) {

        final byte[] entity = baos.toByteArray();

        sb.append(new String(entity, 0, entity.length, charset));
        if (entity.length > maxEntitySize) {
            sb.append("...more...");
        }

        return sb;
    }

    @Override
    public void write(final int i) throws IOException {

        if (baos.size() <= maxEntitySize) {
            baos.write(i);
        }

        out.write(i);
    }
}
