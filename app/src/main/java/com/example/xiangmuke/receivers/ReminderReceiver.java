package com.example.xiangmuke.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;

import com.example.xiangmuke.models.Memo;

public class ReminderReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Memo memo = intent.getParcelableExtra("memo");
        if (memo != null) {
            Log.d("ReminderReceiver", "Received reminder for: " + memo.getTitle());

            // 获取震动服务
            Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);

            if (vibrator != null) {
                // 检查震动服务是否正常可用
                Log.d("ReminderReceiver", "Vibrator service available: " + (vibrator != null));

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    // Android 8.0 及以上使用振动模式
                    VibrationEffect effect = VibrationEffect.createWaveform(new long[]{0, 1000, 1000, 1000}, -1);
                    vibrator.vibrate(effect);
                    Log.d("ReminderReceiver", "Vibration triggered for: " + memo.getTitle());
                } else {
                    // Android 8.0 以下使用普通震动
                    vibrator.vibrate(1000); // 震动 1 秒
                    Log.d("ReminderReceiver", "Vibration triggered (legacy) for: " + memo.getTitle());
                }
            } else {
                Log.e("ReminderReceiver", "Vibrator service not available");
            }
        } else {
            Log.e("ReminderReceiver", "Memo is null");
        }
    }
}
