package ca.retrylife.FrostBucket.items;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

/**
 * A simple wrapper around an {@link ItemStack} that creates a single
 * FrostBucket
 */
public class FrostBucketItemStack extends ItemStack {

    // This is kind of sketchy, but I don't want to make FrostBucketPlugin a
    // Singleton, so we must deal with it. Not sure if this is even needed, but I'm
    // not taking the risk given the oddities of server->client->server code
    @SuppressWarnings({ "deprecation" })
    private static final NamespacedKey IDENTIFIER_KEY = new NamespacedKey("frost_bucket_plugin", "is_frosty");
    @SuppressWarnings({ "deprecation" })
    private static final NamespacedKey DAMAGE_KEY = new NamespacedKey("frost_bucket_plugin", "damage");

    // Damage info
    private static final int MAXIMUM_DAMAGE = 10;
    private static final int DAMAGE_INCREMENT = 1;

    /**
     * Create a FrostBucketItemStack
     */
    public FrostBucketItemStack() {
        super(Material.BUCKET);

        // Fetch internal metadata
        ItemMeta meta = super.getItemMeta();

        // Configure the item enchantments
        meta.addEnchant(Enchantment.FROST_WALKER, 1, true);

        // Configure the item name
        meta.setDisplayName(ChatColor.AQUA + "Frost Bucket");
        meta.setLore(
                List.of("A mysterious item from a land far away.", "Turns water to ice, but we can't figure out how.",
                        "This is a unstable item that may break without warning."));

        // Configure the item durability
        meta.setUnbreakable(false);
        ((Damageable) meta).setDamage(0);
        super.getType().getMaxDurability();

        // Add a custom property so we can implement a network-safe isInstance()
        // function and damage calculation
        meta.getPersistentDataContainer().set(IDENTIFIER_KEY, PersistentDataType.BYTE, (byte) 1);
        meta.getPersistentDataContainer().set(DAMAGE_KEY, PersistentDataType.INTEGER, 0);

        // Attach meta to self
        super.setItemMeta(meta);
    }

    /**
     * Check if an {@link ItemStack} is a FrostBucketItemStack
     * 
     * @param items {@link ItemStack}
     * @return is a FrostBucketItemStack?
     */
    public static boolean isFrostBucket(ItemStack items) {
        if (items == null) {
            return false;
        }

        // Direct instance makes this a frost bucket
        if (items instanceof FrostBucketItemStack) {
            System.out.println("ISINSTANCE");
            return true;
        }

        // Get metadata
        ItemMeta meta = items.getItemMeta();
        if (meta == null) {
            return false;
        }

        // NBT makes this a frost bucket
        if (meta.getPersistentDataContainer().getOrDefault(IDENTIFIER_KEY, PersistentDataType.BYTE,
                (byte) 0) == (byte) 1) {
            System.out.println("IS_NET");

            return true;
        }

        return false;
    }

    public static boolean isBucketFilledWithWater(ItemStack bucket) {
        if (bucket == null) {
            return false;
        }
        return bucket.getType().equals(Material.WATER_BUCKET);
    }

    public static boolean isBucketEmpty(ItemStack bucket) {
        if (bucket == null) {
            return false;
        }
        return bucket.getType().equals(Material.BUCKET);
    }

    public static void fillBucketWithWater(ItemStack bucket) {
        if (bucket == null) {
            return;
        }

        // Set the material
        bucket.setType(Material.WATER_BUCKET);
    }

    public static void emptyBucket(ItemStack bucket) {
        if (bucket == null) {
            return;
        }

        // Set the material
        bucket.setType(Material.BUCKET);
    }

    public static boolean isBucketBroken(ItemStack bucket) {
        if (bucket == null) {
            return false;
        }

        return bucket.getItemMeta().getPersistentDataContainer().getOrDefault(DAMAGE_KEY, PersistentDataType.INTEGER,
                0) >= MAXIMUM_DAMAGE;

    }

    public static void doDurabilityLogic(ItemStack bucket) {
        if (bucket == null) {
            return;
        }

        // Get current damage
        int currentDamage = bucket.getItemMeta().getPersistentDataContainer().getOrDefault(DAMAGE_KEY,
                PersistentDataType.INTEGER, 0);

        // Add increment
        bucket.getItemMeta().getPersistentDataContainer().set(DAMAGE_KEY, PersistentDataType.INTEGER,
                currentDamage + DAMAGE_INCREMENT);

        System.out.println(currentDamage + 1);

    }

    public static void breakBucket(ItemStack bucket) {
        if (bucket == null) {
            return;
        }

        bucket.getItemMeta().getPersistentDataContainer().set(DAMAGE_KEY, PersistentDataType.INTEGER, MAXIMUM_DAMAGE);

    }

}