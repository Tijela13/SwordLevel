package br.tijela.swordlevelv3;

import br.neitan96.swordlevelv3.commands.CmdHelp;
import br.neitan96.swordlevelv3.connector.Connector;
import br.neitan96.swordlevelv3.connector.ConnectorBase;
import br.neitan96.swordlevelv3.events.Bonuses;
import br.neitan96.swordlevelv3.events.Leveler;
import br.neitan96.swordlevelv3.manager.GroupManager;
import br.neitan96.swordlevelv3.util.Lang;
import br.neitan96.swordlevelv3.util.SwordUtil;
import br.neitan96.swordlevelv3.util.YamlUTF8;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public class SwordLevel extends JavaPlugin{

    private static SwordLevel instance = null;
    private static Connector connector = null;

    private static String prefixConsole = null;
    private static String prefixCommands = null;
    private static String prefixErrors = null;

    private static GroupManager manager = null;
    private static Leveler leveler = null;
    private static Bonuses bonuses = null;
    private static Lang lang = null;

    private static int debugLevel = 1;

    public SwordLevel(){
        instance = this;
    }

    public void onEnable(){
        loadPlugin();
    }

    public void onDisable(){
        HandlerList.unregisterAll(this);
        if(connector != null)
            connector.closeConnection();
    }

    public boolean loadPlugin(){
        try{
            saveNotExists("config pt-bt.yml");
            saveNotExists("config.yml");

            saveNotExists("pt-br.yml");
            saveNotExists("en-us.yml");

            saveNotExists("plugin pt-br.yml");
            saveNotExists("plugin.yml");
        }catch (Exception e){
            logError("Error on save the configs: "+e.getMessage());
            return false;
        }

        YamlUTF8 config;
        try{

            config = getConfig("config.yml");

            if(config == null){
                logError("Error on load the config.yml.");
                return false;
            }

        }catch (Exception e){
            logError("Error on load the config.yml: "+e.getMessage());
            return false;
        }

        ConfigurationSection section;
        try{
            section = config.getConfigurationSection("Plugin");

            debugLevel = section.getInt("Debug", debugLevel);
            SwordUtil.uuid = section.getBoolean("UUID", false);

            prefixConsole = section.getString("Prefix.PrefixConsole");
            prefixCommands = section.getString("Prefix.PrefixCommands");
            prefixErrors = section.getString("Prefix.PrefixErrors");

        }catch (Exception e){
            logError("Error on read the main configuration of plugin: "+e.getMessage());
            return false;
        }

        YamlUTF8 messages;
        try{
            messages = getConfig(section.getString("Messages", "pt-br.yml"));

            if(messages == null){
                logError("File the messages not exists!");
                lang = new Lang();

            }else{
                lang = new Lang(messages);
            }

        }catch (Exception e){
            logError("Error on load the messages: "+e.getMessage());
            return false;
        }

        try{
            connector = ConnectorBase.makeConnector(
                    config.getConfigurationSection("Sql"));
        }catch (Exception e){
            logError("Error on connect to database: "+e.getMessage());
            return false;
        }

        try{
            manager = new GroupManager();
            manager.loadFromConfig(config.getConfigurationSection("Grupos"));
            if(config.contains("DefaultGroup"))
                manager.loadDefault(config.getConfigurationSection("DefaultGroup"));
        }catch (Exception e){
            logError("Error on load the groups: "+e.getMessage());
            return false;
        }

        try{
            leveler = new Leveler(manager);
        }catch (Exception e){
            logError("Error on enable the leveling: "+e.getMessage());
            return false;
        }

        try{
            bonuses = new Bonuses(manager);
        }catch (Exception e){
            logError("Error on enable the bonus: "+e.getMessage());
            return false;
        }

        try{
            getCommand("swordlevel").setExecutor(new CmdHelp());
        }catch (Exception e){
            logError("Error on enable the commands: "+e.getMessage());
            return false;
        }

        return true;
    }


    public static YamlUTF8 getConfig(String configName){
        File file = new File(instance.getDataFolder(), configName);
        try{
            return new YamlUTF8(file);
        }catch (IOException e){
            e.printStackTrace();
        }catch (InvalidConfigurationException e){
            e.printStackTrace();
        }
        return null;
    }


    public static void log(String msg){
        if(msg != null)
            Bukkit.getConsoleSender().sendMessage(prefixConsole+msg);
    }

    public static void log(String[] msgs){
        if(msgs != null)
            for (String msg : msgs){
                log(msg);
            }
    }

    public static void log(String msg, int level){
        if(debugLevel >= level && msg != null)
            log(msg);
    }

    public static void log(String[] msgs, int level){
        if(msgs != null)
            for (String msg : msgs){
                log(msg, level);
            }
    }

    public static void log(CommandSender sender, String msg){
        if(msg != null)
            sender.sendMessage(prefixCommands+msg);
    }

    public static void log(CommandSender sender, String[] msgs){
        if(msgs != null)
            for (String msg : msgs){
                log(sender, msg);
            }
    }

    public static void logError(String msg){
        if(msg != null)
            logError(Bukkit.getConsoleSender(), msg);
    }

    public static void logError(String[] msgs){
        if(msgs != null)
            for (String msg : msgs){
                logError(msg);
            }
    }

    public static void logError(CommandSender sender, String msg){
        if(msg != null)
            sender.sendMessage(prefixErrors+msg);
    }

    public static void logError(CommandSender sender, String[] msgs){
        if(msgs != null)
            for (String msg : msgs){
                logError(sender, msgs);
            }
    }


    public static void saveNotExists(String filename){
        File file = new File(getInstance().getDataFolder(), filename);
        if(!file.exists()) getInstance().saveResource(filename, false);
    }

    public static String[] getMsgs(String path){
        return lang.getMsgs(path);
    }

    public static String[] getMsgs(String path, String... binds){
        return lang.getMsgs(path, binds);
    }

    public static Lang getLang(){
        return lang;
    }

    public static Connector getConnector(){
        return connector;
    }

    public static GroupManager getManager(){
        return manager;
    }

    public static Leveler getLeveler(){
        return leveler;
    }

    public static Bonuses getBonuses(){
        return bonuses;
    }

    public static SwordLevel getInstance(){
        return instance;
    }

}
