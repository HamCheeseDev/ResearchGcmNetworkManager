package jp.yskwtnb.researchgcmnetworkmanager.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import jp.yskwtnb.researchgcmnetworkmanager.database.Alarm;
import jp.yskwtnb.researchgcmnetworkmanager.fragments.EditAlarmFragment;

public class EditAlarmActivity extends AppCompatActivity {

    private static final String KEY_ALARM_ID = "alarm_id";

    public static void start(final Context context, final long alarmId) {

        final Intent intent = new Intent(context, EditAlarmActivity.class);
        intent.putExtra(KEY_ALARM_ID, alarmId);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {

            final long alarmId = getIntent().getLongExtra(KEY_ALARM_ID, Alarm.ID_NONE);
            getSupportFragmentManager().beginTransaction()
                    .add(android.R.id.content, EditAlarmFragment.createUpdateFragment(alarmId))
                    .commit();
        }
    }
}
