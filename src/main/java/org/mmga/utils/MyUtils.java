package org.mmga.utils;

import com.mojang.brigadier.CommandDispatcher;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.mmga.utils.events.Registers;
import org.mmga.utils.interfaces.MmgaClassTypeHandler;
import org.mmga.utils.utils.jar.JarUtils;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Logger;

public final class MyUtils extends JavaPlugin {
    private final CommandDispatcher<CommandSender> dispatcher = new CommandDispatcher<>();
    private final Map<Class<?>, MmgaClassTypeHandler> classes = new HashMap<>();
    private Registers registers;
    public CommandDispatcher<CommandSender> getDispatcher(){
        return this.dispatcher;
    }
    public Registers getRegisters(){
        return this.registers;
    }
    private void loadDependedPlugin(){
        Server server = this.getServer();
        PluginManager pluginManager = server.getPluginManager();
        Logger logger = this.getLogger();
        for (Plugin plugin : pluginManager.getPlugins()) {
            if (plugin.getDescription().getDepend().contains("MyUtils")) {
                URLClassLoader classLoader = (URLClassLoader) plugin.getClass().getClassLoader();
                try(JarFile jar = JarUtils.getJarFileFromClassLoader(classLoader)){
                    Enumeration<JarEntry> entries = jar.entries();
                    while (entries.hasMoreElements()) {
                        JarEntry jarEntry = entries.nextElement();
                        String name = jarEntry.getName();
                        if (name.endsWith(".class")){
                            String className = name.replace(".class", "").replace("/", ".");
                            try {
                                Class<?> aClass = classLoader.loadClass(className);
                                classes.forEach((k, v) -> {
                                    if (k.isAssignableFrom(aClass)) {
                                        v.handler(plugin, aClass);
                                    }
                                });
                            } catch (ClassNotFoundException e) {
                                logger.severe("Error when loading class " + className);
                                e.printStackTrace();
                            }
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    private void loadClassHandler(){
        URLClassLoader classLoader = (URLClassLoader) this.getClassLoader();
        JarFile selfJar = JarUtils.getJarFileFromClassLoader(classLoader);
        Enumeration<JarEntry> entries = selfJar.entries();
        Logger logger = this.getLogger();
        while (entries.hasMoreElements()) {
            JarEntry jarEntry = entries.nextElement();
            String name = jarEntry.getName();
            if (name.endsWith(".class") && name.startsWith("org/mmga/utils/classes")){
                String className = name.replace(".class", "").replace("/", ".");
                try {
                    Class<?> aClass = classLoader.loadClass(className);
                    if (!MmgaClassTypeHandler.class.isAssignableFrom(aClass)){
                        continue;
                    }
                    MmgaClassTypeHandler o = (MmgaClassTypeHandler) aClass.getConstructor().newInstance();
                    Class<?> type = o.getType();
                    classes.put(type, o);
                    logger.info("Load class handle of " + type.getName());
                } catch (ClassNotFoundException | NoSuchMethodException | InstantiationException |
                         IllegalAccessException | InvocationTargetException e) {
                    logger.severe("Error when loading class " + className);
                    e.printStackTrace();
                }
            }
        }
    }
    @Override
    public void onEnable() {
        registers = new Registers();
        Server server = this.getServer();
        PluginManager pluginManager = server.getPluginManager();
        pluginManager.registerEvents(registers, this);
        loadClassHandler();
        loadDependedPlugin();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        registers.unRegister();
    }
}
