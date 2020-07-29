package br.tijela.swordlevelv3.events;

import br.tijela.swordlevelv3.SwordLevel;
import br.tijela.swordlevelv3.bonus.Bonus;
import br.tijela.swordlevelv3.manager.Group;
import br.tijela.swordlevelv3.manager.GroupManager;
import br.tijela.swordlevelv3.storage.level.StorageLevel;
import br.tijela.swordlevelv3.util.SwordUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

public class Bonuses implements Listener{

    protected final GroupManager manager;

    public Bonuses(GroupManager manager){
        this.manager = manager;
        Bukkit.getPluginManager().registerEvents(this, SwordLevel.getInstance());
    }

    @EventHandler(priority = EventPriority.HIGH)
    protected void onHit(EntityDamageByEntityEvent event){

        if(!(event.getDamager() instanceof Player) || event.getCause() == EntityDamageEvent.DamageCause.PROJECTILE)
            return;

        Player player = (Player) event.getDamager();
        String uuidPlayer = SwordUtil.getUUIDPlayer(player);
        ItemStack itemInHand = player.getItemInHand();

        if(itemInHand == null || itemInHand.getType() == Material.AIR)
            return;

        Group group = manager.getGroupConditions(player, itemInHand);

        if(group == null)
            return;

        String permission = group.getPermission(player);
        Bonus bonus = group.getBonus(permission);
        StorageLevel storageLevel = group.getStorageLevel(uuidPlayer, itemInHand);

        bonus.applyBonus(event, storageLevel.getLevel(), player);
    }

    @EventHandler(priority = EventPriority.HIGH)
    protected void onDeath(EntityDeathEvent event){

        if(event.getEntity().getKiller() == null)
            return;

        LivingEntity entity = event.getEntity();
        Player player = entity.getKiller();
        String uuidPlayer = SwordUtil.getUUIDPlayer(player);
        ItemStack itemInHand = player.getItemInHand();

        if(itemInHand == null || itemInHand.getType() == Material.AIR)
            return;

        Group group = manager.getGroupConditions(player, itemInHand);

        if(group == null)
            return;

        String permission = group.getPermission(player);
        Bonus bonus = group.getBonus(permission);
        StorageLevel storageLevel = group.getStorageLevel(uuidPlayer, itemInHand);

        bonus.applyBonus(event, storageLevel.getLevel(), player);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    protected void onBreak(BlockBreakEvent event){

        if(event.isCancelled() || event.getPlayer() == null)
            return;

        Player player = event.getPlayer();
        String uuidPlayer = SwordUtil.getUUIDPlayer(player);
        ItemStack itemInHand = player.getItemInHand();

        if(itemInHand == null || itemInHand.getType() == Material.AIR)
            return;

        Group group = manager.getGroupConditions(player, itemInHand);

        if(group == null)
            return;

        String permission = group.getPermission(player);
        Bonus bonus = group.getBonus(permission);
        StorageLevel storageLevel = group.getStorageLevel(uuidPlayer, itemInHand);

        bonus.applyBonus(event, storageLevel.getLevel(), player);
    }
}
