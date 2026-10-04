package one.autremel.fearsome.ui.terror;

import net.minecraft.client.sound.AbstractSoundInstance;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;

class BeatSoundInstance extends AbstractSoundInstance {
    protected BeatSoundInstance(float volume) {
        super(SoundEvents.BLOCK_NOTE_BLOCK_BASEDRUM.value(), SoundCategory.HOSTILE, SoundInstance.createRandom());
        relative = true;
        attenuationType = AttenuationType.NONE;

        pitch = Math.max(random.nextTriangular(0.5f, 0.1f), 0.5f);
        this.volume = Math.max(volume + random.nextFloat() * 0.1f, 0);
    }


    @Override
    public float getPitch() {
        return pitch;
    }

    @Override
    public float getVolume() {
        return volume;
    }
}
