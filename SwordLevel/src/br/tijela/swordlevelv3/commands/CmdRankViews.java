package br.tijela.swordlevelv3.commands;

import br.tijela.swordlevelv3.SwordLevel;
import br.tijela.swordlevelv3.storage.ranks.RankType;
import br.tijela.swordlevelv3.util.DValue;
import br.tijela.swordlevelv3.util.SwordUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CmdRankViews extends CmdSwordLevel{

    protected final RankType type;
    protected final String command;

    protected long lastUpdate = 0;

    public CmdRankViews(RankType type, String command){
        this.type = type;
        this.command = command;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings){

        if(returnNoPermission(commandSender, this.command))
            return true;

        String sql;
        if(strings.length > 1)
            sql = "SELECT `{ColumnPlayer}`,`score` FROM `{TableRanks}` WHERE `type` = ? " +
                    "AND `{ColumnGroup}` = ? " +
                    "ORDER BY `score` DESC LIMIT 0,10";
        else
            sql = "SELECT `{ColumnPlayer}`,SUM(`score`) FROM `{TableRanks}` WHERE `type` = ? "+
                    "GROUP BY `{ColumnPlayer}` " +
                    "ORDER BY `score` DESC LIMIT 0,10";

        PreparedStatement statement = SwordLevel.getConnector().getStatement(sql);

        try{
            statement.setString(1, type.name());

            if(strings.length > 1)
                statement.setString(2, strings[1]);

            ResultSet result = statement.executeQuery();

            List<DValue<String, Integer>> scores = new ArrayList<>();

            while (result.next()){
                String player = result.getString(1);

                player = SwordUtil.getOffPlayer(player).getName();

                scores.add(new DValue<>(player, result.getInt(2)));
            }

            if(scores.size() < 1){
                SwordLevel.log(commandSender, SwordLevel.getMsgs("Rank.NoScore"));

            }else{
                SwordLevel.log(commandSender, SwordLevel.getMsgs("Rank.Start"));
                for (int i = 0; i < scores.size(); i++){
                    DValue<String, Integer> score = scores.get(i);
                    SwordLevel.log(
                            commandSender,
                            SwordLevel.getMsgs("Rank.Format",
                                    "n", String.valueOf(i),
                                    "player", score.getValue1(),
                                    "score", String.valueOf(score.getValue2()))
                    );
                }
                SwordLevel.log(commandSender, SwordLevel.getMsgs("Rank.End"));
            }

            return true;

        }catch (SQLException e){
            e.printStackTrace();
        }

        return false;
    }

}
