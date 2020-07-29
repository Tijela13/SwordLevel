package br.tijela.swordlevelv3.storage.ranks;

import br.neitan96.swordlevelv3.SwordLevel;
import org.bukkit.configuration.ConfigurationSection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RankerDefault implements StorageRank{

    protected final String groupName;

    protected boolean levelUps = false;
    protected boolean xpGained = false;
    protected boolean levelMax = false;


    public RankerDefault(ConfigurationSection section, String groupName){
        loadFromConfig(section);
        this.groupName = groupName;
    }

    public boolean hasPlayer(String player, RankType type){
        PreparedStatement statement = SwordLevel.getConnector().getStatement(
                "SELECT * FROM `{TableRanks}` WHERE `{ColumnPlayer}` = ? AND `{ColumnGroup}` = ? AND `type` = ?");

        try{
            statement.setString(1, player);
            statement.setString(2, groupName);
            statement.setString(3, type.name());

            return statement.executeQuery().next();
        }catch (SQLException e){
            e.printStackTrace();
        }

        return false;
    }

    public int getScore(String player, RankType type){
        PreparedStatement statement = SwordLevel.getConnector().getStatement(
                "SELECT `score` FROM `{TableRanks}` WHERE `{ColumnPlayer}` = ? AND `{ColumnGroup}` = ? AND `type` = ?");

        try{
            statement.setString(1, player);
            statement.setString(2, groupName);
            statement.setString(3, type.name());

            ResultSet result = statement.executeQuery();

            return result.next() ? result.getInt(1) : -1;
        }catch (SQLException e){
            e.printStackTrace();
        }

        return -1;
    }

    public void insertPlayer(String player, RankType type, int score){
        PreparedStatement statement = SwordLevel.getConnector().getStatement(
                "INSERT INTO `{TableRanks}` (`{ColumnPlayer}`, `{ColumnGroup}`, `type`, `score`) VALUES (?, ?, ?, ?)");

        try{
            statement.setString(1, player);
            statement.setString(2, groupName);
            statement.setString(3, type.name());
            statement.setInt(4, score);

            statement.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void updateScore(String player, RankType type, int newScore){
        PreparedStatement statement = SwordLevel.getConnector().getStatement(
                "UPDATE `{TableRanks}` SET `score` = ? WHERE `{ColumnPlayer}` = ? AND `{ColumnGroup}` = ? AND `type` = ?");

        try{
            statement.setInt(1, newScore);
            statement.setString(2, player);
            statement.setString(3, groupName);
            statement.setString(4, type.name());

            statement.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    @Override
    public void updateScore(String player, int levelWin, int xpWin, int levelNow, int xpNow){
        if(levelWin > 0 && levelUps){

            int scoreNow = getScore(player, RankType.LEVELS_UPS);

            if(scoreNow < 0){
                insertPlayer(player, RankType.LEVELS_UPS, levelWin);
            }else{
                updateScore(player, RankType.LEVELS_UPS, scoreNow+levelWin);
            }

        }
        if(xpWin > 0 && xpGained){

            int scoreNow = getScore(player, RankType.XP_GAINED);

            if(scoreNow < 0){
                insertPlayer(player, RankType.XP_GAINED, xpWin);
            }else{
                updateScore(player, RankType.XP_GAINED, scoreNow+xpWin);
            }

        }
        if(levelMax){

            int scoreNow = getScore(player, RankType.LEVEL_MAX);

            if(scoreNow < 0){
                insertPlayer(player, RankType.LEVEL_MAX, levelNow);
            }else if(scoreNow < levelNow){
                updateScore(player, RankType.LEVEL_MAX, levelNow);
            }

        }
    }

    @Override
    public void loadFromConfig(ConfigurationSection section){
        levelUps = section.getBoolean("LevelsUps", levelUps);
        xpGained = section.getBoolean("XpGained", xpGained);
        levelMax = section.getBoolean("LevelMax", levelMax);
    }
}
