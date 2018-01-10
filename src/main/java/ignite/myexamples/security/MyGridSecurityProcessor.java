package ignite.myexamples.security;

import org.apache.ignite.IgniteCheckedException;
import org.apache.ignite.cluster.ClusterNode;
import org.apache.ignite.internal.IgniteInternalFuture;
import org.apache.ignite.internal.processors.security.GridSecurityProcessor;
import org.apache.ignite.internal.processors.security.SecurityContext;
import org.apache.ignite.lang.IgniteFuture;
import org.apache.ignite.plugin.security.*;
import org.apache.ignite.plugin.security.SecurityException;
import org.apache.ignite.spi.IgniteNodeValidationResult;
import org.apache.ignite.spi.discovery.DiscoveryDataBag;
import org.apache.ignite.spi.discovery.DiscoverySpiNodeAuthenticator;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.UUID;

public class MyGridSecurityProcessor implements DiscoverySpiNodeAuthenticator, GridSecurityProcessor {

    @Override
    public SecurityContext authenticateNode(ClusterNode node, SecurityCredentials cred) {
        System.out.println("SecurityContext authenticateNode");
        return new MySecurityContext(node, cred);
    }

    @Override
    public boolean isGlobalNodeAuthentication() {
        return true;
    }

    @Override
    public SecurityContext authenticate(AuthenticationContext ctx) throws IgniteCheckedException {
        System.out.println("Request to authenticate!");
        return null;
    }

    @Override
    public Collection<SecuritySubject> authenticatedSubjects() throws IgniteCheckedException {
        return null;
    }

    @Override
    public SecuritySubject authenticatedSubject(UUID subjId) throws IgniteCheckedException {
        return null;
    }

    @Override
    public void authorize(String name, SecurityPermission perm, @Nullable SecurityContext securityCtx) throws SecurityException {

    }

    @Override
    public void onSessionExpired(UUID subjId) {

    }

    @Override
    /**
     * IMPORTANT to Document.
     */
    public boolean enabled() {
        return true;
    }

    @Override
    public void start() throws IgniteCheckedException {
        System.out.println("START");
    }

    @Override
    public void stop(boolean cancel) throws IgniteCheckedException {

    }

    @Override
    public void onKernalStart(boolean active) throws IgniteCheckedException {

    }

    @Override
    public void onKernalStop(boolean cancel) {

    }

    @Override
    public void collectJoiningNodeData(DiscoveryDataBag dataBag) {

    }

    @Override
    public void collectGridNodeData(DiscoveryDataBag dataBag) {

    }

    @Override
    public void onGridDataReceived(DiscoveryDataBag.GridDiscoveryData data) {

    }

    @Override
    public void onJoiningNodeDataReceived(DiscoveryDataBag.JoiningNodeDiscoveryData data) {

    }

    @Override
    public void printMemoryStats() {

    }

    @Nullable
    @Override
    public IgniteNodeValidationResult validateNode(ClusterNode node) {
        System.out.println("validatenode");
        return new IgniteNodeValidationResult(node.id(),
                    "Node validation", "Node validation");
    }

    @Nullable
    @Override
    public DiscoveryDataExchangeType discoveryDataType() {
        return null;
    }

    @Override
    public void onDisconnected(IgniteFuture<?> reconnectFut) throws IgniteCheckedException {

    }

    @Nullable
    @Override
    public IgniteInternalFuture<?> onReconnected(boolean clusterRestarted) throws IgniteCheckedException {
        return null;
    }
}
