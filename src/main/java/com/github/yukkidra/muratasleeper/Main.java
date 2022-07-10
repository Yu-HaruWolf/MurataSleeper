package com.github.yukkidra.muratasleeper;

import ch.qos.logback.classic.Logger;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import org.slf4j.LoggerFactory;

import javax.security.auth.login.LoginException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    private final String VERSION = "Alpha 20220624";
    private final String PREFIX = "--";
    public static Logger logger;
    public static JDA api;

    private ActivityUpdater activityUpdater;
    public static void main(String[] args) {
        logger = (Logger) LoggerFactory.getLogger(Main.class);
        new Main(args);
    }

    Main(String[] args){
        String token;
        if(args.length >= 1){
            token = args[0];
            logger.debug("token from program argument[0]");
        } else {
            token = System.getenv("MURATASLEEPER_TOKEN");
            logger.debug("token from environment variable(MURATASLEEPER_TOKEN)");
        }
        if(token == null){
            logger.error("MURATASLEEPER_TOKEN has not been set!");
            logger.error("This program need BOT's token from program argument or environment variable(MURATASLEEPER_TOKEN)!");
            System.exit(-1);
        }
        initializeBot(token);
    }
    private void initializeBot(String token){
        logger.info("Bot initializing...");
        try {
            api = JDABuilder.createDefault(token).addEventListeners(new EventListener()).build().awaitReady();
            activityUpdater = new ActivityUpdater(api.getPresence());
            activityUpdater.setActivity(VERSION);
            initializeSlashCommands();
        } catch (InterruptedException | LoginException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    private void initializeSlashCommands(){
        List<CommandData> commands = new ArrayList<>();
        commands.add(new CommandData("sleep","指定された時間(単位：分)後にボイスチャットから抜けるようにします。").addOption(OptionType.INTEGER,"minutes","何分後にボイスチャットを抜けるか指定してください。",true));
        commands.add(new CommandData("cancel", "現在設定されているボイスチャット自動退出をキャンセルします。"));
        for (CommandData command : commands){
            api.upsertCommand(command).queue();
        }
    }
}
