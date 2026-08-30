package com.github.skorlex.hypixelpetreminder;

import net.minecraft.client.Minecraft;
import net.minecraft.event.ClickEvent;
import net.minecraft.event.HoverEvent;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class PetEventHandler {
    private long lastReminderTime = 0L;

    @SubscribeEvent
    public void onChat(ClientChatReceivedEvent event) {
        // Strip color codes to ensure clean matching
        String rawMessage = event.message.getUnformattedText();

        // Match the exact structure, allowing any pet name and any amount of EXP
        if (rawMessage.matches("^Your Lv \\d+ .+ earned [\\d,]+ EXP from the pet mission!$")) {
            // Set the target time to exactly 60 minutes (3,600,000 milliseconds) from right now
            long newTarget = System.currentTimeMillis() + 3600000L;
            PetConfig.saveTargetTime(newTarget);

            Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("§a§l[PetReminder] §e1-Hour mission timer started!"));

            // Play note.harp at max pitch (2.0F) with volume 100.0F to mimic QuestViewer's massive radius spatial sound
            Minecraft.getMinecraft().thePlayer.playSound("note.harp", 100.0F, 2.0F);
        }
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        // Run the check once per tick, and only if the player is in-game
        if (event.phase != TickEvent.Phase.END || Minecraft.getMinecraft().thePlayer == null) return;

        // If targetTime is 0, the timer is inactive
        if (PetConfig.targetTime == 0L) return;

        long currentTime = System.currentTimeMillis();

        if (currentTime >= PetConfig.targetTime) {
            // Remind the user exactly once every 60,000 milliseconds (1 minute)
            if (currentTime - lastReminderTime >= 60000L) {
                lastReminderTime = currentTime;
                sendReminderMessage();
            }
        }
    }

    private void sendReminderMessage() {
        IChatComponent prefix = new ChatComponentText("§a§l[PetReminder] §eYour pet is ready for another mission! ");

        IChatComponent cancelButton = new ChatComponentText("§c§l[CANCEL TIMER]");
        ChatStyle style = new ChatStyle()
                .setChatClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/pr cancel"))
                .setChatHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ChatComponentText("§eClick to stop reminders!")));
        cancelButton.setChatStyle(style);

        prefix.appendSibling(cancelButton);
        Minecraft.getMinecraft().thePlayer.addChatMessage(prefix);

        // Play random.orb at volume 100.0F based on the QuestViewer scaling trick
        Minecraft.getMinecraft().thePlayer.playSound("random.orb", 100.0F, 1.0F);
    }
}