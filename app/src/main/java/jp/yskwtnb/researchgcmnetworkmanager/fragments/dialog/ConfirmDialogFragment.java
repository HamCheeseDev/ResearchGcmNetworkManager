package jp.yskwtnb.researchgcmnetworkmanager.fragments.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;

import jp.yskwtnb.researchgcmnetworkmanager.R;
import jp.yskwtnb.researchgcmnetworkmanager.utils.Objects;

public class ConfirmDialogFragment extends DialogFragment
        implements DialogInterface.OnClickListener {

    private static final String TAG = ConfirmDialogFragment.class.getSimpleName();
    private static final String KEY_MESSAGE = "message";

    private Callback mCallback;

    public static void show(final Fragment fragment, final String message) {

        final ConfirmDialogFragment confirmDialogFragment = new ConfirmDialogFragment();

        final Bundle args = new Bundle();
        args.putString(KEY_MESSAGE, message);
        confirmDialogFragment.setArguments(args);
        confirmDialogFragment.setTargetFragment(fragment, 0);

        confirmDialogFragment.show(fragment.getFragmentManager(), TAG);
    }

    @Override
    public void onAttach(final Activity activity) {
        super.onAttach(activity);

        if (getTargetFragment() instanceof Callback) {
            mCallback = Objects.cast(getTargetFragment());
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {

        final String message = getArguments().getString(KEY_MESSAGE);

        return new AlertDialog.Builder(getActivity())
                .setTitle(R.string.confirm)
                .setMessage(message)
                .setPositiveButton(android.R.string.yes, this)
                .setNegativeButton(android.R.string.no, this)
                .create();
    }

    @Override
    public void onClick(final DialogInterface dialog, final int which) {

        if (mCallback != null) {
            mCallback.onClick(which);
        }
    }

    public interface Callback {
        void onClick(final int which);
    }
}
