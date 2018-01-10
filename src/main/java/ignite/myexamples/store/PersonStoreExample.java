package ignite.myexamples.store;

import ignite.myexamples.model.Person;
import org.apache.ignite.*;
import org.apache.ignite.cache.CacheMode;
import org.apache.ignite.cache.affinity.AffinityKey;
import org.apache.ignite.cache.query.QueryCursor;
import org.apache.ignite.cache.query.SqlFieldsQuery;
import org.apache.ignite.cluster.ClusterNode;
import org.apache.ignite.compute.ComputeJob;
import org.apache.ignite.compute.ComputeJobResult;
import org.apache.ignite.compute.gridify.GridifyArgument;
import org.apache.ignite.compute.gridify.GridifyTaskAdapter;
import org.apache.ignite.configuration.CacheConfiguration;
import org.apache.ignite.internal.util.gridify.GridifyJobAdapter;
import org.apache.ignite.internal.util.typedef.CA;
import org.apache.ignite.internal.util.typedef.CAX;
import org.apache.ignite.lang.IgniteCallable;
import org.apache.ignite.resources.IgniteInstanceResource;
import org.apache.ignite.scheduler.SchedulerFuture;
import org.jetbrains.annotations.Nullable;

import javax.cache.Cache;
import java.util.*;

import static org.apache.ignite.internal.GridClosureCallMode.BROADCAST;

public class PersonStoreExample {
    public static void main(String[] args) throws IgniteException {
        Ignition.setClientMode(true);

        try (Ignite ignite = Ignition.start("config/cluster-config.xml")) {
            try (IgniteCache<Long, Person> cache = ignite.getOrCreateCache("personCache")) {
                // Load cache with data from the database.
                cache.loadCache(null);

                cache.query(new SqlFieldsQuery("INSERT INTO Person(_key, firstName, " +
                        "salary) values (1, 'John', '1000'), (5, 'Mary', '2000')"));

                // Execute query to get id and names of all employees.
                QueryCursor<List<?>> cursor = cache.query(new SqlFieldsQuery(
                        "select id, name from Person"));

                System.out.println(cursor.getAll());
            }
        }
    }
}
