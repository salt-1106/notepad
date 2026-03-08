package com.example.xiangmuke.receivers;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.example.xiangmuke.models.Memo;

import java.util.Calendar;

public class ReminderUtil {
    public static void setReminder(Context context, long reminderTimeInMillis, int requestCode, Memo memo) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            Intent intent = new Intent(context, ReminderReceiver.class);
            // 传递 Memo 对象
            intent.putExtra("memo", memo);

            // 使用 FLAG_IMMUTABLE 或 FLAG_MUTABLE（API 31+）
            PendingIntent pendingIntent = PendingIntent.getBroadcast(
                    context,
                    requestCode,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
            );

            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(reminderTimeInMillis);

            // 根据安卓版本选择合适的闹钟设置方式
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                // 在 Android 6.0 (API 23) 及以上，使用精确闹钟并允许在 Doze 模式下唤醒
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, reminderTimeInMillis, pendingIntent);
            } else {
                // 低于 Android 6.0 时使用普通的 setExact()
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, reminderTimeInMillis, pendingIntent);
            }

            // 格式化时间为可读形式
            Log.d("ReminderUtil", "Reminder set for: " + memo.getTitle() + " at " + calendar.getTime().toString());
        } else {
            Log.e("ReminderUtil", "AlarmManager service not available");
        }
    }
}
