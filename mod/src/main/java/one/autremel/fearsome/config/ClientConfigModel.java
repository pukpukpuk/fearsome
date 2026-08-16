package one.autremel.fearsome.config;

import io.wispforest.owo.config.annotation.Config;
import io.wispforest.owo.config.annotation.Modmenu;
import io.wispforest.owo.config.annotation.SectionHeader;
import one.autremel.fearsome.FearsomeMod;

@Modmenu(modId = FearsomeMod.NAMESPACE)
@Config(name = FearsomeMod.NAMESPACE+"-client-config", wrapperName = "ClientConfig")
public class ClientConfigModel {
    @SectionHeader("controls")
    public SurvivorListExpandType survivorListExpandBy = SurvivorListExpandType.HOLD;

    public enum SurvivorListExpandType {
        HOLD, TOGGLE
    }
}
