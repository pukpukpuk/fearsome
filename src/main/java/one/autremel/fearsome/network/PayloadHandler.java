package one.autremel.fearsome.network;

import lombok.RequiredArgsConstructor;

import java.util.function.Consumer;

@RequiredArgsConstructor
class PayloadHandler<T extends IPayload> {
    private final Consumer<T> consumer;

    public void call(T payload) {
        consumer.accept(payload);
    }
}
