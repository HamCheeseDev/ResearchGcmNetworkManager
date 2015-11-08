package jp.yskwtnb.researchgcmnetworkmanager.fragments.dialog;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.widget.DatePicker;

import java.util.Calendar;

import jp.yskwtnb.researchgcmnetworkmanager.utils.Objects;

public class DatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {

    private static final String TAG = DatePickerFragment.class.getSimpleName();

    private static final String KEY_YEAR = "year";
    private static final String KEY_MONTH = "month";
    private static final String KEY_DAY = "day";

    private Callback mCallback;

    public static void show(final Fragment f, final int year, final int month, final int day) {
        final DatePickerFragment fragment = new DatePickerFragment();
        fragment.setTargetFragment(f, 0);

        final Bundle args = new Bundle();
        args.putInt(KEY_YEAR, year);
        args.putInt(KEY_MONTH, month);
        args.putInt(KEY_DAY, day);
        fragment.setArguments(args);

        fragment.show(f.getActivity().getSupportFragmentManager(), DatePickerFragment.TAG);
    }

    @Override
    public void onAttach(final Activity activity) {
        super.onAttach(activity);

        final Fragment fragment = getTargetFragment();
        if (fragment instanceof Callback) {
            mCallback = Objects.cast(fragment);
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {

        final Calendar c = Calendar.getInstance();
        final Bundle args = getArguments();

        int year = args.getInt(KEY_YEAR, c.get(Calendar.YEAR));
        int month = args.getInt(KEY_MONTH, c.get(Calendar.MONTH));
        int day = args.getInt(KEY_DAY, c.get(Calendar.DAY_OF_MONTH));

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        if (mCallback != null) {
            mCallback.onDateSet(year, month, day);
        }
    }

    public interface Callback {
        void onDateSet(int year, int month, int day);
    }
}
