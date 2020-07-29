package br.tijela.swordlevelv3.bonus.bonus;

import br.tijela.swordlevelv3.SwordLevel;
import br.tijela.swordlevelv3.bonus.Bonus;
import br.tijela.swordlevelv3.util.DamageAmor;
import br.tijela.swordlevelv3.util.SwordUtil;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class BonusDamage extends Bonus{

    protected double damageMin = 0;
    protected double damageMax = 0;
    protected boolean multiplierDamage = false;
    protected boolean ignoreAmor = true;
    protected boolean preventDurability = false;

    public BonusDamage(ConfigurationSection section){
        loadFromConfig(section);
    }

    @Override
    public void applyBonus(EntityDamageByEntityEvent event, int level, Player killer) {

        double damageMin = this.damageMin;
        double damageMax = this.damageMax;

        if(multiplierDamage){
            damageMin *= level;
            damageMax *= level;
        }

        double damageEvent = event.getDamage();
        double damageRandom = SwordUtil.randomDouble(damageMin, damageMax);

        if(damageRandom < 1)
            return;

        if(event.getEntity() instanceof Player){

            Player entity = (Player) event.getEntity();
            PlayerInventory inventory = entity.getInventory();

            if(!ignoreAmor)
                damageRandom =  DamageAmor.reduceDamage(inventory, damageRandom);

            ItemStack[] playerSet = {
                    inventory.getHelmet(), inventory.getChestplate(),
                    inventory.getLeggings(), inventory.getBoots()};

            if(preventDurability)
                for (ItemStack itemStack : playerSet){
                    if(itemStack != null && itemStack.getType() != Material.AIR){
                        short durability = itemStack.getDurability();
                        double damagePlus = damageRandom-damageEvent;
                        itemStack.setDurability((short) (durability-damagePlus));
                    }
                }

        }

        event.setDamage(damageEvent+damageRandom);
    }

    @Override
    public void loadFromConfig(ConfigurationSection section) {
        damageMin = section.getDouble("DamageMin", damageMin);
        damageMax = section.getDouble("DamageMax", damageMax);
        multiplierDamage = section.getBoolean("MultiplierDamage", multiplierDamage);
        ignoreAmor = section.getBoolean("IgnoreAmor", ignoreAmor);
        preventDurability = section.getBoolean("PreventDurability", preventDurability);
    }

    @Override
    public String[] toString(int level){

        double damageMin = this.damageMin;
        double damageMax = this.damageMax;

        if(multiplierDamage){
            damageMin *= level;
            damageMax *= level;
        }

        return SwordLevel.getMsgs("Bonus.Damage", "damageMin", String.valueOf(damageMin), "damageMax", String.valueOf(damageMax));
    }
}
