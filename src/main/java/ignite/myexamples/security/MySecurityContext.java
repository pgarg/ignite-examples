package ignite.myexamples.security;

import org.apache.ignite.cluster.ClusterNode;
import org.apache.ignite.internal.processors.security.SecurityContext;
import org.apache.ignite.internal.util.lang.gridfunc.ClusterNodeGetIdClosure;
import org.apache.ignite.plugin.security.SecurityCredentials;
import org.apache.ignite.plugin.security.SecurityPermission;
import org.apache.ignite.plugin.security.SecuritySubject;

import java.io.Externalizable;
import java.io.Serializable;

public class MySecurityContext implements SecurityContext, Serializable {
    private ClusterNode node;
    private SecurityCredentials cred;

    public MySecurityContext(ClusterNode node, SecurityCredentials cred) {
        this.node = node;
        this.cred = cred;
    }

    @Override
    public SecuritySubject subject() { return null; }

    @Override
    public boolean taskOperationAllowed(String taskClsName, SecurityPermission perm) {
        return false;
    }

    @Override
    public boolean cacheOperationAllowed(String cacheName, SecurityPermission perm) {
        return false;
    }

    @Override
    public boolean serviceOperationAllowed(String srvcName, SecurityPermission perm) {
        return false;
    }

    @Override
    public boolean systemOperationAllowed(SecurityPermission perm) {
        return false;
    }
}
