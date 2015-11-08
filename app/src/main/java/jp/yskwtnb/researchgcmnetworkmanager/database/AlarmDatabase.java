package jp.yskwtnb.researchgcmnetworkmanager.database;

import com.raizlabs.android.dbflow.annotation.Database;

@Database(name = AlarmDatabase.NAME, version = AlarmDatabase.VERSION)
final class AlarmDatabase {

    public static final String NAME = "AlarmDatabase";

    public static final int VERSION = 1;

    private AlarmDatabase() {
    }
}
