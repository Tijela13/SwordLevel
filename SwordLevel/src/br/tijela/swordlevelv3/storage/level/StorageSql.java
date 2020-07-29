package br.tijela.swordlevelv3.storage.level;

import br.neitan96.swordlevelv3.SwordLevel;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StorageSql implements StorageLevel{

    protected final String player;
    protected final String group;

    public StorageSql(String player, String group){
        this.player = player;
        this.group = group;
    }

    public boolean hasPlayer(){
        PreparedStatement statement =
                SwordLevel.getConnector().getStatement(
                        "SELECT * FROM `{TableLevel}` WHERE `{ColumnPlayer}` = ? AND `{ColumnGroup}` = ?");

        try{
            statement.setString(1, player);
            statement.setString(2, group);

            ResultSet result = statement.executeQuery();

            return result.next();
        }catch (SQLException e){
            e.printStackTrace();
            return false;
        }
    }

    public void insertPlayer(int level, int xp){
        PreparedStatement statement =
                SwordLevel.getConnector().getStatement(
                        "INSERT INTO `{TableLevel}` (`{ColumnPlayer}`,`{ColumnGroup}`, `level`, `xp`) VALUES (?, ?, ?, ?)");

        try{
            statement.setString(1, player);
            statement.setString(2, group);
            statement.setInt(3, level);
            statement.setInt(4, xp);

            statement.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    @Override
    public int getLevel(){
        PreparedStatement statement =
                SwordLevel.getConnector().getStatement(
                        "SELECT `level` FROM `{TableLevel}` WHERE `{ColumnPlayer}` = ? AND `{ColumnGroup}` = ?");

        try{
            statement.setString(1, player);
            statement.setString(2, group);

            ResultSet result = statement.executeQuery();

            return result.next() ? result.getInt(1) : 1;
        }catch (SQLException e){
            e.printStackTrace();
            return 1;
        }
    }

    @SuppressWarnings("Duplicates")
    @Override
    public void setLevel(int level){
        level = level < 1 ? 1 : level;

        if(!hasPlayer()){
            insertPlayer(level, 0);

        }else{

            PreparedStatement statement =
                    SwordLevel.getConnector().getStatement(
                            "UPDATE `{TableLevel}` SET `level` = ? WHERE `{ColumnPlayer}` = ? AND `{ColumnGroup}` = ?");

            try{
                statement.setInt(1, level);
                statement.setString(2, player);
                statement.setString(3, group);

                statement.executeUpdate();
            }catch (SQLException e){
                e.printStackTrace();
            }

        }

    }

    @Override
    public int getXp(){
        PreparedStatement statement =
                SwordLevel.getConnector().getStatement(
                        "SELECT `xp` FROM `{TableLevel}` WHERE `{ColumnPlayer}` = ? AND `{ColumnGroup}` = ?");

        try{
            statement.setString(1, player);
            statement.setString(2, group);

            ResultSet result = statement.executeQuery();

            return result.next() ? result.getInt(1) : 0;
        }catch (SQLException e){
            e.printStackTrace();
            return 0;
        }
    }

    @SuppressWarnings("Duplicates")
    @Override
    public void setXp(int xp){

        if(!hasPlayer()){
            insertPlayer(1, xp);

        }else{

            PreparedStatement statement =
                    SwordLevel.getConnector().getStatement(
                            "UPDATE `{TableLevel}` SET `xp` = ? WHERE `{ColumnPlayer}` = ? AND `{ColumnGroup}` = ?");

            try{
                statement.setInt(1, xp);
                statement.setString(2, player);
                statement.setString(3, group);

                statement.executeUpdate();
            }catch (SQLException e){
                e.printStackTrace();
            }

        }
    }
}
