package br.tijela.swordlevelv3.bonus;

import br.tijela.swordlevelv3.util.ConfigLoader;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;

public abstract class Bonus implements ConfigLoader{

    public void applyBonus(EntityDamageByEntityEvent event, int level, Player killer){

    }

    public void applyBonus(EntityDeathEvent event, int level, Player killer){

    }

    public void applyBonus(BlockBreakEvent event, int level, Player killer){

    }

    public abstract String[] toString(int level);

}
