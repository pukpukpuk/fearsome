package one.autremel.fearsome.ui.menus;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public enum MenuSound {
    CLICK("entity.wither_skeleton.step", 0.125f, 1.75f),
    ERROR("block.note_block.didgeridoo", 0.55f, 0.7f),
    PAGE_TURN("item.book.page_turn", 0.3f, 1.4f),
    EQUIP_CHARACTER("block.respawn_anchor.charge", 0.3f, 1.7f),
    EQUIP_PERK("item.lodestone_compass.lock", 0.4f, 1.7f),
    UNEQUIP_PERK("entity.turtle.death", 0.2f, 0.6f),
    ;

    private final SoundEvent event;
    private final float volume, pitch;

    MenuSound(String id, float volume, float pitch) {
        this.event = SoundEvent.of(Identifier.ofVanilla(id));
        this.volume = volume;
        this.pitch = pitch;
    }

    public void play() {
        MinecraftClient.getInstance().getSoundManager().play(PositionedSoundInstance.ui(event, pitch, volume));
    }
}
