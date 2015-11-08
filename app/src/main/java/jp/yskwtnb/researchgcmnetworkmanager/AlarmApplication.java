package jp.yskwtnb.researchgcmnetworkmanager;

import android.app.Application;

import com.raizlabs.android.dbflow.config.FlowManager;

public class AlarmApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        FlowManager.init(this);
    }
}
