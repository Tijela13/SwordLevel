package br.tijela.swordlevelv3.messages;

import br.neitan96.swordlevelv3.util.ConfigLoader;

public interface SwordMessages extends ConfigLoader{

    String getPrefix();


    String getLevelup(int levelNow, int levelUp);

    String getXpReward(int xpWin, int xpMissing);

    String getViewLevel(int levelNow, int xpNow, int xpMissing);

    String getNoView();

}
