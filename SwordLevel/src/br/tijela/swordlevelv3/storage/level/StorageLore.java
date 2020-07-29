package br.tijela.swordlevelv3.storage.level;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class StorageLore implements StorageLevel{

    protected final ItemStack item;

    protected final String levelPrefix;
    protected final String levelSufix;
    protected final String xpPrefix;
    protected final String xpSufix;

    protected int lineLevel = -1;
    protected int lineXp = -1;

    protected int levelNow = 1;
    protected int xpNow = 0;

    public StorageLore(ItemStack item, String levelPrefix, String levelSufix, String xpPrefix, String xpSufix) {
        this.item = item;
        this.levelPrefix = levelPrefix;
        this.levelSufix = levelSufix;
        this.xpPrefix = xpPrefix;
        this.xpSufix = xpSufix;

        reloadInfos();
    }

    public ItemStack getItem() {
        return item;
    }

    public String getLevelPrefix() {
        return levelPrefix;
    }

    public String getLevelSufix() {
        return levelSufix;
    }

    public String getXpPrefix() {
        return xpPrefix;
    }

    public String getXpSufix() {
        return xpSufix;
    }

    public int getLineLevel() {
        return lineLevel;
    }

    public int getLineXp() {
        return lineXp;
    }


    @Override
    public int getLevel() {
        return levelNow;
    }

    @Override
    public int getXp() {
        return xpNow;
    }

    @Override
    public void setXp(int xpNow) {
        this.xpNow = xpNow;
        saveInfos();
    }

    @Override
    public void setLevel(int levelNow) {
        levelNow = levelNow < 1 ? 1 : levelNow;
        this.levelNow = levelNow;
        saveInfos();
    }

    public void addLevel(int level){
        setLevel(getLevel()+level);
    }

    public void addXp(int xp){
        setXp(getXp()+xp);
    }

    public void removeLevel(int level){
        setLevel(getLevel()-level);
    }

    public void removeXp(int xp){
        setXp(getXp()-xp);
    }


    public void reloadInfos(){
        reloadLinesLore();
        reloadValuesLore();
    }

    public void reloadLinesLore(){
        List<String> lore = item.getItemMeta().getLore();

        if(lore != null)
            for (int i = 0; i < lore.size(); i++){
                String line = lore.get(i);

                if(line.startsWith(levelPrefix) && line.endsWith(levelSufix))
                    lineLevel = i;

                else if(line.startsWith(xpPrefix) && line.endsWith(xpSufix))
                    lineXp = i;

            }
    }

    public void reloadValuesLore(){
        List<String> lore = item.getItemMeta().getLore();

        if(lore == null)
            return;

        try{

            if(lineLevel >= 0){
                String lineLevellore = lore.get(lineLevel);
                levelNow = Integer.parseInt(lineLevellore
                        .substring(0, lineLevellore.length() - levelSufix.length())
                        .substring(levelPrefix.length()));
            }

            if(lineXp >= 0){
                String lineXPlore = lore.get(lineXp);
                xpNow = Integer.parseInt(lineXPlore
                        .substring(0, lineXPlore.length() - xpSufix.length())
                        .substring(xpPrefix.length()));
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }


    public void saveInfos(){
        String levelString = levelPrefix+levelNow+levelSufix;
        String xpString = xpPrefix+xpNow+xpSufix;

        ItemMeta itemMeta = item.getItemMeta();
        List<String> lore = itemMeta.getLore();
        if(lore == null) lore = new ArrayList<>();

        if(lineLevel >= 0)
            lore.set(lineLevel, levelString);
        else {
            lineLevel = lore.size();
            lore.add(levelString);
        }

        if(lineXp >= 0)
            lore.set(lineXp, xpString);
        else {
            lineXp = lore.size();
            lore.add(xpString);
        }

        itemMeta.setLore(lore);
        item.setItemMeta(itemMeta);
    }
}
