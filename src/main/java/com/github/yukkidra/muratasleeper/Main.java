package com.github.yukkidra.muratasleeper;

import ch.qos.logback.classic.Logger;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import org.slf4j.LoggerFactory;

import javax.security.auth.login.LoginException;

public class Main {
    private final String VERSION = "Ver 1.5";
    public static Logger logger;
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
            JDA api = JDABuilder.createDefault(token).addEventListeners(new EventListener()).build().awaitReady();
        } catch (InterruptedException | LoginException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }
}
