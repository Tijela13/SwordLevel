package br.tijela.swordlevelv3.util;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class SwordUtil {

    public static boolean uuid = true;

    public static String getUUIDPlayer(OfflinePlayer player){
        if(!uuid)
            return player.getName();
        else{
            try {
                final Method getUniqueId = player.getClass().getMethod("getUniqueId");
                final Object uuid = getUniqueId.invoke(player);
                return uuid.toString();
            } catch (InvocationTargetException | IllegalAccessException | NoSuchMethodException e) {
                e.printStackTrace();
                return player.getName();
            }
        }
    }

    public static Player getPlayer(String uuid){

        if(SwordUtil.uuid){
            try {
                UUID uuidplayer = UUID.fromString(uuid);
                final Method playerMethod = Bukkit.class.getMethod("getPlayer", uuidplayer.getClass());
                Player invoke = (Player) playerMethod.invoke(null, uuidplayer);

                if(invoke != null)
                    return invoke;
            } catch (Exception ignored) {}
        }

        return Bukkit.getPlayer(uuid);
    }

    public static OfflinePlayer getOffPlayer(String uuid){

        if(SwordUtil.uuid){
            try {
                UUID uuidplayer = UUID.fromString(uuid);
                final Method playerMethod = Bukkit.class.getMethod("getOfflinePlayer", uuidplayer.getClass());
                OfflinePlayer invoke = (OfflinePlayer) playerMethod.invoke(null, uuidplayer);

                if(invoke != null)
                    return invoke;
            } catch (Exception ignored) {}
        }

        return Bukkit.getOfflinePlayer(uuid);
    }

    private static final ScriptEngine engine = new ScriptEngineManager().getEngineByName("JavaScript");

    public static Object eval(String str){
        try {
            return engine.eval(str);
        } catch (ScriptException e) {
            return null;
        }
    }

    public static double randomDouble(double min, double max){
        return min + (max - min) * new Random().nextDouble();
    }

    public static int randomInt(int min, int max){
        return new Random().nextInt(max-min)+min;
    }

    public static boolean calculateProvability(int provability){
        return new Random().nextInt(100) < provability;
    }

    public static List<Damageable> getEntitiesDistance(Location location, double distance){
        final List<Damageable> entities = new ArrayList<>();
        final List<Entity> entitiesWorld = location.getWorld().getEntities();

        for (Entity entitieWorld : entitiesWorld) {
            if(entitieWorld instanceof Damageable &&
                    entitieWorld.getLocation().distance(location) <= distance)
                entities.add((Damageable) entitieWorld);
        }

        return entities;
    }

}
