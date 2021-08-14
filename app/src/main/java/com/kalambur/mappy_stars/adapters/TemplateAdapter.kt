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
import com.google.android.gms.ads.nativead.NativeAd
import com.kalambur.mappy_stars.databinding.TemplateItemBinding
import com.kalambur.mappy_stars.databinding.TemplateNativeAdItemBinding
import com.kalambur.mappy_stars.pojo.Template
import com.kalambur.mappy_stars.utils.extensions.resIdByName
import com.kalambur.mappy_stars.utils.helpers.Helper


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
            // Some assets are guaranteed to be in every UnifiedNativeAd.
            adHeadline.text = nativeAd.headline
            adBody.text = nativeAd.body
            adCallToAction.setText(nativeAd.callToAction)

            val icon: NativeAd.Image? = nativeAd.icon

            if (icon == null) {
                adIcon.visibility = View.INVISIBLE
            } else {
                adIcon.setImageDrawable(icon.drawable)
                adIcon.visibility = View.VISIBLE
            }
            if (nativeAd.price == null) {
                adPrice.visibility = View.INVISIBLE
            } else {
                adPrice.visibility = View.VISIBLE
                adPrice.text = nativeAd.price
            }
            if (nativeAd.store == null) {
                adStore.visibility = View.INVISIBLE
            } else {
                adStore.visibility = View.VISIBLE
                adStore.text = nativeAd.store
            }
            if (nativeAd.starRating == null) {
                adStars.visibility = View.INVISIBLE
            } else {
                adStars.rating = nativeAd.starRating.toFloat()
                adStars.visibility = View.VISIBLE
            }
            if (nativeAd.advertiser == null) {
                adAdvertiser.visibility = View.INVISIBLE
            } else {
                adAdvertiser.text = nativeAd.advertiser
                adAdvertiser.visibility = View.VISIBLE
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

        for (ad in mNativeAds) {
            index += offsetNativeAd

            templateList.add(index, ad)
        }

        notifyDataSetChanged()
    }

    private fun loadNativeAds(countAds: Int) {
        val builder = AdLoader.Builder(activity, "ca-app-pub-3940256099942544/2247696110")
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
                }).build()

        // Load the Native Express ad.
        adLoader!!.loadAds(AdRequest.Builder().build(), countAds)
    }

    companion object {
        val TEMPLATE = 0
        val NATIVE_AD = 1
    }
}