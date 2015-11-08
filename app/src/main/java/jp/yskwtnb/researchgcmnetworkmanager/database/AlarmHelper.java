package jp.yskwtnb.researchgcmnetworkmanager.database;

import android.content.Context;

import com.raizlabs.android.dbflow.sql.builder.Condition;
import com.raizlabs.android.dbflow.sql.language.Delete;
import com.raizlabs.android.dbflow.sql.language.Select;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jp.yskwtnb.researchgcmnetworkmanager.services.AlarmService;

public final class AlarmHelper {

    private AlarmHelper() {
    }

    public static List<Alarm> loadAll() {
        final List<Alarm> list = new Select().from(Alarm.class).queryList();
        return (list != null) ? list : new ArrayList<Alarm>();
    }

    public static Alarm get(final long alarmId) {

        return new Select().from(Alarm.class)
                .where(Condition.column(Alarm$Table.ID).eq(alarmId))
                .querySingle();
    }

    private static void add(final Context context, final Date fireDate, final String note) {

        final Alarm alarm = new Alarm();
        alarm.fireDate = fireDate;
        alarm.note = note;
        alarm.update();     // insert if not exist.

        AlarmService.registerAlarm(context, alarm);
    }

    public static void insertOrUpdate(final Context context, final long alarmId,
                                      final Date fireDate, final String note) {

        if (alarmId == Alarm.ID_NONE) {
            add(context, fireDate, note);
        } else {
            AlarmService.unregisterAlarm(context, alarmId);

            final Alarm alarm = get(alarmId);
            if (alarm == null) {
                add(context, fireDate, note);
            } else {
                alarm.fireDate = fireDate;
                alarm.note = note;
                alarm.update();     // insert if not exist.

                AlarmService.registerAlarm(context, alarm);
            }
        }
    }

    public static void delete(final Context context, final long alarmId) {
        new Delete().from(Alarm.class)
                .where(Condition.column(Alarm$Table.ID).eq(alarmId))
                .queryClose();

        AlarmService.unregisterAlarm(context, alarmId);
    }

    public static boolean exists(final long alarmId) {
        return get(alarmId) != null;
    }
}
