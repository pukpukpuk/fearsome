package one.autremel.fearsome;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum CharacterTeam {
    SURVIVORS("survivors", 0xFF3B8AA0, 0xFF46425E),
    MURDERERS("murderers", 0xFFCF7862, 0xFF3D203B),
    ;

    private final String folderName;
    private final int abilityProgressBarColor;
    private final int abilityCountNumberBackgroundColor;
}
