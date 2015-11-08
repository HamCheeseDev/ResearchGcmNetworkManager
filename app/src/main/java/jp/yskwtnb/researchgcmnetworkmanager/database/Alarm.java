package jp.yskwtnb.researchgcmnetworkmanager.database;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import java.util.Date;

@Table(databaseName = AlarmDatabase.NAME)
public class Alarm extends BaseModel {

    public static final long ID_NONE = 0;

    private static final String KEY_ID = "id";
    private static final String KEY_FIRE_DATE = "fire_date";
    private static final String KEY_NOTE = "note";

    @Column
    @PrimaryKey(autoincrement = true)
    long id;

    @Column
    Date fireDate;

    @Column
    String note;

    public long getId() {
        return id;
    }

    public Date getFireDate() {
        return fireDate;
    }

    public String getNote() {
        return note;
    }

    public Bundle toBundle() {

        final Bundle bundle = new Bundle();
        bundle.putLong(KEY_ID, id);
        bundle.putLong(KEY_FIRE_DATE, fireDate.getTime());
        bundle.putString(KEY_NOTE, note);

        return bundle;
    }

    public static Alarm fromBundle(@NonNull final Bundle bundle) {

        final Alarm alarm = new Alarm();
        alarm.id = bundle.getLong(KEY_ID);
        alarm.fireDate = new Date(bundle.getLong(KEY_FIRE_DATE));
        alarm.note = bundle.getString(KEY_NOTE);

        return alarm;
    }
}
