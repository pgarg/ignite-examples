package ignite.myexamples.store;

import ignite.myexamples.model.Person;
import org.apache.ignite.cache.store.CacheStore;
import org.apache.ignite.lang.IgniteBiInClosure;
import org.apache.ignite.resources.SpringResource;
import org.jetbrains.annotations.Nullable;

import javax.cache.Cache;
import javax.cache.integration.CacheLoaderException;
import javax.cache.integration.CacheWriterException;
import javax.sql.DataSource;
import java.sql.*;
import java.util.Collection;
import java.util.Map;

public class PersonStore implements CacheStore<Long, Person> {

    @SpringResource(resourceName = "dataSource")
    private DataSource dataSource;

    @Override
    public void loadCache(IgniteBiInClosure<Long, Person> clo, @Nullable Object... objects) throws CacheLoaderException {
        System.out.println(">> Loading cache from store...");

//        try (Connection conn = dataSource.getConnection()) {
//            try (PreparedStatement st = conn.prepareStatement("select * from PERSON")) {
//                try (ResultSet rs = st.executeQuery()) {
//                    while (rs.next()) {
//                        Person person = new Person(rs.getLong(1), rs.getString(3), rs.getInt(4));
//
//                        clo.apply(person.getId(), person);
//                    }
//                }
//            }
//        }
//        catch (SQLException e) {
//            throw new CacheLoaderException("Failed to load values from cache store.", e);
//        }
    }

    @Override
    public Person load(Long key) throws CacheLoaderException {
        System.out.println(">> Loading person from store...");

//        try (Connection conn = dataSource.getConnection()) {
//            try (PreparedStatement st = conn.prepareStatement("select * from PERSON where id = ?")) {
//                st.setString(1, key.toString());
//
//                ResultSet rs = st.executeQuery();
//
//                return rs.next() ? new Person(rs.getLong(1), rs.getString(3), rs.getInt(4)) : null;
//            }
//        }
//        catch (SQLException e) {
//            throw new CacheLoaderException("Failed to load values from cache store.", e);
//        }

        return null;
    }

    @Override
    public Map<Long, Person> loadAll(Iterable<? extends Long> iterable) throws CacheLoaderException {
        return null;
    }

    @Override
    public void write(Cache.Entry<? extends Long, ? extends Person> entry) throws CacheWriterException {

    }

    @Override
    public void writeAll(Collection<Cache.Entry<? extends Long, ? extends Person>> collection) throws CacheWriterException {

    }

    @Override
    public void delete(Object o) throws CacheWriterException {

    }

    @Override
    public void deleteAll(Collection<?> collection) throws CacheWriterException {

    }

    @Override
    public void sessionEnd(boolean b) throws CacheWriterException {

    }
}
