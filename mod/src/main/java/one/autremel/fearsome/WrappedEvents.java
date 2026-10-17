package one.autremel.fearsome;

import com.google.common.base.Preconditions;
import net.fabricmc.fabric.impl.resource.ResourceLoaderImpl;
import net.minecraft.resource.ResourceType;
import net.minecraft.resource.SynchronousResourceReloader;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class WrappedEvents {
    private static final Identifier ID = FearsomeMod.identifier("wrapped_events");

    private static boolean initialized;
    private static final List<Runnable> registeredForResourceReload = new ArrayList<>();

    public static void initialize() {
        Preconditions.checkState(!initialized);
        initialized = true;

        ResourceLoaderImpl.get(ResourceType.CLIENT_RESOURCES).registerReloader(ID,
                (SynchronousResourceReloader) manager -> {
                    for (var runnable : registeredForResourceReload) {
                        runnable.run();
                    }
                });
    }

    public static void registerOnResourceReload(Runnable runnable) {
        registeredForResourceReload.add(runnable);
    }
}
