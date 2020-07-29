package br.tijela.swordlevelv3.manager;

import br.tijela.swordlevelv3.SwordLevel;
import br.tijela.swordlevelv3.antitheft.AntiTheft;
import br.tijela.swordlevelv3.antitheft.AntiTheftDefault;
import br.tijela.swordlevelv3.bonus.Bonus;
import br.tijela.swordlevelv3.bonus.BonusList;
import br.tijela.swordlevelv3.conditions.Conditions;
import br.tijela.swordlevelv3.conditions.ConditionsDefault;
import br.tijela.swordlevelv3.leveling.Leveling;
import br.tijela.swordlevelv3.leveling.LevelingDefault;
import br.tijela.swordlevelv3.messages.MessagesDefault;
import br.tijela.swordlevelv3.messages.SwordMessages;
import br.tijela.swordlevelv3.rewards.RewardList;
import br.tijela.swordlevelv3.rewards.RewardsDefault;
import br.tijela.swordlevelv3.storage.level.StorageLevel;
import br.tijela.swordlevelv3.storage.level.StorageMake;
import br.tijela.swordlevelv3.storage.ranks.RankerDefault;
import br.tijela.swordlevelv3.storage.ranks.StorageRank;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class GroupDefault implements Group{

    protected String groupName = null;
    protected String[] permissions = new String[0];
    protected Conditions  conditions = null;
    protected StorageMake storageMake = null;
    protected SwordMessages messages = null;
    protected StorageRank storageRank = null;
    protected AntiTheft antiTheft = null;
    protected Map<String, Leveling> levelings = new HashMap<>();
    protected Map<String, Bonus> bonuses = new HashMap<>();
    protected Map<String, RewardList> rewardLists = new HashMap<>();
    protected boolean allowCreative = false;

    public GroupDefault(ConfigurationSection section){
        loadFromConfig(section);
    }

    @Override
    public String getGroupName(){
        return groupName;
    }

    @Override
    public boolean allowCreative(){
        return allowCreative;
    }

    @Override
    public String[] getPermissions(){
        return permissions;
    }

    @Override
    public Conditions getConditions(){
        return conditions;
    }

    @Override
    public StorageLevel getStorageLevel(String player, ItemStack itemStack){
        return storageMake.makeStorage(player, itemStack);
    }

    @Override
    public SwordMessages getMessages(){
        return messages;
    }

    @Override
    public StorageRank getStorageRank(){
        return storageRank;
    }

    @Override
    public AntiTheft getAntiTheft(){
        return antiTheft;
    }

    @Override
    public String getPermission(Player player){
        for (String permission : permissions){
            if(player.hasPermission(permission))
                return permission;
        }

        return null;
    }

    @Override
    public Leveling getLeveling(String permission){
        return levelings.get(permission);
    }

    @Override
    public Bonus getBonus(String permission){
        return bonuses.get(permission);
    }

    @Override
    public RewardList getReward(String permission){
        return rewardLists.get(permission);
    }

    @Override
    public void loadFromConfig(ConfigurationSection section){

        groupName = section.getCurrentPath().substring(
                section.getCurrentPath().lastIndexOf(".") + 1
        );
        SwordLevel.log(SwordLevel.getMsgs("Debug.Groups.Reading", "group", groupName), 1);

        allowCreative = section.getBoolean("AllowCreative", allowCreative);

        if(section.contains("Conditions")){
            conditions = new ConditionsDefault(
                    section.getConfigurationSection("Conditions")
            );
        }else{
            SwordLevel.logError(SwordLevel.getMsgs("Warings.Groups.NoConditions"));
        }

        if(section.contains("Store")){
            storageMake = new StorageMake(
                    section.getConfigurationSection("Store"), groupName
            );
        }else{
            SwordLevel.logError(SwordLevel.getMsgs("Warings.Groups.NoStore"));
        }

        if(section.contains("Messages")){
            messages = new MessagesDefault(
                    section.getConfigurationSection("Messages")
            );
        }else{
            SwordLevel.log(SwordLevel.getMsgs("Debug.Groups.NoMessages"), 2);
        }

        if(section.contains("Ranks")){
            storageRank = new RankerDefault(
                    section.getConfigurationSection("Ranks"), groupName
            );
        }else{
            SwordLevel.log(SwordLevel.getMsgs("Debug.Groups.NoRanks"), 2);
        }

        if(section.contains("AntiTheft")){
            antiTheft = new AntiTheftDefault(
                    section.getConfigurationSection("AntiTheft")
            );
        }else{
            SwordLevel.log(SwordLevel.getMsgs("Debug.Groups.NoAntiTheft"), 2);
        }

        if(section.contains("Permissions")){
            ConfigurationSection subGroups = section.getConfigurationSection("Permissions");
            Set<String> subGroupsNames = subGroups.getKeys(false);
            this.permissions = new String[subGroupsNames.size()];

            int i = 0;
            for (String subGroupName : subGroupsNames){
                String permission = subGroups.getString(subGroupName + ".Permission");
                permissions[i++] = permission;
                SwordLevel.log(SwordLevel.getMsgs("Debug.Groups.ReadingSubGroup", "subgroup", permission), 2);

                if(subGroups.contains(subGroupName+".Leveling"))
                    levelings.put(
                            permission,
                            new LevelingDefault(subGroups.getConfigurationSection(subGroupName+".Leveling"))
                    );
                else SwordLevel.logError(SwordLevel.getMsgs("Warings.Groups.NoLeveling"));
                if(subGroups.contains(subGroupName+".Bonus"))
                    bonuses.put(
                            permission,
                            new BonusList(subGroups.getConfigurationSection(subGroupName+".Bonus"))
                    );
                else SwordLevel.logError(SwordLevel.getMsgs("Warings.Groups.NoBonus"));
                if(subGroups.contains(subGroupName+".Rewards"))
                    rewardLists.put(
                            permission,
                            new RewardsDefault(subGroups.getConfigurationSection(subGroupName+".Rewards"))
                    );
                else SwordLevel.log(SwordLevel.getMsgs("Debug.Groups.NoRewards"), 2);
            }

        }else{
            SwordLevel.logError(SwordLevel.getMsgs("Warings.Groups.NoPermissions"));
        }
    }
}
