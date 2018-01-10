package ignite.myexamples.plugin;

import ignite.myexamples.security.MyGridSecurityProcessor;
import org.apache.ignite.IgniteCheckedException;
import org.apache.ignite.cluster.ClusterNode;
import org.apache.ignite.internal.processors.security.GridSecurityProcessor;
import org.apache.ignite.plugin.*;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.UUID;

public class MyPluginProvider implements PluginProvider<MyPluginConfiguration> {
    @Override
    public String name() {
        return "IGNITE";
    }

    @Override
    public String version() {
        return null;
    }

    @Override
    public String copyright() {
        return null;
    }

    @Override
    public <T extends IgnitePlugin> T plugin() {
        return (T)new MyPlugin();
    }

    @Override
    public void initExtensions(PluginContext pluginContext, ExtensionRegistry extensionRegistry) throws IgniteCheckedException {

    }

    @Nullable
    @Override
    public <T> T createComponent(PluginContext pluginContext, Class<T> aClass) {
        if (aClass.isAssignableFrom(GridSecurityProcessor.class)) {
            System.out.println("Returned security provider");
            return (T)new MyGridSecurityProcessor();
        } else {
            return null;
        }
    }

    @Override
    public CachePluginProvider createCacheProvider(CachePluginContext cachePluginContext) {
        return null;
    }

    @Override
    public void start(PluginContext pluginContext) throws IgniteCheckedException {

    }

    @Override
    public void stop(boolean b) throws IgniteCheckedException {

    }

    @Override
    public void onIgniteStart() throws IgniteCheckedException {
        System.out.println("Hello");
    }

    @Override
    public void onIgniteStop(boolean b) {

    }

    @Nullable
    @Override
    public Serializable provideDiscoveryData(UUID uuid) {
        return null;
    }

    @Override
    public void receiveDiscoveryData(UUID uuid, Serializable serializable) {

    }

    @Override
    public void validateNewNode(ClusterNode clusterNode) throws PluginValidationException {
        System.out.println("Validate new Node");
    }
}
