package net.rasanovum.rosetta.entrypoint;

import net.rasanovum.rosetta.event.ClientHooks;
import net.rasanovum.rosetta.event.ClientRenderHooks;
import net.rasanovum.rosetta.event.ServerHooks;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ConcurrentHashMap;

public final class RosettaEntrypoints {
    private static final List<RosettaCommonEntrypoint> COMMON = new CopyOnWriteArrayList<>();
    private static final Set<Class<?>> REGISTERED = ConcurrentHashMap.newKeySet();
    private static final Set<Class<?>> CLIENT_REGISTERED = ConcurrentHashMap.newKeySet();

    private RosettaEntrypoints() {}

    public static void register(RosettaCommonEntrypoint entrypoint) {
        if (!REGISTERED.add(entrypoint.getClass())) return;
        ServerHooks.register(entrypoint.serverHooks());
        COMMON.add(entrypoint);
    }

    public static void initializeClients() {
        for (RosettaCommonEntrypoint common : COMMON) {
            RosettaClientEntrypoint client = common.clientEntrypoint();
            if (client == null || !CLIENT_REGISTERED.add(client.getClass())) continue;
            client.initialize();
            ClientHooks.register(client);
            ClientRenderHooks.register(client);
        }
    }
}
