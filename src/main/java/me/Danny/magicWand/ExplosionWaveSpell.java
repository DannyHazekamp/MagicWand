package me.Danny.magicWand;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.*;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class ExplosionWaveSpell implements Spell, Listener {
    public void cast(Player player) {
        Location spawnLoc = player.getEyeLocation();
        Vector direction = spawnLoc.getDirection().normalize();

        int explosions = 8;
        double step = 2.0;
        float explosionPower = 4.0f; // TNT-achtige kracht
        double affectRadius = 3.0;   // radius om entities te beïnvloeden
        float extraDamage = 4.0f;

        player.sendMessage("You cast a Explosive wave!");

        new BukkitRunnable() {

            int index = 0;

            @Override
            public void run() {
                if (index >= explosions) {
                    cancel();
                    return;
                }

                Location point = spawnLoc.clone().add(direction.clone().multiply(step * index));

                // Visual TNT explosion effect
                point.getWorld().createExplosion(point, 8F, true);
                point.getWorld().playSound(point, Sound.ENTITY_GENERIC_EXPLODE, 1f, 1f);

                for (Entity e : point.getWorld().getNearbyEntities(point, affectRadius, affectRadius, affectRadius)) {
                    if (e.equals(player)) continue; // skip caster

                    if (e instanceof LivingEntity living) {
                        living.damage(extraDamage, player); // optioneel extra damage

                        // Pushback van het explosiepunt af
                        Vector push = living.getLocation().toVector().subtract(point.toVector()).normalize().multiply(1.5);
                        push.setY(Math.max(0.2, push.getY())); // kleine verticale boost
                        living.setVelocity(push);
                    }
                }

                index++;
            }
        }.runTaskTimer(MagicWand.getPlugin(MagicWand.class), 0L, 6L);
    }
}
