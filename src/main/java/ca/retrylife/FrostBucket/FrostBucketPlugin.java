package ca.retrylife.FrostBucket;

import kr.entree.spigradle.annotations.PluginMain;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.JavaPluginLoader;

import ca.retrylife.FrostBucket.hooks.AnvilEventListener;
import ca.retrylife.FrostBucket.hooks.FrostBucketUsageEventListener;

import java.io.File;

@PluginMain
public class FrostBucketPlugin extends JavaPlugin implements Listener {
    

    public FrostBucketPlugin() {
    }

    public FrostBucketPlugin(JavaPluginLoader loader, PluginDescriptionFile description, File dataFolder, File file) {
        super(loader, description, dataFolder, file);

        // Enchantment.FROST_WALKER;
    }

    @Override
    public void onEnable() {

        // Global events
        getServer().getPluginManager().registerEvents(this, this);

        // Anvil events
        getServer().getPluginManager().registerEvents(new AnvilEventListener(), this);

        // World events
        getServer().getPluginManager().registerEvents(new FrostBucketUsageEventListener(), this);

    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        getLogger().info("Player joined.");
    }

}