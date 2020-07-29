package br.tijela.swordlevelv3.antitheft;

import br.tijela.swordlevelv3.util.ConfigLoader;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public interface AntiTheft extends ConfigLoader{

    int getCountSamePlayer();

    int getTimeSamePlayer();


    int getCountAnyPlayers();

    int getTimeAnyPlayers();


    int getCountMob();

    int getTimeMob();


    int getCountBlock();

    int getTimeBlock();


    boolean validAction(Player player, Player entity);

    boolean validAction(Player player, LivingEntity entity);

    boolean validAction(Player player, Block block);


    void clearCache();


    void loadFromConfig(ConfigurationSection section);
}
