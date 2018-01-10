package ignite.myexamples;

import ignite.myexamples.model.Address;
import ignite.myexamples.model.Person;
import org.apache.ignite.IgniteCheckedException;
import org.apache.ignite.IgniteException;
import org.apache.ignite.Ignition;
import org.apache.ignite.configuration.*;
import org.apache.ignite.logger.log4j.Log4JLogger;
import org.apache.ignite.plugin.security.SecurityCredentials;
import org.apache.ignite.plugin.security.SecurityCredentialsBasicProvider;

public class IgniteNodeStartup {
    public static void main(String[] args) throws IgniteException {


        IgniteConfiguration cfg = new IgniteConfiguration();

//        ClientConnectorConfiguration ccfg = new ClientConnectorConfiguration();
//        ccfg.setHost("127.0.0.1");
//        ccfg.setPort(10900);
//        ccfg.setPortRange(30);
//
//        cfg.setClientConnectorConfiguration(ccfg);
//
//        // Cache configuration
//        CacheConfiguration<Long, Person> cacheCfg = new CacheConfiguration<>();
//        cacheCfg.setName("personCache");
//        // Registering indexed type
//        cacheCfg.setIndexedTypes(Long.class, Person.class);
//
//        cfg.setCacheConfiguration(cacheCfg);
//
//        Ignition.start(cfg);

        Ignition.start("/Users/prachig/myexamples/config/cluster-config.xml");
    }
}