package murataalarm;

/////////////////////////////////////////////////////////
//// 引数1:BOTのトークン 引数2:サーバーID 引数3:ユーザーID ////
/////////////////////////////////////////////////////////

import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.entity.permission.PermissionType;
import org.javacord.api.entity.user.User;
import org.javacord.api.entity.user.UserStatus;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class Main {
    public static void main(String[] args) throws ParseException {
        //初期設定(きれいにしたい)

        String version = "Ver. 1.1";

        String token = args[0];
        DiscordApi api = new DiscordApiBuilder().setToken(token).login().join();
        System.out.println("Logged in!");
        api.updateActivity(version);
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
        final boolean[] isMutetime = {false};
        //
        TimerTask muteTask = new TimerTask() {
            @Override
            public void run() {
                isMutetime[0] = true;
                if(api.getServerById(args[1]).get().hasPermission(api.getYourself(), PermissionType.MUTE_MEMBERS)) {
                    finalUser.mute(api.getServerById(args[1]).get());//disconnectがあれば理想的だった
                    api.updateStatus(UserStatus.IDLE);
                    System.out.println("Mute Executed!");
                } else {
                    api.updateStatus(UserStatus.DO_NOT_DISTURB);
                    System.out.println("Skip mute action because no permission");
                }
            }
        };
        TimerTask unmuteTask = new TimerTask() {
            @Override
            public void run() {
                isMutetime[0] = false;
                if(api.getServerById(args[1]).get().hasPermission(api.getYourself(), PermissionType.MUTE_MEMBERS)) {
                    finalUser.unmute(api.getServerById(args[1]).get());
                    api.updateStatus(UserStatus.ONLINE);
                    System.out.println("UnMute Executed!");
                } else {
                    api.updateStatus(UserStatus.DO_NOT_DISTURB);
                    System.out.println("Skip unmute action because no permission");
                }
            }
        };
        TimerTask checkPermission = new TimerTask() {
            @Override
            public void run() {
                if(!api.getServerById(args[1]).get().hasPermission(api.getYourself(), PermissionType.MUTE_MEMBERS)){
                    api.updateStatus(UserStatus.DO_NOT_DISTURB);
                    System.out.println("Checked Permission and Update Status(DNS)");
                } else if (api.getStatus() == UserStatus.DO_NOT_DISTURB){
                    if(isMutetime[0]){
                        api.updateStatus(UserStatus.IDLE);
                        System.out.println("Checked Permission and Update Status(IDLE)");
                    } else {
                        api.updateStatus(UserStatus.ONLINE);
                        System.out.println("Checked Permission and Update Status(ONLINE)");
                    }
                }
            }
        };
        //1時ミュート初期設定
        if (hour < 1) {
            timer.scheduleAtFixedRate(muteTask, simpleDateFormat.parse(date + " " + "01:00"), 1000 * 60 * 60 * 24);
        } else {
            timer.scheduleAtFixedRate(muteTask, simpleDateFormat.parse(nextDate + " " + "01:00"), 1000 * 60 * 60 * 24);
        }
        //5時ミュート解除初期設定
        if(hour < 5){
            timer.scheduleAtFixedRate(unmuteTask, simpleDateFormat.parse(date + " " + "05:00"), 1000 * 60 * 60 * 24);
        } else {
            timer.scheduleAtFixedRate(unmuteTask, simpleDateFormat.parse(nextDate + " " + "05:00"), 1000 * 60 * 60 * 24);
        }
        timer.scheduleAtFixedRate(checkPermission, 1000 * 10, 1000 * 10);
        //初期ステータス設定
        if(1 <= hour && hour < 4) {
            api.updateStatus(UserStatus.IDLE);
            isMutetime[0] = true;
        } else if (!api.getServerById(args[1]).get().hasPermission(api.getYourself(), PermissionType.MUTE_MEMBERS)){
            api.updateStatus(UserStatus.DO_NOT_DISTURB);
        }
    }

}
