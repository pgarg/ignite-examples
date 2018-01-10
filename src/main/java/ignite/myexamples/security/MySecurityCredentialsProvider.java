package ignite.myexamples.security;

import org.apache.ignite.IgniteCheckedException;
import org.apache.ignite.plugin.security.SecurityCredentials;
import org.apache.ignite.plugin.security.SecurityCredentialsProvider;

public class MySecurityCredentialsProvider implements SecurityCredentialsProvider {
    private SecurityCredentials creds;

    public MySecurityCredentialsProvider(SecurityCredentials creds) {
        System.out.println(creds.getLogin());
        this.creds = creds;
    }

    @Override
    public SecurityCredentials credentials() throws IgniteCheckedException {
        return creds;
    }
}
