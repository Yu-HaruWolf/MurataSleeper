package com.github.yukkidra.muratasleeper;

import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.managers.Presence;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

public class ActivityUpdater {
    private final Presence presence;

    // variables for time update
    private Calendar calendar;
    SimpleDateFormat hourFormat;
    SimpleDateFormat minuteFormat;
    Timer timer;

    public ActivityUpdater(Presence presence){
        this.presence = presence;
        this.hourFormat = new SimpleDateFormat("HH");
        this.minuteFormat = new SimpleDateFormat("mm");
        this.timer = new Timer(false);
        timeUpdate();
    }

    public void setActivity(String content){
        presence.setActivity(Activity.playing(content));
    }

    private void timeUpdate(){
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                calendar = Calendar.getInstance();
                int hour = Integer.parseInt(hourFormat.format(calendar.getTime()));
                int minute = Integer.parseInt(minuteFormat.format(calendar.getTime()));
                int remainWholeMinute = 1440 - hour * 60 - minute;
                int remainHour = remainWholeMinute / 60;
                int remainMinute = remainWholeMinute % 60;
                String remainMinuteS = String.valueOf(remainMinute);
                if(remainMinute < 10){
                    remainMinuteS = "0" + remainMinute;
                }
                if(remainHour == 23){
                    setActivity("あと" + remainMinute + "分でミュート");
                } else {
                    setActivity("今日は残り" + remainHour + "時間" + remainMinuteS + "分");
                }
            }
        };
        SimpleDateFormat secondFormat = new SimpleDateFormat("ss");
        task.run();
        calendar = Calendar.getInstance();
        timer.scheduleAtFixedRate(task,60 - Integer.parseInt(secondFormat.format(calendar.getTime())),1000 * 60);
    }
}
