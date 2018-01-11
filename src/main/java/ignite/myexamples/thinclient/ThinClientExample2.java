package ignite.myexamples.thinclient;

import ignite.myexamples.model.Person;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.IgniteCheckedException;
import org.apache.ignite.Ignition;
import org.apache.ignite.cache.query.QueryCursor;
import org.apache.ignite.cache.query.SqlFieldsQuery;
import org.apache.ignite.cache.query.SqlQuery;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;


public class ThinClientExample2 {
    private static final String cacheName = "myCache";

        /* General-purpose operations. */
    /** */
    private static final short OP_RESOURCE_CLOSE = 0;

    /* Cache operations */
    /** */
    private static final short OP_CACHE_GET = 1000;

    /** */
    private static final short OP_CACHE_PUT = 1001;

    /** */
    private static final short OP_CACHE_PUT_IF_ABSENT = 1002;

    /** */
    private static final short OP_CACHE_GET_ALL = 1003;

    /** */
    private static final short OP_CACHE_PUT_ALL = 1004;

    /** */
    private static final short OP_CACHE_GET_AND_PUT = 1005;

    /** */
    private static final short OP_CACHE_GET_AND_REPLACE = 1006;

    /** */
    private static final short OP_CACHE_GET_AND_REMOVE = 1007;

    /** */
    private static final short OP_CACHE_GET_AND_PUT_IF_ABSENT = 1008;

    /** */
    private static final short OP_CACHE_REPLACE = 1009;

    /** */
    private static final short OP_CACHE_REPLACE_IF_EQUALS = 1010;

    /** */
    private static final short OP_CACHE_CONTAINS_KEY = 1011;

    /** */
    private static final short OP_CACHE_CONTAINS_KEYS = 1012;

    /** */
    private static final short OP_CACHE_CLEAR = 1013;

    /** */
    private static final short OP_CACHE_CLEAR_KEY = 1014;

    /** */
    private static final short OP_CACHE_CLEAR_KEYS = 1015;

    /** */
    private static final short OP_CACHE_REMOVE_KEY = 1016;

    /** */
    private static final short OP_CACHE_REMOVE_IF_EQUALS = 1017;

    /** */
    private static final short OP_CACHE_REMOVE_KEYS = 1018;

    /** */
    private static final short OP_CACHE_REMOVE_ALL = 1019;

    /** */
    private static final short OP_CACHE_GET_SIZE = 1020;

    /* Cache create / destroy, configuration. */
    /** */
    private static final short OP_CACHE_GET_NAMES = 1050;

    /** */
    private static final short OP_CACHE_CREATE_WITH_NAME = 1051;

    /** */
    private static final short OP_CACHE_GET_OR_CREATE_WITH_NAME = 1052;

    /** */
    private static final short OP_CACHE_CREATE_WITH_CONFIGURATION = 1053;

    /** */
    private static final short OP_CACHE_GET_OR_CREATE_WITH_CONFIGURATION = 1054;

    /** */
    private static final short OP_CACHE_GET_CONFIGURATION = 1055;

    /** */
    private static final short OP_CACHE_DESTROY = 1056;

    /* Query operations. */
    /** */
    private static final short OP_QUERY_SCAN = 2000;

    /** */
    private static final short OP_QUERY_SCAN_CURSOR_GET_PAGE = 2001;

    /** */
    private static final short OP_QUERY_SQL = 2002;

    /** */
    private static final short OP_QUERY_SQL_CURSOR_GET_PAGE = 2003;

    /** */
    private static final short OP_QUERY_SQL_FIELDS = 2004;

    /** */
    private static final short OP_QUERY_SQL_FIELDS_CURSOR_GET_PAGE = 2005;

    /* Binary metadata operations. */
    /** */
    private static final short OP_BINARY_TYPE_NAME_GET = 3000;

    /** */
    private static final short OP_BINARY_TYPE_NAME_PUT = 3001;

    /** */
    private static final short OP_BINARY_TYPE_GET = 3002;

    /** */
    private static final short OP_BINARY_TYPE_PUT = 3003;


    public static void main(String[] args) throws IOException, IgniteCheckedException {
//        Ignition.setClientMode(true);
//
//        try (Ignite ignite = Ignition.start("/Users/prachig/myexamples/config/cluster-config.xml")) {
//            IgniteCache<Long, Person> cache = ignite.getOrCreateCache("personCache");
//
//            cache.query(new SqlFieldsQuery("INSERT INTO Person(_key, id, name, " +
//                    "salary) values (1, 1, 'John', '1000'), (2, 2, 'Mary', '2000')"));
//
//            QueryCursor<List<?>> cursor = cache.query(new SqlQuery( "Person",
//                    "select * from Person"));
//
//            System.out.println(cursor.getAll());
//        }

        Socket socket = new Socket();
        socket.connect(new InetSocketAddress("127.0.0.1", 10900));
        doHandshake(socket);
        cachePut(socket, 1, 11);
        cacheGet(socket, 1);
    }

    private static void putBinaryType(Socket socket) throws  IOException {
        String type = "ignite.myexamples.model.Person";

        DataOutputStream out = new DataOutputStream(socket.getOutputStream());

        // Request Header
        requestHeader(15, OP_BINARY_TYPE_PUT, 1, out);

        // Type id
        writeIntLittleEndian(type.hashCode(), out);

        // Type name
        writeString(type, out);

        // Affinity key field name
        writeByteLittleEndian(101, out);

        // Field count
        writeIntLittleEndian(3, out);

        // Field 1
        String field1 = "id";
        writeString(field1, out);
        writeIntLittleEndian("long".hashCode(), out);
        writeIntLittleEndian(field1.hashCode(), out);

        // Field 2
        String field2 = "name";
        writeString(field2, out);
        writeIntLittleEndian("String".hashCode(), out);
        writeIntLittleEndian(field2.hashCode(), out);

        // Field 3
        String field3 = "salary";
        writeString(field3, out);
        writeIntLittleEndian("int".hashCode(), out);
        writeIntLittleEndian(field3.hashCode(), out);

        // isEnum
        out.writeBoolean(false);

        // Schema count
        writeIntLittleEndian(1, out);

        // Schema
        writeIntLittleEndian(0x811C9DC5, out);
        writeByteLittleEndian(14, out);
        writeIntLittleEndian(3, out);
        writeIntLittleEndian(field1.hashCode(), out);
        writeIntLittleEndian(field2.hashCode(), out);
        writeIntLittleEndian(field3.hashCode(), out);


        // Read result
        DataInputStream in = new DataInputStream(socket.getInputStream());

        responseHeader(in);
    }

    private static void doQueryScan(Socket socket) throws IOException {
        DataOutputStream out = new DataOutputStream(socket.getOutputStream());

        // Request header
        requestHeader(18, OP_QUERY_SCAN, 1, out);

        // Cache id
        String queryCacheName = "personCache";
        writeIntLittleEndian(queryCacheName.hashCode(), out);

        // Filter Object
        writeIntLittleEndian(1000, out);

        // Filter Platform
        writeByteLittleEndian(1, out);

        // Cursor page size
        writeIntLittleEndian(1, out);

        // Partition to query
        writeIntLittleEndian(-1, out);

        // local flag
        out.writeBoolean(false);

        // Read result
        DataInputStream in = new DataInputStream(socket.getInputStream());

        //Response header
        responseHeader(in);

        // Cursor id
        long cursorId = readLongLittleEndian(in);
        System.out.println("cursorId: " + cursorId);
    }

    private static void doSQLQuery(Socket socket) throws IOException {
        String entityName = "Person";
        int entityNameLength = getStrLen(entityName);

        String sql = "Select * from Person";
        int sqlLength = getStrLen(sql);

        DataOutputStream out = new DataOutputStream(socket.getOutputStream());

        // Request header
        requestHeader(34 + entityNameLength + sqlLength, OP_QUERY_SQL, 1, out);

        // Cache id
        String queryCacheName = "personCache";
        writeIntLittleEndian(queryCacheName.hashCode(), out);

        // Flag = none
        writeByteLittleEndian(0, out);

        // Query Entity
        writeString(entityName, out);

        // SQL query
        writeString(sql, out);

        // Argument count
        writeIntLittleEndian(0, out);

        // Joins
        out.writeBoolean(false);

        // Local query
        out.writeBoolean(false);

        // Replicated
        out.writeBoolean(false);

        // cursor page size
        writeIntLittleEndian(1, out);

        // Timeout
        writeLongLittleEndian(5000, out);

        // Send request
        out.flush();

        // Read result
        DataInputStream in = new DataInputStream(socket.getInputStream());

        // Response header
        responseHeader(in);

        long cursorId = readLongLittleEndian(in);
        System.out.println("cursorId: " + cursorId);

        int rowCount = readIntLittleEndian(in);
        System.out.println("rowCount: " + rowCount);

        // Read entries (as user objects)
        for (int i = 0; i < rowCount; i++) {
            int resTypeCode1 = readByteLittleEndian(in);
            System.out.println(readLongLittleEndian(in));

            int resTypeCode2 = readByteLittleEndian(in);
            System.out.println(resTypeCode2);

            // read value based on resTypeCode2
        }

        boolean moreResults = readBooleanLittleEndian(in);
        System.out.println("moreResults: " + moreResults);

        if (moreResults == true) {
            getQueryCursorPage(socket, cursorId);
        }
    }

    private static void querySqlFields(Socket socket) throws IOException {
        String sql = "Select id, salary from Person";

        String sqlSchema = "personCache";

        DataOutputStream out = new DataOutputStream(socket.getOutputStream());

        // Request header
        requestHeader(43 + getStrLen(sql) + getStrLen(sqlSchema), OP_QUERY_SQL_FIELDS, 1, out);

        // Cache id
        String queryCacheName = "personCache";
        writeIntLittleEndian(queryCacheName.hashCode(), out);

        // Flag = none
        writeByteLittleEndian(0, out);

        // Schema
        writeString(sqlSchema, out);

        // cursor page size
        writeIntLittleEndian(2, out);

        // Max Rows
        writeIntLittleEndian(5, out);

        // SQL query
        writeString(sql, out);

        // Argument count
        writeIntLittleEndian(0, out);

        // Statement type
        writeByteLittleEndian(1, out);

        // Joins
        out.writeBoolean(false);

        // Local query
        out.writeBoolean(false);

        // Replicated
        out.writeBoolean(false);

        // Enforce join order
        out.writeBoolean(false);

        // collocated
        out.writeBoolean(false);

        // Lazy
        out.writeBoolean(false);

        // Timeout
        writeLongLittleEndian(5000, out);

        // Replicated
        out.writeBoolean(false);

        // Read result
        DataInputStream in = new DataInputStream(socket.getInputStream());

        // Response header
        responseHeader(in);

        long cursorId = readLongLittleEndian(in);
        System.out.println("cursorId: " + cursorId);

        int colCount = readIntLittleEndian(in);
        System.out.println("colCount : " + colCount);

        int rowCount = readIntLittleEndian(in);
        System.out.println("rowCount: " + rowCount);

        // Read entries
        for (int i = 0; i < rowCount; i++){
            int resTypeCode1 = readByteLittleEndian(in);
            System.out.println(readLongLittleEndian(in));

            int resTypeCode2 = readByteLittleEndian(in);
            System.out.println(readIntLittleEndian(in));
        }

        boolean moreResults = readBooleanLittleEndian(in);
        System.out.println("moreResults: " + moreResults);
    }

    private static void getQueryCursorPage(Socket socket, long cursorId) throws IOException {
        DataOutputStream out = new DataOutputStream(socket.getOutputStream());

        // Request header
        requestHeader(8, OP_QUERY_SQL_CURSOR_GET_PAGE, 1, out);

        // Cursor Id
        writeLongLittleEndian(cursorId, out);

        // Send request
        out.flush();

        // Read result
        DataInputStream in = new DataInputStream(socket.getInputStream());

        // Response header
        responseHeader(in);

        int rowCount = readIntLittleEndian(in);
        System.out.println("rowCount: " + rowCount);

        // Read entries (as user objects)
        for (int i = 0; i < rowCount; i++){
            // ...
        }

        boolean moreResults = readBooleanLittleEndian(in);
        System.out.println("moreResults: " + moreResults);
    }

    private static void createCacheWithConfiguration(Socket socket) throws IOException {
        DataOutputStream out = new DataOutputStream(socket.getOutputStream());

        requestHeader(12, OP_CACHE_CREATE_WITH_CONFIGURATION, 1, out);

        // CacheAtomicityMode
        writeIntLittleEndian(0, out);

        // Backups
        writeIntLittleEndian(2, out);

        // CacheMode
        writeIntLittleEndian(2, out);

        // Read result
        DataInputStream in = new DataInputStream(socket.getInputStream());

        // Response header
        responseHeader(in);
    }

    private static void getCacheNames(Socket socket) throws IOException {
        DataOutputStream out = new DataOutputStream(socket.getOutputStream());

        // Request header
        requestHeader(0, OP_CACHE_GET_NAMES, 1, out);

        // Read result
        DataInputStream in = new DataInputStream(socket.getInputStream());

        // Response header
        responseHeader(in);

        // Cache count
        int cacheCount = readIntLittleEndian(in);
        System.out.println("cache count: " + cacheCount);

        // Cache names
        for (int i = 0; i < cacheCount; i++) {
           readString(in);
        }
    }

    private static void createCacheWithName(Socket socket) throws IOException {
        String cacheName = "myNewCache";

        DataOutputStream out = new DataOutputStream(socket.getOutputStream());

        // Request header
        requestHeader(5, OP_CACHE_CREATE_WITH_NAME, 1, out);

        // Cache name
        writeString(cacheName, out);

        // Read result
        DataInputStream in = new DataInputStream(socket.getInputStream());

        // Response header
        responseHeader(in);
    }


    private static void cacheContainsKeys(Socket socket, int[] key) throws IOException{
        DataOutputStream out = new DataOutputStream(socket.getOutputStream());

        // Request header
        requestHeader(19, OP_CACHE_CONTAINS_KEYS, 1, out);

        // Cache id
        writeIntLittleEndian(cacheName.hashCode(), out);

        // Flags = none
        writeByteLittleEndian(0, out);

        //Count
        writeIntLittleEndian(2, out);

        // Cache key
        writeByteLittleEndian(3, out);  // Integer type code
        writeIntLittleEndian(key[0], out);   // Cache key

        // Cache key
        writeByteLittleEndian(3, out);  // Integer type code
        writeIntLittleEndian(key[1], out);   // Cache key

        // Read result
        DataInputStream in = new DataInputStream(socket.getInputStream());

        // Response header
        responseHeader(in);

        boolean res = readBooleanLittleEndian(in);
        System.out.printf("contains key: " + res);
    }

    private static void cachePut(Socket socket, int key, int value) throws IOException {
        DataOutputStream out = new DataOutputStream(socket.getOutputStream());

        // Request header
        requestHeader(15, OP_CACHE_PUT, 1, out);

        // Cache id
        writeIntLittleEndian(cacheName.hashCode(), out);

        // Flags = none
        writeByteLittleEndian(0, out);

        // Cache key
        writeByteLittleEndian(3, out);  // Integer type code
        writeIntLittleEndian(key, out);   // Cache key

        writeByteLittleEndian(3, out);  // Integer type code
        writeIntLittleEndian(value, out);   // Cache value

        // send
        out.flush();

        // Read result
        DataInputStream in = new DataInputStream(socket.getInputStream());

        // Response header
        responseHeader(in);
    }

    private static void cachePutAll(Socket socket, int[] keys, int[] values) throws IOException {
        DataOutputStream out = new DataOutputStream(socket.getOutputStream());

        // Request header
        requestHeader(29, OP_CACHE_PUT_ALL, 1, out);

        // Cache id
        writeIntLittleEndian(cacheName.hashCode(), out);

        // Flags = none
        writeByteLittleEndian(0, out);

        // Entry Count
        writeIntLittleEndian(2, out);

        // Cache key
        writeByteLittleEndian(3, out);  // Integer type code
        writeIntLittleEndian(keys[0], out);   // Cache key

        writeByteLittleEndian(3, out);  // Integer type code
        writeIntLittleEndian(values[0], out);   // Cache value

        // Cache key
        writeByteLittleEndian(3, out);  // Integer type code
        writeIntLittleEndian(keys[1], out);   // Cache key

        writeByteLittleEndian(3, out);  // Integer type code
        writeIntLittleEndian(values[1], out);   // Cache value

        // send
        out.flush();

        // Read result
        DataInputStream in = new DataInputStream(socket.getInputStream());

        // Response header
        responseHeader(in);
    }

    private static void cacheGet(Socket socket, int key) throws IOException {
        DataOutputStream out = new DataOutputStream(socket.getOutputStream());

        // Request header
        requestHeader(10, OP_CACHE_GET, 1, out);

        // Cache id
        writeIntLittleEndian(cacheName.hashCode(), out);

        // Flags = none
        writeByteLittleEndian(0, out);

        // Cache key
        writeByteLittleEndian(3, out);  // Integer type code
        writeIntLittleEndian(key, out); // Cache key

        // send
        out.flush();

        // Read result
        DataInputStream in = new DataInputStream(socket.getInputStream());

        // Response header
        responseHeader(in);

        // Integer
        int resTypeCode = readByteLittleEndian(in);
        System.out.println("resTypeCode: " + resTypeCode);

        // Resulting cache value
        int result = readIntLittleEndian(in);
        System.out.println("result: " + result);
    }

    private static void cacheGetALL(Socket socket, int[] keys) throws IOException {
        DataOutputStream out = new DataOutputStream(socket.getOutputStream());

        // Request header
        requestHeader(19, OP_CACHE_GET_ALL, 1, out);

        // Cache id
        writeIntLittleEndian(cacheName.hashCode(), out);

        // Flags = none
        writeByteLittleEndian(0, out);

        // Key count
        writeIntLittleEndian(2, out);

        // Cache key
        writeByteLittleEndian(3, out);  // Integer type code
        writeIntLittleEndian(keys[0], out);   // Cache key

        // Cache key
        writeByteLittleEndian(3, out);  // Integer type code
        writeIntLittleEndian(keys[1], out);   // Cache key

        // send
        out.flush();

        // Read result
        DataInputStream in = new DataInputStream(socket.getInputStream());

        // Response header
        responseHeader(in);

        // Result count
        int resCount = readIntLittleEndian(in);
        System.out.println("resCount: " + resCount);

        for (int i = 0; i < resCount; i++) {
            // Integer
            int resKeyTypeCode = readByteLittleEndian(in);
            System.out.println("resKeyTypeCode" + i + ": "  + resKeyTypeCode);

            // Resulting key
            int resKey = readIntLittleEndian(in);
            System.out.println("resKey" + i + ": " + resKey);

            // Integer
            int resValTypeCode = readByteLittleEndian(in);
            System.out.println("resValTypeCode" + i + ": " + resValTypeCode);

            // Resulting cache value
            int resValue1 = readIntLittleEndian(in);
            System.out.println("resValue" + i + ": " + resValue1);
        }

    }

    private static void doHandshake (Socket socket) throws IOException{
        System.out.println("Handshake");

        DataOutputStream out = new DataOutputStream(socket.getOutputStream());

        // Message length
        writeIntLittleEndian(8, out);

        // Handshake operation
        writeByteLittleEndian(1, out);

        // Protocol version 1.0.0
        writeShortLittleEndian(1, out);
        writeShortLittleEndian(0, out);
        writeShortLittleEndian(0, out);

        // Client type: thin client
        writeByteLittleEndian(2, out);

        // send request
        out.flush();

        // Receive result
        DataInputStream in = new DataInputStream(socket.getInputStream());

        int length = readIntLittleEndian(in);
        System.out.println("handshake length: " + length);

        int res = readByteLittleEndian(in);
        System.out.println("handshake success/failure code: " + res);
    }

    private static void writeIntLittleEndian(int v, DataOutputStream out) throws IOException {
        out.write((v >>> 0) & 0xFF);
        out.write((v >>> 8) & 0xFF);
        out.write((v >>> 16) & 0xFF);
        out.write((v >>> 24) & 0xFF);
    }

    private static final void writeShortLittleEndian(int v, DataOutputStream out) throws IOException {
        out.write((v >>> 0) & 0xFF);
        out.write((v >>> 8) & 0xFF);
    }

    private static void writeLongLittleEndian(long v, DataOutputStream out) throws IOException {
        byte writeBuffer[] = new byte[8];
        writeBuffer[0] = (byte)(v >>>  0);
        writeBuffer[1] = (byte)(v >>>  8);
        writeBuffer[2] = (byte)(v >>> 16);
        writeBuffer[3] = (byte)(v >>> 24);
        writeBuffer[4] = (byte)(v >>> 32);
        writeBuffer[5] = (byte)(v >>> 40);
        writeBuffer[6] = (byte)(v >>> 48);
        writeBuffer[7] = (byte)(v >>> 56);
        out.write(writeBuffer, 0, 8);
    }

    private static void writeByteLittleEndian(int v, DataOutputStream out) throws IOException {
        out.writeByte(v);
    }

    private static void writeBytesLittleEndian(String v, DataOutputStream out) throws IOException {
        out.writeBytes(v);
    }

    private static void writeString (String str, DataOutputStream out) throws IOException {
        writeByteLittleEndian(9, out); // Type code for String

        int strLen = getStrLen(str); // Length of the string
        writeIntLittleEndian(strLen, out);

        out.writeBytes(str);
    }

    private static int getStrLen(String str) throws UnsupportedEncodingException {
        return str.getBytes("UTF-8").length;
    }

    private static int readIntLittleEndian(DataInputStream in) throws IOException {
        int ch1 = in.read();
        int ch2 = in.read();
        int ch3 = in.read();
        int ch4 = in.read();
        if ((ch1 | ch2 | ch3 | ch4) < 0)
            throw new EOFException();
        return ((ch4 << 24) + (ch3 << 16) + (ch2 << 8) + (ch1 << 0));
    }

    private static long readLongLittleEndian(DataInputStream in) throws IOException {
        byte readBuffer[] = new byte[8];
        readFully(in, readBuffer, 0, 8);
        return (((long)readBuffer[7] << 56) +
                ((long)(readBuffer[6] & 255) << 48) +
                ((long)(readBuffer[5] & 255) << 40) +
                ((long)(readBuffer[4] & 255) << 32) +
                ((long)(readBuffer[3] & 255) << 24) +
                ((readBuffer[2] & 255) << 16) +
                ((readBuffer[1] & 255) <<  8) +
                ((readBuffer[0] & 255) <<  0));
    }


    private static void readFully(DataInputStream in, byte b[], int off, int len) throws IOException {
        if (len < 0)
            throw new IndexOutOfBoundsException();
        int n = 0;
        while (n < len) {
            int count = in.read(b, off + n, len - n);
            if (count < 0)
                throw new EOFException();
            n += count;
        }
    }

    private static short readShortLittleEndian(DataInputStream in) throws IOException {
        int ch1 = in.read();
        int ch2 = in.read();
        if ((ch1 | ch2) < 0)
            throw new EOFException();
        return (short)((ch2 << 8) + (ch1 << 0));
    }

    private static byte readByteLittleEndian(DataInputStream in) throws IOException {
        return in.readByte();
    }

    private static boolean readBooleanLittleEndian(DataInputStream in) throws IOException {
        return in.readBoolean();
    }

    private static void readString(DataInputStream in) throws IOException {
        int type = readByteLittleEndian(in); // type code

        int strLen = readIntLittleEndian(in); // length

        byte[] buf = new byte[strLen];

        readFully(in, buf, 0, strLen);

        String s = new String(buf); // cache name

        System.out.println(s);
    }

    private static void requestHeader(int reqLength, int opCode, int reqId, DataOutputStream out) throws IOException {
        // Message length
        writeIntLittleEndian(10 + reqLength, out);

        // Op code
        writeShortLittleEndian(opCode, out);

        // Request id
        writeLongLittleEndian(reqId, out);
    }

    private static void responseHeader(DataInputStream in) throws IOException {
        // Response length
        final int len = readIntLittleEndian(in);
        System.out.println("len: " + len);

        // Request id
        long resReqId = readLongLittleEndian(in);
        System.out.println("resReqId: " + resReqId);

        // Success
        int statusCode = readIntLittleEndian(in);
        System.out.println("status code: " + statusCode);
    }
}