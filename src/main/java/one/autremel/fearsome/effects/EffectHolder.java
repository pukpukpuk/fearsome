package one.autremel.fearsome.effects;

import java.util.*;

class EffectHolder {
    private final HashMap<Integer, DisplayedEffect> effectMap = new HashMap<>();
    private final HashSet<DisplayedEffect> withDisplayName = new HashSet<>();
    private final HashSet<DisplayedEffect> withIcon = new HashSet<>();
    private final Set<DisplayedEffect> withDisplayNameCopy = Collections.unmodifiableSet(withDisplayName);
    private final Set<DisplayedEffect> withIconCopy = Collections.unmodifiableSet(withIcon);

    public Set<DisplayedEffect> getWithDisplayName() {
        return withDisplayNameCopy;
    }

    public Set<DisplayedEffect> getWithIcon() {
        return withIconCopy;
    }

    void add(DisplayedEffect effect) {
        effectMap.put(effect.getUniqueId(), effect);
        if (effect.getName() != null) withDisplayName.add(effect);
        if (effect.getIconSymbol() != null) withIcon.add(effect);
    }

    DisplayedEffect remove(int id) {
        var effect = effectMap.remove(id);
        if (effect == null) return null;

        if (effect.getName() != null) withDisplayName.remove(effect);
        if (effect.getIconSymbol() != null) withIcon.remove(effect);
        return effect;
    }

    DisplayedEffect get(int id) {
        return effectMap.get(id);
    }

    void clear() {
        effectMap.clear();
        withDisplayName.clear();
        withIcon.clear();
    }
}
