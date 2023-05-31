package com.example.imagesearch.base

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import com.example.imagesearch.R

object Common {
    var prefs: SharedPreferences? = null

    fun setLightMode(context: Context, open: Boolean) {
        val preferences =
            context.getSharedPreferences(context.packageName, Context.MODE_MULTI_PROCESS)
        preferences.edit().putBoolean("KEY_MODE_LIGHT", open).apply()
    }


//    fun logEvent(context: Context, eventName: String) {
//        val firebaseAnalytics = FirebaseAnalytics.getInstance(context)
//        val bundle = Bundle()
//        bundle.putString("onCreateScreeen", context.javaClass.simpleName)
//        firebaseAnalytics.logEvent(eventName + "_" + BuildConfig.VERSION_CODE, bundle)
//        val eventParams: MutableMap<String, String> = HashMap()
//        eventParams["screen"] = context.javaClass.simpleName
//        Log.e("==event", eventName + "_" + BuildConfig.VERSION_CODE)
//    }

    fun getLightMode(context: Context): Boolean {
        val preferences = context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)
        return preferences.getBoolean("KEY_MODE_LIGHT", false)
    }

//    fun setThemeForActivity(activity: Activity) {
//        val lightMode = getLightMode(activity)
//        if (lightMode) {
//            activity.setTheme(R.style.light_mode)
//        } else {
//            activity.setTheme(R.style.dark_mode)
//        }
//    }
    fun setThemeForActivity(activity: Activity) {
        when (getTheme(activity)) {
            1 -> {
                activity.setTheme(R.style.light_mode)
            }
            2 -> {
                activity.setTheme(R.style.dark_mode)
            }
        }
    }

    fun setTheme(context: Context, open: Int) {
        val preferences = context.getSharedPreferences(context.packageName, Context.MODE_MULTI_PROCESS)
        preferences.edit().putInt("KEY_THEME", open).apply()
    }

    fun getTheme(context: Context): Int {
        val preferences = context.getSharedPreferences(context.packageName, Context.MODE_MULTI_PROCESS)
        return preferences.getInt("KEY_THEME", 1)
    }

    fun setMinWidth(context: Context, open: Int) {
        val preferences =
            context.getSharedPreferences(context.packageName, Context.MODE_MULTI_PROCESS)
        preferences.edit().putInt("KEY_MINWIDTH", open).apply()
    }

    fun getMinWidth(mContext: Context): Int {
        val preferences =
            mContext.getSharedPreferences(mContext.packageName, Context.MODE_MULTI_PROCESS)
        return preferences.getInt("KEY_MINWIDTH", 0)
    }

    fun setMinHeight(context: Context, open: Int) {
        val preferences =
            context.getSharedPreferences(context.packageName, Context.MODE_MULTI_PROCESS)
        preferences.edit().putInt("KEY_MINHEIGHT", open).apply()
    }

    fun getMinHeight(mContext: Context): Int {
        val preferences =
            mContext.getSharedPreferences(mContext.packageName, Context.MODE_MULTI_PROCESS)
        return preferences.getInt("KEY_MINHEIGHT", 0)
    }

    fun setColors(context: Context, open: String) {
        val preferences =
            context.getSharedPreferences(context.packageName, Context.MODE_MULTI_PROCESS)
        preferences.edit().putString("KEY_COLOR", open).apply()
    }

    fun getColors(mContext: Context): String {
        val preferences = mContext.getSharedPreferences(mContext.packageName, Context.MODE_MULTI_PROCESS)
        return preferences.getString("KEY_COLOR", "").toString()
    }

    fun getColorInt(mContext: Context): Int {
        val preferences =
            mContext.getSharedPreferences(mContext.packageName, Context.MODE_MULTI_PROCESS)
        return preferences.getInt("KEY_COLORINT", -1)
    }

    fun setColorInt(context: Context, open: Int) {
        val preferences =
            context.getSharedPreferences(context.packageName, Context.MODE_MULTI_PROCESS)
        preferences.edit().putInt("KEY_COLORINT", open).apply()
    }

    fun setCategory(context: Context, open: String) {
        val preferences =
            context.getSharedPreferences(context.packageName, Context.MODE_MULTI_PROCESS)
        preferences.edit().putString("KEY_CATEGORY", open).apply()
    }

    fun getCategory(mContext: Context): String {
        val preferences = mContext.getSharedPreferences(mContext.packageName, Context.MODE_MULTI_PROCESS)
        return preferences.getString("KEY_CATEGORY", "").toString()
    }

    fun setOrientation(context: Context, open: String) {
        val preferences =
            context.getSharedPreferences(context.packageName, Context.MODE_MULTI_PROCESS)
        preferences.edit().putString("KEY_ORIENTATION", open).apply()
    }

    fun getOrientation(mContext: Context): String {
        val preferences = mContext.getSharedPreferences(mContext.packageName, Context.MODE_MULTI_PROCESS)
        return preferences.getString("KEY_ORIENTATION", "all").toString()
    }

    fun setImageType(context: Context, open: String) {
        val preferences =
            context.getSharedPreferences(context.packageName, Context.MODE_MULTI_PROCESS)
        preferences.edit().putString("KEY_IMAGETYPE", open).apply()
    }

    fun getImageType(mContext: Context): String {
        val preferences = mContext.getSharedPreferences(mContext.packageName, Context.MODE_MULTI_PROCESS)
        return preferences.getString("KEY_IMAGETYPE", "all").toString()
    }
    fun setOrder(context: Context, open: String) {
        val preferences =
            context.getSharedPreferences(context.packageName, Context.MODE_MULTI_PROCESS)
        preferences.edit().putString("KEY_ORDER", open).apply()
    }

    fun getOrder(mContext: Context): String {
        val preferences = mContext.getSharedPreferences(mContext.packageName, Context.MODE_MULTI_PROCESS)
        return preferences.getString("KEY_ORDER", "").toString()
    }

    fun setPerMission(context: Context, open: Boolean) {
        val preferences =
            context.getSharedPreferences(context.packageName, Context.MODE_MULTI_PROCESS)
        preferences.edit().putBoolean("KEY_PERMISSION", open).apply()
    }

    fun getPerMission(context: Context): Boolean {
        val preferences = context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)
        return preferences.getBoolean("KEY_PERMISSION", false)
    }

}