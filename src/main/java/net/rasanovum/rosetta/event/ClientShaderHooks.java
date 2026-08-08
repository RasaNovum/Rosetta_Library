package net.rasanovum.rosetta.event;

//? if <26.1 {
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

/** Legacy core-shader registration without a direct Fabric API dependency. */
public final class ClientShaderHooks {
    private static final List<Registration> REGISTRATIONS = new CopyOnWriteArrayList<>();

    private ClientShaderHooks() {}

    public static void register(Registration registration) { REGISTRATIONS.add(registration); }

    public static void registerShaders(Registrar registrar) throws IOException {
        for (Registration registration : REGISTRATIONS) registration.register(registrar);
    }

    @FunctionalInterface
    public interface Registration {
        void register(Registrar registrar) throws IOException;
    }

    @FunctionalInterface
    public interface Registrar {
        void register(ResourceLocation id, VertexFormat format, Consumer<ShaderInstance> onLoad) throws IOException;
    }
}
//?} else {
/*public final class ClientShaderHooks {
    private ClientShaderHooks() {}
}
*///?}
