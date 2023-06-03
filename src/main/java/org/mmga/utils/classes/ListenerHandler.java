package org.mmga.utils.classes;

import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.mmga.utils.MyUtils;
import org.mmga.utils.events.Registers;
import org.mmga.utils.interfaces.MmgaClassTypeHandler;

import java.lang.reflect.InvocationTargetException;

/**
 * @author wzp
 * @version 1.0
 * @since 2023/6/2 23:10:17
 */
public class ListenerHandler implements MmgaClassTypeHandler {
    private final Registers registers;
    public ListenerHandler(){
        this.registers = JavaPlugin.getPlugin(MyUtils.class).getRegisters();
    }
    @Override
    public Class<?> getType() {
        return Listener.class;
    }

    @Override
    public void handler(Plugin plugin, Class<?> clazz) {
        try {
            Listener o = (Listener) clazz.getConstructor().newInstance();
            this.registers.addEvent(plugin, o);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }
}
