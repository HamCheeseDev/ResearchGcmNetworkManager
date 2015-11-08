package jp.yskwtnb.researchgcmnetworkmanager.utils;

public final class Objects {

    private Objects() {
    }

    @SuppressWarnings("unchecked")
    public static <T> T cast(final Object object) {
        return (T) object;
    }
}
