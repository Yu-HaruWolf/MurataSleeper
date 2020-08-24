package com.github.yukkidra.murataalarm;

/////////////////////////////////////////////////////////
//// 引数1:BOTのトークン 引数2:サーバーID 引数3:ユーザーID ////
/////////////////////////////////////////////////////////

import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.entity.user.User;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class Main {
    public static void main(String[] args) throws ParseException {
        //初期設定(きれいにしたい)
        String token = args[0];
        DiscordApi api = new DiscordApiBuilder().setToken(token).login().join();
        System.out.println("Logged in!");
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
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        SimpleDateFormat hourFormat = new SimpleDateFormat("HH");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        Timer timer = new Timer(false);
        User finalUser = user;
        Calendar calendar = Calendar.getInstance();
        int hour = Integer.parseInt(hourFormat.format(calendar.getTime()));
        String date =  dateFormat.format(calendar.getTime());
        calendar.add(Calendar.DAY_OF_MONTH , 1);
        String nextDate = dateFormat.format(calendar.getTime());
        //
        TimerTask muteTask = new TimerTask() {
            @Override
            public void run() {
                finalUser.mute(api.getServerById(args[1]).get());//disconnectがあれば理想的だった
                System.out.println("Mute Executed!");
            }
        };
        TimerTask unmuteTask = new TimerTask() {
            @Override
            public void run() {
                finalUser.unmute(api.getServerById(args[1]).get());
                System.out.println("UnMute Executed!");
            }
        };
        //1時ミュート初期設定
        if (hour < 1) {
            timer.scheduleAtFixedRate(muteTask, simpleDateFormat.parse(date + " " + "01:00"), 1000 * 60 * 60 * 24);
        } else {
            timer.schedule(muteTask, simpleDateFormat.parse(nextDate + " " + "01:00"), 1000 * 60 * 60 * 24);
        }
        //4時ミュート解除初期設定
        if(hour < 4){
            timer.schedule(unmuteTask, simpleDateFormat.parse(date + " " + "04:00"), 1000 * 60 * 60 * 24);
        } else {
            timer.schedule(unmuteTask, simpleDateFormat.parse(nextDate + " " + "04:00"), 1000 * 60 * 60 * 24);
        }
        /*
        timer.schedule(muteTask, simpleDateFormat.parse("01:00"));//1時にミュート
        timer.schedule(unmuteTask, simpleDateFormat.parse("05:00"));//5時にミュート解除
         */
    }

}
