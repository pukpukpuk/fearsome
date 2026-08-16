package one.autremel.fearsome.input;

import com.google.common.base.Preconditions;
import lombok.Getter;

@Getter
public enum BindingId {
    EXPAND_SURVIVOR_LIST("expand_survivor_list"),

    // Sent to the server
    ATTACK(0, "attack"),
    INTERACTION(1, "interaction"),
    FIRST_ABILITY(2, "first_ability"),
    SECOND_ABILITY(3, "second_ability"),
    SMART_INTERACTION(4, "smart_interaction"),

    HEALING("healing"),
    ;

    private final byte byteId;
    private final String id;

    BindingId(String id) {
        this.byteId = -1;
        this.id = id;
    }

    BindingId(int byteId, String id) {
        Preconditions.checkArgument(byteId >= 0 && byteId <= 127);
        this.byteId = (byte)byteId;
        this.id = id;
    }
}
