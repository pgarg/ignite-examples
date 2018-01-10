package ignite.myexamples;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.Ignition;

import java.io.IOException;

public class SimplePutGet {
    public static void main(String[] args) throws IOException {
        try (Ignite ignite = Ignition.start("/Users/prachig/myexamples/config/cluster-config.xml")) {
            IgniteCache<Integer, String> cache = ignite.getOrCreateCache("myCache");
            cache.put(5,"55");

            System.out.println(cache.get(5));
        }

    }
}
