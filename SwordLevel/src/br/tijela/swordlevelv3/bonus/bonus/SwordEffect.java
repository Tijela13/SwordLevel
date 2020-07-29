package br.tijela.swordlevelv3.bonus.bonus;

import br.tijela.swordlevelv3.SwordLevel;
import br.tijela.swordlevelv3.bonus.Bonus;
import br.tijela.swordlevelv3.util.SwordUtil;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class SwordEffect extends Bonus{

    protected PotionEffectType effectType;
    protected int levelAllow = 1;
    protected double secondsEffect = 3;
    protected int amplifier = 1;
    protected int provability = 1;
    protected boolean multiplierProvability = false;

    public SwordEffect(ConfigurationSection section) {
        loadFromConfig(section);
    }

    @Override
    public void applyBonus(EntityDamageByEntityEvent event, int level, Player killer) {

        if(level < levelAllow
                || !SwordUtil.calculateProvability(multiplierProvability ? provability * level : provability)
                || !(event.getEntity() instanceof LivingEntity))
            return;

        final LivingEntity entity = (LivingEntity) event.getEntity();

        int ticks = (int) (secondsEffect *20);

        for (PotionEffect effect : entity.getActivePotionEffects()) {
            if(effect.getType() == effectType && effect.getDuration() > ticks)
                return;
        }

        PotionEffect potionEffect = new PotionEffect(effectType, ticks, amplifier);
        entity.addPotionEffect(potionEffect, true);
    }

    @Override
    public void applyBonus(BlockBreakEvent event, int level, Player killer) {

    }

    @Override
    public void loadFromConfig(ConfigurationSection section) {
        String currentPath = section.getCurrentPath();
        String effectName = currentPath.substring(currentPath.lastIndexOf(".") + 1);

        effectType = PotionEffectType.getByName(effectName);
        levelAllow = section.getInt("LevelAllow", levelAllow);
        secondsEffect = section.getDouble("SecondsEffect", secondsEffect);
        amplifier = section.getInt("Amplifier", amplifier);
        provability = section.getInt("Provability", provability);
        multiplierProvability = section.getBoolean("MultiplierProvability", multiplierProvability);
    }

    @Override
    public String[] toString(int level){
        return SwordLevel.getMsgs("Bonus.Effect",
                "name", effectType.getName(),
                "level", String.valueOf(amplifier),
                "seconds", String.valueOf(secondsEffect),
                "provability", String.valueOf(
                        multiplierProvability ? provability * level : provability));
    }
}
