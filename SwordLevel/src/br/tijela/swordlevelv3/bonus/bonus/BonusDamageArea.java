package br.tijela.swordlevelv3.bonus.bonus;

import br.tijela.swordlevelv3.SwordLevel;
import br.tijela.swordlevelv3.bonus.Bonus;
import br.tijela.swordlevelv3.util.SwordUtil;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.List; 

public class BonusDamageArea extends Bonus{

    protected int provability = 100;
    protected boolean multiplierProvability = false;
    protected double distance = 3;
    protected double damageMin = 0;
    protected double damageMax = 0;
    protected boolean multiplierDamage = false;
    protected int levelAllow = 1;

    public BonusDamageArea(ConfigurationSection section) {
        loadFromConfig(section);
    }

    @Override
    public void applyBonus(EntityDamageByEntityEvent event, int level, Player killer) {

        if(level < levelAllow) return;

        int provability = this.provability;

        if(multiplierProvability)
            provability *= level;

        if(!SwordUtil.calculateProvability(provability))
            return;

        double damageMin = this.damageMin;
        double damageMax = this.damageMax;

        if(multiplierDamage){
            damageMin *= level;
            damageMax *= level;
        }

        final double damageRandom = SwordUtil.randomDouble(damageMin, damageMax);

        final Location location = event.getEntity().getLocation();
        if(location == null)
            return;

        final List<Damageable> entities = SwordUtil.getEntitiesDistance(location, distance);

        for (Damageable entity : entities) {
            if(killer != entity)
                entity.damage(damageRandom);
        }

    }

    @Override
    public void loadFromConfig(ConfigurationSection section) {
        provability = section.getInt("Provability", provability);
        multiplierProvability = section.getBoolean("MultiplierProvability", multiplierProvability);
        distance = section.getDouble("Distance", distance);
        damageMin = section.getDouble("DamageMin", damageMin);
        damageMax = section.getDouble("DamageMax", damageMax);
        multiplierDamage = section.getBoolean("MultiplierDamage", multiplierDamage);
        levelAllow = section.getInt("LevelAllow", levelAllow);
    }

    @Override
    public String[] toString(int level){

        int provability = this.provability;

        if(multiplierProvability)
            provability *= level;

        double damageMin = this.damageMin;
        double damageMax = this.damageMax;

        if(multiplierDamage){
            damageMin *= level;
            damageMax *= level;
        }

        if(level < levelAllow)
            return SwordLevel.getMsgs("Bonus.DamageAreaNo", "levelAllow", String.valueOf(levelAllow));
        else
            return SwordLevel.getMsgs("Bonus.DamageArea",
                    "damageMin", String.valueOf(damageMin),
                    "damageMax", String.valueOf(damageMax),
                    "distance", String.valueOf(distance),
                    "provability", String.valueOf(provability));
    }

}
