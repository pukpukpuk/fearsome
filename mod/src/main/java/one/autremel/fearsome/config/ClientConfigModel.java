package one.autremel.fearsome.config;

import io.wispforest.owo.config.annotation.Config;
import io.wispforest.owo.config.annotation.Modmenu;
import io.wispforest.owo.config.annotation.SectionHeader;
import one.autremel.fearsome.FearsomeMod;

@Modmenu(modId = FearsomeMod.NAMESPACE)
@Config(name = FearsomeMod.NAMESPACE+"-client-config", wrapperName = "ClientConfig")
public class ClientConfigModel {
    @SectionHeader("controls")
    public ViewMethodType survivorListExpandMethod = ViewMethodType.HOLD;
    public ViewMethodType generatorsHighlightMethod = ViewMethodType.TOGGLE;

    @SectionHeader("vanilla")
    public boolean useChatSizePreset = true;
    public boolean allowMessageVerifications = false;
}
