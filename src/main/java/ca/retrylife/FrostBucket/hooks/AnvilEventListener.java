package ca.retrylife.FrostBucket.hooks;

import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;

import ca.retrylife.FrostBucket.items.FrostBucketItemStack;

public class AnvilEventListener implements Listener {

    @EventHandler(priority = EventPriority.HIGH)
    public void handleAnvilUsage(PrepareAnvilEvent event) {

        // Fetch the anvil inventory
        ItemStack[] anvilInventoryContents = event.getInventory().getContents();
        ItemStack primaryAnvilItemSlot = anvilInventoryContents[0];
        ItemStack secondaryAnvilItemSlot = anvilInventoryContents[1];

        // We only care if we have two items
        if (primaryAnvilItemSlot == null || secondaryAnvilItemSlot == null) {
            return;
        }

        // We need a bucket and a frost walker book
        ItemStack bucket = this.findMaterialOrNull(Material.BUCKET, primaryAnvilItemSlot, secondaryAnvilItemSlot);
        ItemStack book = this.findEnchantedBookOrNull(Enchantment.FROST_WALKER, primaryAnvilItemSlot,
                secondaryAnvilItemSlot);

        // If either are null, we cannot complete this action
        if (bucket == null || book == null) {
            return;
        }
        Bukkit.getLogger().log(Level.FINEST,
                "FrostBucket: Hijacking currently active anvil session to inject custom item");

        // Set the repair cost for this action
        // TODO: Make this a configurable thing
        event.getInventory().setRepairCost(39);

        // Create a FrostBucket
        event.setResult(new FrostBucketItemStack());

    }

    /**
     * Searches a list of {@link ItemStack}s and returns the first matching material
     * it finds, or NULL if none are found
     * 
     * @param material   {@link Material} to search for
     * @param itemStacks List of {@link ItemStack}s
     * @return {@link ItemStack} or NULL
     */
    private ItemStack findMaterialOrNull(Material material, ItemStack... itemStacks) {
        for (ItemStack itemStack : itemStacks) {
            if (itemStack.getType().equals(material)) {
                return itemStack;
            }
        }
        return null;
    }

    /**
     * Searches a list of {@link ItemStack}s and returns the first enchanted book
     * with a specific {@link Enchantment}, or NULL if none are found
     * 
     * @param enchantment {@link Enchantment} to look for
     * @param itemStacks  List of {@link ItemStack}s
     * @return {@link ItemStack} or NULL
     */
    private ItemStack findEnchantedBookOrNull(Enchantment enchantment, ItemStack... itemStacks) {
        for (ItemStack itemStack : itemStacks) {

            // Must be an enchanted book
            if (itemStack.getType().equals(Material.ENCHANTED_BOOK)) {

                // Get the enchantment storage metadata
                EnchantmentStorageMeta meta = (EnchantmentStorageMeta) itemStack.getItemMeta();
                if (meta.hasStoredEnchant(enchantment)) {
                    return itemStack;
                }
            }
        }
        return null;
    }

}