package org.mmga.utils.interfaces;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import org.bukkit.command.CommandSender;

/**
 * @author wzp
 * @version 1.0
 * @since 2023/6/2 23:05:07
 */
public interface MmgaCommand {
    LiteralArgumentBuilder<CommandSender> getCommandBuilder();
}
