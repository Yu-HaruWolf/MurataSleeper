package com.github.yukkidra.murataalarm;

/////////////////////////////////////////////////////////
//// 引数1:BOTのトークン 引数2:サーバーID 引数3:ユーザーID ////
/////////////////////////////////////////////////////////

import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.entity.user.User;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class Main {
    public static void main(String[] args) throws ParseException {
        //初期設定(きれいにしたい)
        String token = args[0];
        DiscordApi api = new DiscordApiBuilder().setToken(token).login().join();
        String userId = args[2];
        CompletableFuture<User> CFuser = api.getUserById(userId);
        User user = null;
        try {
            user = CFuser.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
        Timer timer = new Timer(false);
        User finalUser = user;
        //
        TimerTask muteTask = new TimerTask() {
            @Override
            public void run() {
                finalUser.mute(api.getServerById(args[1]).get());//disconnectがあれば理想的だった
            }
        };
        TimerTask unmuteTask = new TimerTask() {
            @Override
            public void run() {
                finalUser.unmute(api.getServerById(args[1]).get());
            }
        };
        timer.schedule(muteTask, simpleDateFormat.parse("01:00"));//1時にミュート
        timer.schedule(unmuteTask, simpleDateFormat.parse("05:00"));//5時にミュート
    }

}
