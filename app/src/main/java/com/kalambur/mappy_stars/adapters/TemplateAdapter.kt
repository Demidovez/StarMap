package com.kalambur.mappy_stars.adapters

import android.app.Activity
import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.updatePadding
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.nativead.MediaView
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdOptions
import com.google.android.gms.ads.nativead.NativeAdOptions.ADCHOICES_TOP_RIGHT
import com.kalambur.mappy_stars.R
import com.kalambur.mappy_stars.databinding.TemplateItemBinding
import com.kalambur.mappy_stars.databinding.TemplateNativeAdItemBinding
import com.kalambur.mappy_stars.pojo.Template
import com.kalambur.mappy_stars.utils.extensions.resIdByName
import com.kalambur.mappy_stars.utils.helpers.Helper
import java.util.*
import kotlin.collections.ArrayList
import com.google.android.gms.ads.nativead.AdChoicesView

class TemplateAdapter(private val activity: Activity): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var templateList = ArrayList<Any?>()
    private var adLoader: AdLoader? = null
    private val offsetNativeAd = 3
    private val mNativeAds: ArrayList<NativeAd> = ArrayList()

    class TemplateHolder(item: View): RecyclerView.ViewHolder(item) {
        private val binding = TemplateItemBinding.bind(item)

        fun bind(template: Template?) = with(binding) {
            if(adapterPosition == 0) {
                rootItem.updatePadding(top = Helper.dpToPx(24F, itemView.context))
            } else {
                rootItem.updatePadding(top = 0)
            }

            labelTemplate.text = template!!.category
            rating.rating = template.rating!!
            description.text = template.desc

            cardView.setOnClickListener { view ->
                val bundle = bundleOf(
                    "templateId" to template.id
                )

                view.findNavController().navigate(com.kalambur.mappy_stars.R.id.action_navigation_templates_to_templateFragment, bundle)
            }

            val imageStream = itemView.context.resources.openRawResource(itemView.context.resIdByName(template.image, "raw"))
            val bitmap = BitmapFactory.decodeStream(imageStream)

            imageTemplate.setImageBitmap(bitmap)
        }
    }

    class NativeAdHolder(item: View): RecyclerView.ViewHolder(item) {
        private val binding = TemplateNativeAdItemBinding.bind(item)

        fun bind(nativeAd: NativeAd) = with(binding) {
            adHeadline.text = nativeAd.headline
            adBody.text = nativeAd.body
            adCallToAction.text = nativeAd.callToAction?.lowercase(Locale.getDefault()).toString().replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }

            val icon: NativeAd.Image? = nativeAd.icon

            adView.callToActionView = adCallToAction
            adView.headlineView = adHeadline
            adView.mediaView = adMedia
            adView.bodyView = adBody
            adView.iconView = adIcon
            adView.priceView = adPrice
            adView.starRatingView = adStars
            adView.storeView = adStore
            adView.advertiserView = adAdvertiser

            if (icon == null) {
                adIcon.visibility = View.GONE
            } else {
                adIcon.setImageDrawable(icon.drawable)
                adIcon.visibility = View.VISIBLE
            }
            if (nativeAd.price == null) {
                adPrice.visibility = View.GONE
            } else {
                adPrice.text = nativeAd.price
                adPrice.visibility = View.VISIBLE
            }
            if (nativeAd.store == null) {
                adStore.visibility = View.GONE
            } else {
                adStore.visibility = View.VISIBLE
                adStore.text = nativeAd.store
            }
            if (nativeAd.starRating == null) {
                adStars.visibility = View.GONE
            } else {
                adStars.rating = nativeAd.starRating!!.toFloat()
                adStars.visibility = View.VISIBLE
            }
            if (nativeAd.advertiser == null) {
                adAdvertiser.visibility = View.GONE
            } else {
                adAdvertiser.text = nativeAd.advertiser
                adAdvertiser.visibility = View.VISIBLE
            }
            if (nativeAd.adChoicesInfo != null) {
                adView.adChoicesView = AdChoicesView(adView.context)
            }

            // Assign native ad object to the native view.
            adView.setNativeAd(nativeAd)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return if(viewType == NATIVE_AD) {
            val view = LayoutInflater.from(parent.context).inflate(com.kalambur.mappy_stars.R.layout.template_native_ad_item, parent, false)

            NativeAdHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context).inflate(com.kalambur.mappy_stars.R.layout.template_item, parent, false)

            TemplateHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == TEMPLATE) {
            (holder as TemplateHolder).bind(templateList[position] as Template)
        } else {
            (holder as NativeAdHolder).bind(templateList[position] as NativeAd)
        }
    }

    override fun getItemCount(): Int {
        return templateList.size
    }

    fun addAllTemplateList(list: ArrayList<Any?>) {
        templateList = list
        notifyDataSetChanged()

        loadNativeAds(templateList.size / offsetNativeAd + 1)
    }

    override fun getItemViewType(position: Int): Int {
        return if(templateList[position] is NativeAd) {
            NATIVE_AD
        } else {
            TEMPLATE
        }
    }

    private fun insertAdsInMenuItems() {
        if (mNativeAds.isEmpty()) {
            notifyDataSetChanged()
            return
        }

        var index = -1
        val fullSizeList = templateList.size + mNativeAds.size

        for (ad in mNativeAds) {
            index += offsetNativeAd

            index = index.coerceAtMost(fullSizeList - 1)

            templateList.add(index, ad)
        }

        notifyDataSetChanged()
    }

    private fun loadNativeAds(countAds: Int) {
        // Test id: ca-app-pub-3940256099942544/2247696110
        // My id:   ca-app-pub-2452666921240977/6202246695
        val builder = AdLoader.Builder(activity, "ca-app-pub-2452666921240977/6202246695")
        adLoader =
            builder.forNativeAd { nativeAd ->
                mNativeAds.add(nativeAd)

                if (!adLoader!!.isLoading) {
                    insertAdsInMenuItems()
                }
            }.withAdListener(
                object : AdListener() {
                    fun onAdFailedToLoad(errorCode: Int) {
                        Log.d("Admob", "The previous native ad failed to load. Attempting to load another.")

                        if (!adLoader!!.isLoading) {
                            insertAdsInMenuItems()
                        }
                    }
            })
            .withNativeAdOptions(NativeAdOptions.Builder().build())
            .build()

        // Load the Native Express ad.
        adLoader!!.loadAds(AdRequest.Builder().build(), countAds)
    }

    companion object {
        val TEMPLATE = 0
        val NATIVE_AD = 1
    }
}