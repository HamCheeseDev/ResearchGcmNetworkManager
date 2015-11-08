package jp.yskwtnb.researchgcmnetworkmanager.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import butterknife.ButterKnife;
import jp.yskwtnb.researchgcmnetworkmanager.R;
import jp.yskwtnb.researchgcmnetworkmanager.database.Alarm;
import jp.yskwtnb.researchgcmnetworkmanager.fragments.AlarmListFragment;

public class AlarmListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        final Toolbar toolbar = ButterKnife.findById(this, R.id.toolbar);
        setSupportActionBar(toolbar);

        final FloatingActionButton fab = ButterKnife.findById(this, R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditAlarmActivity.start(AlarmListActivity.this, Alarm.ID_NONE);
            }
        });

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.content, AlarmListFragment.newInstance())
                    .commit();
        }
    }
}
