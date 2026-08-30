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
    private long lastTriggerTime = 0L;

    @SubscribeEvent
    public void onChat(ClientChatReceivedEvent event) {
        String rawMessage = event.message.getUnformattedText();

        if (rawMessage.matches("^Your Lv \\d+ .+ earned [\\d,]+ EXP from the pet mission!$")) {
            long currentTime = System.currentTimeMillis();

            // Debounce window: If another pet message fired less than 1.5 seconds ago, ignore this one
            if (currentTime - lastTriggerTime < 1500L) {
                return;
            }
            lastTriggerTime = currentTime;

            long newTarget = currentTime + 3600000L;
            PetConfig.saveTargetTime(newTarget);

            Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("§a§l[PetReminder] §e1-Hour mission timer started!"));
            Minecraft.getMinecraft().thePlayer.playSound("note.harp", 100.0F, 2.0F);
        }
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END || Minecraft.getMinecraft().thePlayer == null) return;

        if (PetConfig.targetTime == 0L) return;

        long currentTime = System.currentTimeMillis();

        if (currentTime >= PetConfig.targetTime) {
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

        Minecraft.getMinecraft().thePlayer.playSound("random.orb", 100.0F, 1.0F);
    }
}