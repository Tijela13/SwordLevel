package br.tijela.swordlevelv3.rewards;

import br.tijela.swordlevelv3.util.ConfigLoader;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;

public interface RewardList extends ConfigLoader{

    Map<Integer, List<String>> getRewards();

    void sendRewards(Player player, int level);

}