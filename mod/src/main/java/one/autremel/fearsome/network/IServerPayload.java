package one.autremel.fearsome.network;

import org.jetbrains.annotations.NotNull;

public interface IServerPayload<T extends IServerPayload<T>> extends IPayload<T> {
    @NotNull
    ServerPayloadId<T> getPayloadId();
}
