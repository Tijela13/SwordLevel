package br.tijela.swordlevelv3.storage.level;

import br.neitan96.swordlevelv3.util.ConfigLoader;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

public class StorageMake implements ConfigLoader{

    protected boolean itemMode = true;

    protected String levelPrefix = "§9Level: §2";
    protected String levelSufix = "";
    protected String xpPrefix = "§5Xp: §9";
    protected String xpSufix = "";

    protected String groupName;

    public StorageMake(ConfigurationSection section, String groupName){
        this.groupName = groupName;
        loadFromConfig(section);
    }

    public StorageLevel makeStorage(String player, ItemStack itemStack){
        return itemMode ?
                new StorageLore(itemStack, levelPrefix,levelSufix, xpPrefix, xpSufix) :
                new StorageSql(player, groupName);
    }

    @Override
    public void loadFromConfig(ConfigurationSection section){
        itemMode = !section.getString("Mode", "Item").equalsIgnoreCase("Player");

        levelPrefix = section.getString("LoreItem.Level.Prefix", levelPrefix);
        levelSufix = section.getString("LoreItem.Level.Sufix", levelSufix);
        xpPrefix = section.getString("LoreItem.Xp.Prefix", xpPrefix);
        xpSufix = section.getString("LoreItem.Xp.Sufix", xpSufix);

    }
}
