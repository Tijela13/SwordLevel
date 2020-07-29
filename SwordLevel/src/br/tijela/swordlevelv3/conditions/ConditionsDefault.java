package br.tijela.swordlevelv3.conditions;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class ConditionsDefault implements Conditions{


    protected String[] worldsWhite = null;
    protected String[] worldsBlack = null;

    protected Material[] materials = null;

    protected String[] names = null;
    protected String[] nameContains = null;

    protected String[] loreLine = null;
    protected String[] loreContains = null;

    public ConditionsDefault(ConfigurationSection section) {
        loadFromConfig(section);
    }

    @Override
    public String[] getWorldsWhite() {
        return worldsWhite;
    }

    @Override
    public boolean validWhiteWorld(String world) {
        if(worldsWhite != null && world != null){
            for (String worldList : worldsWhite) {
                if(world.equalsIgnoreCase(worldList))
                    return true;
            }
            return false;
        }
        return true;
    }

    @Override
    public String[] getWorldsBlack() {
        return worldsBlack;
    }

    @Override
    public boolean validBlackWorld(String world) {
        if(worldsBlack != null && world != null){
            for (String worldList : worldsBlack) {
                if(world.equalsIgnoreCase(worldList))
                    return false;
            }
        }
        return true;
    }

    @Override
    public Material[] getMaterials() {
        return materials;
    }

    @Override
    public boolean hasMaterial(Material material) {
        if(materials != null && material != null){
            for (Material materialList : materials) {
                if(material == materialList)
                    return true;
            }
        }
        return false;
    }

    @Override
    public String[] getNames() {
        return names;
    }

    @Override
    public boolean hasName(String name) {
        if(names != null && name != null){
            for (String nameList : names) {
                if(name.equalsIgnoreCase(nameList))
                    return true;
            }
        }
        return false;
    }

    @Override
    public String[] getNamesContains() {
        return nameContains;
    }

    @Override
    public boolean hasNameContains(String name) {
        if(nameContains != null && name != null){
            for (String nameList : nameContains) {
                if(name.contains(nameList))
                    return true;
            }
        }
        return false;
    }

    @Override
    public String[] getLoreLines() {
        return loreLine;
    }

    @Override
    public boolean hasLoreLine(List<String> lore) {
        if(loreLine != null && lore != null){
            for (String lineLore : lore) {
                for (String line : loreLine) {
                    if(line.equalsIgnoreCase(lineLore))
                        return true;
                }
            }
        }
        return false;
    }

    @Override
    public String[] getLoreContains() {
        return loreContains;
    }

    @Override
    public boolean hasLoreContains(List<String> lore) {
        if(loreLine != null && lore != null){
            for (String lineLore : lore) {
                for (String line : loreContains) {
                    if(lineLore.contains(line))
                        return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean conditionValid(Player player, ItemStack item) {
        return
                (validWhiteWorld(player.getWorld().getName()) && validBlackWorld(player.getWorld().getName()))
                        && (hasMaterial(item.getType())
                        || (item.getItemMeta() != null
                        && (hasName(item.getItemMeta().getDisplayName())
                        || hasNameContains(item.getItemMeta().getDisplayName())
                        || hasLoreLine(item.getItemMeta().getLore())
                        || hasLoreContains(item.getItemMeta().getLore()))));
    }

    @Override
    public void loadFromConfig(ConfigurationSection section) {
        if(section.contains("WorldsWhite")){
            final List<String> worldsWhite = section.getStringList("WorldsWhite");
            this.worldsWhite = worldsWhite.toArray(new String[worldsWhite.size()]);
        }
        if(section.contains("WorldsBlack")){
            final List<String> worldsBlack = section.getStringList("WorldsBlack");
            this.worldsBlack = worldsBlack.toArray(new String[worldsBlack.size()]);
        }
        if(section.contains("Material")){
            final List<String> materials = section.getStringList("Material");
            this.materials = new Material[materials.size()];
            for (int i = 0; i < materials.size(); i++) {
                this.materials[i] = Material.valueOf(materials.get(i));
            }
        }
        if(section.contains("Name")){
            final List<String> name = section.getStringList("Name");
            this.names = name.toArray(new String[name.size()]);
        }
        if(section.contains("NameContains")){
            final List<String> nameContains = section.getStringList("NameContains");
            this.nameContains = nameContains.toArray(new String[nameContains.size()]);
        }
        if(section.contains("LoreLine")){
            final List<String> loreLine = section.getStringList("LoreLine");
            this.loreLine = loreLine.toArray(new String[loreLine.size()]);
        }
        if(section.contains("LoreContains")){
            final List<String> loreContains = section.getStringList("LoreContains");
            this.loreContains = loreContains.toArray(new String[loreContains.size()]);
        }
    }
}
