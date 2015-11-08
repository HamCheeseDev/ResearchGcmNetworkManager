package jp.yskwtnb.researchgcmnetworkmanager.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;

public final class ResUtils {

    private ResUtils() {
    }

    public static String getString(@NonNull final Context context, @StringRes final int resId,
                                   final Object... formatArgs) {
        return context.getResources().getString(resId, formatArgs);
    }
}
