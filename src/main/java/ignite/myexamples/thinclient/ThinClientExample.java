package ignite.myexamples.thinclient;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.IgniteCheckedException;
import org.apache.ignite.Ignition;
import org.apache.ignite.binary.BinaryObjectException;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.internal.binary.*;
import org.apache.ignite.internal.binary.streams.BinaryHeapInputStream;
import org.apache.ignite.internal.binary.streams.BinaryHeapOutputStream;
import org.apache.ignite.internal.binary.streams.BinaryOutputStream;
import org.apache.ignite.internal.processors.odbc.ClientListenerNioListener;
import org.apache.ignite.internal.processors.odbc.ClientListenerRequest;
import org.apache.ignite.internal.processors.odbc.SqlStateCode;
import org.apache.ignite.internal.util.ipc.loopback.IpcClientTcpEndpoint;
import org.apache.ignite.marshaller.Marshaller;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.sql.SQLException;

public class ThinClientExample {
    public static void main(String[] args)  throws IOException, IgniteCheckedException  {
        final String cacheName = "myCache";
        final int key = 1;
        Ignition.setClientMode(true);
        try (Ignite ignite = Ignition.start("/Users/prachig/myexamples/config/cluster-config.xml")) {
            IgniteCache<Integer, Integer> cache = ignite.getOrCreateCache(cacheName);
            cache.put(key,12);

            System.out.println(cache.get(key));
        }

        doRun(cacheName, key);
    }



    public static void doRun(String cacheName, int key) throws IOException, IgniteCheckedException {
        Socket sock = new Socket();


        sock.connect(new InetSocketAddress("127.0.0.1", 10800));

        IpcClientTcpEndpoint endpoint = new IpcClientTcpEndpoint(sock);

        BufferedOutputStream out = new BufferedOutputStream(endpoint.outputStream());
        BufferedInputStream in = new BufferedInputStream(endpoint.inputStream());
        handshake(sock, in, out);
        doGet(cacheName, sock, in, out);

    }
    private static void doGet(final String cacheName, Socket s, BufferedInputStream bi, BufferedOutputStream bo) throws IOException, BinaryObjectException, IgniteCheckedException {

        final BinaryContext ctx = new BinaryContext(null, new IgniteConfiguration(),null);
        BinaryWriterExImpl writer = new BinaryWriterExImpl(ctx, new BinaryHeapOutputStream(128),
                null, null);

        writer.writeShort(1); // OP_GET
        writer.writeLong(1); // request
        writer.writeInt(cacheName.hashCode()); // cacheId
        writer.writeByte(0); // flag

        writer.writeObject(1); // key

        send(bo, writer.array());

        final BinaryContext ctx2 = new BinaryContext(null, new IgniteConfiguration(),null);
        BinaryReaderExImpl reader = new BinaryReaderExImpl(ctx2, new BinaryHeapInputStream(read(bi)),
                null, null, false);

        long requestId = reader.readLong();
        int status = reader.readInt();
        int value =  (Integer) reader.readObject();
         System.out.println("value is=" + value);
    }

    private static void handshake(Socket s, BufferedInputStream bi, BufferedOutputStream bo) throws IOException {
        BinaryWriterExImpl writer = new BinaryWriterExImpl(null, new BinaryHeapOutputStream(13),
                null, null);

        writer.writeByte((byte) ClientListenerRequest.HANDSHAKE);

        writer.writeShort(1);
        writer.writeShort(0);
        writer.writeShort(0);

        writer.writeByte(2);

        send(bo, writer.array());

        BinaryReaderExImpl reader = new BinaryReaderExImpl(null, new BinaryHeapInputStream(read(bi)),
                null, null, false);

        boolean accepted = reader.readBoolean();
    }

    private static void send(OutputStream out, byte[] req) throws IOException {
        int size = req.length;

        out.write(size & 0xFF);
        out.write((size >> 8) & 0xFF);
        out.write((size >> 16) & 0xFF);
        out.write((size >> 24) & 0xFF);

        out.write(req);

        out.flush();
    }

    private static byte[] read(final BufferedInputStream in) throws IOException {
        byte[] sizeBytes = read(in, 4);

        int msgSize = (((0xFF & sizeBytes[3]) << 24) | ((0xFF & sizeBytes[2]) << 16)
                | ((0xFF & sizeBytes[1]) << 8) + (0xFF & sizeBytes[0]));

        return read(in, msgSize);
    }

    private static byte[] read(final BufferedInputStream in, int size) throws IOException {
        int off = 0;

        byte[] data = new byte[size];

        while (off != size) {
            int res = in.read(data, off, size - off);

            if (res == -1)
                throw new IOException("Failed to read incoming message (not enough data).");

            off += res;
        }

        return data;
    }
}
