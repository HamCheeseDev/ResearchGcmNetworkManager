package jp.yskwtnb.researchgcmnetworkmanager.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import jp.yskwtnb.researchgcmnetworkmanager.R;
import jp.yskwtnb.researchgcmnetworkmanager.activities.EditAlarmActivity;
import jp.yskwtnb.researchgcmnetworkmanager.database.Alarm;
import jp.yskwtnb.researchgcmnetworkmanager.database.AlarmHelper;
import jp.yskwtnb.researchgcmnetworkmanager.fragments.dialog.ErrorDialogFragment;
import jp.yskwtnb.researchgcmnetworkmanager.utils.Objects;
import jp.yskwtnb.researchgcmnetworkmanager.utils.ResUtils;


public class AlarmListFragment extends Fragment {

    private final AlarmAdapter mAlarmAdapter;

    public static AlarmListFragment newInstance() {
        return new AlarmListFragment();
    }

    public AlarmListFragment() {
        mAlarmAdapter = new AlarmAdapter();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_alarm_list, container, false);
    }

    @Override
    public void onViewCreated(final View view, final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final SwipeRefreshLayout layout = ButterKnife.findById(view, R.id.swipe_refresh);
        layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mAlarmAdapter.reload();
                if (layout.isRefreshing()) {
                    layout.setRefreshing(false);
                }
            }
        });

        final ListView listView = ButterKnife.findById(view, android.R.id.list);
        listView.setAdapter(mAlarmAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> parent, final View view,
                                    final int position, final long id) {

                final long alarmId = Objects.<Alarm>cast(mAlarmAdapter.getItem(position)).getId();
                if (!AlarmHelper.exists(alarmId)) {

                    ErrorDialogFragment.show(
                            AlarmListFragment.this, getString(R.string.error_already_deleted));
                    return;
                }

                EditAlarmActivity.start(getActivity(), alarmId);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        mAlarmAdapter.reload();
    }

    private static class AlarmAdapter extends BaseAdapter {

        private List<Alarm> mAlarmList;

        public AlarmAdapter() {
            loadFromDatabase();
        }

        private void loadFromDatabase() {
            mAlarmList = AlarmHelper.loadAll();
        }

        @Override
        public int getCount() {
            return mAlarmList.size();
        }

        @Override
        public Object getItem(final int position) {
            return mAlarmList.get(position);
        }

        @Override
        public long getItemId(final int position) {
            return mAlarmList.get(position).getId();
        }

        @Override
        public View getView(final int position, final View convertView, final ViewGroup parent) {

            final ViewHolder viewHolder;
            View view = convertView;
            if (view == null) {

                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.list_item_alarm, parent, false);

                viewHolder = new ViewHolder(view);
                view.setTag(viewHolder);

            } else {
                viewHolder = ((ViewHolder) view.getTag());
            }

            viewHolder.setAlarm(mAlarmList.get(position));
            return view;
        }

        public void reload() {
            loadFromDatabase();
            notifyDataSetChanged();
        }
    }

    static class ViewHolder {

        @Bind(R.id.text_id)
        TextView mTextId;

        @Bind(R.id.text_fire_date)
        TextView mTextFireDate;

        @Bind(R.id.text_note)
        TextView mTextNote;

        public ViewHolder(@NonNull final View view) {
            ButterKnife.bind(this, view);
        }

        public void setAlarm(@NonNull final Alarm alarm) {

            final Context ctx = mTextId.getContext();
            final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US);
            mTextId.setText(ResUtils.getString(ctx, R.string.format_alarm_id, alarm.getId()));
            mTextFireDate.setText(
                    ResUtils.getString(ctx, R.string.format_alarm_fire_date, sdf.format(alarm.getFireDate())));
            mTextNote.setText(alarm.getNote());
        }
    }
}
