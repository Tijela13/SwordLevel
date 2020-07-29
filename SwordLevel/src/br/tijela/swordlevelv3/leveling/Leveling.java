package br.tijela.swordlevelv3.leveling;

import br.tijela.swordlevelv3.util.ConfigLoader;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;

public interface Leveling extends ConfigLoader{

    int getXpKillMob();

    int getXpKillMob(EntityType entityType);

    int getXpKillPlayer();

    int getXpKillPlayer(String player);

    int getXpBreakBlock(Material material);

    int getLevelMax();

    String getXpRequired();

    int calculateXPRequired(int level);

}
