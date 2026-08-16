package one.autremel.fearsome.network;

import org.jetbrains.annotations.NotNull;

public interface IClientPayload<T extends IClientPayload<T>> extends IPayload<T> {
    @NotNull
    ClientPayloadId<T> getPayloadId();
}
