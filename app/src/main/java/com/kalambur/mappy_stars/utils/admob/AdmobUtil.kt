package com.kalambur.mappy_stars.utils.admob

import android.app.Activity
import android.content.Context
import android.util.Log
import com.kalambur.mappy_stars.R
import com.google.android.gms.ads.*
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdOptions
import com.google.android.gms.ads.nativead.NativeAdView
import java.util.*

object AdmobUtil {
    private var mInterstitialAd: InterstitialAd? = null
//    private lateinit var mNativeLoader: AdLoader
    private var TAG = "Admob"
//    private var activity: Activity? = null
    private var timeLastShowInterstitialAd = Calendar.getInstance().time.time

    fun loadAdmobInterstitialAd(context: Context) {
        val adRequest = AdRequest.Builder().build()

        // Test id: ca-app-pub-3940256099942544/1033173712
        // My id:   ca-app-pub-4277307989752479/4869979576
        InterstitialAd.load(context,"ca-app-pub-3940256099942544/1033173712", adRequest, object : InterstitialAdLoadCallback() {
            override fun onAdFailedToLoad(adError: LoadAdError) {
                Log.d(TAG, adError.message)
                mInterstitialAd = null
            }

            override fun onAdLoaded(interstitialAd: InterstitialAd) {
                Log.d(TAG, "Ad was loaded.")
                mInterstitialAd = interstitialAd

                mInterstitialAd?.fullScreenContentCallback = object: FullScreenContentCallback() {
                    override fun onAdDismissedFullScreenContent() {
                        Log.d(TAG, "Ad was dismissed.")
                    }

                    override fun onAdFailedToShowFullScreenContent(adError: AdError?) {
                        Log.d(TAG, "Ad failed to show.")
                    }

                    override fun onAdShowedFullScreenContent() {
                        Log.d(TAG, "Ad showed fullscreen content.")
                        mInterstitialAd = null

                        loadAdmobInterstitialAd(context)
                    }
                }
            }
        })
    }

//    fun loadAdmobNativeAd(activity: Activity) {
//        // Test id: ca-app-pub-3940256099942544/2247696110
//        // My id:   ca-app-pub-4277307989752479/8387386901
//        val mNativeLoader = AdLoader.Builder(activity, "ca-app-pub-3940256099942544/2247696110")
//            .forNativeAd { ad : NativeAd ->
//                val adView = activity.layoutInflater.inflate(R.layout.template_native_ad_item, null) as NativeAdView
//
//                populateNativeAdView(ad, adView)
//
//                ad_frame.removeAllViews()
//                ad_frame.addView(adView)
//
//                // TODO: А как же isDestroyed?
//            }
//            .withAdListener(object : AdListener() {
//                override fun onAdFailedToLoad(adError: LoadAdError) {
//                    // Handle the failure by logging, altering the UI, and so on.
//                }
//            })
//            .withNativeAdOptions(
//                NativeAdOptions.Builder()
//                // Methods in the NativeAdOptions.Builder class can be
//                // used here to specify individual options settings.
//                .build())
//            .build()
//
//        mNativeLoader.loadAd(AdRequest.Builder().build())
//    }

    fun showInterstitialAd(activity1: Activity) {
        val pauseTime = Calendar.getInstance().time.time - timeLastShowInterstitialAd

        if (mInterstitialAd != null && pauseTime > 15000 ) {
            timeLastShowInterstitialAd = Calendar.getInstance().time.time
            mInterstitialAd?.show(activity1)
        } else {
            Log.d(TAG, "The interstitial ad wasn't ready yet.")
        }
    }
}