package one.autremel.fearsome.mixin;

import net.minecraft.client.render.command.OrderedRenderCommandQueueImpl;
import one.autremel.fearsome.mixinhelpers.ICommandsAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Mixin(targets = "net.minecraft.client.render.command.LabelCommandRenderer$Commands")
public interface LabelCommandRendererCommandsAccessor extends ICommandsAccess {
    @Accessor("normalLabels") @Override
    List<OrderedRenderCommandQueueImpl.LabelCommand> getNormalLabels();
}

