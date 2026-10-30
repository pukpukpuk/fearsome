package one.autremel.fearsome.builds;

import net.minecraft.network.PacketByteBuf;
import one.autremel.fearsome.network.IServerPayload;
import one.autremel.fearsome.network.Payload;
import one.autremel.fearsome.network.ServerPayloadId;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Payload
public record PlayerBuildsPayload(TeamBuilds survivors, TeamBuilds murderers) implements IServerPayload<PlayerBuildsPayload> {
    public static final ServerPayloadId<PlayerBuildsPayload> ID = new ServerPayloadId<>(
            PlayerBuildsPayload.class,
            false,
            PlayerBuildsPayload::decode
    );

    private static PlayerBuildsPayload decode(PacketByteBuf buf) {
        return new PlayerBuildsPayload(readTeam(buf), readTeam(buf));
    }

    private static TeamBuilds readTeam(PacketByteBuf buf) {
        var selectedCharacterId = buf.readVarInt();

        var count = buf.readVarInt();
        var builds = new ArrayList<Set<Integer>>(count);
        for (int i = 0; i < count; i++) {
            var size = Byte.toUnsignedInt(buf.readByte());
            var build = new HashSet<Integer>(size);
            for (int j = 0; j < size; j++) build.add(buf.readVarInt());
            builds.add(build);
        }

        return new TeamBuilds(selectedCharacterId, builds);
    }

    @Override
    public @NotNull ServerPayloadId<PlayerBuildsPayload> getPayloadId() {
        return ID;
    }

    public record TeamBuilds(int selectedCharacterId, List<Set<Integer>> builds) {}
}
