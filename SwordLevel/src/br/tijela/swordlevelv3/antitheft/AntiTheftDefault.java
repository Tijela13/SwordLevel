package br.tijela.swordlevelv3.antitheft;

import br.tijela.swordlevelv3.SwordLevel;
import br.tijela.swordlevelv3.util.SwordUtil;
import br.tijela.swordlevelv3.util.TimeMark;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public class AntiTheftDefault implements AntiTheft, Runnable{

    protected String permissionAllow = null;

    protected int samePlayerCount = 0;
    protected int samePlayerTime = 0;

    protected int anyPlayerCount = 0;
    protected int anyPlayerTime = 0;

    protected int mobCount = 0;
    protected int mobTime = 0;

    protected int blockCount = 0;
    protected int blockTime = 0;

    protected TimeMark<String> playersKill = new TimeMark<>();
    protected TimeMark<String> mobsKill = new TimeMark<>();
    protected TimeMark<String> blockBreak = new TimeMark<>();

    protected int limitCache = 150;

    public AntiTheftDefault(ConfigurationSection section) {
        loadFromConfig(section);
        limitCache = Bukkit.getServer().getMaxPlayers();
    }

    @Override
    public int getCountSamePlayer() {
        return samePlayerCount;
    }

    @Override
    public int getTimeSamePlayer() {
        return samePlayerTime;
    }

    @Override
    public int getCountAnyPlayers() {
        return anyPlayerCount;
    }

    @Override
    public int getTimeAnyPlayers() {
        return anyPlayerTime;
    }

    @Override
    public int getCountMob() {
        return mobCount;
    }

    @Override
    public int getTimeMob() {
        return mobTime;
    }

    @Override
    public int getCountBlock() {
        return blockCount;
    }

    @Override
    public int getTimeBlock() {
        return blockTime;
    }

    @Override
    public boolean validAction(Player player, Player entity){
        if(player.hasPermission(permissionAllow))
            return true;

        String uuidPlayer = SwordUtil.getUUIDPlayer(player);

        long nowSame = System.currentTimeMillis() - (getTimeSamePlayer() * 1000);
        long nowAny = System.currentTimeMillis() - (getTimeAnyPlayers() * 1000);

        boolean valid =
                playersKill.countMarks(uuidPlayer, nowSame) < getCountSamePlayer() &&
                playersKill.countMarks(uuidPlayer, nowAny, SwordUtil.getUUIDPlayer(entity)) < getCountAnyPlayers();

        if(valid)
            playersKill.add(uuidPlayer, SwordUtil.getUUIDPlayer(entity));
        else
            SwordLevel.log(SwordLevel.getMsgs("Debug.AntiTheft.BlockPlayer",
                    "playerName", player.getName(), "playerUUID", uuidPlayer), 4);

        Bukkit.getScheduler().runTask(SwordLevel.getInstance(), this);
        return valid;
    }

    @Override
    public boolean validAction(Player player, LivingEntity entity){
        if(entity instanceof Player)
            return validAction(player, ((Player) entity));

        if(player.hasPermission(permissionAllow))
            return true;

        String uuidPlayer = SwordUtil.getUUIDPlayer(player);

        long now = System.currentTimeMillis() - (getTimeMob() * 1000);

        boolean valid = mobsKill.countMarks(uuidPlayer, now) < getCountMob();

        if(valid)
            mobsKill.add(uuidPlayer);
        else
            SwordLevel.log(SwordLevel.getMsgs("Debug.AntiTheft.BlockMob",
                    "playerName", player.getName(), "playerUUID", uuidPlayer), 4);

        Bukkit.getScheduler().runTask(SwordLevel.getInstance(), this);
        return valid;
    }

    @Override
    public boolean validAction(Player player, Block block){
        if(player.hasPermission(permissionAllow))
            return true;

        String uuidPlayer = SwordUtil.getUUIDPlayer(player);

        long now = System.currentTimeMillis() - (getTimeBlock() * 1000);

        boolean valid = blockBreak.countMarks(uuidPlayer, now) < getCountBlock();

        if(valid)
            playersKill.add(uuidPlayer);
        else
            SwordLevel.log(SwordLevel.getMsgs("Debug.AntiTheft.BlockBlock",
                    "playerName", player.getName(), "playerUUID", uuidPlayer), 4);

        Bukkit.getScheduler().runTask(SwordLevel.getInstance(), this);
        return valid;
    }

    @Override
    public synchronized void clearCache(){
        if(playersKill.countMarks() > limitCache)
            playersKill.removeMarks(
                    System.currentTimeMillis()-(getTimeAnyPlayers()*1000)
            );
        if(mobsKill.countMarks() > limitCache)
            mobsKill.removeMarks(
                    System.currentTimeMillis()-(getTimeMob()*1000)
            );
        if(blockBreak.countMarks() > limitCache)
            blockBreak.removeMarks(
                    System.currentTimeMillis()-(getTimeBlock()*1000)
            );
        SwordLevel.log(SwordLevel.getMsgs("Debug.AntiTheft.CacheClear"), 3);
    }

    @Override
    public void loadFromConfig(ConfigurationSection section) {

        permissionAllow = section.getString("PermissionAllow");
        samePlayerCount = section.getInt("Player.SamePlayer.Count", -1);
        samePlayerTime = section.getInt("Player.SamePlayer.Time", -1);
        anyPlayerCount = section.getInt("Player.AnyPlayer.Count", -1);
        samePlayerTime = section.getInt("Player.AnyPlayer.Time",  -1);
        mobCount = section.getInt("Mob.Count", -1);
        mobTime = section.getInt("Mob.Time", -1);
        blockCount = section.getInt("Block.Count", -1);
        blockTime = section.getInt("Block.Time", -1);

    }

    @Override
    public void run(){
        clearCache();
    }
}
