package me.Danny.magicWand;

import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.meta.FireworkMeta;

public class LeapSpell implements Spell, Listener {

    public void cast(Player player) {
        player.sendMessage("You took a leap");
        player.setVelocity(player.getLocation().getDirection().multiply(3).setY(1.5));
        Location loc = player.getLocation();
        Firework fw = (Firework) loc.getWorld().spawnEntity(loc, EntityType.FIREWORK_ROCKET);
        FireworkMeta fwm = fw.getFireworkMeta();
        fwm.addEffect(FireworkEffect.builder().withColor(Color.BLACK).withFade(Color.BLACK).with(FireworkEffect.Type.BURST).build());

        fw.setFireworkMeta(fwm);
        fw.detonate();
    }
}
