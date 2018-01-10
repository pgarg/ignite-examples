package ignite.myexamples;

import ignite.myexamples.model.Address;
import ignite.myexamples.model.Person;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.IgniteCheckedException;
import org.apache.ignite.Ignition;
import org.apache.ignite.cache.query.QueryCursor;
import org.apache.ignite.cache.query.SqlFieldsQuery;
import org.apache.ignite.configuration.CacheConfiguration;
import org.apache.ignite.configuration.IgniteConfiguration;

import java.io.IOException;
import java.util.List;

public class IndexExample {

    public static void main(String[] args) throws IOException, IgniteCheckedException {
        Ignition.setClientMode(true);

        try (Ignite ignite = Ignition.start()) {
            IgniteCache<Long, Person> personCache = ignite.getOrCreateCache("personCache");

            personCache.put(Long.valueOf(1), new Person(1, "Alex", 1000, new Address("street1", 11111)));
            personCache.put(Long.valueOf(2), new Person(2, "Dan", 2000, new Address("street2", 22222)));
            personCache.put(Long.valueOf(3), new Person(3, "Rob", 3000, new Address("street3", 33333)));

            QueryCursor<List<?>> cursor = personCache.query(new SqlFieldsQuery(
                    "select * from Person where street = 'street1'"));

            System.out.println(cursor.getAll());
        }
    }
}
