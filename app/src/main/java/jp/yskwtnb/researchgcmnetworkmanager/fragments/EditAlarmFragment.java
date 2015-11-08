package jp.yskwtnb.researchgcmnetworkmanager.fragments;


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Calendar;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.yskwtnb.researchgcmnetworkmanager.R;
import jp.yskwtnb.researchgcmnetworkmanager.database.Alarm;
import jp.yskwtnb.researchgcmnetworkmanager.database.AlarmHelper;
import jp.yskwtnb.researchgcmnetworkmanager.fragments.dialog.ConfirmDialogFragment;
import jp.yskwtnb.researchgcmnetworkmanager.fragments.dialog.DatePickerFragment;
import jp.yskwtnb.researchgcmnetworkmanager.fragments.dialog.TimePickerFragment;
import jp.yskwtnb.researchgcmnetworkmanager.utils.ResUtils;

public class EditAlarmFragment extends Fragment
        implements DatePickerFragment.Callback, TimePickerFragment.Callback,
        ConfirmDialogFragment.Callback {

    private static final String KEY_ALARM_ID = "alarm_id";

    private int mYear;
    private int mMonth;
    private int mDay;
    private int mHour;
    private int mMinute;

    @Bind(R.id.text_date)
    TextView mTextDate;

    @Bind(R.id.edit_note)
    EditText mEditNote;

    @Bind(R.id.text_input_layout)
    TextInputLayout mTextInputLayout;

    @Bind(R.id.button_edit)
    Button mButtonEdit;

    @Bind(R.id.button_delete)
    Button mButtonDelete;

    public static EditAlarmFragment createRegisterFragment() {
        return createUpdateFragment(Alarm.ID_NONE);
    }

    public static EditAlarmFragment createUpdateFragment(final long alarmId) {
        final EditAlarmFragment editAlarmFragment = new EditAlarmFragment();

        final Bundle args = new Bundle();
        args.putLong(KEY_ALARM_ID, alarmId);
        editAlarmFragment.setArguments(args);

        return editAlarmFragment;
    }

    public EditAlarmFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_edit_alarm, container, false);
        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onViewCreated(final View view, final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final long alarmId = getArguments().getLong(KEY_ALARM_ID, Alarm.ID_NONE);
        final Alarm alarm = (alarmId != Alarm.ID_NONE) ? AlarmHelper.get(alarmId) : null;

        final Calendar c = Calendar.getInstance();
        if (alarm == null) {

            mButtonEdit.setText(R.string.register);
            mButtonDelete.setVisibility(View.GONE);

        } else {
            c.setTime(alarm.getFireDate());

            mEditNote.setText(alarm.getNote());

            mButtonEdit.setText(R.string.update);
            mButtonDelete.setVisibility(View.VISIBLE);
        }

        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);

        setTextDate();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick(R.id.button_set_date)
    void onClickSetDateButton() {
        DatePickerFragment.show(this, mYear, mMonth, mDay);
    }


    @Override
    public void onDateSet(final int year, final int month, final int day) {

        mYear = year;
        mMonth = month;
        mDay = day;

        setTextDate();

        TimePickerFragment.show(this, mHour, mMinute);
    }

    @Override
    public void onTimeSet(final int hourOfDay, final int minute) {
        mHour = hourOfDay;
        mMinute = minute;

        setTextDate();
    }

    @OnClick(R.id.button_edit)
    void onClickEditButton() {

        final Calendar target = Calendar.getInstance();
        target.set(mYear, mMonth, mDay, mHour, mMinute);

        if (TextUtils.isEmpty(mEditNote.getText().toString())) {
            mTextInputLayout.setError(getString(R.string.error_empty_note));
            mTextInputLayout.setErrorEnabled(true);
            return;
        }

        AlarmHelper.insertOrUpdate(getActivity(),
                getArguments().getLong(KEY_ALARM_ID, Alarm.ID_NONE),
                target.getTime(), mEditNote.getText().toString());

        getActivity().finish();
    }

    @OnClick(R.id.button_delete)
    void onClickDeleteButton() {
        ConfirmDialogFragment.show(this, getString(R.string.confirm_delete_alarm));
    }

    @Override
    public void onClick(final int which) {
        if (which == DialogInterface.BUTTON_POSITIVE) {
            AlarmHelper.delete(getActivity(), getArguments().getLong(KEY_ALARM_ID, Alarm.ID_NONE));
            getActivity().finish();
        }
    }

    private void setTextDate() {
        mTextDate.setText(ResUtils.getString(
                getActivity(), R.string.format_date, mYear, (mMonth + 1), mDay, mHour, mMinute));
    }
}
