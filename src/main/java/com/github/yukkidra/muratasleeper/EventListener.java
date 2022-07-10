package com.github.yukkidra.muratasleeper;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import static com.github.yukkidra.muratasleeper.Main.logger;


public class EventListener extends ListenerAdapter {
    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        logger.debug(event.getAuthor().getName() + " : " + event.getMessage().getContentRaw());
    }
}
