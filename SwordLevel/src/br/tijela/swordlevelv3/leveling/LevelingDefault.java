package br.tijela.swordlevelv3.leveling;

import br.tijela.swordlevelv3.SwordLevel;
import br.tijela.swordlevelv3.util.SwordUtil;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.EntityType;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class LevelingDefault implements Leveling{

    protected int xpKillMob = 0;
    protected Map<EntityType, Integer> xpCustomMobs = new HashMap<>();
    protected int xpKillPlayer = 0;
    protected Map<String, Integer> xpCustomPlayers = new HashMap<>();
    protected Map<Material, Integer> xpCustomBlocks = new HashMap<>();

    protected String leveling = null;
    protected int levelMax = Integer.MAX_VALUE;

    public LevelingDefault(ConfigurationSection section){
        loadFromConfig(section);
    }

    @Override
    public int getXpKillMob(){
        return xpKillMob;
    }

    @Override
    public int getXpKillMob(EntityType entityType){
        return xpCustomMobs.containsKey(entityType) ?
                xpCustomMobs.get(entityType) : getXpKillMob();
    }

    @Override
    public int getXpKillPlayer(){
        return xpKillPlayer;
    }

    @Override
    public int getXpKillPlayer(String player){
        return xpCustomPlayers.containsKey(player) ?
                xpCustomPlayers.get(player) : getXpKillPlayer();
    }

    @Override
    public int getXpBreakBlock(Material material){
        return xpCustomBlocks.containsKey(material) ?
                xpCustomBlocks.get(material) : 0;
    }

    @Override
    public int getLevelMax(){
        return levelMax;
    }

    @Override
    public String getXpRequired(){
        return leveling;
    }

    @Override
    public int calculateXPRequired(int level){
        try{

            String required = getXpRequired().replace("{level}", String.valueOf(level));
            Object eval = SwordUtil.eval(required);
            if(eval == null)
                return -1;

            String evalString = String.valueOf(eval);

            int requiredXP =  (int) Math.ceil(Double.parseDouble(evalString));

            return requiredXP < 1 ? 1 : requiredXP;

        }catch (Exception e){
            SwordLevel.logError(e.getMessage());
            SwordLevel.logError("LevelUp: '"+getXpRequired()+"' is inválid!");
            return Integer.MAX_VALUE;
        }
    }

    @Override
    public void loadFromConfig(ConfigurationSection section){
        xpKillMob = section.getInt("Kills.Mobs", xpKillMob);

        if(section.contains("Kills.CustomMobs")){

            Set<String> keys =
                    section.getConfigurationSection("Kills.CustomMobs").getKeys(false);

            for (String key : keys){
                EntityType entityType = EntityType.valueOf(key);
                int xp = section.getInt("Kills.CustomMobs." + key);
                xpCustomMobs.put(entityType, xp);
            }

        }

        xpKillPlayer = section.getInt("Kills.Players", xpKillPlayer);

        if(section.contains("Kills.CustomPlayers")){

            Set<String> keys =
                    section.getConfigurationSection("Kills.CustomPlayers").getKeys(false);

            for (String key : keys){
                int xp = section.getInt("Kills.CustomPlayers." + key);
                xpCustomPlayers.put(key, xp);
            }

        }

        if(section.contains("Blocks")){

            Set<String> keys =
                    section.getConfigurationSection("Blocks").getKeys(false);

            for (String key : keys){
                Material material = Material.valueOf(key);
                int xp = section.getInt("Blocks." + key);
                xpCustomBlocks.put(material, xp);
            }

        }

        leveling = section.getString("LevelUp", leveling);
        levelMax = section.getInt("LevelMax", levelMax);

    }
}
