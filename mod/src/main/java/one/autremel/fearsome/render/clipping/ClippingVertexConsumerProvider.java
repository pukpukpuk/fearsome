package one.autremel.fearsome.render.clipping;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;

import java.util.ArrayList;
import java.util.List;

public class ClippingVertexConsumerProvider implements VertexConsumerProvider {
    private final VertexConsumerProvider delegate;
    private final ClippingPass pass;

    private final List<ClippingVertexConsumer> created = new ArrayList<>();

    public ClippingVertexConsumerProvider(VertexConsumerProvider delegate, ClippingPass pass) {
        this.delegate = delegate;
        this.pass = pass;
    }

    @Override
    public VertexConsumer getBuffer(RenderLayer layer) {
        var consumer = new ClippingVertexConsumer(delegate.getBuffer(layer), pass);
        created.add(consumer);
        return consumer;
    }

    public void flush() {
        created.forEach(ClippingVertexConsumer::commitVertices);
        created.clear();
    }
}
