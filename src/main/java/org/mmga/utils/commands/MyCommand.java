package org.mmga.utils.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestion;
import com.mojang.brigadier.suggestion.Suggestions;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.plugin.java.JavaPlugin;
import org.mmga.utils.MyUtils;
import org.mmga.utils.classes.MmgaCommandHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * @author wzp
 * @version 1.0
 * @since 2023/6/3 0:26:04
 */
public class MyCommand implements CommandExecutor, TabCompleter {
    private final CommandDispatcher<CommandSender> dispatcher;
    public MyCommand(){
        dispatcher = JavaPlugin.getPlugin(MyUtils.class).getDispatcher();
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        String name = command.getName();
        String argsString = String.join(" ", args);
        String fullCommand = name + " " + argsString;
        if (args.length == 0){
            fullCommand = fullCommand.replace(" ", "");
        }
        try {
            this.dispatcher.execute(fullCommand, sender);
        } catch (CommandSyntaxException e) {
            sender.sendMessage(ChatColor.RED + e.getMessage());
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        String name = command.getName();
        String argsString = String.join(" ", args);
        String fullCommand = name + " " + argsString;
        CompletableFuture<Suggestions> completionSuggestions = this.dispatcher.getCompletionSuggestions(this.dispatcher.parse(fullCommand, sender));
        List<String> result = new ArrayList<>();
        try {
            List<Suggestion> list = completionSuggestions.get().getList();
            list.stream().map(Suggestion::getText).forEach(result::add);
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
        return result;
    }
}