package jp.yskwtnb.researchgcmnetworkmanager.fragments.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;

import jp.yskwtnb.researchgcmnetworkmanager.R;

public class ErrorDialogFragment extends DialogFragment {

    private static final String TAG = ErrorDialogFragment.class.getSimpleName();

    private static final String KEY_MESSAGE = "message";

    public static void show(final Fragment fragment, final String message) {

        final ErrorDialogFragment errorDialogFragment = new ErrorDialogFragment();

        final Bundle args = new Bundle();
        args.putString(KEY_MESSAGE, message);
        errorDialogFragment.setArguments(args);

        errorDialogFragment.show(fragment.getFragmentManager(), TAG);
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {

        final String message = getArguments().getString(KEY_MESSAGE);

        return new AlertDialog.Builder(getActivity())
                .setTitle(R.string.error)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, null)
                .create();
    }
}
