package br.tijela.swordlevelv3.messages;

import org.bukkit.configuration.ConfigurationSection;

import java.util.HashMap;
import java.util.Map;

public class MessagesDefault implements SwordMessages{

    protected Map<String, String> messages;

    public MessagesDefault(ConfigurationSection section) {
        loadFromConfig(section);
    }

    @Override
    public String getPrefix() {
        if(messages.containsKey("Prefix")){

            String message = messages.get("Prefix");
            return message.isEmpty() ? null : message;
        }
        return null;
    }

    @Override
    public String getLevelup(int levelNow, int levelUp) {
        if(messages.containsKey("LevelUp")){

            String message = messages.get("LevelUp");

            if(message.isEmpty())
                return null;

            return message
                    .replace("{0}", String.valueOf(levelNow))
                    .replace("{1}", String.valueOf(levelUp));
        }
        return null;
    }

    @Override
    public String getXpReward(int xpWin, int xpMissing) {
        if(messages.containsKey("XpReward")){

            String message = messages.get("XpReward");

            if(message.isEmpty())
                return null;

            return message
                    .replace("{0}", String.valueOf(xpWin))
                    .replace("{1}", String.valueOf(xpMissing));
        }
        return null;
    }

    @Override
    public String getViewLevel(int levelNow, int xpNow, int xpMissing) {
        if(messages.containsKey("ViewLevel")){

            String message = messages.get("ViewLevel");

            if(message.isEmpty())
                return null;

            return message
                    .replace("{0}", String.valueOf(levelNow))
                    .replace("{1}", String.valueOf(xpNow))
                    .replace("{2}", String.valueOf(xpMissing));
        }
        return null;
    }

    @Override
    public String getNoView(){
        return messages.get("NoView");
    }

    @Override
    public void loadFromConfig(ConfigurationSection section) {

        Map<String, String> messages = new HashMap<>();

        for (String key : section.getKeys(false)) {
            try{

                messages.put(key, section.getString(key));

            }catch (Exception ignored){}
        }

        this.messages = messages;
    }

}
