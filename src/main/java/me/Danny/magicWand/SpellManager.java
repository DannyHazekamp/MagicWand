package me.Danny.magicWand;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.util.*;

public class SpellManager {
    private final List<Spell> spells = new ArrayList<>();
    private final Map<UUID, Integer> selectedSpell = new HashMap<>();

    public SpellManager() {
        spells.add(new FireballSpell());
        spells.add(new LeapSpell());
        spells.add(new ExplosionWaveSpell());

    }

    public Spell getSelectedSpell(Player player) {
        int index = selectedSpell.getOrDefault(player.getUniqueId(), 0);
        return spells.get(index);
    }

    public void cycleSpell(Player player) {
        UUID id = player.getUniqueId();
        int index = selectedSpell.getOrDefault(id, 0);

        index = (index + 1) % spells.size();
        selectedSpell.put(id, index);

        player.sendMessage("Selected spell: " + spells.get(index).getClass().getSimpleName());
    }


}
