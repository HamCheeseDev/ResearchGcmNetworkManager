package jp.yskwtnb.researchgcmnetworkmanager.fragments.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.widget.TimePicker;

import java.util.Calendar;

import jp.yskwtnb.researchgcmnetworkmanager.utils.Objects;

public class TimePickerFragment extends DialogFragment
        implements TimePickerDialog.OnTimeSetListener {

    private static final String TAG = TimePickerFragment.class.getSimpleName();

    private static final String KEY_HOUR = "hour";
    private static final String KEY_MINUTE = "minute";

    private Callback mCallback;

    public static void show(final Fragment f, final int hour, final int minute) {
        final TimePickerFragment fragment = new TimePickerFragment();
        fragment.setTargetFragment(f, 0);

        final Bundle args = new Bundle();
        args.putInt(KEY_HOUR, hour);
        args.putInt(KEY_MINUTE, minute);
        fragment.setArguments(args);

        fragment.show(f.getActivity().getSupportFragmentManager(), TimePickerFragment.TAG);
    }

    @Override
    public void onAttach(final Activity activity) {
        super.onAttach(activity);

        final Fragment fragment = getTargetFragment();
        if (fragment instanceof Callback) {
            mCallback = Objects.cast(fragment);
        }
    }

    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {

        final Calendar c = Calendar.getInstance();

        final Bundle args = getArguments();
        final int hour = args.getInt(KEY_HOUR, c.get(Calendar.HOUR_OF_DAY));
        final int minute = args.getInt(KEY_MINUTE, c.get(Calendar.MINUTE));

        return new TimePickerDialog(getActivity(), this, hour, minute, true);
    }

    @Override
    public void onTimeSet(final TimePicker view, final int hourOfDay, final int minute) {
        if (mCallback != null) {
            mCallback.onTimeSet(hourOfDay, minute);
        }
    }


    public interface Callback {
        void onTimeSet(final int hourOfDay, final int minute);
    }
}
