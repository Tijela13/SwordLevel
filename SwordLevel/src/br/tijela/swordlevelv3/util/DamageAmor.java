package br.tijela.swordlevelv3.util;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class DamageAmor{

    public static final Map<Material,Integer> defPItens = new HashMap<>();
    public static final Map<Enchantment,Double> defPEnchantment = new HashMap<>();

    static {
        defPItens.put(Material.LEATHER_HELMET, 1);
        defPItens.put(Material.LEATHER_CHESTPLATE, 3);
        defPItens.put(Material.LEATHER_LEGGINGS, 2);
        defPItens.put(Material.LEATHER_BOOTS, 1);

        defPItens.put(Material.GOLD_HELMET, 2);
        defPItens.put(Material.GOLD_CHESTPLATE, 5);
        defPItens.put(Material.GOLD_LEGGINGS, 3);
        defPItens.put(Material.GOLD_BOOTS, 1);

        defPItens.put(Material.CHAINMAIL_HELMET, 2);
        defPItens.put(Material.CHAINMAIL_CHESTPLATE, 5);
        defPItens.put(Material.CHAINMAIL_LEGGINGS, 4);
        defPItens.put(Material.CHAINMAIL_BOOTS, 1);

        defPItens.put(Material.IRON_HELMET, 2);
        defPItens.put(Material.IRON_CHESTPLATE, 6);
        defPItens.put(Material.IRON_LEGGINGS, 5);
        defPItens.put(Material.IRON_BOOTS, 2);

        defPItens.put(Material.DIAMOND_HELMET, 3);
        defPItens.put(Material.DIAMOND_CHESTPLATE, 8);
        defPItens.put(Material.DIAMOND_LEGGINGS, 6);
        defPItens.put(Material.DIAMOND_BOOTS, 3);

        defPEnchantment.put(Enchantment.PROTECTION_ENVIRONMENTAL,  0.75);
    }

    public static int getDefPointsMaterial(Material material){
        if(material == null)
            return 0;

        return defPItens.containsKey(material) ?
                defPItens.get(material) : 0;
    }

    public static double getDefPointsEnchantment(Enchantment enchantment, int level){
        if(enchantment == null || !defPEnchantment.containsKey(enchantment))
            return 0;

        return Math.floor((6 + level ^ 2) * defPEnchantment.get(enchantment) / 3);
    }

    public static double getDefPointsEnchantments(Map<Enchantment, Integer> enchantments){
        double defPoints = 0;
        if(enchantments != null)
            for (Map.Entry<Enchantment, Integer> enchantment : enchantments.entrySet()){
                defPoints += getDefPointsEnchantment(enchantment.getKey(), enchantment.getValue());
            }
        return defPoints > 25 ? 25 : defPoints;
    }

    public static double getDefPointsItem(ItemStack itemStack){
        double defPoints = 0;

        if(itemStack != null && itemStack.getType() != Material.AIR){

            double enchanReduce = new Random().nextInt(50);
            enchanReduce += 50;
            enchanReduce = enchanReduce/100;

            defPoints += getDefPointsMaterial(itemStack.getType());
            defPoints += getDefPointsEnchantments(itemStack.getEnchantments())*0.2*enchanReduce;

        }

        return defPoints;
    }

    public static double getReduceDamage(PlayerInventory inventory){

        ItemStack helmet = inventory.getHelmet();
        ItemStack chestplate = inventory.getChestplate();
        ItemStack leggings = inventory.getLeggings();
        ItemStack boots = inventory.getBoots();

        double defPoints = 0;
        defPoints += getDefPointsItem(helmet);
        defPoints += getDefPointsItem(chestplate);
        defPoints += getDefPointsItem(leggings);
        defPoints += getDefPointsItem(boots);

        return defPoints*4;
    }

    public static double reduceDamage(PlayerInventory inventory, double damage){
        double reduceDamage = 100-getReduceDamage(inventory);
        return damage*(reduceDamage/100);
    }

}
