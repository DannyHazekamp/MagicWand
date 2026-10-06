package me.Danny.magicWand;

import org.bukkit.*;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.scheduler.BukkitRunnable;

public class FireballSpell implements Spell, Listener {

    @Override
    public void cast(Player player) {
        Location spawnLoc = player.getEyeLocation().add(player.getLocation().getDirection().multiply(2));
        Fireball fireball = player.getWorld().spawn(spawnLoc, Fireball.class);
        fireball.setVelocity(player.getLocation().getDirection().multiply(4.0));
        player.sendMessage("You cast a fireball!");

        new BukkitRunnable() {
            @Override
            public void run() {
                if (fireball.isDead() || !fireball.isValid()) {
                    cancel();
                    return;
                }
                Location loc = fireball.getLocation();
                Firework fw = (Firework) loc.getWorld().spawnEntity(loc, EntityType.FIREWORK_ROCKET);
                FireworkMeta fwm = fw.getFireworkMeta();
                fwm.addEffect(FireworkEffect.builder().withColor(Color.ORANGE).withFade(Color.RED).with(FireworkEffect.Type.BURST).build());

                fw.setFireworkMeta(fwm);
                fw.detonate();
            }
        }.runTaskTimer(MagicWand.getPlugin(MagicWand.class), 0L, 1L);

    }

    @EventHandler
    public void onFireballHit(ProjectileHitEvent event) {
        if(event.getEntity() instanceof Fireball fireball) {
            Location location = fireball.getLocation();
            fireball.getWorld().createExplosion(location, 8F, true);
            fireball.getWorld().spawnParticle(Particle.EXPLOSION, location, 1);
            fireball.getWorld().spawnParticle(Particle.FLAME, location, 30, 1.2, 1.0, 1.2, 0.05);
            fireball.getWorld().spawnParticle(Particle.SMOKE, location, 20, 1.5, 1.2, 1.5, 0.01);
        }
    }
}
