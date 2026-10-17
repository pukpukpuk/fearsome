package one.autremel.fearsome.mixinhelpers;

import net.minecraft.client.render.command.OrderedRenderCommandQueueImpl;

import java.util.List;

public interface ICommandsAccess {
    List<OrderedRenderCommandQueueImpl.LabelCommand> getNormalLabels();
}
