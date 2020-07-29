package br.tijela.swordlevelv3.bonus.bonus;

import br.tijela.swordlevelv3.SwordLevel;
import br.tijela.swordlevelv3.bonus.Bonus;
import br.tijela.swordlevelv3.util.SwordUtil;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

public class BonusDropMob extends Bonus{

    protected int provability = 100;
    protected boolean multiplierProvability = false;
    protected double multiplier = 2;
    protected boolean multiplierMultuplier = false;
    protected int levelAllow = 1;

    public BonusDropMob(ConfigurationSection section) {
        loadFromConfig(section);
    }

    @Override
    public void applyBonus(EntityDeathEvent event, int level, Player killer) {
        if(level < levelAllow
                || !SwordUtil.calculateProvability(multiplierProvability ? provability*level : provability))
            return;

        double multiplier = multiplierMultuplier ? this.multiplier*level : this.multiplier;

        for (ItemStack itemStack : event.getDrops()) {
            itemStack.setAmount(
                    (int) (itemStack.getAmount()*multiplier)
            );
        }
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
            return SwordLevel.getMsgs("Bonus.DropMobNo", "levelAllow", String.valueOf(levelAllow));
        else
            return SwordLevel.getMsgs("Bonus.DropMob",
                    "multiplier", String.valueOf(multiplier),
                    "provability", String.valueOf(provability));
    }
}
