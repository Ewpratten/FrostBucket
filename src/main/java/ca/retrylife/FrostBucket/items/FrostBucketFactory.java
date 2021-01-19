package ca.retrylife.FrostBucket.items;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

public class FrostBucketFactory {

    public static ItemStack createFrostBucket() {

        // Create a bucket
        ItemStack bucket = new ItemStack(Material.BUCKET);
        ItemMeta bucketMeta = bucket.getItemMeta();

        // Configure the item enchantments
        bucketMeta.addEnchant(Enchantment.FROST_WALKER, 1, true);

        // Configure the item name
        bucketMeta.setDisplayName(ChatColor.AQUA + "Frost Bucket");
        bucketMeta.setLore(
                List.of("A mysterious item from a land far away.", "Turns water to ice, but we can't figure out how."));

        // Configure the item durability
        bucketMeta.setUnbreakable(false);
        ((Damageable) bucketMeta).setDamage(0);

        // Attach meta to item
        bucket.setItemMeta(bucketMeta);

        return bucket;

    }

}