package br.tijela.swordlevelv3.manager;

import br.tijela.swordlevelv3.antitheft.AntiTheft;
import br.tijela.swordlevelv3.bonus.Bonus;
import br.tijela.swordlevelv3.conditions.Conditions;
import br.tijela.swordlevelv3.leveling.Leveling;
import br.tijela.swordlevelv3.messages.SwordMessages;
import br.tijela.swordlevelv3.rewards.RewardList;
import br.tijela.swordlevelv3.storage.level.StorageLevel;
import br.tijela.swordlevelv3.storage.ranks.StorageRank;
import br.tijela.swordlevelv3.util.ConfigLoader;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface Group extends ConfigLoader{

    String getGroupName();

    boolean allowCreative();

    String[] getPermissions();

    Conditions getConditions();

    StorageLevel getStorageLevel(String player, ItemStack itemStack);

    SwordMessages getMessages();

    StorageRank getStorageRank();

    AntiTheft getAntiTheft();


    String getPermission(Player player);


    Leveling getLeveling(String permission);

    Bonus getBonus(String permission);

    RewardList getReward(String permission);

}
