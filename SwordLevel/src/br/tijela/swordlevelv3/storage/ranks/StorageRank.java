package br.tijela.swordlevelv3.storage.ranks;

import br.neitan96.swordlevelv3.util.ConfigLoader;

public interface StorageRank extends ConfigLoader{

    void updateScore(String player, int levelWin, int xpWin, int levelNow, int xpNow);

}
