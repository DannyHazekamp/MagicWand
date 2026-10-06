package me.Danny.magicWand;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.command.Command;

public final class MagicWand extends JavaPlugin implements Listener {

    private final FireballSpell fireballSpell = new FireballSpell();
    private SpellManager spellManager;

    @Override
    public void onEnable() {
        // Plugin startup logic
        getLogger().info("Plugin enabled!");
        getServer().getPluginManager().registerEvents(this, this);

        spellManager = new SpellManager();
        FireballSpell fireballSpell = new FireballSpell();
        getServer().getPluginManager().registerEvents(fireballSpell, this);
    }

    public SpellManager getSpellManager() {
        return spellManager;
    }

    @Override
    public void onDisable() {
        getLogger().info("Plugin disabled!");
    }

    // event handler to get player that joins
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        getLogger().info("hello" + player.getName());
    }

    // Command for /magicwand
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(command.getName().equalsIgnoreCase("magicwand")) {
            Player player = (Player) sender;
            PlayerInventory inventory = player.getInventory();

            ItemStack item = new ItemStack(Material.BLAZE_ROD, 1);
            ItemMeta itemMeta = item.getItemMeta();
            itemMeta.displayName(Component.text("Magic Wand").color(NamedTextColor.DARK_RED));

            item.setItemMeta(itemMeta);

            inventory.addItem(item);

            player.sendMessage("You have been given a powerful wand");
            return true;
        }
        return false;
    }

    @EventHandler
    public void onLeftClick(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();

        if(item.getType() != Material.BLAZE_ROD) {
            return;
        }

        if(!item.hasItemMeta() || !item.getItemMeta().hasDisplayName()) {
            return;
        }

        if(!Component.text("Magic Wand").color(NamedTextColor.DARK_RED)
                .equals(item.getItemMeta().displayName())) {
            return;
        }

        if(event.getAction() == Action.LEFT_CLICK_BLOCK || event.getAction() == Action.LEFT_CLICK_AIR) {
            spellManager.getSelectedSpell(player).cast(player);
        }
    }

    @EventHandler
    public void onRightCLick(PlayerInteractEvent event) {
        if (event.getHand() == EquipmentSlot.OFF_HAND) return;
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();

        if(item.getType() != Material.BLAZE_ROD) {
            return;
        }

        if(!item.hasItemMeta() || !item.getItemMeta().hasDisplayName()) {
            return;
        }

        if(!Component.text("Magic Wand").color(NamedTextColor.DARK_RED)
                .equals(item.getItemMeta().displayName())) {
            return;
        }

        if(event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR) {
            MagicWand plugin = MagicWand.getPlugin(MagicWand.class);
            plugin.getSpellManager().cycleSpell(player);
        }

    }
}
