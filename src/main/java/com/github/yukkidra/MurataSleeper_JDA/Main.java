package com.github.yukkidra.MurataSleeper_JDA;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;

public class Main {
    public static void main(String[] args) throws Exception {
        JDA api = JDABuilder.createDefault(args[0]).build().awaitReady();
        System.out.println("Ready!");
        Guild guild = api.getGuildById(args[1]);
        User user = api.getUserById(args[2]);
        Member member = guild.getMember(user);
    }
}
