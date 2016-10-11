package org.ancode.alivelib.base;


import android.content.SharedPreferences;

public abstract class BaseSPUtils {


    protected abstract SharedPreferences getSharedPreferences();

    protected boolean isFirstTime(String key) {
        if (getBoolean(key, false)) {
            return false;
        } else {
            putBoolean(key, true);
            return true;
        }
    }

    protected boolean contains(String key) {
        return getSharedPreferences().contains(key);
    }

    protected int getInt(final String key, final int defaultValue) {
        return getSharedPreferences().getInt(key, defaultValue);
    }

    protected boolean putInt(final String key, final int pValue) {
        final SharedPreferences.Editor editor = getSharedPreferences().edit();

        editor.putInt(key, pValue);

        return editor.commit();
    }

    protected long getLong(final String key, final long defaultValue) {
        return getSharedPreferences().getLong(key, defaultValue);
    }

    protected Long getLong(final String key, final Long defaultValue) {
        if (getSharedPreferences().contains(key)) {
            return getSharedPreferences().getLong(key, 0);
        } else {
            return null;
        }
    }


    protected boolean putLong(final String key, final long pValue) {
        final SharedPreferences.Editor editor = getSharedPreferences().edit();

        editor.putLong(key, pValue);

        return editor.commit();
    }

    protected boolean getBoolean(final String key, final boolean defaultValue) {
        return getSharedPreferences().getBoolean(key, defaultValue);
    }

    protected boolean putBoolean(final String key, final boolean pValue) {
        final SharedPreferences.Editor editor = getSharedPreferences().edit();

        editor.putBoolean(key, pValue);

        return editor.commit();
    }

    protected String getString(final String key, final String defaultValue) {
        return getSharedPreferences().getString(key, defaultValue);
    }

    protected boolean putString(final String key, final String pValue) {
        final SharedPreferences.Editor editor = getSharedPreferences().edit();

        editor.putString(key, pValue);
        return editor.commit();
    }


    protected float getFloat(final String key, final float defaultValue) {
        return getSharedPreferences().getFloat(key, defaultValue);
    }

    protected boolean putFloat(final String key, final float pValue) {
        final SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.putFloat(key, pValue);
        return editor.commit();
    }


    protected boolean remove(final String key) {
        final SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.remove(key);
        return editor.commit();
    }


}