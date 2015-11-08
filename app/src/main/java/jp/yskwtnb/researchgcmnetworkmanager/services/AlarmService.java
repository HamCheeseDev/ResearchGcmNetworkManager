package jp.yskwtnb.researchgcmnetworkmanager.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.gcm.GcmNetworkManager;
import com.google.android.gms.gcm.GcmTaskService;
import com.google.android.gms.gcm.OneoffTask;
import com.google.android.gms.gcm.Task;
import com.google.android.gms.gcm.TaskParams;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import jp.yskwtnb.researchgcmnetworkmanager.R;
import jp.yskwtnb.researchgcmnetworkmanager.activities.AlarmListActivity;
import jp.yskwtnb.researchgcmnetworkmanager.database.Alarm;
import jp.yskwtnb.researchgcmnetworkmanager.database.AlarmHelper;
import jp.yskwtnb.researchgcmnetworkmanager.utils.Objects;

public class AlarmService extends GcmTaskService {

    @SuppressWarnings("unused")
    private static final String TAG = AlarmService.class.getSimpleName();

    private static final String TAG_FORMAT = "alarm-%d";

    public static void registerAlarm(final Context context, final Alarm alarm) {

        final Date nowDate = new Date();
        final long startSec = TimeUnit.MILLISECONDS.toSeconds(
                Math.max(0, alarm.getFireDate().getTime() - nowDate.getTime()));
        final long endSec = startSec + 10L;

        final Calendar c = Calendar.getInstance();

        Log.e(TAG, "registerAlarm");
        Log.e(TAG, "startSec:" + startSec + " endSec:" + endSec);
        c.setTime(alarm.getFireDate());
        Log.e(TAG, "fire date:" + alarm.getFireDate().getTime() + " # " + c.toString());
        c.setTime(nowDate);
        Log.e(TAG, "now:" + nowDate.getTime() + " # " + c.toString());
        Log.e(TAG, "id:" + alarm.getId());

        final OneoffTask task = new OneoffTask.Builder()
                .setService(AlarmService.class)
                .setTag(alarmTag(alarm.getId()))
                .setExecutionWindow(startSec, endSec)
                .setRequiredNetwork(Task.NETWORK_STATE_ANY)
                .setRequiresCharging(false)
                .setPersisted(true)
                .setExtras(alarm.toBundle())
                .build();

        unregisterAlarm(context, alarm.getId());
        GcmNetworkManager.getInstance(context).schedule(task);
    }

    public static void unregisterAlarm(final Context context, final long alarmId) {
        GcmNetworkManager.getInstance(context).cancelTask(alarmTag(alarmId), AlarmService.class);
    }

    @Override
    public void onInitializeTasks() {
        super.onInitializeTasks();

        // When Google Play Services or the client app is updated, all scheduled tasks will be removed.
        // GcmNetworkManager invokes the client app’s onInitializeTasks().
        // Override this function to reschedule necessary tasks.

        Log.e(TAG, "onInitializeTasks");

        List<Alarm> list = AlarmHelper.loadAll();
        for (final Alarm alarm : list) {
            registerAlarm(this, alarm);
        }
    }

    @Override
    public int onRunTask(final TaskParams taskParams) {

        Log.e(TAG, "onRunTask");

        final Alarm alarm = Alarm.fromBundle(taskParams.getExtras());
        Log.e(TAG, "alarm id:" + alarm.getId());
        if (!AlarmHelper.exists(alarm.getId())) {
            return GcmNetworkManager.RESULT_SUCCESS;
        }

        final Intent intent = new Intent(this, AlarmListActivity.class);
        final PendingIntent pendingIntent = PendingIntent.getActivity(
                this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        final android.support.v4.app.NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setContentIntent(pendingIntent)
                .setContentTitle(getString(R.string.app_name))
                .setContentText(alarm.getNote())
                .setTicker(alarm.getNote())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setWhen(System.currentTimeMillis())
                .setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_SOUND
                        | Notification.DEFAULT_VIBRATE
                        | Notification.DEFAULT_LIGHTS);

        final NotificationManager manager = Objects.cast(getSystemService(Service.NOTIFICATION_SERVICE));
        manager.notify((int) alarm.getId(), builder.build());

        AlarmHelper.delete(this, alarm.getId());

        return GcmNetworkManager.RESULT_SUCCESS;
    }

    private static String alarmTag(final long id) {
        return String.format(Locale.US, TAG_FORMAT, id);
    }
}
