package br.tijela.swordlevelv3.rewards;

import br.tijela.swordlevelv3.util.SwordUtil;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RewardsDefault implements RewardList{

    protected Map<Integer, List<String>> rewards;


    public RewardsDefault(ConfigurationSection section) {
        loadFromConfig(section);
    }


    @Override
    public Map<Integer, List<String>> getRewards() {
        return rewards;
    }

    @Override
    public void sendRewards(Player player, int level) {
        if(rewards.containsKey(level)){
            for(String reward : rewards.get(level)){
                reward = reward.replace("{0}", player.getName());
                reward = reward.replace("{1}", SwordUtil.getUUIDPlayer(player));

                Bukkit.dispatchCommand(
                        Bukkit.getConsoleSender(), reward
                );
            }
        }
    }

    @Override
    public void loadFromConfig(ConfigurationSection section) {

        Map<Integer, List<String>> rewards = new HashMap<>();

        for (String key : section.getKeys(false)) {
            try{

                int keyInt = Integer.parseInt(key);

                List<String> reward = section.getStringList(key);

                rewards.put(keyInt, reward);

            }catch (Exception ignored){}
        }

        this.rewards = rewards;
    }
}