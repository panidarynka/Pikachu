package app.daryna.pikachu;

/**
 * Created by dasha on 02.02.16.
 */
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

class StreamUtils {
    public static final int IO_BUFFER_SIZE = 8 * 1024;

    public static byte[] streamToBytes(final InputStream pInputStream) throws IOException {
        return StreamUtils.streamToBytes(pInputStream, -1);
    }

    public static byte[] streamToBytes(final InputStream pInputStream, final int pReadLimit) throws IOException {
        final ByteArrayOutputStream os = new ByteArrayOutputStream((pReadLimit == -1) ? IO_BUFFER_SIZE : pReadLimit);
        StreamUtils.copy(pInputStream, os, pReadLimit);
        return os.toByteArray();
    }

    public static void copy(final InputStream pInputStream, final OutputStream pOutputStream) throws IOException {
        StreamUtils.copy(pInputStream, pOutputStream, -1);
    }
    /**
     * Copy the content of the input stream into the output stream, using a temporary
     * byte array buffer whose size is defined by {@link #IO_BUFFER_SIZE}.
     *
     * @param pInputStream The input stream to copy from.
     * @param pOutputStream The output stream to copy to.
     * @param pByteLimit not more than so much bytes to read, or unlimited if smaller than 0.
     *
     * @throws IOException If any error occurs during the copy.
     */
    public static void copy(final InputStream pInputStream, final OutputStream pOutputStream, final long pByteLimit) throws IOException {
        if(pByteLimit < 0) {
            final byte[] b = new byte[IO_BUFFER_SIZE];
            int read;
            while((read = pInputStream.read(b)) != -1) {
                pOutputStream.write(b, 0, read);
            }
        } else {
            final byte[] b = new byte[IO_BUFFER_SIZE];
            final int bufferReadLimit = Math.min((int)pByteLimit, IO_BUFFER_SIZE);
            long pBytesLeftToRead = pByteLimit;

            int read;
            while((read = pInputStream.read(b, 0, bufferReadLimit)) != -1) {
                if(pBytesLeftToRead > read) {
                    pOutputStream.write(b, 0, read);
                    pBytesLeftToRead -= read;
                } else {
                    pOutputStream.write(b, 0, (int) pBytesLeftToRead);
                    break;
                }
            }
        }
        pOutputStream.flush();
    }

}
