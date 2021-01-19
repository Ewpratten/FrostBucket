package ca.retrylife.FrostBucket.hooks;

import org.bukkit.FluidCollisionMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.BlockIterator;

import ca.retrylife.FrostBucket.items.FrostBucketItemStack;

public class FrostBucketUsageEventListener implements Listener {

    private static final int PLAYER_REACH = 9;

    @EventHandler(priority = EventPriority.HIGH)
    public void handlePlayerRightClick(PlayerInteractEvent event) {

        // We only care if the player is right-clicking a block or fluid
        if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {

            // Get the player
            Player player = event.getPlayer();

            // Check if they are holding a frost bucket
            ItemStack mainHandItem = player.getInventory().getItemInMainHand();
            if (mainHandItem != null && FrostBucketItemStack.isFrostBucket(mainHandItem)) {

                // Remove any regular Minecraft bucket logic
                event.setCancelled(true);

                // Find what the player is looking at
                Block targetedBlock = player.getTargetBlockExact(PLAYER_REACH, FluidCollisionMode.SOURCE_ONLY);
                if (targetedBlock != null) {

                    // If the bucket is filled, we must place
                    if (FrostBucketItemStack.isBucketFilledWithWater(mainHandItem)) {

                        // Empty the bucket
                        FrostBucketItemStack.emptyBucket(mainHandItem);

                        // Find the farthest air block the player is looking at (This is where ice will
                        // go)
                        BlockIterator iterator = new BlockIterator(player, PLAYER_REACH);

                        Block farthestAirBlock = iterator.next();
                        while (iterator.hasNext()) {
                            Block nextBlock = iterator.next();
                            if (nextBlock.getType() == Material.AIR) {
                                farthestAirBlock = nextBlock;
                                continue;
                            }
                            break;
                        }

                        // Actually place a block
                        farthestAirBlock.setType(Material.PACKED_ICE);

                        // Take damage on the bucket
                        FrostBucketItemStack.doDurabilityLogic(mainHandItem);

                        // If this just broke the bucket, play the break sound, otherwise the block
                        // place sound
                        if (FrostBucketItemStack.isBucketBroken(mainHandItem)) {
                            player.playSound(player.getLocation(), Sound.ENTITY_ITEM_BREAK, SoundCategory.BLOCKS, 1.0f,
                                    1.0f);
                            // Clear the item from the player
                            player.getInventory().setItemInMainHand(null);
                        } else {
                            player.playSound(player.getLocation(), Sound.BLOCK_STONE_PLACE, SoundCategory.BLOCKS, 1.0f,
                                    1.0f);
                        }

                    } else {
                        // If the targeted block is a water source, pick it up
                        if (targetedBlock.getType().equals(Material.WATER)) {

                            // Fill the bucket
                            FrostBucketItemStack.fillBucketWithWater(mainHandItem);

                            // Clear the targeted block
                            targetedBlock.setType(Material.AIR);

                            // Remove the block
                            targetedBlock.breakNaturally();

                        } else if (targetedBlock.getType().equals(Material.LAVA)) {

                            // Burn the bucket
                            FrostBucketItemStack.breakBucket(mainHandItem);

                            // Clear the targeted block
                            targetedBlock.setType(Material.AIR);

                            // Clear the item from the player
                            player.getInventory().setItemInMainHand(null);

                            // Play burn sound
                            player.playSound(player.getLocation(), Sound.ENTITY_GENERIC_BURN, SoundCategory.BLOCKS,
                                    1.0f, 1.0f);

                        }
                    }

                }
            }
        }
    }
}