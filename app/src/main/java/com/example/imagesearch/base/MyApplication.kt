package com.example.imagesearch.base

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import com.example.imagesearch.R
import java.util.*

class MyApplication : Application(), Application.ActivityLifecycleCallbacks{
    override fun onCreate() {
        super.onCreate()
        registerActivityLifecycleCallbacks(this)

    }

    override fun onActivityCreated(activity: Activity, p1: Bundle?) {
        activity.setLocale(activity, activity.getPreLanguage(activity))
    }

    override fun onActivityStarted(p0: Activity) {

    }

    override fun onActivityResumed(p0: Activity) {

    }

    override fun onActivityPaused(p0: Activity) {

    }

    override fun onActivityStopped(p0: Activity) {

    }

    override fun onActivitySaveInstanceState(p0: Activity, p1: Bundle) {
    }

    override fun onActivityDestroyed(p0: Activity) {

    }
}

fun Context?.getPreLanguage(mContext: Context): String? {
    val preferences = mContext.getSharedPreferences(
        mContext.packageName,
        Application.MODE_MULTI_PROCESS
    )
    return preferences.getString("KEY_LANGUAGE", "en")
}

fun Context?.setPreLanguage(context: Context, language: String?) {
    if (TextUtils.isEmpty(language)) return
    val preferences = context.getSharedPreferences(
        context.packageName,
        Application.MODE_MULTI_PROCESS
    )
    preferences.edit().putString("KEY_LANGUAGE", language).apply()
}

fun Context?.getPreLanguageName(mContext: Context): String? {
    val preferences = mContext.getSharedPreferences(
        mContext.packageName,
        Application.MODE_MULTI_PROCESS
    )
    return preferences.getString("KEY_LANGUAGE_NAME", "English")
}

fun Context?.setPreLanguageName(context: Context, language: String?) {
    if (TextUtils.isEmpty(language)) return
    val preferences = context.getSharedPreferences(
        context.packageName,
        Application.MODE_MULTI_PROCESS
    )
    preferences.edit().putString("KEY_LANGUAGE_NAME", language).apply()
}

fun Context?.getPreLanguageflag(mContext: Context): Int {
    val preferences = mContext.getSharedPreferences(
        mContext.packageName,
        Application.MODE_MULTI_PROCESS
    )
    return preferences.getInt("KEY_FLAG", R.drawable.ic_english)
}

fun Context?.setPreLanguageflag(context: Context, flag: Int) {
    val preferences = context.getSharedPreferences(
        context.packageName,
        Application.MODE_MULTI_PROCESS
    )
    preferences.edit().putInt("KEY_FLAG", flag).apply()
}



fun Context?.setPosition(context: Context, open: Int) {
    val preferences = context.getSharedPreferences(
        context.packageName,
        Application.MODE_MULTI_PROCESS
    )
    preferences.edit().putInt("KEY_POSITION", open).apply()
}

fun Context?.getPosition(mContext: Context): Int {
    val preferences = mContext.getSharedPreferences(
        mContext.packageName,
        Application.MODE_MULTI_PROCESS
    )
    return preferences.getInt("KEY_POSITION", 0)
}

fun Context?.getFirtOpen(mContext: Context): Boolean {
    val preferences = mContext.getSharedPreferences(
        mContext.packageName,
        Application.MODE_MULTI_PROCESS
    )
    return preferences.getBoolean("KEY_OPEN", true)
}
fun Context?.setFirtOpen(context: Context, open: Boolean) {
    val preferences = context.getSharedPreferences(
        context.packageName,
        Application.MODE_MULTI_PROCESS
    )
    preferences.edit().putBoolean("KEY_OPEN", open).apply()
}
fun Context?.getFirtOpen2(mContext: Context): Boolean {
    val preferences = mContext.getSharedPreferences(
        mContext.packageName,
        Application.MODE_MULTI_PROCESS
    )
    return preferences.getBoolean("KEY_OPEN2", true)
}
fun Context?.setFirtOpen2(context: Context, open: Boolean) {
    val preferences = context.getSharedPreferences(
        context.packageName,
        Application.MODE_MULTI_PROCESS
    )
    preferences.edit().putBoolean("KEY_OPEN2", open).apply()
}
fun Context?.getFirtOpen3(mContext: Context): Boolean {
    val preferences = mContext.getSharedPreferences(
        mContext.packageName,
        Application.MODE_MULTI_PROCESS
    )
    return preferences.getBoolean("KEY_OPEN3", true)
}
fun Context?.setFirtOpen3(context: Context, open: Boolean) {
    val preferences = context.getSharedPreferences(
        context.packageName,
        Application.MODE_MULTI_PROCESS
    )
    preferences.edit().putBoolean("KEY_OPEN3", open).apply()
}


fun Context?.setLocale(context: Context, lang: String?) {
    val myLocale = Locale(lang)
    val res = context.resources
    val dm = res.displayMetrics
    val conf = res.configuration
    conf.locale = myLocale
    res.updateConfiguration(conf, dm)
    context.setPreLanguage(context, lang)
}

fun View.gone() {
    visibility = View.GONE
}

fun View.visible() {
    visibility = View.VISIBLE
}