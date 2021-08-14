package com.kalambur.mappy_stars.utils.extensions

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.kalambur.mappy_stars.utils.admob.AdmobUtil

fun androidx.appcompat.app.AlertDialog.dismissWithAds(activity: Activity) {
    AdmobUtil.showInterstitialAd(activity)

    this.dismiss()
}

fun Dialog.dismissWithAds(activity: Activity) {
    AdmobUtil.showInterstitialAd(activity)

    this.dismiss()
}