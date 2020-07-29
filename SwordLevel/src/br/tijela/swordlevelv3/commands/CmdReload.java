package br.tijela.swordlevelv3.commands;

import br.tijela.swordlevelv3.SwordLevel;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class CmdReload extends CmdSwordLevel{

    protected final String command = "reload";

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings){
        if(returnNoPermission(commandSender, this.command))
            return true;

        SwordLevel.getInstance().onDisable();

        if(SwordLevel.getInstance().loadPlugin()){
            //SwordLevel.log(commandSender, "Plugin recarregado!");
            SwordLevel.log(commandSender, "Plugin reloaded!");
        }else{
            //SwordLevel.log(commandSender, "Erro ao recarregar o plugin, olhe no console!");
            SwordLevel.log(commandSender, "Error on reload the plugin, see the console!");
        }


        return true;
    }
}
