package br.tijela.swordlevelv3.commands;

import br.tijela.swordlevelv3.SwordLevel;
import br.tijela.swordlevelv3.manager.Group;
import br.tijela.swordlevelv3.storage.level.StorageLevel;
import br.tijela.swordlevelv3.util.SwordUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class CmdBonus extends CmdSwordLevel{

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings){

        if(returnNoPermission(commandSender, "bonus"))
            return true;

        if(!(commandSender instanceof Player)){
            SwordLevel.log(commandSender, SwordLevel.getMsgs("Warings.NoConsole"));
            return true;
        }

        Player player = (Player) commandSender;

        ItemStack itemInHand = player.getItemInHand();

        Group group = SwordLevel.getManager().getGroupConditions(player, itemInHand);

        if(group == null){
            SwordLevel.log(commandSender, SwordLevel.getMsgs("Warings.Groups.NoView"));
            return true;
        }

        String permission = group.getPermission(player);
        StorageLevel storageLevel = group.getStorageLevel(SwordUtil.getUUIDPlayer(player), itemInHand);

        SwordLevel.log(commandSender, SwordLevel.getMsgs("Bonus.Start"));
        SwordLevel.log(commandSender,
            group.getBonus(permission).toString(storageLevel.getLevel())
        );

        return true;
    }

}
