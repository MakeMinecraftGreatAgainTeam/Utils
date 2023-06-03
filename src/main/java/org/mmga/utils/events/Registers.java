package org.mmga.utils.events;

import org.bukkit.Server;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.mmga.utils.MyUtils;
import org.mmga.utils.commands.MyCommand;
import org.mmga.utils.interfaces.MmgaBukkitCommand;
import org.mmga.utils.utils.map.IncreaseAbleHashMap;
import org.mmga.utils.utils.map.IncreaseAbleMap;

import java.util.Objects;
import java.util.logging.Logger;
import java.util.stream.Stream;

/**
 * @author wzp
 * @version 1.0
 * @since 2023/5/31 23:11:37
 */
public class Registers implements Listener {
    private final IncreaseAbleMap<Plugin, Listener> events = new IncreaseAbleHashMap<>();
    private final IncreaseAbleHashMap<Plugin, String> bridgerCommands = new IncreaseAbleHashMap<>();
    private final MyCommand exec = new MyCommand();
    private final Logger logger;
    public Registers(){
        MyUtils plugin = JavaPlugin.getPlugin(MyUtils.class);
        logger = plugin.getLogger();
    }
    @EventHandler
    public void onPluginEnable(PluginEnableEvent event){
        JavaPlugin plugin = (JavaPlugin) event.getPlugin();
        Server server = plugin.getServer();
        PluginManager pluginManager = server.getPluginManager();
        events.forEach(plugin, e -> pluginManager.registerEvents(e, plugin));
        bridgerCommands.forEach(plugin, e -> {
            PluginCommand command = plugin.getCommand(e);
            if (command == null){
                logger.warning("Cannot getCommand " + e);
                return;
            }
            command.setExecutor(exec);
            command.setTabCompleter(exec);
        });
    }
    public void addEvent(Plugin plugin, Listener listener){
        events.add(plugin, listener);
    }
    public void addCommand(Plugin plugin, String command){
        bridgerCommands.add(plugin, command);
    }
    public void unRegister(){
        bridgerCommands.forEach((k,v) -> {
            JavaPlugin plugin = (JavaPlugin) k;
            v.stream().map(plugin::getCommand).filter(Objects::nonNull).forEach(e -> {e.setExecutor(null);e.setTabCompleter(null);});
        });
    }
}
