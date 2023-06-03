package org.mmga.utils.classes;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import org.bukkit.command.*;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.mmga.utils.MyUtils;
import org.mmga.utils.events.Registers;
import org.mmga.utils.interfaces.MmgaClassTypeHandler;
import org.mmga.utils.interfaces.MmgaCommand;

import java.lang.reflect.InvocationTargetException;

/**
 * @author wzp
 * @version 1.0
 * @since 2023/6/2 23:26:22
 */
public class MmgaCommandHandler implements MmgaClassTypeHandler {
    private final CommandDispatcher<CommandSender> dispatcher;
    private final Registers registers;
    public MmgaCommandHandler(){
        MyUtils plugin = JavaPlugin.getPlugin(MyUtils.class);
        this.registers = plugin.getRegisters();
        this.dispatcher = plugin.getDispatcher();
    }
    @Override
    public Class<?> getType() {
        return MmgaCommand.class;
    }

    @Override
    public void handler(Plugin plugin, Class<?> aClass) {
        try {
            MmgaCommand o = (MmgaCommand) aClass.getConstructor().newInstance();
            LiteralArgumentBuilder<CommandSender> commandBuilder = o.getCommandBuilder();
            String commandName = commandBuilder.getLiteral();
            dispatcher.register(commandBuilder);
            registers.addCommand(plugin, commandName);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }
}
