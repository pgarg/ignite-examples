package ignite.myexamples;

import org.apache.ignite.*;
import org.apache.ignite.internal.processors.platform.client.ClientMessageParser;

public class LoggerExample {

    public static void main(String[] args) throws IgniteException {
        try (Ignite ignite = Ignition.start("/Users/prachig/myexamples/config/cluster-config.xml")) {
            ignite.log().info("Info Message 1 Logged !!!");
            ignite.log().info("Info Message 2 Logged !!!");
            
            System.out.println(ignite.log().getClass());
        }
    }
}
