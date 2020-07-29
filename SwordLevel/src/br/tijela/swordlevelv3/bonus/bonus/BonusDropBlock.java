package br.tijela.swordlevelv3.bonus.bonus;

import br.tijela.swordlevelv3.SwordLevel;
import br.tijela.swordlevelv3.bonus.Bonus;
import br.tijela.swordlevelv3.util.SwordUtil;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class BonusDropBlock extends Bonus{

    protected int provability = 100;
    protected boolean multiplierProvability = false;
    protected double multiplier = 2;
    protected boolean multiplierMultuplier = false;
    protected int levelAllow = 1;

    public BonusDropBlock(ConfigurationSection section) {
        loadFromConfig(section);
    }

    @Override
    public void applyBonus(BlockBreakEvent event, int level, Player player) {
        if(level < levelAllow
                || !SwordUtil.calculateProvability(multiplierProvability ? provability * level : provability)
                || player.getGameMode() == GameMode.CREATIVE)
            return;

        double multiplier = multiplierMultuplier ? this.multiplier*level : this.multiplier;

        List<ItemStack> newDrops = new ArrayList<>();

        for (ItemStack itemStack : event.getBlock().getDrops()) {
            int amount = itemStack.getAmount();

            ItemStack newItem = itemStack.clone();
            newItem.setAmount((int) ((amount * multiplier) - amount));
            newDrops.add(newItem);
        }

        Location location = event.getBlock().getLocation();
        World world = location.getWorld();
        for (ItemStack newDrop : newDrops)
            world.dropItem(location, newDrop);

    }

    @SuppressWarnings("Duplicates")
    @Override
    public void loadFromConfig(ConfigurationSection section) {
        provability = section.getInt("Provability", provability);
        multiplierProvability = section.getBoolean("MultiplierProvability", multiplierProvability);
        multiplier = section.getDouble("Multiplier", multiplier);
        multiplierMultuplier = section.getBoolean("MultiplierMulplier", multiplierMultuplier);
        levelAllow = section.getInt("LevelAllow", levelAllow);
    }

    @Override
    public String[] toString(int level){
        double multiplier = multiplierMultuplier ? this.multiplier*level : this.multiplier;
        int provability = multiplierProvability ? this.provability * level : this.provability;
        if(level < levelAllow)
            return SwordLevel.getMsgs("Bonus.DropBlockNo", "levelAllow", String.valueOf(levelAllow));
        else
            return SwordLevel.getMsgs("Bonus.DropBlock",
                    "multiplier", String.valueOf(multiplier),
                    "provability", String.valueOf(provability));
    }
}
