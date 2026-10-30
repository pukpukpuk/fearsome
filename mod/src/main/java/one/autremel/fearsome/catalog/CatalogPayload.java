package one.autremel.fearsome.catalog;

import net.minecraft.network.PacketByteBuf;
import one.autremel.fearsome.CharacterTeam;
import one.autremel.fearsome.network.IServerPayload;
import one.autremel.fearsome.network.Payload;
import one.autremel.fearsome.network.ServerPayloadId;
import org.jetbrains.annotations.NotNull;

@Payload
public record CatalogPayload(TeamCatalog survivors, TeamCatalog murderers) implements IServerPayload<CatalogPayload> {
    public static final ServerPayloadId<CatalogPayload> ID = new ServerPayloadId<>(
            CatalogPayload.class,
            false,
            CatalogPayload::decode
    );

    private static CatalogPayload decode(PacketByteBuf buf) {
        var survivors = TeamCatalog.read(buf, CharacterTeam.SURVIVORS);
        var murderers = TeamCatalog.read(buf, CharacterTeam.MURDERERS);
        return new CatalogPayload(survivors, murderers);
    }

    @Override
    public @NotNull ServerPayloadId<CatalogPayload> getPayloadId() {
        return ID;
    }
}
