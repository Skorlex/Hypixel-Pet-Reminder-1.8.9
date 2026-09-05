package com.github.skorlex.hypixelpetreminder;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.BlockPos;
import java.util.List;
import java.util.Arrays;

public class PetCommand extends CommandBase {

    @Override
    public String getCommandName() {
        return "petreminder";
    }

    @Override
    public List<String> getCommandAliases() {
        return Arrays.asList("pr");
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/pr <help|timer|cancel|toggle>";
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true; // Always return true for client-side commands
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        if (args.length == 0 || args[0].equalsIgnoreCase("help")) {
            printHelp(sender);
            return;
        }

        if (args[0].equalsIgnoreCase("cancel")) {
            PetConfig.saveTargetTime(0L);
            sender.addChatMessage(new ChatComponentText("§a§l[PetReminder] §cTimer cancelled."));
        } else if (args[0].equalsIgnoreCase("toggle")) {
            boolean newState = !PetConfig.enabled;
            PetConfig.setEnabled(newState);

            if (newState) {
                sender.addChatMessage(new ChatComponentText("§a§l[PetReminder] §aMod enabled."));
            } else {
                PetConfig.saveTargetTime(0L);
                sender.addChatMessage(new ChatComponentText("§a§l[PetReminder] §cMod disabled and timer cancelled."));
            }
        } else if (args[0].equalsIgnoreCase("timer")) {
            if (!PetConfig.enabled) {
                sender.addChatMessage(new ChatComponentText("§a§l[PetReminder] §cThe mod is currently disabled. Type §e/pr toggle §cto enable it."));
                return;
            }

            long remainingMillis = PetConfig.targetTime - System.currentTimeMillis();

            if (PetConfig.targetTime == 0L || remainingMillis <= 0) {
                sender.addChatMessage(new ChatComponentText("§a§l[PetReminder] §eYou have no active pet missions."));
            } else {
                long minutes = (remainingMillis / 1000) / 60;
                long seconds = (remainingMillis / 1000) % 60;
                sender.addChatMessage(new ChatComponentText(String.format("§a§l[PetReminder] §eTime remaining: §b%02d:%02d", minutes, seconds)));
            }
        } else {
            sender.addChatMessage(new ChatComponentText("§cUnknown command. Type §e/pr help §cfor a list of commands."));
        }
    }

    private void printHelp(ICommandSender sender) {
        sender.addChatMessage(new ChatComponentText("§m----------------------------------------"));
        sender.addChatMessage(new ChatComponentText("§a§lHypixel Pet Reminder"));
        sender.addChatMessage(new ChatComponentText(""));
        sender.addChatMessage(new ChatComponentText("§e/pr timer §7- View remaining time on your current mission"));
        sender.addChatMessage(new ChatComponentText("§e/pr cancel §7- Stop the 1-hour timer and disable reminders"));
        sender.addChatMessage(new ChatComponentText("§e/pr toggle §7- Enable or disable the mod entirely"));
        sender.addChatMessage(new ChatComponentText("§e/pr help §7- Display this help menu"));
        sender.addChatMessage(new ChatComponentText("§m----------------------------------------"));
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
        if (args.length == 1) {
            return getListOfStringsMatchingLastWord(args, "help", "timer", "cancel", "toggle");
        }
        return Arrays.asList();
    }
}